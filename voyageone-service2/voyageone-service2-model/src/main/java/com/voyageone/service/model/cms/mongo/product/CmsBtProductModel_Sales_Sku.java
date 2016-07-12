package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.Map;

/**
 * CmsBtProductModel_Sales
 *
 * @author chuanyu.liang, 2016/06/03
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Sales_Sku extends BaseMongoMap<String, Object> {
    public CmsBtProductModel_Sales_Sku() {
    }

    public CmsBtProductModel_Sales_Sku(Map<String, Object> map) {
        this.putAll(map);
    }

    public String getSkuCode() {
        return getAttribute("skuCode");
    }
    public void setSkuCode(String skuCode) {
        setAttribute("skuCode", skuCode);
    }

    public int getCartId() {
        return getAttribute("cartId");
    }
    public void setCartId(int cartId) {
        setAttribute("cartId", cartId);
    }

    //code_sum_7
    public Integer getSkuSum7() {
        return getAttribute(CmsBtProductModel_Sales.CODE_SUM_7);
    }
    public void setSkuSum7(Integer qty) {
        setAttribute(CmsBtProductModel_Sales.CODE_SUM_7, qty);
    }

    //code_sum_30
    public Integer getSkuSum30() {
        return getAttribute(CmsBtProductModel_Sales.CODE_SUM_30);
    }
    public void setSkuSum30(Integer qty) {
        setAttribute(CmsBtProductModel_Sales.CODE_SUM_30, qty);
    }

    //code_sum_all
    public Integer getSkuSumAll() {
        return getAttribute(CmsBtProductModel_Sales.CODE_SUM_ALL);
    }
    public void setSkuSumAll(Integer qty) {
        setAttribute(CmsBtProductModel_Sales.CODE_SUM_ALL, qty);
    }
}
