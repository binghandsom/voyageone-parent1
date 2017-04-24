package com.voyageone.service.bean.cms.jumei;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/31.
 */
public class BatchDeleteProductParameter {
    List<Integer> listPromotionProductId;
    int promotionId;
    List<String> listProductCode;
    Map<String, Object> searchInfo;
    boolean selAll;

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

    public List<String> getListProductCode() {
        return listProductCode;
    }

    public void setListProductCode(List<String> listProductCode) {
        this.listProductCode = listProductCode;
    }




    public int getPromotionId() {
        return promotionId;
    }
    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public List<Integer> getListPromotionProductId() {
        return listPromotionProductId;
    }

    public void setListPromotionProductId(List<Integer> listPromotionProductId) {
        this.listPromotionProductId = listPromotionProductId;
    }
}
