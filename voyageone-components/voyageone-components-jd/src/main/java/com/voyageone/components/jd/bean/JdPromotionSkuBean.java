package com.voyageone.components.jd.bean;

/**
 * 京东添加到促销的SKU对象结构
 *
 * Created by desmond on 2016/11/17.
 */
public class JdPromotionSkuBean {

    private String skuCode;               // skuCode

    private String jdSkuId;               // 京东skuId

    private String jdPrice;               // 京东价

    private String jdPromoPrice;          // 促销价

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getJdSkuId() {
        return jdSkuId;
    }

    public void setJdSkuId(String jdSkuId) {
        this.jdSkuId = jdSkuId;
    }

    public String getJdPrice() {
        return jdPrice;
    }

    public void setJdPrice(String jdPrice) {
        this.jdPrice = jdPrice;
    }

    public String getJdPromoPrice() {
        return jdPromoPrice;
    }

    public void setJdPromoPrice(String jdPromoPrice) {
        this.jdPromoPrice = jdPromoPrice;
    }
}
