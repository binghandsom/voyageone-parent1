package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsAddToPromotionService extends BaseAppService {

    @Autowired
    private TagService tagService;

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private PromotionDetailService promotionDetailService;

    public List<CmsBtTagModel> getPromotionTags(Map<String, Object> params) {
        int tag_id = Integer.valueOf(params.get("refTagId").toString());
        return this.selectListByParentTagId(tag_id);
    }

    /**
     * 获取二级Tag
     */
    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        return tagService.getListByParentTagId(parentTagId);
    }

    /**
     * addToPromotion
     */
    public void addToPromotion(Map<String, Object> params, UserSessionBean userInfo) {
        String channelId = userInfo.getSelChannelId();
        String modifier = userInfo.getUserName();

        Integer promotionId = Integer.valueOf(params.get("promotionId").toString());
        Integer tagId = Integer.valueOf(params.get("tagId").toString());
        Integer cartId = Integer.valueOf(params.get("cartId").toString());
        List<Long> productIds = CommonUtil.changeListType((ArrayList<Integer>) params.get("productIds"));
        List<Map<String, String>> products = (ArrayList<Map<String, String>>) params.get("products");


        // 获取promotion信息
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        if (promotion == null) {
            throw new BusinessException("promotionId不存在：" + promotionId);
        }

        // 获取Tag列表
        List<CmsBtTagModel> tags = selectListByParentTagId(promotion.getRefTagId());
        CmsBtTagModel tagInfo = searchTag(tags, tagId);
        if (tagInfo == null) {
            throw new BusinessException("Tag不存在：" + tagInfo.getTagPathName());
        }

        // 给产品数据添加活动标签
        Map<String, Object> result = addTagProducts(channelId, tagInfo.getTagPath(), productIds, modifier);
        if ("success".equals(result.get("result"))) {

            products.forEach(item -> {

                PromotionDetailAddBean request=new PromotionDetailAddBean();
                request.setModifier(modifier);
                request.setChannelId(promotion.getChannelId());
                request.setOrgChannelId(channelId);
                request.setCartId(cartId);
                request.setProductId(Long.valueOf(String.valueOf(item.get("id"))));
                request.setProductCode(String.valueOf(item.get("code")));
                request.setPromotionId(promotionId);
                request.setPromotionPrice(0.00);
                request.setTagId(tagInfo.getTagId());
                request.setTagPath(tagInfo.getTagPath());

                promotionDetailService.addPromotionDetail(request);

            });
        }
    }

    /**
     * 增加商品的Tag
     */
    private Map<String, Object> addTagProducts(String channelId, String tagPath, List<Long> productIds, String modifier) {
        productTagService.saveTagProducts(channelId, tagPath, productIds, modifier);

        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "success");
        return ret;
    }

    /**
     * 检测选中的tag是否存在
     * @param tags List<CmsBtTagModel>
     * @param tagName Integer
     * @return CmsBtTagModel
     */
    private CmsBtTagModel searchTag(List<CmsBtTagModel> tags, Integer tagName) {

        for (CmsBtTagModel tag : tags) {
            if (tag.getTagId().equals(tagName)) {
                return tag;
            }
        }
        return null;
    }
}
