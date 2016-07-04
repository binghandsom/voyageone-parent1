package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.Map;

/**
 * 的商品Model Skus
 * @author linanbin on 6/29/2016
 * @version 2.2.0
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Sku extends BaseMongoMap<String, Object> {

    public CmsBtProductModel_Sku() {
    }

    public CmsBtProductModel_Sku(Map m) {
        this.putAll(m);
    }

    //skuCode
    public String getSkuCode() {
        return getStringAttribute("skuCode");
    }
    public void setSkuCode(String skuCode) {
        setStringAttribute("skuCode", skuCode);
    }

    //clientSkuCode
    public String getClientSkuCode() {
        return getStringAttribute("clientSkuCode");
    }
    public void setClientSkuCode(String clientSkuCode) {
        setStringAttribute("clientSkuCode", clientSkuCode);
    }

    //barcode
    public String getBarcode() {
        return getStringAttribute("barcode");
    }
    public void setBarcode(String barcode) {
        setStringAttribute("barcode", barcode);
    }

    //size
    public String getSize() {
        return getStringAttribute("size");
    }
    public void setSize(String size) {
        setStringAttribute("size", size);
    }

    //clientSize
    public String getClientSize() {
        return getStringAttribute("clientSize");
    }
    public void setClientSize(String clientSize) {
        setStringAttribute("clientSize", clientSize);
    }

    //sizeNick
    public String getSizeNick() {
        return getStringAttribute("sizeNick");
    }
    public void setSizeNick(String sizeNick) {
        setStringAttribute("sizeNick", sizeNick);
    }

    //clientMsrpPrice
    public Double getClientMsrpPrice () {
        return getDoubleAttribute("clientMsrpPrice");
    }
    public void setClientMsrpPrice (Double clientMsrpPrice) {
        setAttribute("clientMsrpPrice", clientMsrpPrice == null ? 0.00 : clientMsrpPrice);
    }

    //clientRetailPrice
    public Double getClientRetailPrice () {
        return getDoubleAttribute("clientRetailPrice");
    }
    public void setClientRetailPrice (Double clientRetailPrice) {
        setAttribute("clientRetailPrice", clientRetailPrice == null ? 0.00 : clientRetailPrice);
    }

    //clientNetPrice
    public Double getClientNetPrice () {
        return getDoubleAttribute("clientNetPrice");
    }
    public void setClientNetPrice (Double clientNetPrice) {
        setAttribute("clientNetPrice", clientNetPrice == null ? 0.00 : clientNetPrice);
    }

    //priceMsrp
    public Double getPriceMsrp() {
        return getDoubleAttribute("priceMsrp");
    }
    public void setPriceMsrp(Double priceMsrp) {
        setAttribute("priceMsrp", priceMsrp == null ? 0.00 : priceMsrp);
    }

    //priceRetail
    public Double getPriceRetail() {
        return getDoubleAttribute("priceRetail");
    }
    public void setPriceRetail(Double priceRetail) {
        setAttribute("priceRetail", priceRetail == null ? 0.00 : priceRetail);
    }

    //qty
    public Integer getQty() {
        return getIntAttribute("qty");
    }
    public void setQty(Integer qty) {
        setAttribute("qty", qty == null ? 0 : qty);
    }
}
