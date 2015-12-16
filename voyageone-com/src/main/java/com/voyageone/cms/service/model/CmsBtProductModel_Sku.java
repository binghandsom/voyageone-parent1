package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.Constants;
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
public class CmsBtProductModel_Sku extends BaseMongoMap<Object, Object> {

    public CmsBtProductModel_Sku() {

    }
    public CmsBtProductModel_Sku(Map m) {
        this.putAll(m);
    }


    public String getSku() {
        return getAttribute("sku");
    }

    public void setSku(String sku) {
        setAttribute("sku", sku);
    }

    public String getUpc() {
        return getAttribute("upc");
    }

    public void setUpc(String upc) {
        setAttribute("upc", upc);
    }

    public double getCostPrice() {
        return getAttribute("costPrice");
    }

    public void setCostPrice(double costPrice) {
        setAttribute("costPrice", costPrice);
    }

    public double getPriceMsrp() {
        return getAttribute("priceMsrp");
    }

    public void setPriceMsrp(double priceMsrp) {
        setAttribute("priceMsrp", priceMsrp);
    }

    public double getPriceRetail() {
        return getAttribute("priceRetail");
    }

    public void setPriceRetail(double priceRetail) {
        setAttribute("priceRetail", priceRetail);
    }

    public double getPriceSale() {
        return getAttribute("priceSale");
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

    public boolean isIncludeCart(CartEnums.Cart cartEnum) {
        boolean result = false;
        if (cartEnum != null) {
            result = isIncludeCart(Integer.parseInt(cartEnum.getId()));
        }
        return result;
    }

    public boolean isIncludeCart(int cartId) {
        List<Integer> carts = getCarts();
        return carts.contains(cartId);
    }

    public void setCart(CartEnums.Cart cartEnum) {
        if (cartEnum != null) {
            setCart(Integer.parseInt(cartEnum.getId()));
        }
    }

    public void setCart(int cartId) {
        List<Integer> carts = getCarts();
        if (!carts.contains(cartId)) {
            carts.add(cartId);
        }
    }

}