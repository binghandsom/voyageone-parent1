package com.voyageone.web2.cms.bean;

import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;

import java.util.List;

/**
 * Created by Ethan Shi on 2016/5/25.
 */
public class SellerCatRequestBean {

    private String cId;

    private String catName;

    private  int cartId;

    private List<CmsBtSellerCatModel> sellerCatList;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<CmsBtSellerCatModel> getSellerCatList() {
        return sellerCatList;
    }

    public void setSellerCatList(List<CmsBtSellerCatModel> sellerCatList) {
        this.sellerCatList = sellerCatList;
    }
}
