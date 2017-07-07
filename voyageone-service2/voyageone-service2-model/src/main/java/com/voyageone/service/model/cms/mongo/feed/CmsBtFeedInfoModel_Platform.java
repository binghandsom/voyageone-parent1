package com.voyageone.service.model.cms.mongo.feed;

import java.util.Map;

/**
 * 美国CMS2 Feed Approve时 USA&&CN 各平台信息
 *
 * @Author rex.wu
 * @Create 2017-07-06 14:03
 */
public class CmsBtFeedInfoModel_Platform {

   private Double msrp;
   private Double price;

    /**
     * KV: 平台ID-Days Old Before Sharing
     * <p>Days Old Before Sharing 默认0</p>
     */
   private Map<Integer, Integer> carts;

    public Double getMsrp() {
        return msrp;
    }

    public void setMsrp(Double msrp) {
        this.msrp = msrp;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Map<Integer, Integer> getCarts() {
        return carts;
    }

    public void setCarts(Map<Integer, Integer> carts) {
        this.carts = carts;
    }
}
