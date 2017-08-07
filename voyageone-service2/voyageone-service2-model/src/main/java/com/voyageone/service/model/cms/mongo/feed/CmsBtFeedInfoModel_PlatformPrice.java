package com.voyageone.service.model.cms.mongo.feed;

/**
 * Feed各平台价格
 *
 * @Author rex.wu
 * @Create 2017-08-07 11:18
 */
public class CmsBtFeedInfoModel_PlatformPrice {

    private Integer cartId;
    private Integer isSale;
    private Double priceClientMsrp;
    private Double priceClientRetail;
    private Double priceMsrp;
    private Double priceCurrent;
    private Integer sharingDay;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getIsSale() {
        return isSale;
    }

    public void setIsSale(Integer isSale) {
        this.isSale = isSale;
    }

    public Double getPriceClientMsrp() {
        return priceClientMsrp;
    }

    public void setPriceClientMsrp(Double priceClientMsrp) {
        this.priceClientMsrp = priceClientMsrp;
    }

    public Double getPriceClientRetail() {
        return priceClientRetail;
    }

    public void setPriceClientRetail(Double priceClientRetail) {
        this.priceClientRetail = priceClientRetail;
    }

    public Double getPriceMsrp() {
        return priceMsrp;
    }

    public void setPriceMsrp(Double priceMsrp) {
        this.priceMsrp = priceMsrp;
    }

    public Double getPriceCurrent() {
        return priceCurrent;
    }

    public void setPriceCurrent(Double priceCurrent) {
        this.priceCurrent = priceCurrent;
    }

    public Integer getSharingDay() {
        return sharingDay;
    }

    public void setSharingDay(Integer sharingDay) {
        this.sharingDay = sharingDay;
    }
}
