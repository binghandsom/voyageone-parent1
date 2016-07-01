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
        return getAttribute("skuCode");
    }
    public void setSkuCode(String skuCode) {
        setAttribute("skuCode", skuCode);
    }

    //clientSkuCode
    public String getClientSkuCode() {
        return getAttribute("clientSkuCode");
    }
    public void setClientSkuCode(String clientSkuCode) {
        setAttribute("clientSkuCode", clientSkuCode);
    }

    //barcode
    public String getBarcode() {
        return getAttribute("barcode");
    }
    public void setBarcode(String barcode) {
        setAttribute("barcode", barcode);
    }

    //size
    public String getSize() {
        return getAttribute("size");
    }
    public void setSize(String size) {
        setAttribute("size", size);
    }

    //clientSize
    public String getClientSize() {
        return getAttribute("clientSize");
    }
    public void setClientSize(String clientSize) {
        setAttribute("clientSize", clientSize);
    }

    //sizeNick
    public String getSizeNick() {
        return getAttribute("sizeNick");
    }
    public void setSizeNick(String sizeNick) {
        setAttribute("sizeNick", sizeNick);
    }

    //clientMsrpPrice
    public Double getClientMsrpPrice () {
        return getDoubleAttribute("clientMsrpPrice");
    }
    public void setClientMsrpPrice (Double clientMsrpPrice) {
        setAttribute("clientMsrpPrice", clientMsrpPrice);
    }

    //clientRetailPrice
    public Double getClientRetailPrice () {
        return getDoubleAttribute("clientRetailPrice");
    }
    public void setClientRetailPrice (Double clientRetailPrice) {
        setAttribute("clientRetailPrice", clientRetailPrice);
    }

    //clientNetPrice
    public Double getClientNetPrice () {
        return getDoubleAttribute("clientNetPrice");
    }
    public void setClientNetPrice (Double clientNetPrice) {
        setAttribute("clientNetPrice", clientNetPrice);
    }

    //priceMsrp
    public Double getPriceMsrp() {
        return getDoubleAttribute("priceMsrp");
    }
    public void setPriceMsrp(Double priceMsrp) {
        setAttribute("priceMsrp", priceMsrp);
    }

    //priceRetail
    public Double getPriceRetail() {
        return getDoubleAttribute("priceRetail");
    }
    public void setPriceRetail(Double priceRetail) {
        setAttribute("priceRetail", priceRetail);
    }

    //qty
    public Integer getQty() {
        return getAttribute("qty");
    }
    public void setQty(Integer qty) {
        setAttribute("qty", qty);
    }
}
