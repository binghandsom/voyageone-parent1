package com.voyageone.service.bean.cms.jumei;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/30.
 */
public class BatchSynchPriceParameter {

    List<Long> listPromotionProductId;
    int promotionId;
    private Map<String, Object> searchInfo;
    private boolean selAll;

    public int getPromotionId() {
        return promotionId;
    }
    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public List<Long> getListPromotionProductId() {
        return listPromotionProductId;
    }

    public void setListPromotionProductId(List<Long> listPromotionProductId) {
        this.listPromotionProductId = listPromotionProductId;
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
}
