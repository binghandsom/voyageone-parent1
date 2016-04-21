package com.voyageone.service.model.jumei.businessmodel.PromotionProduct;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2016/4/14.
 */
public class ParameterUpdateDealEndTimeAll {
    int promotionId;
    Date dealEndTime;
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
