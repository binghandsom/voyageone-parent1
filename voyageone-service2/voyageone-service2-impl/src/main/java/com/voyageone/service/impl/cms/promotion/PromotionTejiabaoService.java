package com.voyageone.service.impl.cms.promotion;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtTaskTejiabaoDao;
import com.voyageone.service.daoext.cms.CmsBtTaskTejiabaoDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionSkusModel;
import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/2/14.
 * 更新全店特价宝的价格
 */
@Service
public class PromotionTejiabaoService extends BaseService {
    private final PromotionTaskService promotionTaskService;

    private final PromotionSkuService promotionSkuService;

    @Autowired
    public PromotionTejiabaoService(PromotionTaskService promotionTaskService, PromotionSkuService promotionSkuService) {
        this.promotionTaskService = promotionTaskService;
        this.promotionSkuService = promotionSkuService;
    }

    public void updateTejiabaoPrice(String channelId, Integer cartId, String code, List<BaseMongoMap<String, Object>> skus, String modifier){

        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId
                , CmsConstants.ChannelConfig.TEJIABAO_ID
                , cartId.toString());
        if(cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())){
            return;
        }
        Integer promotionId = Integer.parseInt(cmsChannelConfigBean.getConfigValue1());


        for(BaseMongoMap<String, Object> sku : skus) {
            CmsBtPromotionSkusModel cmsBtPromotionSkusModel = promotionSkuService.get(promotionId, code, sku.getStringAttribute(Platform_SKU_COM.skuCode.name()));
            if (cmsBtPromotionSkusModel != null) {
                BigDecimal promotionPrice = new BigDecimal(sku.getDoubleAttribute(Platform_SKU_COM.priceSale.name()));
                if (cmsBtPromotionSkusModel.getPromotionPrice().compareTo(promotionPrice) != 0) {
                    cmsBtPromotionSkusModel.setPromotionPrice(promotionPrice);
                    cmsBtPromotionSkusModel.setModifier(modifier);
                    promotionSkuService.update(cmsBtPromotionSkusModel);
                }
            }
        }
        CmsBtTaskTejiabaoModel cmsBtTaskTejiabaoModel = promotionTaskService.get(promotionId, code);
        if(cmsBtTaskTejiabaoModel != null && (cmsBtTaskTejiabaoModel.getSynFlg() == 2 || cmsBtTaskTejiabaoModel.getSynFlg() == 3)){
            cmsBtTaskTejiabaoModel.setSynFlg(1);
            cmsBtTaskTejiabaoModel.setModifier(modifier);
            promotionTaskService.updatePromotionTask(cmsBtTaskTejiabaoModel);
        }

    }

}
