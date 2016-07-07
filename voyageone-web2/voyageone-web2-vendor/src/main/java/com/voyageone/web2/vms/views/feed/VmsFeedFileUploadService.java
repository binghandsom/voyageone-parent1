package com.voyageone.web2.vms.views.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.vms.feed.FeedFileUploadService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.vms.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CmsImageGroupDetail Service
 *
 * @author jeff.duan 2016/5/5.
 * @version 2.0.0
 */
@Service
public class VmsFeedFileUploadService extends BaseAppService {

    private final static String URL_PREFIX = "http://image.voyageone.com.cn/cms";

    private final static int FILE_LIMIT_SIZE = 50145728;

    private final static String CSV_TYPE = "csv";

    @Autowired
    private MqSender sender;

    @Autowired
    private FeedFileUploadService feedFileUploadService;

    /**
     * 保存上传的FeedFile
     *
     * @param channelId 渠道
     * @param userName 用户名
     * @param file  上传的文件
     */
    public void saveFeedFile(String channelId, String userName, MultipartFile file) {

        // check
        doSaveFeedFileCheck(file);

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
            // TODO 上传文件失败
            throw new BusinessException("7000087");
        }

        // 上传文件
        String newFileName= feedFileUploadService.saveFile(channelId, file.getOriginalFilename(), inputStream);

        //更新vms_bt_feed_file表
        // feedFileUploadService.insertFeedFileInfo(channelId, file.getOriginalFilename(), newFileName, VmsConstants.FeedFileStatus.WAITING_IMPORT, userName);
    }

    /**
     * checkFeedFile
     *
     * @param file 上传的FeedFile文件
     */
    private void doSaveFeedFileCheck(MultipartFile file) {

        // 文件名
        String fileName = "";

        // 文件大小判断
        if (file.getSize() >= FILE_LIMIT_SIZE) {
            // TODO FeedFile大小不能超过3M
            throw new BusinessException("7000087");
        }
        fileName = file.getOriginalFilename();

        // 获取文件后缀
        String suffix = null;
        if (fileName.lastIndexOf(".") > -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        // 判断后缀是否合法（csv）
        if (suffix == null || !CSV_TYPE.toLowerCase().contains(suffix.toLowerCase())) {
            // TODO 文件扩展名非法
            throw new BusinessException("7000084");
        }
    }
}