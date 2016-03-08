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

    public String getSkuCode() {
        return getAttribute("skuCode");
    }

    public void setSkuCode(String skuCode) {
        setAttribute("skuCode", skuCode);
    }

    public String getBarcode() {
        return getAttribute("barcode");
    }

    public void setBarcode(String barcode) {
        setAttribute("barcode", barcode);
    }

    public String getSize() {
        return getAttribute("size");
    }

    public void setSize(String size) {
        setAttribute("size", size);
    }

    public Double getPriceMsrp() {
        return getAttribute("priceMsrp");
    }

    public void setPriceMsrp(Double priceMsrp) {
        setAttribute("priceMsrp", priceMsrp);
    }

    public Double getPriceRetail() {
        return getAttribute("priceRetail");
    }

    public void setPriceRetail(Double priceRetail) {
        setAttribute("priceRetail", priceRetail);
    }

    public Double getPriceSale() {
        // 注: 有时候这个值在数据库里被读出来会变成int型, 为了避免错误, 增加了转换
        Object result = getAttribute("priceSale");
        if (result == null) {
            return null;
        } else {
            return Double.parseDouble(result.toString());
        }
    }

    public void setPriceSale(Double priceSale) {
        setAttribute("priceSale", priceSale);
    }

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

    public int getQty() {
        return getAttribute("qty");
    }

    public void setQty(int qty) {
        setAttribute("qty", qty);
    }
}