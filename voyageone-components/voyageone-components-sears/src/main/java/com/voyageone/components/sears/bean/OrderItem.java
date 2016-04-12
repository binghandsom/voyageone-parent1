package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james.li on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class OrderItem {

    private String itemId;
    private int quantity;
    private float itemPrice;
    private float shipCharge;
    private float customsDuty;
    private OrderFeesBean fees;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public float getShipCharge() {
        return shipCharge;
    }

    public void setShipCharge(float shipCharge) {
        this.shipCharge = shipCharge;
    }

    public float getCustomsDuty() {
        return customsDuty;
    }

    public void setCustomsDuty(float customsDuty) {
        this.customsDuty = customsDuty;
    }

    public OrderFeesBean getFees() {
        return fees;
    }

    public void setFees(OrderFeesBean fees) {
        this.fees = fees;
    }
}
