package com.voyageone.service.bean.cms.businessmodel.PromotionProduct;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ParameterUpdateDealEndTime implements Serializable {
    int promotionId;
    List<Integer> productIdList;
    Date dealEndTime;

    public List<Integer> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List<Integer> productIdList) {
        this.productIdList = productIdList;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public Date getDealEndTime() {
        return dealEndTime;
    }

    public void setDealEndTime(Date dealEndTime) {
        this.dealEndTime = dealEndTime;
    }
}
