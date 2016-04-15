package com.voyageone.components.intltarget.bean.cart;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartShippingAddress {

    private String addressId;

    private String orderItemid;

    /********* getter setter  *****/

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getOrderItemid() {
        return orderItemid;
    }

    public void setOrderItemid(String orderItemid) {
        this.orderItemid = orderItemid;
    }
}

