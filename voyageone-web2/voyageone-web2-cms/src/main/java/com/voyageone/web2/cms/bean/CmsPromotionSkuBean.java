package com.voyageone.web2.cms.bean;

/**
 * @author james.li on 2015/12/16.
 * @version 2.0.0
 */
public class CmsPromotionSkuBean {
    private String productSku;
    private Integer qty;

    public CmsPromotionSkuBean(){

    }
    public CmsPromotionSkuBean(String productSku, Integer qty){
        this.productSku = productSku;
        this.qty = qty;
    }
    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
