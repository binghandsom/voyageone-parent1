package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author gubuchun 15/12/21
 * @version 2.0.0
 */
@Service
public class CmsPromotionHistoryService extends BaseAppService {

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

}
