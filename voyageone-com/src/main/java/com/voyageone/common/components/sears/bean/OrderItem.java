package com.voyageone.common.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james.li on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class OrderItem {

    private String itemId;
    private int quantity;
    private double itemPrice;
    private double shipCharge;
    private double customsDuty;
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

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getShipCharge() {
        return shipCharge;
    }

    public void setShipCharge(double shipCharge) {
        this.shipCharge = shipCharge;
    }

    public double getCustomsDuty() {
        return customsDuty;
    }

    public void setCustomsDuty(double customsDuty) {
        this.customsDuty = customsDuty;
    }

    public OrderFeesBean getFees() {
        return fees;
    }

    public void setFees(OrderFeesBean fees) {
        this.fees = fees;
    }
}
