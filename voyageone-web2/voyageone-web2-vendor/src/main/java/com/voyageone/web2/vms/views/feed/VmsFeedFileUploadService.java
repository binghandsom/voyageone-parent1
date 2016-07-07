package com.voyageone.web2.vms.views.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.web2.base.BaseAppService;
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

//    @Autowired
//    private FeedFileUploadService feedFileUploadService;

    /**
     * 保存ImageGroup中的图片信息
     *
     * @param param 客户端参数
     * @param file  导入文件
     */
    public void saveImage(Map<String, Object> param, MultipartFile file) {

        // check
        doSaveFeedFileCheck(file);

        boolean uploadFlg = true;
        // 上传文件流
        InputStream inputStream = null;
        try {
            // 本地上传的场合
            if (file != null) {
                inputStream = file.getInputStream();
            }
        } catch (IOException ignored) {
        }

        // 上传文件
//            feedFileUploadService.uploadFile((String) param.get("channelId"), inputStream);

        //更新vms_bt_feed_file表
//        feedFileUploadService.addFeedFile();

        // 发MQ

    }

    /**
     * checkFeedFile
     *
     * @param file 上传的FeedFile文件
     */
    private void doSaveFeedFileCheck(MultipartFile file) {

        // 文件名
        String fileName = "";
        InputStream inputStream = null;

        // 本地上传的场合
        if (file.getSize() >= FILE_LIMIT_SIZE) {
            // 图片大小不能超过3M
            throw new BusinessException("7000087");
        }
        fileName = file.getOriginalFilename();
        try {
            inputStream = file.getInputStream();
        } catch (IOException ignored) {
        }

        // 获取文件后缀
        String suffix = null;
        if (fileName.lastIndexOf(".") > -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        // 判断后缀是否合法（csv）
        if (suffix == null || !CSV_TYPE.toLowerCase().contains(suffix.toLowerCase())) {
            // 文件扩展名非法
            throw new BusinessException("7000084");
        }
    }
}