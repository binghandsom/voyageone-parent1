package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.PromotionDetailAddRequest;
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
public class CmsAddToPromotionService extends BaseAppService {

    @Autowired
    private VoApiDefaultClient voApiClient;

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;

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
    public void addToPromotion(Map<String, Object> params, UserSessionBean userInfo) {
        String channelId = userInfo.getSelChannelId();
        String modifier = userInfo.getUserName();

        Integer promotionId = Integer.valueOf(params.get("promotionId").toString());
        Integer tagId = Integer.valueOf(params.get("tagId").toString());
        Integer cartId = Integer.valueOf(params.get("cartId").toString());
        List<Long> productIds = CommonUtil.changeListType((ArrayList<Integer>) params.get("productIds"));


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
        Map<String, Object> result = productTagClient.addTagProducts(channelId, tagInfo.getTagPath(), productIds, modifier);
        if ("success".equals(result.get("result"))) {

            productIds.forEach(item -> {

                PromotionDetailAddRequest request=new PromotionDetailAddRequest();
                request.setModifier(modifier);
                request.setChannelId(channelId);
                request.setCartId(cartId);
                request.setProductId(Long.valueOf(item.toString()));
                request.setPromotionId(promotionId);
                request.setPromotionPrice(0.00);
                request.setTagId(tagInfo.getTagId());
                request.setTagPath(tagInfo.getTagPath());

                voApiClient.execute(request);

            });
        }
    }

    /**
     * 检测选中的tag是否存在
     * @param tags
     * @param tagName
     * @return
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
