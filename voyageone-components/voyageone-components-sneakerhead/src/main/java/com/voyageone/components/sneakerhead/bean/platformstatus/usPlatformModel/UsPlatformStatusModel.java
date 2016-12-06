package com.voyageone.components.sneakerhead.bean.platformstatus.usPlatformModel;


import com.voyageone.components.sneakerhead.enums.UsPlatformStatus;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
public class UsPlatformStatusModel {
    private String cartId;
    private UsPlatformStatus status;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public UsPlatformStatus getStatus() {
        return status;
    }

    public void setStatus(UsPlatformStatus status) {
        this.status = status;
    }
}
