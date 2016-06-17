package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.Enums.CartEnums;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model Skus
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_CommonSku extends BaseMongoMap<String, Object> {

    public CmsBtProductModel_CommonSku() {
    }

    public CmsBtProductModel_CommonSku(Map m) {
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
        // 过滤数据中的double型
        List<Object> val = getAttribute("skuCarts");
        List<Integer> rs = new ArrayList<Integer>(val.size());
        for (Object cartId : val) {
            if (cartId == null) {
                continue;
            }
            if (cartId instanceof  Number) {
                rs.add(((Number) cartId).intValue());
            } else {
                int cVal = NumberUtils.toInt(cartId.toString(), -1);
                if (cVal >= 0) {
                    rs.add(cVal);
                }
            }
        }
        return rs;
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
    public Integer getQty() {
        return getAttribute("qty");
    }
    public void setQty(Integer qty) {
        setAttribute("qty", qty);
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

    //priceChgFlg
    public String getPriceChgFlg () {
        return getStringAttribute("priceChgFlg");
    }
    public void setPriceChgFlg (String priceChgFlg) {
        setAttribute("priceChgFlg", priceChgFlg);
    }
}
