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
public class JmImageUploadService  extends BaseViewService {

    @Autowired
    CmsBtJmImageTemplateService cmsBtJmImageTemplateService;


    public Map<String, Object> uploadImage(MultipartFile file, Long promotionId, String imageName, UserSessionBean user) throws Exception {

        Map<String, Object> response = new HashMap<>();

        String orderChannelId = user.getSelChannelId();

        //FTP服务器保存目录设定
        String uploadPath = ChannelConfigs.getVal1(user.getSelChannelId(), ChannelConfigEnums.Name.scene7_image_folder);
        if (StringUtils.isEmpty(uploadPath)) {
            String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表", orderChannelId);
            $error(orderChannelId);
            throw new BusinessException(err);
        }

        String upLoadName = promotionId + "-" + imageName;

        HttpScene7.uploadImageFile(uploadPath, upLoadName, file.getInputStream());

        response.put("templateUrl", cmsBtJmImageTemplateService.getUrl(upLoadName,"appEntrance",Integer.parseInt(String.valueOf(promotionId))));

        return response;
    }

}
