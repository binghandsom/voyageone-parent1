
package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for ShippingRateInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShippingRateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DestinationZoneName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CarrierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClassCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstItemRate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="AdditionalItemRate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="FirstItemHandlingRate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="AdditionalItemHandlingRate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="FreeShippingIfBuyItNow" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="FirstItemRateAttribute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstItemHandlingRateAttribute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdditionalItemRateAttribute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdditionalItemHandlingRateAttribute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShippingRateInfo", propOrder = {
    "destinationZoneName",
    "carrierCode",
    "classCode",
    "firstItemRate",
    "additionalItemRate",
    "firstItemHandlingRate",
    "additionalItemHandlingRate",
    "freeShippingIfBuyItNow",
    "firstItemRateAttribute",
    "firstItemHandlingRateAttribute",
    "additionalItemRateAttribute",
    "additionalItemHandlingRateAttribute"
})
public class ShippingRateInfo {

    @XmlElement(name = "DestinationZoneName")
    protected String destinationZoneName;
    @XmlElement(name = "CarrierCode")
    protected String carrierCode;
    @XmlElement(name = "ClassCode")
    protected String classCode;
    @XmlElement(name = "FirstItemRate", required = true, nillable = true)
    protected BigDecimal firstItemRate;
    @XmlElement(name = "AdditionalItemRate", required = true, nillable = true)
    protected BigDecimal additionalItemRate;
    @XmlElement(name = "FirstItemHandlingRate", required = true, nillable = true)
    protected BigDecimal firstItemHandlingRate;
    @XmlElement(name = "AdditionalItemHandlingRate", required = true, nillable = true)
    protected BigDecimal additionalItemHandlingRate;
    @XmlElement(name = "FreeShippingIfBuyItNow")
    protected boolean freeShippingIfBuyItNow;
    @XmlElement(name = "FirstItemRateAttribute")
    protected String firstItemRateAttribute;
    @XmlElement(name = "FirstItemHandlingRateAttribute")
    protected String firstItemHandlingRateAttribute;
    @XmlElement(name = "AdditionalItemRateAttribute")
    protected String additionalItemRateAttribute;
    @XmlElement(name = "AdditionalItemHandlingRateAttribute")
    protected String additionalItemHandlingRateAttribute;

    /**
     * Gets the value of the destinationZoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationZoneName() {
        return destinationZoneName;
    }

    /**
     * Sets the value of the destinationZoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationZoneName(String value) {
        this.destinationZoneName = value;
    }

    /**
     * Gets the value of the carrierCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierCode() {
        return carrierCode;
    }

    /**
     * Sets the value of the carrierCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierCode(String value) {
        this.carrierCode = value;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassCode(String value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the firstItemRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFirstItemRate() {
        return firstItemRate;
    }

    /**
     * Sets the value of the firstItemRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFirstItemRate(BigDecimal value) {
        this.firstItemRate = value;
    }

    /**
     * Gets the value of the additionalItemRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalItemRate() {
        return additionalItemRate;
    }

    /**
     * Sets the value of the additionalItemRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalItemRate(BigDecimal value) {
        this.additionalItemRate = value;
    }

    /**
     * Gets the value of the firstItemHandlingRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFirstItemHandlingRate() {
        return firstItemHandlingRate;
    }

    /**
     * Sets the value of the firstItemHandlingRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFirstItemHandlingRate(BigDecimal value) {
        this.firstItemHandlingRate = value;
    }

    /**
     * Gets the value of the additionalItemHandlingRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalItemHandlingRate() {
        return additionalItemHandlingRate;
    }

    /**
     * Sets the value of the additionalItemHandlingRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalItemHandlingRate(BigDecimal value) {
        this.additionalItemHandlingRate = value;
    }

    /**
     * Gets the value of the freeShippingIfBuyItNow property.
     * 
     */
    public boolean isFreeShippingIfBuyItNow() {
        return freeShippingIfBuyItNow;
    }

    /**
     * Sets the value of the freeShippingIfBuyItNow property.
     * 
     */
    public void setFreeShippingIfBuyItNow(boolean value) {
        this.freeShippingIfBuyItNow = value;
    }

    /**
     * Gets the value of the firstItemRateAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstItemRateAttribute() {
        return firstItemRateAttribute;
    }

    /**
     * Sets the value of the firstItemRateAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstItemRateAttribute(String value) {
        this.firstItemRateAttribute = value;
    }

    /**
     * Gets the value of the firstItemHandlingRateAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstItemHandlingRateAttribute() {
        return firstItemHandlingRateAttribute;
    }

    /**
     * Sets the value of the firstItemHandlingRateAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstItemHandlingRateAttribute(String value) {
        this.firstItemHandlingRateAttribute = value;
    }

    /**
     * Gets the value of the additionalItemRateAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalItemRateAttribute() {
        return additionalItemRateAttribute;
    }

    /**
     * Sets the value of the additionalItemRateAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalItemRateAttribute(String value) {
        this.additionalItemRateAttribute = value;
    }

    /**
     * Gets the value of the additionalItemHandlingRateAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalItemHandlingRateAttribute() {
        return additionalItemHandlingRateAttribute;
    }

    /**
     * Sets the value of the additionalItemHandlingRateAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalItemHandlingRateAttribute(String value) {
        this.additionalItemHandlingRateAttribute = value;
    }

}
