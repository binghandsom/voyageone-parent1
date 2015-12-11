package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmsBtProductModel_Sku extends BaseMongoMap {

    public CmsBtProductModel_Sku() {

    }
    public CmsBtProductModel_Sku(Map m) {
        this.putAll(m);
    }


    public String getSku() {
        return (String) getAttribute("sku");
    }

    public void setSku(String sku) {
        setAttribute("sku", sku);
    }

    public String getUpc() {
        return (String) getAttribute("upc");
    }

    public void setUpc(String upc) {
        setAttribute("upc", upc);
    }

    public double getCostPrice() {
        return (double) getAttribute("costPrice");
    }

    public void setCostPrice(double costPrice) {
        setAttribute("costPrice", costPrice);
    }

    public double getPriceMsrp() {
        return (double) getAttribute("priceMsrp");
    }

    public void setPriceMsrp(double priceMsrp) {
        setAttribute("priceMsrp", priceMsrp);
    }

    public double getPriceRetail() {
        return (double) getAttribute("priceRetail");
    }

    public void setPriceRetail(double priceRetail) {
        setAttribute("priceRetail", priceRetail);
    }

    public double getPriceSale() {
        return (double) getAttribute("priceSale");
    }

    public void setPriceSale(double priceSale) {
        setAttribute("priceSale", priceSale);
    }

    public List<Integer> getCarts() {
        if (!this.containsKey("carts") || getAttribute("carts") == null) {
            setAttribute("carts", new ArrayList<Integer>());
        }
        return (List<Integer>) getAttribute("carts");
    }

    public void setCarts(List<Integer> carts) {
        setAttribute("carts", carts);
    }

    public boolean isIncludeCart(Constants.CartEnum cartEnum) {
        boolean result = false;
        if (cartEnum != null) {
            result = isIncludeCart(cartEnum.getName());
        }
        return result;
    }

    public boolean isIncludeCart(int cartId) {
        List<Integer> carts = getCarts();
        return carts.contains(cartId);
    }

    public void setCart(Constants.CartEnum cartEnum) {
        if (cartEnum != null) {
            setCart(cartEnum.getName());
        }
    }

    public void setCart(int cartId) {
        List<Integer> carts = getCarts();
        if (!carts.contains(cartId)) {
            carts.add(cartId);
        }
    }

}