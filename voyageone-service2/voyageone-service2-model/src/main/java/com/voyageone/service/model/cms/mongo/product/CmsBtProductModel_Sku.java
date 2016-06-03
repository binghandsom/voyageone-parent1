package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.Enums.CartEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model Skus
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

    //priceSale
    public Double getPriceSale() {
        return getDoubleAttribute("priceSale");
    }
    public void setPriceSale(Double priceSale) {
        setAttribute("priceSale", priceSale);
    }

    //skuCarts
    public List<Integer> getSkuCarts() {
        if (!this.containsKey("skuCarts") || getAttribute("skuCarts") == null) {
            setAttribute("skuCarts", new ArrayList<Integer>());
        }
        return (List<Integer>) getAttribute("skuCarts");
    }
    public void setSkuCarts(List<Integer> skuCarts) {
        setAttribute("skuCarts", skuCarts);
    }
    public boolean isIncludeCart(CartEnums.Cart cartEnum) {
        boolean result = false;
        if (cartEnum != null) {
            result = isIncludeCart(Integer.parseInt(cartEnum.getId()));
        }
        return result;
    }
    public boolean isIncludeCart(int cartId) {
        List<Integer> carts = getSkuCarts();
        return carts.contains(cartId);
    }
    public void setCart(CartEnums.Cart cartEnum) {
        if (cartEnum != null) {
            setCart(Integer.parseInt(cartEnum.getId()));
        }
    }
    public void setCart(int cartId) {
        List<Integer> carts = getSkuCarts();
        if (!carts.contains(cartId)) {
            carts.add(cartId);
        }
    }

    //qty
    public int getQty() {
        return getAttribute("qty");
    }
    public void setQty(int qty) {
        setAttribute("qty", qty);
    }

    //clientMsrpPrice
    public Double getClientMsrpPrice () {
        return getDoubleAttribute("client_msrp_price");
    }
    public void setClientMsrpPrice (Double clientMsrpPrice) {
        setAttribute("client_msrp_price", clientMsrpPrice);
    }

    //clientRetailPrice
    public Double getClientRetailPrice () {
        return getDoubleAttribute("client_retail_price");
    }
    public void setClientRetailPrice (Double clientRetailPrice) {
        setAttribute("client_retail_price", clientRetailPrice);
    }

    //clientNetPrice
    public Double getClientNetPrice () {
        return getDoubleAttribute("client_net_price");
    }
    public void setClientNetPrice (Double clientNetPrice) {
        setAttribute("client_net_price", clientNetPrice);
    }

    // isSale
    public String getIsSale() {
        return getAttribute("isSale");
    }
    public void setIsSale(String isSale) {
        setAttribute("isSale", isSale);
    }

    //priceChgFlg
    public String getPriceChgFlg () {
        return getStringAttribute("priceChgFlg");
    }
    public void setPriceChgFlg (String priceChgFlg) {
        setAttribute("priceChgFlg", priceChgFlg);
    }
}
