
package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QuantityInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuantityInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Available" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OpenAllocated" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OpenUnallocated" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PendingCheckout" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PendingPayment" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PendingShipment" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Total" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OpenAllocatedPooled" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OpenUnallocatedPooled" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PendingCheckoutPooled" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PendingPaymentPooled" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PendingShipmentPooled" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalPooled" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuantityInfoResponse", propOrder = {
    "available",
    "openAllocated",
    "openUnallocated",
    "pendingCheckout",
    "pendingPayment",
    "pendingShipment",
    "total",
    "openAllocatedPooled",
    "openUnallocatedPooled",
    "pendingCheckoutPooled",
    "pendingPaymentPooled",
    "pendingShipmentPooled",
    "totalPooled"
})
public class QuantityInfoResponse {

    @XmlElement(name = "Available")
    protected int available;
    @XmlElement(name = "OpenAllocated")
    protected int openAllocated;
    @XmlElement(name = "OpenUnallocated")
    protected int openUnallocated;
    @XmlElement(name = "PendingCheckout")
    protected int pendingCheckout;
    @XmlElement(name = "PendingPayment")
    protected int pendingPayment;
    @XmlElement(name = "PendingShipment")
    protected int pendingShipment;
    @XmlElement(name = "Total")
    protected int total;
    @XmlElement(name = "OpenAllocatedPooled")
    protected int openAllocatedPooled;
    @XmlElement(name = "OpenUnallocatedPooled")
    protected int openUnallocatedPooled;
    @XmlElement(name = "PendingCheckoutPooled")
    protected int pendingCheckoutPooled;
    @XmlElement(name = "PendingPaymentPooled")
    protected int pendingPaymentPooled;
    @XmlElement(name = "PendingShipmentPooled")
    protected int pendingShipmentPooled;
    @XmlElement(name = "TotalPooled")
    protected int totalPooled;

    /**
     * Gets the value of the available property.
     * 
     */
    public int getAvailable() {
        return available;
    }

    /**
     * Sets the value of the available property.
     * 
     */
    public void setAvailable(int value) {
        this.available = value;
    }

    /**
     * Gets the value of the openAllocated property.
     * 
     */
    public int getOpenAllocated() {
        return openAllocated;
    }

    /**
     * Sets the value of the openAllocated property.
     * 
     */
    public void setOpenAllocated(int value) {
        this.openAllocated = value;
    }

    /**
     * Gets the value of the openUnallocated property.
     * 
     */
    public int getOpenUnallocated() {
        return openUnallocated;
    }

    /**
     * Sets the value of the openUnallocated property.
     * 
     */
    public void setOpenUnallocated(int value) {
        this.openUnallocated = value;
    }

    /**
     * Gets the value of the pendingCheckout property.
     * 
     */
    public int getPendingCheckout() {
        return pendingCheckout;
    }

    /**
     * Sets the value of the pendingCheckout property.
     * 
     */
    public void setPendingCheckout(int value) {
        this.pendingCheckout = value;
    }

    /**
     * Gets the value of the pendingPayment property.
     * 
     */
    public int getPendingPayment() {
        return pendingPayment;
    }

    /**
     * Sets the value of the pendingPayment property.
     * 
     */
    public void setPendingPayment(int value) {
        this.pendingPayment = value;
    }

    /**
     * Gets the value of the pendingShipment property.
     * 
     */
    public int getPendingShipment() {
        return pendingShipment;
    }

    /**
     * Sets the value of the pendingShipment property.
     * 
     */
    public void setPendingShipment(int value) {
        this.pendingShipment = value;
    }

    /**
     * Gets the value of the total property.
     * 
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     */
    public void setTotal(int value) {
        this.total = value;
    }

    /**
     * Gets the value of the openAllocatedPooled property.
     * 
     */
    public int getOpenAllocatedPooled() {
        return openAllocatedPooled;
    }

    /**
     * Sets the value of the openAllocatedPooled property.
     * 
     */
    public void setOpenAllocatedPooled(int value) {
        this.openAllocatedPooled = value;
    }

    /**
     * Gets the value of the openUnallocatedPooled property.
     * 
     */
    public int getOpenUnallocatedPooled() {
        return openUnallocatedPooled;
    }

    /**
     * Sets the value of the openUnallocatedPooled property.
     * 
     */
    public void setOpenUnallocatedPooled(int value) {
        this.openUnallocatedPooled = value;
    }

    /**
     * Gets the value of the pendingCheckoutPooled property.
     * 
     */
    public int getPendingCheckoutPooled() {
        return pendingCheckoutPooled;
    }

    /**
     * Sets the value of the pendingCheckoutPooled property.
     * 
     */
    public void setPendingCheckoutPooled(int value) {
        this.pendingCheckoutPooled = value;
    }

    /**
     * Gets the value of the pendingPaymentPooled property.
     * 
     */
    public int getPendingPaymentPooled() {
        return pendingPaymentPooled;
    }

    /**
     * Sets the value of the pendingPaymentPooled property.
     * 
     */
    public void setPendingPaymentPooled(int value) {
        this.pendingPaymentPooled = value;
    }

    /**
     * Gets the value of the pendingShipmentPooled property.
     * 
     */
    public int getPendingShipmentPooled() {
        return pendingShipmentPooled;
    }

    /**
     * Sets the value of the pendingShipmentPooled property.
     * 
     */
    public void setPendingShipmentPooled(int value) {
        this.pendingShipmentPooled = value;
    }

    /**
     * Gets the value of the totalPooled property.
     * 
     */
    public int getTotalPooled() {
        return totalPooled;
    }

    /**
     * Sets the value of the totalPooled property.
     * 
     */
    public void setTotalPooled(int value) {
        this.totalPooled = value;
    }

}
