package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

/**
 * Feed各平台价格
 *
 * @Author rex.wu
 * @Create 2017-08-07 11:18
 */
public class CmsBtFeedInfoModel_Platform_Cart extends BaseMongoMap<String, Object> {

    private Integer cartId;
    private Integer isSale;
    private Double priceClientMsrp;
    private Double priceClientRetail;
    private Double priceMsrp;
    private Double priceCurrent;
    private Integer sharingDay;

    public Integer getCartId() {
        return getIntAttribute("cartId");
    }

    public void setCartId(Integer cartId) {
        setAttribute("cartId", cartId);
    }

    public Integer getIsSale() {
        return getIntAttribute("isSale");
    }

    public void setIsSale(Integer isSale) {
        setAttribute("isSale", isSale);
    }

    public Double getPriceClientMsrp() {
        return getDoubleAttribute("priceClientMsrp");
    }

    public void setPriceClientMsrp(Double priceClientMsrp) {
        setAttribute("priceClientMsrp", priceClientMsrp);
    }

    public Double getPriceClientRetail() {
        return getDoubleAttribute("priceClientRetail");
    }

    public void setPriceClientRetail(Double priceClientRetail) {
        setAttribute("priceClientRetail", priceClientRetail);
    }

    public Double getPriceMsrp() {
        return getDoubleAttribute("priceMsrp");
    }

    public void setPriceMsrp(Double priceMsrp) {
        setAttribute("priceMsrp", priceMsrp);
    }

    public Double getPriceCurrent() {
        return getDoubleAttribute("priceCurrent");
    }

    public void setPriceCurrent(Double priceCurrent) {
        setAttribute("priceCurrent", priceCurrent);
    }

    public Integer getSharingDay() {
        return getIntAttribute("sharingDay");
    }

    public void setSharingDay(Integer sharingDay) {
        setAttribute("sharingDay", sharingDay);
    }
}
