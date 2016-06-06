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
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
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

    @Override
    protected void onCreate(String filePath) {
        LOG.info("监控到目录创建" + filePath);
    }

    @Override
    protected void onDelete(String filePath) {
        LOG.info("监控到目录删除" + filePath);
    }

    @Autowired
    private TaskDao taskDao;

    @Override
    protected void onModify(String filePath, String channelId) {

        List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASK_NAME);
        TaskControlBean taskControlBean = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.run_flg).get(0);

        if (!"1".equals(taskControlBean.getCfg_val1())) {
            LOG.warn(TASK_NAME + ":的run_flg设置为0,不执行该job");
            return;
        }

        LOG.info("监控到目录更新" + filePath);
        String tempDir = buildDirPath(filePath, "temp");
//        String channelId = Properties.readValue(getClass().getSimpleName() + "_monitor_home_path_channelId");
        String modifyDirName = new File(filePath).getName();
        Arrays.asList(new File(filePath).listFiles((dir, name) -> name.endsWith(".zip"))).forEach(file -> {
            try {
                //解压缩到临时目录
                deComparess(file, tempDir);
                //依据临时目录内容上传ftp，之后更新Mongo数据
                List<String> upFtpSuccessFiles = new ArrayList<String>();
                ftpUpload(channelId, tempDir, buildDirPath(filePath, "error")).forEach((k, v) -> {
                    if (v) upFtpSuccessFiles.add(k);
                    else LOG.info(k + "Ftp上传失败");
                });
                //排序
                Collections.sort(upFtpSuccessFiles);
                upFtpSuccessFiles.forEach(k -> {
                    CmsBtProductModel model = productService.getProductBySku(channelId, k.split("-")[2]);
                    if (model != null) {
                        mongoOperator(model, modifyDirName, k);
                        approvedOperator(model);
                    }
                });
                //删除临时目录
                FileUtils.deleteDirectory(new File(tempDir));
                //移动文件到备份目录
                FileUtils.moveFile(file, new File(buildDirPath(filePath, "backup") + file.getName() + "." + System.currentTimeMillis()));
            } catch (IOException e) {
                LOG.error("处理zip文件" + filePath + "发生Io异常：", e);
            }
        });
        taskControlBean.setEnd_time(DateTimeUtil.getNow());
        taskDao.updateTaskControl(taskControlBean);
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
    private static void filterImgFile(File dirFile, List<File> fileList) {
        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()) {
                filterImgFile(file, fileList);
            } else fileList.add(file);
        }
    }

    @Autowired
    private ProductService productService;

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
}
