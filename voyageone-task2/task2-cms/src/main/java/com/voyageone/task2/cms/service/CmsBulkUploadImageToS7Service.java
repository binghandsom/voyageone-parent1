package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.CmsChannelConfigs;
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
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.task2.base.BaseCronTaskService;
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

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Name.image_server_bulk_path;

/**
 * CMS 批量上传图片文件到Scene Server Ftp Service
 * replace com.voyageone.task2.cms.service.monitor.ImageUploadService
 *
 * @author chuanyu.liang on 2016/07/18.
 * @version 2.1.0
 * @since 2.1.0
 */
@Service
public class CmsBulkUploadImageToS7Service extends BaseCronTaskService {

    private final String TASK_NAME = "CmsBulkUploadImageToS7Job";
    private final String MODIFIER = getClass().getSimpleName();

    @Autowired
    private BusinessLogService businessLogService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SxProductService sxProductService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return TASK_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        $info("CmsBulkUploadImageToS7Service.onStartup start");

        //取得上传的目录List
        List<FileUploadBean> fileUploadBeans = getFilePaths(taskControlList);
        // 循环处理批量图片给上传
        for (FileUploadBean fileUploadBean : fileUploadBeans) {
            // 根据各个channel do File Upload
            doFileUpload(taskControlList, fileUploadBean);
        }

