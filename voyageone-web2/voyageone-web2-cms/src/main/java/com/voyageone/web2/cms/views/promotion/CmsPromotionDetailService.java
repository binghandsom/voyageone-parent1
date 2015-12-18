package com.voyageone.web2.cms.views.promotion;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.dao.CmsPromotionCodeDao;
import com.voyageone.web2.cms.dao.CmsPromotionDao;
import com.voyageone.web2.cms.dao.CmsPromotionModelDao;
import com.voyageone.web2.cms.dao.CmsPromotionSkuDao;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
import com.voyageone.web2.cms.model.CmsBtPromotionGroupModel;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import com.voyageone.web2.cms.model.CmsBtPromotionSkuModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    protected VoApiDefaultClient voApiClient;

    @Autowired
    CmsPromotionModelDao cmsPromotionModelDao;

    @Autowired
    CmsPromotionCodeDao cmsPromotionCodeDao;

    @Autowired
    CmsPromotionSkuDao cmsPromotionSkuDao;

    @Autowired
    private CmsPromotionDao cmsPromotionDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

    /**
     * promotion商品插入
     *
     * @param productPrices 需要插入的Product列表
     * @param promotionId   活动ID
     * @param operator      操作者
     * @return Map  成功和失败的列表
     */
    public Map<String, List<String>> insertPromotionProduct(List<CmsPromotionProductPriceBean> productPrices, int promotionId, String operator) {

        Map<String, List<String>> response = new HashMap<>();
        response.put("succeed", new ArrayList<>());
        response.put("fail", new ArrayList<>());

        CmsBtPromotionModel promotion = cmsPromotionDao.getPromotionById(promotionId);
        if (promotion == null) {
            logger.info("promotionId不存在：" + promotionId);
            productPrices.forEach(m -> {
                response.get("fail").add(m.getCode());
            });
            return response;
        }
        String channelId = promotion.getChannelId();
        Integer cartId = promotion.getCartId();
        productPrices.forEach(item -> {
            boolean errflg = false;
            simpleTransaction.openTransaction();
            try {
                // 获取Product信息
                //CmsBtProductModel productInfo = cmsProductService.getProductByCode(channelId, item.getCode());
                //设置参数
                PostProductSelectOneRequest requestModel = new PostProductSelectOneRequest(channelId);
                requestModel.setProductCode(item.getCode());
                //SDK取得Product 数据
                CmsBtProductModel productInfo = voApiClient.execute(requestModel).getProduct();


                // 插入cms_bt_promotion_model表
                CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId, operator);
                cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

                // 插入cms_bt_promotion_code表
                CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId, operator);
                cmsBtPromotionCodeModel.setPromotionPrice(item.getPrice());
                if (cmsPromotionCodeDao.updatePromotionModel(cmsBtPromotionCodeModel) == 0) {
                    cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodeModel);
                }

                productInfo.getSkus().forEach(sku -> {
                    CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId, operator, sku.getSkuCode(), sku.getQty());
                    if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModel) == 0) {
                        cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
                    }
                });
            } catch (Exception e) {
                simpleTransaction.rollback();
                response.get("fail").add(item.getCode());
                errflg = true;
            }
            if (!errflg) {
                simpleTransaction.commit();
                response.get("succeed").add(item.getCode());
            }
        });
        return response;
    }

    public List<Map<String, Object>> getPromotionGroup(Map<String, Object> param) {
        List<Map<String, Object>> promotionGroups = cmsPromotionModelDao.getPromotionModelDetailList(param);
        promotionGroups.forEach(map -> {
            //CmsBtProductModel cmsBtProductModel = cmsProductService.getProductById("300", (Long) map.get("productId"));
            //是否需要提供函数？
            //设置参数
            PostProductSelectOneRequest requestModel = new PostProductSelectOneRequest("300");
            requestModel.setProductId((Long) map.get("productId"));
            //SDK取得Product 数据
            CmsBtProductModel cmsBtProductModel = voApiClient.execute(requestModel).getProduct();

            if(cmsBtProductModel != null) {
                map.put("image", cmsBtProductModel.getFields().getImages1().get(0).getName());
            }
        });

        return promotionGroups;
    }
}
