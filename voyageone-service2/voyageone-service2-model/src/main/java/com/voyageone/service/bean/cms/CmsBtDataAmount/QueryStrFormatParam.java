package com.voyageone.service.bean.cms.CmsBtDataAmount;
public class QueryStrFormatParam {
    int cartId;

    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    String queryStr;
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
