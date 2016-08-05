package com.voyageone.web2.vms.views.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.vms.feed.FeedFileService;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.vms.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * VmsFeedFileUploadService
 * Created on 2016/5/5.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsFeedFileUploadService extends BaseAppService {

    private final static int FILE_LIMIT_SIZE = 52428800;

    private final static String CSV_TYPE = "csv";

    @Autowired
    private FeedFileService feedFileService;

    /**
     * 保存上传的FeedFile
     *
     * @param channelId 渠道
     * @param userName 用户名
     * @param file  上传的文件
     */
    public void saveFeedFile(String channelId, String userName, MultipartFile file) {

        // check
        doSaveFeedFileCheck(channelId, file);

        // 上传文件流
        try (InputStream inputStream = file.getInputStream();) {

            String newFileName = "Feed_" + channelId + DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv";

            // 保存文件
            feedFileService.saveOnlineFile(channelId, newFileName, inputStream);

            // 往vms_bt_feed_file表插入数据
            feedFileService.insertFeedFileInfo(channelId, file.getOriginalFilename(), newFileName, VmsConstants.FeedFileUploadType.ONLINE,
                    VmsConstants.FeedFileStatus.WAITING_IMPORT, userName);

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            $error(ex);
            // Failed to upload file.
            throw new BusinessException("8000016");
        }
    }

    /**
     * checkFeedFile
     *
     * @param channelId 渠道
     * @param uploadFile 上传的FeedFile文件
     */
    private void doSaveFeedFileCheck(String channelId, MultipartFile uploadFile) {

        // vms_bt_feed_file表存在状态为1：等待上传或者2：上传中的数据那么不允许上传
        List<VmsBtFeedFileModel> waitingImportModels = feedFileService.getFeedFileInfoByStatus(channelId, VmsConstants.FeedFileStatus.WAITING_IMPORT);
        List<VmsBtFeedFileModel> importingModels = feedFileService.getFeedFileInfoByStatus(channelId, VmsConstants.FeedFileStatus.IMPORTING);
        if (waitingImportModels.size() > 0 || importingModels.size() > 0) {
            // Have Feed file is processing, please upload later.
            throw new BusinessException("8000013", new Object[]{"feed"});
        }

        // 取得ftp测 Feed文件上传路径
        String feedFileFtpPath = com.voyageone.common.configs.Properties.readValue("vms.feed.ftp.upload");
        feedFileFtpPath +=  "/" + channelId + "/feed/";

        // 目录下有文件存在的话不允许上传（FTP有上传的情况下）
        File root = new File(feedFileFtpPath);
        // 扫描根目录下面的所有文件（不包含子目录）
        File[] files = root.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                // 只处理文件，跳过目录
                if (!file.isDirectory()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.lastIndexOf(".csv") > -1) {
                        if (".csv".equals(fileName.substring(fileName.length() - 4))) {
                            // Have Feed file is processing, please upload later.
                            throw new BusinessException("8000013", new Object[]{"feed"});
                        }
                    }
                }
            }
        }

        // 文件大小判断
        if (uploadFile.getSize() >= FILE_LIMIT_SIZE) {
            // The size of feed file exceeds the limit.
            throw new BusinessException("8000014", new Object[]{"feed"});
        }

        // 文件名
        String fileName = uploadFile.getOriginalFilename();

        // 获取文件后缀
        String suffix = null;
        if (fileName.lastIndexOf(".") > -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        // 判断后缀是否合法（csv）
        if (suffix == null || !CSV_TYPE.toLowerCase().contains(suffix.toLowerCase())) {
            // Please upload a feed file with csv format.
            throw new BusinessException("8000015", new Object[]{"feed"});
        }
    }
}