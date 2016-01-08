package com.voyageone.web2.cms.views.pop.tag.promotion;

import com.voyageone.common.util.CommonUtil;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.TagsGetRequest;
import com.voyageone.web2.sdk.api.service.ProductTagClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsPromotionSelectService extends BaseAppService {

    @Autowired
    private VoApiDefaultClient voApiClient;

    @Autowired
    private ProductTagClient productTagClient;

    public List<CmsBtTagModel> getPromotionTags(Map<String, Object> params) {
        int tag_id = (int) params.get("refTagId");
        return this.selectListByParentTagId(tag_id);
    }

    /**
     * 获取二级Tag
     */
    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        //设置参数
        TagsGetRequest requestModel = new TagsGetRequest();
        requestModel.setParentTagId(parentTagId);
        return voApiClient.execute(requestModel).getTags();
    }

    /**
     * addToPromotion
     */
    public Map<String, Object> addToPromotion(Map<String, Object> params, String channelId, String modifier) {
        String tag_path = params.get("tagPath").toString();
        List<Long> productIds = CommonUtil.changeListType((ArrayList<Integer>) params.get("productIds"));
        return productTagClient.addTagProducts(channelId, tag_path, productIds, modifier);
    }

    /**
     * promotion商品插入
     *
     * @param productPrices 需要插入的Product列表
     * @param promotionId   活动ID
     * @param operator      操作者
     * @return Map  成功和失败的列表
     */
    /*
    public Map<String, List<String>> insertPromotionProduct(List<CmsPromotionProductPriceBean> productPrices, int promotionId, String operator) {

        Map<String, List<String>> response = new HashMap<>();
        response.put("succeed", new ArrayList<>());
        response.put("fail", new ArrayList<>());

        // 获取promotion信息
        CmsBtPromotionModel promotion = cmsPromotionDao.getPromotionById(promotionId);
        if (promotion == null) {
            logger.info("promotionId不存在：" + promotionId);
            productPrices.forEach(m -> {
                response.get("fail").add(m.getCode());
            });
            return response;
        }
        // 获取Tag列表
        List<CmsBtTagModel> tags = cmsPromotionSelectService.selectListByParentTagId(promotion.getRefTagId());
        String channelId = promotion.getChannelId();
        Integer cartId = promotion.getCartId();
        productPrices.forEach(item -> {
            boolean errflg = false;
            simpleTransaction.openTransaction();
            try {

                CmsBtTagModel tagId = searchTag(tags, item.getTag());
                if (tagId == null) {
                    throw (new Exception("Tag不存在"));
                }

                // 获取Product信息
                CmsBtProductModel productInfo = ProductGetClient.getProductByCode(channelId, item.getCode());

                // 插入cms_bt_promotion_model表
                CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId, operator);
                cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

                // 插入cms_bt_promotion_code表
                CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId, operator);
                cmsBtPromotionCodeModel.setPromotionPrice(item.getPrice());
                cmsBtPromotionCodeModel.setTagId(tagId.getTagId());
                if (cmsPromotionCodeDao.updatePromotionCode(cmsBtPromotionCodeModel) == 0) {
                    cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodeModel);
                }

                productInfo.getSkus().forEach(sku -> {
                    CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId, operator, sku.getSkuCode(), 0);
                    if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModel) == 0) {
                        cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
                    }
                });

                // tag写入数据库
                List<Long> prodIds = new ArrayList<>();
                prodIds.add(productInfo.getProdId());
                //liang change
                //cmsPromotionSelectService.add(prodIds, channelId, tagId.getTagPath(), operator);
                productTagClient.addTagProducts(channelId, tagId.getTagPath(), prodIds, operator);
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
    }*/
}
