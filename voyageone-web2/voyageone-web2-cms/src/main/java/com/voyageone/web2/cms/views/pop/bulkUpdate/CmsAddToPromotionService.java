package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsAddToPromotionService extends BaseViewService {

    @Autowired
    private TagService tagService;
    @Autowired
    private CmsPromotionIndexService cmsPromotionService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductTagService productTagService;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private CmsBtBrandBlockService brandBlockService;

    public List<CmsBtTagModel> getPromotionTags(Map<String, Object> params) {
        //fix error by holysky
        int tag_id = Integer.parseInt(String.valueOf(params.get("refTagId")));
        return this.selectListByParentTagId(tag_id);
    }

    /**
     * 获取二级Tag
     */
    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        return tagService.getListByParentTagId(parentTagId);
    }

    /**
     * 检查所选择的产品是否已存在同级别的promotion tag
     * 已存在则返回 {'hasTags':true, 'prodCodeListStr':'code1, code2, code3'}
     * 不存在则返回 {'hasTags':false}
     */
    public Map<String, Object> checkPromotionTags(String channelId, Map<String, Object> params) {
        int tagId = (Integer) params.get("tagId");
        List<Long> productIds = CommonUtil.changeListType((List<Integer>) params.get("productIds"));
        CmsBtTagModel tagBean = tagService.getTagByTagId(tagId);
        List<CmsBtTagModel> modelList = tagService.getListBySameLevel(channelId, tagBean.getParentTagId(), tagId);

        Map<String, Object> result = new HashMap<>();
        List<String> tagList = new ArrayList<>();
        modelList.forEach(model -> tagList.add(model.getTagPath()));
        if (tagList.isEmpty()) {
            result.put("hasTags", false);
            return result;
        }

        JongoQuery queryObject = new JongoQuery();
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("{");
        queryStr.append(MongoUtils.splicingValue("prodId", productIds.toArray(), "$in"));
        queryStr.append(",");
        queryStr.append(MongoUtils.splicingValue("tags", tagList.toArray(new String[tagList.size()]), "$in"));
        queryStr.append("}");
        queryObject.setQuery(queryStr.toString());
        queryObject.setProjection("{'common.fields.code':1,'_id':0}");
        List<CmsBtProductModel> prodInfoList = productService.getList(channelId, queryObject);
        if (prodInfoList.isEmpty()) {
            result.put("hasTags", false);
            return result;
        }

        List<String> codeList = new ArrayList<>();
        prodInfoList.forEach(model -> codeList.add(model.getCommon().getFields().getCode()));

        result.put("hasTags", true);
        result.put("prodCodeListStr", StringUtils.join(codeList, ", "));
        return result;
    }

    /**
     * addToPromotion
     */
    public void addToPromotion(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        String channelId = userInfo.getSelChannelId();
        String modifier = userInfo.getUserName();

        Integer promotionId = Integer.valueOf(params.get("promotionId").toString());
        Integer tagId = Integer.valueOf(params.get("tagId").toString());
        Integer cartId = Integer.valueOf(params.get("cartId").toString());
        if (cartId == 0) {
            $warn("addToPromotion cartId为空 params=" + params.toString());
            throw new BusinessException("未选择平台");
        }

        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }
        List<Long> productIds = null;
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productIds = advanceSearchService.getProductIdList(userInfo.getSelChannelId(), cmsSession);
        } else {
            productIds = CommonUtil.changeListType((ArrayList<Integer>) params.get("productIds"));
        }
        if (productIds == null || productIds.isEmpty()) {
            $warn("addToPromotion 未选择商品 params=" + params.toString());
            throw new BusinessException("未选择商品");
        }

        // 获取promotion信息
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        if (promotion == null) {
            $warn("addToPromotion promotionId不存在 params=" + params.toString());
            throw new BusinessException("promotionId不存在：" + promotionId);
        }

        // 获取Tag列表
        List<CmsBtTagModel> tags = selectListByParentTagId(promotion.getRefTagId());
        CmsBtTagModel tagInfo = searchTag(tags, tagId);
        if (tagInfo == null) {
            $warn("addToPromotion tagInfo不存在 params=" + params.toString());
            throw new BusinessException("Tag不存在：" + tagInfo.getTagPathName());
        }

        // 检查品牌黑名单
        Iterator<Long> iter = productIds.iterator();
        while (iter.hasNext()) {
            Long prodId = iter.next();
            CmsBtProductModel prodObj = productService.getProductById(channelId, prodId);
            if (prodObj == null) {
                $warn("addToPromotion CmsBtProductModel不存在 channelId=%s, prodId=%d", channelId, prodId);
                iter.remove();
                continue;
            }
            String prodCode = StringUtils.trimToNull(prodObj.getCommonNotNull().getFieldsNotNull().getCode());
            if (prodCode == null) {
                $warn("addToPromotion CmsBtProductModel不存在(没有code) channelId=%s, prodId=%d", channelId, prodId);
                iter.remove();
                continue;
            }
            // 取得feed 品牌
            CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(channelId, prodCode);
            String feedBrand = null;
            if (cmsBtFeedInfoModel == null) {
                $warn("addToPromotion CmsBtFeedInfoModel channelId=%s, code=%s", channelId, prodCode);
            } else {
                feedBrand = cmsBtFeedInfoModel.getBrand();
            }
            String masterBrand = prodObj.getCommonNotNull().getFieldsNotNull().getBrand();
            String platBrand = prodObj.getPlatformNotNull(cartId).getpBrandId();
            if (brandBlockService.isBlocked(channelId, cartId, feedBrand, masterBrand, platBrand)) {
                $warn("addToPromotion 该品牌为黑名单 channelId=%s, cartId=%d, code=%s, feed brand=%s, master brand=%s, platform brand=%s", channelId, cartId, prodCode, feedBrand, masterBrand, platBrand);
                iter.remove();
                continue;
            }
        }
        if (productIds.isEmpty()) {
            $info("addToPromotion：没有商品需要加入活动");
            return;
        }

        // 给产品数据添加活动标签
        productTagService.addProdTag(channelId, tagInfo.getTagPath(), productIds);
        productIds.forEach(item -> {
            PromotionDetailAddBean request = new PromotionDetailAddBean();
            request.setModifier(modifier);
            request.setChannelId(promotion.getChannelId());
            request.setOrgChannelId(channelId);
            request.setCartId(cartId);
            request.setProductId(item);
            request.setPromotionId(promotionId);
            request.setTagId(tagInfo.getId());
            request.setTagPath(tagInfo.getTagPath());
            promotionDetailService.addPromotionDetail(request, false);
        });
    }

    /**
     * 检测选中的tag是否存在
     *
     * @param tags    List<CmsBtTagModel>
     * @param tagName Integer
     * @return CmsBtTagModel
     */
    private CmsBtTagModel searchTag(List<CmsBtTagModel> tags, Integer tagName) {
        for (CmsBtTagModel tag : tags) {
            if (tag.getId() == tagName.intValue()) {
                return tag;
            }
        }
        return null;
    }
}
