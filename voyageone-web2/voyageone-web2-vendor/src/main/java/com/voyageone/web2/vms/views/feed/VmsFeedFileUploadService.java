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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * CmsImageGroupDetail Service
 *
 * @author jeff.duan 2016/5/5.
 * @version 2.0.0
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
        InputStream inputStream = null;
        try {
            // 本地上传的场合
            if (file != null) {
                inputStream = file.getInputStream();
            }
        } catch (IOException ignored) {
        }

        // 上传文件失败
        if (inputStream == null) {
            // Failed to upload file.
            throw new BusinessException("8000016");
        }

        String newFileName = "Feed_" + channelId + DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv";

        // 往vms_bt_feed_file表插入数据
        Integer id = feedFileService.insertFeedFileInfo(channelId, file.getOriginalFilename(), newFileName, VmsConstants.FeedFileStatus.WAITING_IMPORT, userName);

        // 保存文件
        try {
            feedFileService.saveFile(channelId, newFileName, inputStream);
        } catch (Exception ex) {
            // 删除之前插入的数据，之所以先更新DB再保存文件，是因为batch也有读取相同目录下FTP上传的文件。
            // 不希望客户系统上传的文件被batch读到。
            feedFileService.deleteFeedFileInfo(id);
            throw ex;
        }
    }

    /**
     * checkFeedFile
     *
     * @param channelId 渠道
     * @param uploadFile 上传的FeedFile文件
     */
    private void doSaveFeedFileCheck(String channelId, MultipartFile uploadFile) {

        // 取得Feed文件上传路径
        String feedFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.upload");
        feedFilePath +=  "/" + channelId + "/feed/";

        // vms_bt_feed_file表存在状态为1：等待上传的数据也是不允许上传
        List<VmsBtFeedFileModel> models = feedFileService.getFeedFileInfoByStatus(channelId, VmsConstants.FeedFileStatus.WAITING_IMPORT);
        if (models.size() > 0) {
            // Have Feed file is processing, please upload later.
            throw new BusinessException("8000013");
        }

        // 目录下有文件存在的话不允许上传（FTP有上传的情况下）
        File root = new File(feedFilePath);
        // 扫描根目录下面的所有文件（不包含子目录）
        File[] files = root.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                // 只处理文件，跳过目录
                if (!file.isDirectory()) {
                    // Have Feed file is processing, please upload later.
                    throw new BusinessException("8000013");
                }
            }
        }

        // 文件大小判断
        if (uploadFile.getSize() >= FILE_LIMIT_SIZE) {
            // The size of feed file exceeds the limit.
            throw new BusinessException("8000014");
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
            throw new BusinessException("8000015");
        }
    }
}