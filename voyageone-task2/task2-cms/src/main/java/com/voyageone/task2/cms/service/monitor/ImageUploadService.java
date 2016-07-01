package com.voyageone.task2.cms.service.monitor;

import com.google.common.collect.Lists;
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
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;
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

    private final String TASK_NAME = "CmsBulkUploadImageToS7Job";
    private final static String[] EVENTS = {"close_write"};
    private final String MODIFIER = getClass().getSimpleName();

    @Autowired
    private BusinessLogService businessLogService;
    @Autowired
    private ProductService productService;

    @Override
    protected String getTaskName() {
        return TASK_NAME;
    }

    @Override
    protected String[] getInotifyEvents() {
        return EVENTS;
    }

    @Override
    protected boolean eventCheck(String event, String watchPath, String filePath, String fileName, String channelId) {
        boolean result = true;
        if (!fileName.endsWith(".zip")) {
            result = false;
        } else {
            String watchPathTemp = watchPath;
            if (!(watchPathTemp.endsWith("/") || watchPathTemp.endsWith("\\"))) {
                watchPathTemp = watchPathTemp + "/";
            }
            File file = new File(filePath);
            String parentPath = file.getParent().replaceAll("\\\\", "/");
            if (!parentPath.endsWith("/")) {
                parentPath = parentPath + "/";
            }
            if (!watchPathTemp.equals(parentPath)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    protected void doEvent(String event, String filePath, String fileName, String channelId) {
        LOG.info(String.format("doEvent event=%s filePath=%s fileName=%s channelId=%s", event, filePath, fileName, channelId));
        List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASK_NAME);
        TaskControlBean taskControlBean = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.run_flg).get(0);

        if (!"1".equals(taskControlBean.getCfg_val1())) {
            LOG.warn(String.format(TASK_NAME + ":的run_flg设置为0,不执行该job. event=%s filePath=%s fileName=%s channelId=%s", event, filePath, fileName, channelId));
            return;
        }

        String baseTempDir = buildDirPath(filePath, "temp");
        String baseErrorDir = buildDirPath(filePath, "error");
        String modifyDirName = new File(filePath).getName();
        File file = new File(filePath + fileName);

        int readImgCount = 0;
        int failedImgCount = 0;

        //异常信息
        StringBuilder errorMsg = new StringBuilder();
        try {
            String tempDir = buildDirPath(baseTempDir, file.getName());
            String errorDir = buildDirPath(baseErrorDir, file.getName());
            //解压缩到临时目录
            deComparess(file, tempDir);
            //依据临时目录内容上传ftp，之后更新Mongo数据
            LOG.info(String.format("deComparess finish tempDir=%s filePath=%s fileName=%s channelId=%s", tempDir, filePath, fileName, channelId));
            List<String> upFtpSuccessFiles = new ArrayList<>();
            Map<String, Boolean> ftpUploadResultMap = ftpUpload(taskControlList, channelId, tempDir, errorDir);

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
            LOG.info(String.format("upload image file count:%s:%s filePath=%s fileName=%s channelId=%s", readImgCount, upFtpSuccessFiles.size(), filePath, fileName, channelId));
            Collections.sort(upFtpSuccessFiles);
            Set<String> skuApprovedSet = new HashSet<>();
            for (String strFileName : upFtpSuccessFiles) {
                String sku = strFileName.substring(8, strFileName.lastIndexOf("-"));
                LOG.info(String.format("fileName:%s sku:%s filePath=%s fileName=%s channelId=%s", strFileName, sku, filePath, fileName, channelId));
                CmsBtProductModel model = productService.getProductBySku(channelId, sku);
                if (model != null) {
                    mongoOperator(model, modifyDirName, strFileName);
                    if (skuApprovedSet.add(sku))
                        approvedOperator(model);
                } else {
                    errorMsg.append(strFileName.split("-ftp-")[1]).append("：找不到指定商品");
                }
            }
            LOG.info(String.format("update products finish filePath=%s fileName=%s channelId=%s", filePath, fileName, channelId));
            //删除临时目录
            FileUtils.deleteDirectory(new File(tempDir));
            LOG.info(String.format("remove tempDir finish filePath=%s fileName=%s channelId=%s", filePath, fileName, channelId));
            //移动文件到备份目录
            File backupFile = new File(buildDirPath(filePath, "backup") + file.getName() + "." + System.currentTimeMillis());
            LOG.info(String.format("move zipFile to backupDir[%s] start filePath=%s fileName=%s channelId=%s", backupFile.getPath(), filePath, fileName, channelId));
            FileUtils.moveFile(file, backupFile);
            LOG.info(String.format("move zipFile to backupDir[%s] finish filePath=%s fileName=%s channelId=%s", backupFile.getPath(), filePath, fileName, channelId));

        } catch (Exception e) {
            errorMsg.append("其他异常：").append(e.getMessage());
            LOG.error(String.format("doEvent error filePath=%s fileName=%s channelId=%s", filePath, fileName, channelId), e);
        }

        //上传日志
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
        String filePathTmp = filePath.replaceAll("\\\\", "/");
        if (!filePathTmp.endsWith("/")) {
            filePathTmp = filePathTmp + "/";
        }
        File dir = new File(filePathTmp + dirName);
        if (!dir.exists()) dir.mkdirs();
        return filePathTmp + dirName + "/";
    }

    /**
     * 解压缩zip文件到临时目录
     *
     * @param zfile   zip文件
     * @param tempDir 临时目录
     */
    private void deComparess(File zfile, String tempDir) throws IOException {
        Charset CP866 = Charset.forName("iso-8859-1");
        ZipFile zf = new ZipFile(zfile, CP866);
        Enumeration entries = zf.entries();

        LOG.info("deComparess start:" + zf.getName());
        while (entries.hasMoreElements()) {
            try {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                File cf = new File(tempDir + entry.getName());
                String oldFileName = cf.getName();
                if (cf.getName().indexOf("－") > 0) {
                    String newFileName = oldFileName.replaceAll("－", "-");
                    com.voyageone.common.util.FileUtils.moveFile(cf.getPath(), cf.getParent() + "/" + newFileName);
                    cf = new File(cf.getParent() + "/" + newFileName);
                }
                if (zf.getInputStream(entry).available() > 0) {
                    FileUtils.copyInputStreamToFile(zf.getInputStream(entry), cf);
                }
            } catch (Exception e) {
                LOG.error("deComparess error:", e);
            }
        }

        LOG.info("deComparess finish:" + zf.getName());
        zf.close();
    }

    /**
     * 上传图片到指定目录，并返回上传结果集
     *
     * @param tempDir dir
     * @return 结果集 key 图片路径，value 上传结果
     */
    private Map<String, Boolean> ftpUpload(List<TaskControlBean> taskControlList, String channelId, String tempDir, String errorDir) throws InterruptedException {
        // FtpBean初期化
        LOG.info(String.format("start upload file to S7 tempDir=%s errorDir=%s channelId=%s", tempDir, errorDir, channelId));

        String uploadPath = ChannelConfigs.getVal1(channelId, ChannelConfigEnums.Name.scene7_image_folder);
        if (StringUtils.isEmpty(uploadPath)) {
            String err = String.format("uploadPath未配置 channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表", channelId);
            LOG.error(err);
            throw new RuntimeException(err);
        }

        Map<String, Boolean> uploadResultMap = new ConcurrentHashMap<>();

        //取得图片文件
        List<File> fileList = new ArrayList<>();
        filterImgFile(new File(tempDir), fileList);

        LOG.info(String.format("start upload file to S7 fileCount=%s tempDir=%s errorDir=%s channelId=%s", fileList.size(), tempDir, errorDir, channelId));

        // 线程List
        List<Runnable> threads = new ArrayList<>();
        List<List<File>> fileLists = Lists.partition(fileList, getThreadCount(taskControlList));

        final String finalUploadPath = uploadPath;
        for (List<File> subFileLists : fileLists) {
            if (subFileLists == null || subFileLists.isEmpty()) {
                continue;
            }
            threads.add(() -> uploadFileToS7(subFileLists, uploadResultMap, tempDir, errorDir, finalUploadPath, channelId));
        }
        // check threads
        if (!threads.isEmpty()) {
            ExecutorService pool = Executors.newFixedThreadPool(threads.size());
            threads.forEach(pool::execute);
            pool.shutdown();
            // 等待子线程结束，再继续执行下面的代码
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }

        LOG.info(String.format("finish upload file to S7 tempDir=%s errorDir=%s channelId=%s", tempDir, errorDir, channelId));
        return uploadResultMap;
    }

    /**
     * 上传图片到指定目录，并返回上传结果集
     */
    private void uploadFileToS7(List<File> fileList, Map<String, Boolean> uploadResultMap, String tempDir, String errorDir, String uploadPath, String channelId) {
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.SCENE7_FTP);
        try {
            //建立连接
            ftpComponent.openConnect();
            ftpComponent.enterLocalPassiveMode();

            for (File imgFile : fileList) {
                String uploadFileName = channelId + "-ftp-" + imgFile.getName();
                FtpFileBean ftpFileBean = new FtpFileBean(imgFile.getParent(), imgFile.getName(), uploadPath, uploadFileName);
                try {
                    ftpComponent.uploadFile(ftpFileBean);
                    uploadResultMap.put(uploadFileName, true);
                    LOG.info(String.format("upload %s to S7 success tempDir=%s", uploadFileName, tempDir));
                } catch (Exception e) {
                    LOG.error(String.format("upload %s to S7 success tempDir=%s", uploadFileName, tempDir), e);
                    uploadResultMap.put(uploadFileName, false);
                    try {
                        FileUtils.copyFileToDirectory(imgFile, new File(errorDir), true);
                    } catch (IOException e1) {
                        LOG.error("移动上传错误图片到error目录异常", e);
                    }
                }
            }
        } finally {
            ftpComponent.closeConnect();
        }
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
            String fileName = file.getName().toLowerCase();
            if (file.isDirectory()) {
                filterImgFile(file, fileList);
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif") || fileName.endsWith(".jpeg")) {
                fileList.add(file);
            } else {
                LOG.warn(String.format("%s:不支持上传的的文件类型", fileName));
            }
        }
    }

    /**
     * 操作mongodb
     */
    private void mongoOperator(CmsBtProductModel model, String modifyDirName, String uploadFileName) {
        String code = "";
        try {
            /* fields */
            CmsBtProductModel_Field fields = model.getCommon().getFields();
            code = fields.getCode();

            /* change fields.images */
            List<Map<String, Object>> images = new CopyOnWriteArrayList<>();
            if (!ObjectUtils.isEmpty(fields.get(modifyDirName))) {
                images = new CopyOnWriteArrayList<>(JacksonUtil.jsonToMapList(fields.get(modifyDirName).toString()));

                // 只有一张图片,并且该图片的值为空的时候,删除其对应的图片信息
                // 修改为 如果Map的Value值为空 删除其对应的图片信息 liang
                for (Map<String, Object> imageMap : images) {
                    boolean isNeedRemove = true;
                    if (imageMap != null && !imageMap.isEmpty()) {
                        for (Map.Entry entry : imageMap.entrySet()) {
                            if (entry.getValue() != null && !StringUtils.isEmpty(((String) entry.getValue()).trim())) {
                                isNeedRemove = false;
                                break;
                            }
                        }
                    }
                    if (isNeedRemove) {
                        images.remove(imageMap);
                    }
                }
            }

            // 图片存在，返回
            for (Map<String, Object> img : images) {
                if (uploadFileName.equals(img.get(modifyDirName)))
                    return;
            }

            //加入图片
            images.add(new HashMap<String, Object>() {{
                put(modifyDirName.replace("s", ""), uploadFileName.split("\\.")[0]);
            }});

            /* 是images6 忽略大小写,重新排序 */
            if (modifyDirName.equalsIgnoreCase("images6")) {
                /* flag 0不copy,1copybefore,2copyafter */
                CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(model.getChannelId(), CmsConstants.ChannelConfig.IMAGE_UPLOAD_SERVICE);
                if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                    if ("1".equals(cmsChannelConfigBean.getConfigValue1())) {
                        images.addAll(0, JacksonUtil.jsonToMapList(fields.get("images1").toString().replace("image1", "image6")));
                    } else if ("2".equals(cmsChannelConfigBean.getConfigValue1())) {
                        images.addAll(JacksonUtil.jsonToMapList(fields.get("images1").toString().replace("image1", "image6")));
                    }
                }
            }

            //去除重复
            Set<Map<String, Object>> sets = new HashSet<>();
            for (Map<String, Object> map : images) {
                if (!sets.add(map)) {
                    images.remove(map);
                }
            }

            updateProductModel(model.getChannelId(), model.getProdId(), modifyDirName, images);

            LOG.info("mongoOperator success:" + code);
        } catch (IOException e) {
            LOG.info(String.format("mongoOperator code:%s error:", code), e);
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

        String errorMsgTmp = stringBuilder.toString();
        if (errorMsgTmp.length() > 2000) {
            errorMsgTmp = errorMsgTmp.substring(0, 2000);
        }

        CmsBtBusinessLogModel logModel = new CmsBtBusinessLogModel();
        logModel.setChannelId(channelId);
        logModel.setErrorMsg(errorMsgTmp);
        logModel.setErrorTypeId(2);
        logModel.setCreater(TASK_NAME);
        businessLogService.insertBusinessLog(logModel);
    }

    /**
     * getThreadCount
     */
    private int getThreadCount(List<TaskControlBean> taskControlList) {
        String threadCount = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.thread_count);
        int intThreadCount = 1;

        if (!StringUtils.isNullOrBlank2(threadCount)) {
            intThreadCount = Integer.valueOf(threadCount);
        }

        // 如果最终计算获得线程数量无效，则提示错误
        if (intThreadCount < 1) {
            throw new IllegalArgumentException("thread count error.");
        }

        return intThreadCount;
    }
}
