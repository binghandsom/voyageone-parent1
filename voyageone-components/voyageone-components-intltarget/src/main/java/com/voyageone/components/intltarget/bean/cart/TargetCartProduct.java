package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartProduct {

    private String orderId;//	1609501	Order Id of the order

    private String orderItemId;//	12105008	Order Item Id of the item ordered

    private String Title;//	Children’s Rest Mat	The name of the product

    private String quantity;//	1	Quantity of the product required by the user.

    private String Availabiltiy;//	AVL	Specifies availability of the inventory

    private List<TargetCartAttachment> attachments;//

    private String attrName;//	DeliveryMethod	Specifies Item attribute name

    private String attrValue;//	Mail	Specifies Item attribute Value

    /***********  getter setter  *********/

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAvailabiltiy() {
        return Availabiltiy;
    }

    public void setAvailabiltiy(String availabiltiy) {
        Availabiltiy = availabiltiy;
    }

    public List<TargetCartAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<TargetCartAttachment> attachments) {
        this.attachments = attachments;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
