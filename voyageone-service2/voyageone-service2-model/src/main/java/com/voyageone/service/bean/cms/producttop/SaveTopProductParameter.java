package com.voyageone.service.bean.cms.producttop;

import java.util.List;

/**
 * Created by dell on 2016/11/29.
 */
public class SaveTopProductParameter {

    int cartId;//平台id
    String sellerCatId;//店铺内分类

    public String getSellerCatId() {
        return sellerCatId;
    }

    public void setSellerCatId(String sellerCatId) {
        this.sellerCatId = sellerCatId;
    }


    List<String> codeList;//   /Code

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

}
