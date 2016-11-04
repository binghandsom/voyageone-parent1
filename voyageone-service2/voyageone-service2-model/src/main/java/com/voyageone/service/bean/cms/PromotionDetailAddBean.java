package com.voyageone.service.bean.cms;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/1/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionDetailAddBean {

    private Integer tagId;
    private String tagPath;

    private Integer promotionId;
    private Integer cartId;
    private String channelId;
    private String orgChannelId;
    private Map<String, Double> promotionPrice;
    private String productCode;
    private Long productId;
    private List<TagTreeNode> tagList;
    public List<TagTreeNode> getTagList() {
        return tagList;
    }
    public void setTagList(List<TagTreeNode> tagList) {
        this.tagList = tagList;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Map<String, Double> getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Map<String, Double> promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getOrgChannelId() {
        return orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

    /**
     *modifier
     */
    protected String modifier;
    public String getModifier() {
        return modifier;
    }
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return JsonUtil.getJsonString(this);
    }
}
