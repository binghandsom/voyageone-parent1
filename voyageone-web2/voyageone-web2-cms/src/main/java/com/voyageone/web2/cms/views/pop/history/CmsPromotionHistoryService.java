package com.voyageone.web2.cms.views.pop.history;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.service.bean.cms.CmsBtPromotionHistoryBean;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;

/**
 * @author gubuchun 15/12/21
 * @version 2.0.0
 */
@Service
public class CmsPromotionHistoryService extends BaseViewService {

    @Autowired
    private PromotionService promotionService;

    public Map<String, Object> getPromotionList(Map<String, Object> params, UserSessionBean userInfo, String language) {
        params.put("channelId", userInfo.getSelChannelId());
        params.put("lang", language);

        Map<String, Object> result = promotionService.getPromotionHistory(params);

        // 获取cart list
        result.put("cartList", TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(),Constants.comMtTypeChannel.SKU_CARTS_53_D,  language));
        return result;
    }
    
    public Map<String, List<Map<String, Object>>> getUnduePromotion(Map<String, Object> params, UserSessionBean userInfo, String language) {
        params.put("channelId", userInfo.getSelChannelId());
        params.put("lang", language);
        return promotionService.getUnduePromotion(params);
    }

}
