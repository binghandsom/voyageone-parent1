package com.voyageone.service.bean.cms.businessmodel.PromotionProduct;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/27.
 */
public class UpdateListPromotionProductTagParameter {
    List<Integer> listPromotionProductId;
    private Map<String, Object> searchInfo;
    private boolean selAll;
    /**
     * 操作类型，0：修改；-1删除，默认是修改
     */
    private int actionType;
    private Integer promotionId; // 非聚美活动ID
    private String key; // 非聚美活动检索key

    public List<Integer> getListPromotionProductId() {
        return listPromotionProductId;
    }

    public void setListPromotionProductId(List<Integer> listPromotionProductId) {
        this.listPromotionProductId = listPromotionProductId;
    }

    List<ProductTagInfo> tagList;

    public List<ProductTagInfo> getTagList() {
        return tagList;
    }

    public void setTagList(List<ProductTagInfo> tagList) {
        this.tagList = tagList;
    }

    public Map<String, Object> getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(Map<String, Object> searchInfo) {
        this.searchInfo = searchInfo;
    }

    public boolean isSelAll() {
        return selAll;
    }

    public void setSelAll(boolean selAll) {
        this.selAll = selAll;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}


