package com.voyageone.service.bean.cms.producttop;

import java.util.List;

/**
 * Created by dell on 2016/11/29.
 */
public class AddTopProductParameter {

    int cartId;//平台id
    String sellerCatId;//店铺内分类

    public String getSellerCatId() {
        return sellerCatId;
    }

    public void setSellerCatId(String sellerCatId) {
        this.sellerCatId = sellerCatId;
    }

    List<String> codeList;//   /Code
    ProductPageParameter searchParameter;//查询参数
    boolean isSeachAdd;//是否搜索 全量加入


    public boolean isSeachAdd() {
        return isSeachAdd;
    }

    public void setSeachAdd(boolean seachAdd) {
        isSeachAdd = seachAdd;
    }

    public ProductPageParameter getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(ProductPageParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

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
