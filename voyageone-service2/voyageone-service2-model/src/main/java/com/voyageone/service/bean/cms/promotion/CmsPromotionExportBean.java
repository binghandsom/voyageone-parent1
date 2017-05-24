package com.voyageone.service.bean.cms.promotion;

/**
 * @author james.li on 2016/8/2.
 * @version 2.0.0
 */
public class CmsPromotionExportBean {
    private String numIid;
    private Integer promotionPrice;
    private Integer msrpPrice;
    private Integer qty;

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public Integer getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Integer promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public Integer getMsrpPrice() {
        return msrpPrice;
    }

    public void setMsrpPrice(Integer msrpPrice) {
        this.msrpPrice = msrpPrice;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
