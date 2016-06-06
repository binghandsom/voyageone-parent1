package com.voyageone.task2.cms.service.monitor;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class ImageUploadService extends AbstractFileMonitoService {

    private static final String S7FTP_CONFIG = "S7FTP_CONFIG";

    private static final List<String> failedZipFileList=new ArrayList<>();

    @Autowired
    private BusinessLogService businessLogService;

    @Autowired
    private ProductService productService;

    @Override
    protected void onCreate(String filePath) {
        LOG.info("监控到目录创建" + filePath);
    }

    @Override
    protected void onDelete(String filePath) {
        LOG.info("监控到目录删除" + filePath);
    }

    @Override
    protected void onModify(String filePath) {
        LOG.info("监控到目录更新" + filePath);
        String tempDir = buildDirPath(filePath, "temp");
        String channelId = Properties.readValue(getClass().getSimpleName() + "_monitor_home_path_channelId");
        String modifyDirName = new File(filePath).getName();
        Arrays.asList(new File(filePath).listFiles((dir, name) -> name.endsWith(".zip"))).forEach(file -> {
            if(!failedZipFileList.contains(file.getName())){
                int readImgCount=0;
                int failedImgCount=0;
                //异常信息
                StringBuilder errorMsg=new StringBuilder();
                try {
                    //解压缩到临时目录
                    deComparess(file, tempDir);
                    //依据临时目录内容上传ftp，之后更新Mongo数据
                    List<String> upFtpSuccessFiles = new ArrayList<>();
                    Map<String, Boolean> ftpUploadResultMap = ftpUpload(channelId, tempDir, buildDirPath(filePath, "error"));
                    readImgCount=ftpUploadResultMap.size();
                    for(Map.Entry<String,Boolean> entry:ftpUploadResultMap.entrySet()){
                        if(entry.getValue()){
                            upFtpSuccessFiles.add(entry.getKey());
                        } else{
                            failedImgCount++;
                            errorMsg.append(entry.getKey().split("-ftp-")[1]+"：图片上传到FTP服务器失败");
                        }
                    }
                    //排序
                    Collections.sort(upFtpSuccessFiles);
                    upFtpSuccessFiles.forEach(k -> {
                        CmsBtProductModel model = productService.getProductBySku(channelId, k.split("-")[2]);
                        if (model != null) {
                            mongoOperator(model, modifyDirName, k);
                            approvedOperator(model);
                        }else{
                            errorMsg.append(k.split("-ftp-")[1]+"：找不到指定商品");
                        }
                    });
                    //删除临时目录
                    FileUtils.deleteDirectory(new File(tempDir));
                    //移动文件到备份目录
                    FileUtils.moveFile(file, new File(buildDirPath(filePath, "backup") + file.getName() + "." + System.currentTimeMillis()));
                } catch (IOException e) {
                    failedZipFileList.add(file.getName());
                    errorMsg.append("其他异常："+e.getMessage());
                    LOG.error("处理zip文件" + filePath + "发生Io异常：", e);
                }
                logForUpload(file.getName(),readImgCount,readImgCount-failedImgCount,failedImgCount,errorMsg.toString());
            }
        });
    }

    /**
     * 构建目录
     *
     * @param filePath 监控目录
     * @param dirName  文件目录
     * @return 目录path
     */
    private String buildDirPath(String filePath, String dirName) {
        File dir = new File(filePath + "/" + dirName);
        if (!dir.exists()) dir.mkdirs();
        return filePath + "/" + dirName + "/";
    }

    /**
     * 解压缩zip文件到临时目录
     *
     * @param zfile   zip文件
     * @param tempDir 临时目录
     * @throws IOException
     */
    private void deComparess(File zfile, String tempDir) throws IOException {
        ZipFile zf = new ZipFile(zfile);
        Enumeration entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            File cf = new File(tempDir + entry.getName());
            if (zf.getInputStream(entry).available() > 0) FileUtils.copyInputStreamToFile(zf.getInputStream(entry), cf);
        }
        zf.close();
    }

    /**
     * 上传图片到指定目录，并返回上传结果集
     *
     * @param tempDir dir
     * @return 结果集 key 图片路径，value 上传结果
     */
    private Map<String, Boolean> ftpUpload(String channelId, String tempDir, String errorDir) throws IOException {
        String uploadHome = Properties.readValue(getClass().getSimpleName() + "_ftpupload_destdir_path") + "/";

        FtpBean ftpBean = new FtpBean();
        // ftp连接port
        String port = Codes.getCodeName(S7FTP_CONFIG, "Port");
        ftpBean.setPort(port);
        // ftp连接url
        String url = Codes.getCodeName(S7FTP_CONFIG, "Url");
        ftpBean.setUrl(url);
        // ftp连接usernmae
        String userName = Codes.getCodeName(S7FTP_CONFIG, "UserName");
        ftpBean.setUsername(userName);
        // ftp连接password
        String password = Codes.getCodeName(S7FTP_CONFIG, "Password");
        ftpBean.setPassword(password);
        // ftp连接上传文件编码
        String fileEncode = Codes.getCodeName(S7FTP_CONFIG, "FileCoding");
        ftpBean.setFile_coding(fileEncode);
        //FTP服务器保存目录设定
//        ftpBean.setUpload_path(uploadHome);
        String uploadPath = ChannelConfigs.getVal1(channelId, ChannelConfigEnums.Name.scene7_image_folder);
        ftpBean.setUpload_path(uploadPath);

        FtpUtil ftpUtil = new FtpUtil();
        Map<String, Boolean> uploadResultMap = new HashMap<>();
        FTPClient ftpClient = ftpUtil.linkFtp(ftpBean);
        List<File> fileList = new ArrayList<>();
        filterImgFile(new File(tempDir), fileList);
        final FTPClient finalFtpClient = ftpClient;
        fileList.forEach(imgfile -> {
            try {
                String uploadFileName = channelId + "-ftp-" + imgfile.getName();
                //ftpBean.setUpload_path(uploadHome + imgfile.getParent().substring(tempDir.length()).replace("\\", "/"));
                ftpBean.setUpload_localpath(imgfile.getParent());
                ftpBean.setUpload_localfilename(imgfile.getName());
                ftpBean.setUpload_filename(uploadFileName);
                uploadResultMap.put(uploadFileName, ftpUtil.uploadFile(ftpBean, finalFtpClient));
            } catch (IOException e) {
                try {
                    FileUtils.copyFileToDirectory(imgfile, new File(errorDir + imgfile.getPath().split(tempDir)[1]), true);
                } catch (IOException e1) {
                    LOG.error("移动上传错误图片到error目录异常", e);
                }
            }
        });
        if (ftpClient != null) {
            try {
                ftpUtil.disconnectFtp(ftpClient);
            } catch (IOException e) {
                LOG.error("关闭ftp连接异常", e);
            }
        }
        return uploadResultMap;
    }

    /**
     * 过滤Dir
     *
     * @param dirFile  目录文件
     * @param fileList 文件list
     */
    private void filterImgFile(File dirFile, List<File> fileList) {
        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()) {
                filterImgFile(file, fileList);
            } else if(file.getName().endsWith(".jpg")||file.getName().endsWith(".png")||file.getName().endsWith(".gif")
                    ||file.getName().endsWith(".jpeg")){
                fileList.add(file);
            }else{
                LOG.warn("不支持上传的的文件类型");
            }
        }
    }

    /**
     * 操作mongodb
     */
    private void mongoOperator(CmsBtProductModel model, String modifyDirName, String uploadFileName) {
        try {
            /* fields */
            CmsBtProductModel_Field fields = model.getFields();

            /* change fileds.images */
            List<Map<String, Object>> images = new CopyOnWriteArrayList<>();
            if (!ObjectUtils.isEmpty(fields.get(modifyDirName)))
                images = new CopyOnWriteArrayList<>(JacksonUtil.jsonToMapList(fields.get(modifyDirName).toString()));
            for (Map<String, Object> img : images) {
                if (uploadFileName.equals(img.get(modifyDirName)))
                    return;
            }
            images.add(new HashMap<String, Object>() {{
                put(modifyDirName.replace("s",""), uploadFileName.split("\\.")[0]);
            }});

            /* 是images6 忽略大小写,重新排序 */
            if (modifyDirName.equalsIgnoreCase("images6")) {
                /* flag 0不copy,1copybefore,2copyafter */
                CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(model.getChannelId()
                        , CmsConstants.ChannelConfig.IMAGE_UPLOAD_SERVICE);
                if ("1".equals(cmsChannelConfigBean.getConfigValue1()))
                    images.addAll(0, JacksonUtil.jsonToMapList(fields.get("images1").toString().replace("image1", "image6")));
                if ("2".equals(cmsChannelConfigBean.getConfigValue1()))
                    images.addAll(images.size(), JacksonUtil.jsonToMapList(fields.get("images1").toString().replace("image1", "image6")));
            }

            Set<Map<String,Object>> sets=new HashSet<>();
            for(Map<String,Object> map:images) if(!sets.add(map)) images.remove(map);

            updateProductModel(model.getChannelId(), model.getProdId(), modifyDirName, images);
        } catch (IOException e) {
            LOG.info("JacksonUtil解析异常", e);
        }
    }

    /**
     * 批准操作
     *
     * @param model model
     */
    private void approvedOperator(CmsBtProductModel model) {
        if (CmsConstants.ProductStatus.Approved.name().equals(model.getFields().getStatus()))
            productService.insertSxWorkLoad(model.getChannelId(), model, MODIFIER);
    }


    private void updateProductModel(String channelId, Long prodId, String fieldName, List<Map<String, Object>> images) {
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("fields." + fieldName, images);
        rsMap.put("modifier", MODIFIER);
        rsMap.put("modified", DateTimeUtil.getNowTimeStamp());

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        productService.updateProduct(channelId, queryMap, updateMap);
    }

    /**
     * 上传日志
     * @param zipName zip文件名称
     * @param readSkuCount 读取sku个数
     * @param processedSkuCount 处理sku个数
     * @param failedSkuCount 失败sku个数
     * @param errorMsg 错误信息
     */
    private void logForUpload(String zipName,int readSkuCount,int processedSkuCount,int failedSkuCount,String errorMsg){
        StringBuilder stringBuilder=new StringBuilder("ImageUploadService处理压缩文件"+zipName+"操作日志");
        stringBuilder.append("\n本次读入图片个数：").append(readSkuCount);
        stringBuilder.append("\n正常处理图片个数：").append(processedSkuCount);
        stringBuilder.append("\n异常处理图片个数：").append(failedSkuCount);
        stringBuilder.append("\n错误说明：").append(errorMsg);
        CmsBtBusinessLogModel logModel=new CmsBtBusinessLogModel();
        logModel.setErrorMsg(stringBuilder.toString());
        logModel.setErrorTypeId(2);
        businessLogService.insertBusinessLog(logModel);
    }
}
