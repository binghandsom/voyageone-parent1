package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by james.li on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class OrderLookupItem {

    private String itemId;
    private String productName;
    private String manufacturerPartNumber;
    private String status;
    private String statusCode;
    private String cancelReasonCode;
    private String errorMessage;
    private String inStorePickupStatus;
    private String expectedInStoreDate;
    private int quantity;
    private int billableQuantity;
    private float price;
    private float subTotal;
    private float shipping;
    private float shipTax;
    private float tax;
    private float shippingAdjustment;
    private float grandTotal;
    private String salesCheckNumber;
    private String trackingNumbers;

    // trackingNumbers 再分配使用
    private List<OrderLookupItemSub> trackingNumberList;

    private String shippingDate;
    private String shippingCarrier;
    private String expectedDateOfArrival;
    private int returnedQuantity;
    private String shippingMode;
    private String expectedShipDate;
    private float customsDuty;
    private float itemPrice;
    private float shipCharge;
    // TODO 与CreateOrder（fee） 不一致
    private OrderFeesBean fees;

    private OrderDeliveryBean delivery;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCancelReasonCode() {
        return cancelReasonCode;
    }

    public void setCancelReasonCode(String cancelReasonCode) {
        this.cancelReasonCode = cancelReasonCode;
    }

    public String getInStorePickupStatus() {
        return inStorePickupStatus;
    }

    public void setInStorePickupStatus(String inStorePickupStatus) {
        this.inStorePickupStatus = inStorePickupStatus;
    }

    public String getExpectedInStoreDate() {
        return expectedInStoreDate;
    }

    public void setExpectedInStoreDate(String expectedInStoreDate) {
        this.expectedInStoreDate = expectedInStoreDate;
    }

    public int getBillableQuantity() {
        return billableQuantity;
    }

    public void setBillableQuantity(int billableQuantity) {
        this.billableQuantity = billableQuantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public float getShipTax() {
        return shipTax;
    }

    public void setShipTax(float shipTax) {
        this.shipTax = shipTax;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getShippingAdjustment() {
        return shippingAdjustment;
    }

    public void setShippingAdjustment(float shippingAdjustment) {
        this.shippingAdjustment = shippingAdjustment;
    }

    public float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getSalesCheckNumber() {
        return salesCheckNumber;
    }

    public void setSalesCheckNumber(String salesCheckNumber) {
        this.salesCheckNumber = salesCheckNumber;
    }

    public String getTrackingNumbers() {
        return trackingNumbers;
    }

    public void setTrackingNumbers(String trackingNumbers) {
        this.trackingNumbers = trackingNumbers;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getShippingCarrier() {
        return shippingCarrier;
    }

    public void setShippingCarrier(String shippingCarrier) {
        this.shippingCarrier = shippingCarrier;
    }

    public String getExpectedDateOfArrival() {
        return expectedDateOfArrival;
    }

    public void setExpectedDateOfArrival(String expectedDateOfArrival) {
        this.expectedDateOfArrival = expectedDateOfArrival;
    }

    public int getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(int returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public String getShippingMode() {
        return shippingMode;
    }

    public void setShippingMode(String shippingMode) {
        this.shippingMode = shippingMode;
    }

    public String getExpectedShipDate() {
        return expectedShipDate;
    }

    public void setExpectedShipDate(String expectedShipDate) {
        this.expectedShipDate = expectedShipDate;
    }

    public OrderDeliveryBean getDelivery() {
        return delivery;
    }

    public void setDelivery(OrderDeliveryBean delivery) {
        this.delivery = delivery;
    }

    public List<OrderLookupItemSub> getTrackingNumberList() {
        return trackingNumberList;
    }

    public void setTrackingNumberList(List<OrderLookupItemSub> trackingNumberList) {
        this.trackingNumberList = trackingNumberList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
