package com.voyageone.web2.cms.views.promotion;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.masterdate.schema.Util.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.dao.CmsPromotionCodeDao;
import com.voyageone.web2.cms.dao.CmsPromotionModelDao;
import com.voyageone.web2.cms.dao.CmsPromotionSkuDao;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
import com.voyageone.web2.cms.model.CmsBtPromotionGroupModel;
import com.voyageone.web2.cms.model.CmsBtPromotionSkuModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsPromotionDetailService extends BaseAppService {

    @Autowired
    CmsProductService cmsProductService;

    @Autowired
    CmsPromotionModelDao cmsPromotionModelDao;

    @Autowired
    CmsPromotionCodeDao cmsPromotionCodeDao;

    @Autowired
    CmsPromotionSkuDao cmsPromotionSkuDao;
    @Autowired
    private SimpleTransaction simpleTransaction;

    public Map<String, List<String>> insertPromotionProduct(List<CmsPromotionProductPriceBean> productPrices, String channelId, int promotionId, int cartId, String operator) {

        Map<String, List<String>> response = new HashMap<>();
        response.put("succeed",new ArrayList<>());
        response.put("fail",new ArrayList<>());
        productPrices.forEach(item -> {
            boolean errflg = false;
            simpleTransaction.openTransaction();
            try {
                // 获取Product信息
                CmsBtProductModel productInfo = cmsProductService.getProductByCode(channelId, item.getCode());

                // 插入cms_bt_promotion_model表
                CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId, operator);
                cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

                // 插入cms_bt_promotion_code表
                CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId, operator);
                cmsBtPromotionCodeModel.setPromotionPrice(item.getPrice());
                if(cmsPromotionCodeDao.updatePromotionModel(cmsBtPromotionCodeModel) == 0){
                    cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodeModel);
                }

                productInfo.getSkus().forEach(sku -> {
                    CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId, operator, sku.getSkuCode(), sku.getQty());
                    if(cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModel) == 0){
                        cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
                    }
                });
            } catch (Exception e) {
                simpleTransaction.rollback();
                response.get("fail").add(item.getCode());
                errflg = true;
            }
            if(!errflg){
                simpleTransaction.commit();
                response.get("succeed").add(item.getCode());
            }
        });
        return response;
    }
}
