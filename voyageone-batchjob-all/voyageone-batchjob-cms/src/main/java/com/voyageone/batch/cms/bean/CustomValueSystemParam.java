package com.voyageone.batch.cms.bean;

import java.util.List;

/**
 * Created by Leo on 15-6-18.
 */
public class CustomValueSystemParam {
    private String orderChannelId;
    private int cartId;
    private SxProductBean mainSxProduct;
    private List<SxProductBean> sxProductBeans;

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public SxProductBean getMainSxProduct() {
        return mainSxProduct;
    }

    public void setMainSxProduct(SxProductBean mainSxProduct) {
        this.mainSxProduct = mainSxProduct;
    }

    public List<SxProductBean> getSxProductBeans() {
        return sxProductBeans;
    }

    public void setSxProductBeans(List<SxProductBean> sxProductBeans) {
        this.sxProductBeans = sxProductBeans;
    }
}
