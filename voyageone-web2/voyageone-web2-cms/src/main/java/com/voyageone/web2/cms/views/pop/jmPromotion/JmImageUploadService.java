package com.voyageone.web2.cms.views.pop.jmPromotion;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.HttpScene7;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.jumei.CmsBtJmImageTemplateService;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author piao
 * @version 2.8.0
 * @since 2.8.0
 */
@Service
public class JmImageUploadService extends BaseViewService {

    private static String ORIGINAL_SCENE7_IMAGE_URL = "http://s7d5.scene7.com/is/image/sneakerhead/✓?fmt=jpg&scl=1&qlt=100";

    @Autowired
    CmsBtJmImageTemplateService cmsBtJmImageTemplateService;

    public Map<String, Object> uploadImage(MultipartFile file, Long promotionId, String imageType, UserSessionBean user, boolean useTemplate) throws Exception {

        Map<String, Object> response = new HashMap<>();

        String orderChannelId = user.getSelChannelId(),
                templateUrl = null,
                upLoadName = promotionId + "-" + imageType + "-" + DateTimeUtil.getNowTimeStampLong();

        uploadToServer(orderChannelId, upLoadName, file);

        if (useTemplate == true) {
            templateUrl = cmsBtJmImageTemplateService.getUrl(upLoadName, imageType, Integer.parseInt(String.valueOf(promotionId)));
        } else {
            templateUrl = ORIGINAL_SCENE7_IMAGE_URL.replace("✓",upLoadName);
        }

        response.put("templateUrl", templateUrl);
        response.put("imageName", upLoadName);

        return response;
    }

    public Map<String, Object> batchUploadImage(MultipartFile file, String upLoadName, UserSessionBean user) {

        Map<String, Object> response = new HashMap<>();
        String orderChannelId = user.getSelChannelId();

        try {
            uploadToServer(orderChannelId, upLoadName, file);
            response.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
        }

        return response;
    }

    private void uploadToServer(String orderChannelId, String upLoadName, MultipartFile file) throws Exception {
        //FTP服务器保存目录设定
        String uploadPath = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.scene7_image_folder);
        if (StringUtils.isEmpty(uploadPath)) {
            String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表", orderChannelId);
            $error(orderChannelId);
            throw new BusinessException(err);
        }

        HttpScene7.uploadImageFile(uploadPath, upLoadName, file.getInputStream());
    }

}
