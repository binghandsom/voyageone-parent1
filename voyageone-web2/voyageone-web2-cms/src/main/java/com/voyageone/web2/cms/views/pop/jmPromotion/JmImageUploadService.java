package com.voyageone.web2.cms.views.pop.jmPromotion;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
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

    @Autowired
    CmsBtJmImageTemplateService cmsBtJmImageTemplateService;

    public Map<String, Object> uploadImage(MultipartFile file, Long promotionId, String imageName, UserSessionBean user) throws Exception {

        Map<String, Object> response = new HashMap<>();

        String orderChannelId = user.getSelChannelId(),
                upLoadName = promotionId + "-" + imageName;

        uploadToServer(orderChannelId, upLoadName, file);

        response.put("templateUrl", cmsBtJmImageTemplateService.getUrl(upLoadName, imageName, Integer.parseInt(String.valueOf(promotionId))));

        return response;
    }

    public Map<String, Object> batchUploadImage(MultipartFile file, int promotionId, String imageType, UserSessionBean user) {

        Map<String, Object> response = new HashMap<>();
        String orderChannelId = user.getSelChannelId(),
                upLoadName = promotionId + "-" + imageType;

        try {
            uploadToServer(orderChannelId, upLoadName, file);
            response.put("result","success");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result","error");
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
