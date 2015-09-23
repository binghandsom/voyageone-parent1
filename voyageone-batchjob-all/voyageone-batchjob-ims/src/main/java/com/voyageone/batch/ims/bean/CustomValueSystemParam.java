package com.voyageone.batch.ims.bean;

import com.voyageone.batch.ims.modelbean.CmsCodePropBean;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;

/**
 * Created by Leo on 15-6-18.
 */
public class CustomValueSystemParam {
    private String orderChannelId;
    private int cartId;
    private CmsCodePropBean mainProductProp;
    private CmsModelPropBean cmsModelProp;

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

    public CmsCodePropBean getMainProductProp() {
        return mainProductProp;
    }

    public void setMainProductProp(CmsCodePropBean mainProductProp) {
        this.mainProductProp = mainProductProp;
    }

    public CmsModelPropBean getCmsModelProp() {
        return cmsModelProp;
    }

    public void setCmsModelProp(CmsModelPropBean cmsModelProp) {
        this.cmsModelProp = cmsModelProp;
    }
}
