
package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for PriceInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PriceInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cost" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="RetailPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="StartingPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ReservePrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TakeItPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="SecondChanceOfferPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="StorePrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PriceInfo", propOrder = {
    "cost",
    "retailPrice",
    "startingPrice",
    "reservePrice",
    "takeItPrice",
    "secondChanceOfferPrice",
    "storePrice"
})
public class PriceInfo {

    @XmlElement(name = "Cost", required = true, nillable = true)
    protected BigDecimal cost;
    @XmlElement(name = "RetailPrice", required = true, nillable = true)
    protected BigDecimal retailPrice;
    @XmlElement(name = "StartingPrice", required = true, nillable = true)
    protected BigDecimal startingPrice;
    @XmlElement(name = "ReservePrice", required = true, nillable = true)
    protected BigDecimal reservePrice;
    @XmlElement(name = "TakeItPrice", required = true, nillable = true)
    protected BigDecimal takeItPrice;
    @XmlElement(name = "SecondChanceOfferPrice", required = true, nillable = true)
    protected BigDecimal secondChanceOfferPrice;
    @XmlElement(name = "StorePrice", required = true, nillable = true)
    protected BigDecimal storePrice;

    /**
     * Gets the value of the cost property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Sets the value of the cost property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCost(BigDecimal value) {
        this.cost = value;
    }

    /**
     * Gets the value of the retailPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    /**
     * Sets the value of the retailPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRetailPrice(BigDecimal value) {
        this.retailPrice = value;
    }

    /**
     * Gets the value of the startingPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    /**
     * Sets the value of the startingPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStartingPrice(BigDecimal value) {
        this.startingPrice = value;
    }

    /**
     * Gets the value of the reservePrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    /**
     * Sets the value of the reservePrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReservePrice(BigDecimal value) {
        this.reservePrice = value;
    }

    /**
     * Gets the value of the takeItPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTakeItPrice() {
        return takeItPrice;
    }

    /**
     * Sets the value of the takeItPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTakeItPrice(BigDecimal value) {
        this.takeItPrice = value;
    }

    /**
     * Gets the value of the secondChanceOfferPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSecondChanceOfferPrice() {
        return secondChanceOfferPrice;
    }

    /**
     * Sets the value of the secondChanceOfferPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSecondChanceOfferPrice(BigDecimal value) {
        this.secondChanceOfferPrice = value;
    }

    /**
     * Gets the value of the storePrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStorePrice() {
        return storePrice;
    }

    /**
     * Sets the value of the storePrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStorePrice(BigDecimal value) {
        this.storePrice = value;
    }

}