        $info("CmsBulkUploadImageToS7Service.run end");
    }

    /**
     * 根据各个channel do File Upload
     *
     * @param taskControlList taskControlList
     * @param fileUploadBean  fileUploadBean
     */
    private void doFileUpload(List<TaskControlBean> taskControlList, FileUploadBean fileUploadBean) {
        String finalPath = fileUploadBean.filePath;
        $info(String.format("CmsBulkUploadImageToS7Service.onStartup finalPath=%s", finalPath));

        // 这个渠道的Feed文件的根目录
        File filePath = new File(finalPath);
        // 扫描根目录下面的所有文件（不包含子目录）
        File[] files = filePath.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        // ftp upload thread count
        int threadCount = getThreadCount(taskControlList);

        // 如果存在文件，那么逐个处理
        for (File file : files) {
            // 只处理文件，跳过目录
            if (file.isDirectory()) {
                continue;
            }
            // 只处理扩展名为.zip的文件
            String fileName = file.getName();
            // 处理zip后缀的文件
            if (!fileName.toLowerCase().endsWith(".zip")) {
                continue;
            }
            // 进行改名，改名失败的情况说明这个文件正被占用，所以略过处理
            File newFile = new File(file.getPath().substring(0, file.getPath().length() - 4) + "-w.zip");
            boolean result = file.renameTo(newFile);
            if (!result) {
                continue;
            }
            result = newFile.renameTo(file);
            if (!result) {
                continue;
            }

            // channelId
            String channelId = fileUploadBean.channelId;

            String baseTempDir = buildDirPath(finalPath, "temp");
            String baseErrorDir = buildDirPath(finalPath, "error");
            String modifyDirName = new File(finalPath).getName();

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
                $info(String.format("CmsBulkUploadImageToS7Service deComparessed tempDir=%s filePath=%s fileName=%s channelId=%s", tempDir, filePath, fileName, channelId));
                List<String> upFtpSuccessFiles = new ArrayList<>();
                Map<String, Boolean> ftpUploadResultMap = ftpUpload(threadCount, channelId, tempDir, errorDir);

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
                $info(String.format("upload image file count:%s:%s filePath=%s fileName=%s channelId=%s", readImgCount, upFtpSuccessFiles.size(), filePath, fileName, channelId));
                Collections.sort(upFtpSuccessFiles);
                Set<String> skuApprovedSet = new HashSet<>();
                for (String strFileName : upFtpSuccessFiles) {
                    String sku = strFileName.substring(8, strFileName.lastIndexOf("-"));
                    $info(String.format("fileName:%s sku:%s filePath=%s fileName=%s channelId=%s", strFileName, sku, filePath, fileName, channelId));
                    CmsBtProductModel model = productService.getProductBySku(channelId, sku);
                    if (model != null) {
                        mongoOperator(model, modifyDirName, strFileName);
                        if (skuApprovedSet.add(sku))
                            approvedOperator(model);
                    } else {
                        errorMsg.append(strFileName.split("-ftp-")[1]).append("：找不到指定商品");
                    }
                }
                $info(String.format("update products finish filePath=%s fileName=%s channelId=%s", filePath, fileName, channelId));
                //删除临时目录
                FileUtils.deleteDirectory(new File(tempDir));
                $info(String.format("remove tempDir finish filePath=%s fileName=%s channelId=%s", filePath, fileName, channelId));
                //移动文件到备份目录
                File backupFile = new File(buildDirPath(finalPath, "backup") + file.getName() + "." + System.currentTimeMillis());
                $info(String.format("move zipFile to backupDir[%s] start filePath=%s fileName=%s channelId=%s", backupFile.getPath(), filePath, fileName, channelId));
                FileUtils.moveFile(file, backupFile);
                $info(String.format("move zipFile to backupDir[%s] finish filePath=%s fileName=%s channelId=%s", backupFile.getPath(), filePath, fileName, channelId));

            } catch (Exception e) {
                errorMsg.append("其他异常：").append(e.getMessage());
                $error(String.format("CmsBulkUploadImageToS7Service error filePath=%s fileName=%s channelId=%s", filePath, fileName, channelId), e);
            }

            //上传日志
            logForUpload(file.getName(), readImgCount, readImgCount - failedImgCount, failedImgCount, errorMsg.toString(), channelId);
        }
    }

    /**
     * 取得上传的目录List
     *
     * @param taskControlList List
     * @return FileUploadBean List
     */
    private List<FileUploadBean> getFilePaths(List<TaskControlBean> taskControlList) {
        List<FileUploadBean> filePaths = new ArrayList<>();

        // 循环处理批量图片给上传
        for (TaskControlBean taskControl : taskControlList) {
            if ("order_channel_id".equals(taskControl.getCfg_name())) {
                String channelId = taskControl.getCfg_val1();
                String finalPath = taskControl.getCfg_val2();
                if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(finalPath)) {
                    $warn(String.format("CmsBulkUploadImageToS7Service channelId | finalPath  not found finalPath=%s", finalPath));
                    continue;
                }
                File filePath = new File(finalPath);
                if (!filePath.exists()) {
                    $warn(String.format("CmsBulkUploadImageToS7Service filePath not found finalPath=%s", finalPath));
                    continue;
                }
                if (!filePath.isDirectory()) {
                    $warn(String.format("CmsBulkUploadImageToS7Service filePath not isDirectory finalPath=%s", finalPath));
                    continue;
                }
                File[] files = filePath.listFiles();
                if (files == null || files.length == 0) {
                    continue;
                }
                for (File file : files) {
                    if (file.isDirectory() || file.getName().startsWith("images")) {
                        filePaths.add(new FileUploadBean(file.getPath(), channelId));
                    }
                }
            }
        }
        return filePaths;
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

        $info("deComparess start:" + zf.getName());
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
                $error("deComparess error:", e);
            }
        }

        $info("deComparess finish:" + zf.getName());
        zf.close();
    }

    /**
     * 上传图片到指定目录，并返回上传结果集
     *
     * @return 结果集 key 图片路径，value 上传结果
     */
    private Map<String, Boolean> ftpUpload(int threadCount, String channelId, String tempDir, String errorDir) throws InterruptedException {
        // FtpBean初期化
        $info("Prepare upload to image server (mid) ftp, tempDir %s, errorDir %s, channelId %s",
                tempDir, errorDir, channelId);

        String uploadPath = ChannelConfigs.getVal1(channelId, image_server_bulk_path);

        if (StringUtils.isEmpty(uploadPath)) {
            MissingImageServerFtpFolderException exception = new MissingImageServerFtpFolderException(channelId);
            $error(exception);
            throw exception;
        }

        Map<String, Boolean> uploadResultMap = new ConcurrentHashMap<>();

        //取得图片文件
        List<File> fileList = new ArrayList<>();
        filterImgFile(new File(tempDir), fileList);

        $info("Distribute upload task, fileCount %s, tempDir %s, errorDir %s, channelId%s", fileList.size(),
                tempDir, errorDir, channelId);

        // 线程List
        List<Runnable> threads = new ArrayList<>();
        List<List<File>> fileLists = Lists.partition(fileList, threadCount);

        final String finalUploadPath = uploadPath;
        for (List<File> subFileLists : fileLists) {
            if (subFileLists == null || subFileLists.isEmpty()) {
                continue;
            }
            threads.add(() -> uploadFiles(subFileLists, uploadResultMap, tempDir, errorDir, finalUploadPath, channelId));
        }

        // check threads
        if (!threads.isEmpty()) {
            ExecutorService pool = Executors.newFixedThreadPool(threads.size());
            threads.forEach(pool::execute);
            pool.shutdown();
            // 等待子线程结束，再继续执行下面的代码
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }

        $info("Finish upload to image server (mid) ftp, tempDir %s, errorDir %s, channelId %s",
                tempDir, errorDir, channelId);
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
            String fileName = file.getName().toLowerCase();
            if (file.isDirectory()) {
                filterImgFile(file, fileList);
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif") || fileName.endsWith(".jpeg")) {
                fileList.add(file);
            } else {
                $warn(String.format("%s:不支持上传的的文件类型", fileName));
            }
        }
    }

    /**
     * 上传图片到指定目录，并返回上传结果集
     */
    private void uploadFiles(List<File> fileList, Map<String, Boolean> uploadResultMap, String tempDir,
                             String errorDir, String uploadPath, String channelId) {

        BaseFtpComponent ftpComponent = FtpComponentFactory.get(FtpConstants.FtpConnectEnum.IS);

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
                    $info("upload %s success tempDir=%s", uploadFileName, tempDir);
                } catch (Exception e) {
                    $error(String.format("upload %s success tempDir=%s", uploadFileName, tempDir), e);
                    uploadResultMap.put(uploadFileName, false);
                    try {
                        FileUtils.copyFileToDirectory(imgFile, new File(errorDir), true);
                    } catch (IOException e1) {
                        $error("移动上传错误图片到error目录异常", e);
                    }
                }
            }
        } finally {
            ftpComponent.closeConnect();
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

            $info("mongoOperator success:" + code);
        } catch (Exception e) {
            $info(String.format("mongoOperator code:%s error:", code), e);
        }
    }

    /**
     * 更新Product Collection Data
     */
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
     * 批准操作
     *
     * @param model model
     */
    private void approvedOperator(CmsBtProductModel model) {
        sxProductService.insertSxWorkLoad(model, MODIFIER);
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
     * Created by DELL on 2016/7/8.
     */
    private class FileUploadBean {
        String filePath;
        String channelId;

        FileUploadBean(String filePath, String channelId) {
            this.filePath = filePath;
            this.channelId = channelId;
        }
    }

    private class MissingImageServerFtpFolderException extends RuntimeException {
        MissingImageServerFtpFolderException(String channelId) {
            super("没有为渠道 "+ channelId +" 在 tm_order_channel_config 表中配置 image_server_bulk_path 配置项");
        }
    }
}
