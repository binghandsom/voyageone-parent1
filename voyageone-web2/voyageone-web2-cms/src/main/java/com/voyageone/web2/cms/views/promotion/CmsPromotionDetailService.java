package com.voyageone.web2.cms.views.promotion;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.common.masterdate.schema.Util.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.dao.CmsPromotionCodeDao;
import com.voyageone.web2.cms.dao.CmsPromotionModelDao;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
import com.voyageone.web2.cms.model.CmsBtPromotionGroupModel;
import com.voyageone.web2.cms.model.CmsBtPromotionSkuModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Map<String, Object> insertPromotionProduct(List<CmsPromotionProductPriceBean> productPrices, String channelId, int promotionId, int cartId, String operator) {


        productPrices.forEach(item -> {
            // 获取Product信息
            CmsBtProductModel productInfo = cmsProductService.getProductByCode(channelId, item.getCode());

            // 插入cms_bt_promotion_model表
            CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId,operator);
            cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

            // 插入cms_bt_promotion_code表
            CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId,operator);
            cmsBtPromotionCodeModel.setPromotionPrice(item.getPrice());
            cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodeModel);

            CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId,operator);


        });
        return null;
    }

//    /**
//     * PromotionGroupModel数据做成
//     * @param productInfo 成品信息
//     * @return PromotionGroupModel数据
//     */
//    private CmsBtPromotionGroupModel promotionGroupSetter(CmsBtProductModel productInfo, int cartId, int promotionId, String operator){
//        CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId,operator);
//
//        return cmsBtPromotionGroupModel;
//    }
//
//    /**
//     * PromotionCodeModel数据做成
//     * @param productInfo 成品信息
//     * @return PromotionCodeModel数据
//     */
//    private CmsBtPromotionCodeModel promotionCodeSetter(CmsBtProductModel productInfo, int cartId, int promotionId, String operator){
//
//        CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId,operator);
//
//        return cmsBtPromotionCodeModel;
//    }
//
//    /**
//     * PromotionSkuModel数据做成
//     * @param productInfo 成品信息
//     * @return PromotionSkuModel数据
//     */
//    private CmsBtPromotionSkuModel promotionSkuSetter(CmsBtProductModel productInfo, int cartId, int promotionId, String operator){
//        CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId,operator);
//
//        return cmsBtPromotionSkuModel;
//    }
}
