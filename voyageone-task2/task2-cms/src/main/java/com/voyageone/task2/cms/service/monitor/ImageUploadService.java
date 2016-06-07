package com.voyageone.task2.cms.service.monitor;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.FtpComponentFactory;
import com.voyageone.components.ftp.FtpConstants;
import com.voyageone.components.ftp.bean.FtpFileBean;
import com.voyageone.components.ftp.service.BaseFtpComponent;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.io.FileUtils;
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
 * ImageUploadService
 *
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class ImageUploadService extends AbstractFileMonitoService {

    @Autowired
    private BusinessLogService businessLogService;

    @Autowired
    private ProductService productService;

    @Override
    protected void onCreate(String filePath, String fileName, String channelId) {
        LOG.info("监控到目录创建" + filePath);
        onModify(filePath, fileName, channelId);
    }

    @Override
    protected void onDelete(String filePath, String fileName) {
        LOG.info("监控到目录删除" + filePath + fileName);
    }

    @Autowired
    private TaskDao taskDao;


    @Override
    protected void onModify(String filePath, String fileName, String channelId) {
        if (!fileName.endsWith(".zip")) {
            return;
        }
        List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASK_NAME);
        TaskControlBean taskControlBean = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.run_flg).get(0);

        if (!"1".equals(taskControlBean.getCfg_val1())) {
            LOG.warn(TASK_NAME + ":的run_flg设置为0,不执行该job");
            return;
        }

        LOG.info("监控到目录更新" + filePath);
        String tempDir = buildDirPath(filePath, "temp");
        String modifyDirName = new File(filePath).getName();
        File file = new File(filePath + fileName);

        int readImgCount = 0;
        int failedImgCount = 0;
        //异常信息
        StringBuilder errorMsg = new StringBuilder();
        try {
            //解压缩到临时目录
            deComparess(file, tempDir);
            //依据临时目录内容上传ftp，之后更新Mongo数据
            List<String> upFtpSuccessFiles = new ArrayList<>();
            Map<String, Boolean> ftpUploadResultMap = ftpUpload(channelId, tempDir, buildDirPath(filePath, "error"));
            readImgCount = ftpUploadResultMap.size();
            for (Map.Entry<String, Boolean> entry : ftpUploadResultMap.entrySet()) {
                if (entry.getValue()) {
                    upFtpSuccessFiles.add(entry.getKey());
                } else {
                    failedImgCount++;
                    errorMsg.append(entry.getKey().split("-ftp-")[1]).append("：图片上传到FTP服务器失败");
                }
            }
            //排序
            Collections.sort(upFtpSuccessFiles);
            Set<String> skuApprovedSet = new HashSet<>();
            upFtpSuccessFiles.forEach(k -> {
                CmsBtProductModel model = productService.getProductBySku(channelId, k.split("-")[2]);
                if (model != null) {
                    mongoOperator(model, modifyDirName, k);
                    if (skuApprovedSet.add(k.split("-")[2]))
                        approvedOperator(model);
                } else {
                    errorMsg.append(k.split("-ftp-")[1]).append("：找不到指定商品");
                }
            });
            //删除临时目录
            FileUtils.deleteDirectory(new File(tempDir));
            //移动文件到备份目录
            FileUtils.moveFile(file, new File(buildDirPath(filePath, "backup") + file.getName() + "." + System.currentTimeMillis()));
        } catch (IOException e) {
            errorMsg.append("其他异常：").append(e.getMessage());
            LOG.error("处理zip文件" + filePath + "发生Io异常：", e);
        }
        logForUpload(file.getName(), readImgCount, readImgCount - failedImgCount, failedImgCount, errorMsg.toString(), channelId);

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
    private Map<String, Boolean> ftpUpload(String channelId, String tempDir, String errorDir) {
        // FtpBean初期化
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.SCENE7_FTP);

        String uploadPath = ChannelConfigs.getVal1(channelId, ChannelConfigEnums.Name.scene7_image_folder);
        if (StringUtils.isEmpty(uploadPath)) {
            String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表", channelId);
            LOG.error(err);
            throw new RuntimeException(err);
        }

        Map<String, Boolean> uploadResultMap = new HashMap<>();

        //取得图片文件
        List<File> fileList = new ArrayList<>();
        filterImgFile(new File(tempDir), fileList);

        //建立连接
        ftpComponent.openConnect();
        fileList.forEach(imgfile -> {
            String uploadFileName = channelId + "-ftp-" + imgfile.getName();
            FtpFileBean ftpFileBean = new FtpFileBean(imgfile.getParent(), imgfile.getName(), uploadPath, uploadFileName);
            try {
                ftpComponent.uploadFile(ftpFileBean);
                uploadResultMap.put(uploadFileName, true);
            } catch (Exception e) {
                uploadResultMap.put(uploadFileName, false);
                try {
                    FileUtils.copyFileToDirectory(imgfile, new File(errorDir + imgfile.getPath().split(tempDir)[1]), true);
                } catch (IOException e1) {
                    LOG.error("移动上传错误图片到error目录异常", e);
                }
            }
        });

        ftpComponent.closeConnect();
        return uploadResultMap;
    }

    /**
     * 过滤Dir
     *
     * @param dirFile  目录文件
     * @param fileList 文件list
     */
    private void filterImgFile(File dirFile, List<File> fileList) {
        File[] files = dirFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                filterImgFile(file, fileList);
            } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".gif")
                    || file.getName().endsWith(".jpeg")) {
                fileList.add(file);
            } else {
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
            if (!ObjectUtils.isEmpty(fields.get(modifyDirName))) {
                images = new CopyOnWriteArrayList<>(JacksonUtil.jsonToMapList(fields.get(modifyDirName).toString()));

                // 只有一张图片,并且该图片的值为空的时候,删除其对应的图片信息
                if (images.size() == 1)
                    for (Map<String, Object> image : images) {
                        StringUtils.isEmpty(String.valueOf(image.get(modifyDirName.replace("s", ""))));
                        images.remove(image);
                    }

            }
            for (Map<String, Object> img : images) {
                if (uploadFileName.equals(img.get(modifyDirName)))
                    return;
            }
            images.add(new HashMap<String, Object>() {{
                put(modifyDirName.replace("s", ""), uploadFileName.split("\\.")[0]);
            }});

            /* 是images6 忽略大小写,重新排序 */
            if (modifyDirName.equalsIgnoreCase("images6")) {
                /* flag 0不copy,1copybefore,2copyafter */
                CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(model.getChannelId()
                        , CmsConstants.ChannelConfig.IMAGE_UPLOAD_SERVICE);
                if ("1".equals(cmsChannelConfigBean.getConfigValue1()))
                    images.addAll(0, JacksonUtil.jsonToMapList(fields.get("images1").toString().replace("image1", "image6")));
                if ("2".equals(cmsChannelConfigBean.getConfigValue1()))
                    images.addAll(JacksonUtil.jsonToMapList(fields.get("images1").toString().replace("image1", "image6")));
            }

            Set<Map<String, Object>> sets = new HashSet<>();
            for (Map<String, Object> map : images) if (!sets.add(map)) images.remove(map);

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
     *
     * @param zipName           zip文件名称
     * @param readSkuCount      读取sku个数
     * @param processedSkuCount 处理sku个数
     * @param failedSkuCount    失败sku个数
     * @param errorMsg          错误信息
     */
    private void logForUpload(String zipName, int readSkuCount, int processedSkuCount, int failedSkuCount, String errorMsg, String channelId) {
        StringBuilder stringBuilder = new StringBuilder("ImageUploadService处理压缩文件" + zipName + "操作日志");
        stringBuilder.append("\n本次读入图片个数：").append(readSkuCount);
        stringBuilder.append("\n正常处理图片个数：").append(processedSkuCount);
        stringBuilder.append("\n异常处理图片个数：").append(failedSkuCount);
        stringBuilder.append("\n错误说明：").append(errorMsg);

        CmsBtBusinessLogModel logModel = new CmsBtBusinessLogModel();
        logModel.setChannelId(channelId);
        logModel.setErrorMsg(stringBuilder.toString());
        logModel.setErrorTypeId(2);
        logModel.setCreater(TASK_NAME);
        businessLogService.insertBusinessLog(logModel);
    }
}
