
package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InventoryItemDetailLevel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InventoryItemDetailLevel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IncludeQuantityInfo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IncludePriceInfo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IncludeClassificationInfo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InventoryItemDetailLevel", propOrder = {
    "includeQuantityInfo",
    "includePriceInfo",
    "includeClassificationInfo"
})
public class InventoryItemDetailLevel {

    @XmlElement(name = "IncludeQuantityInfo", required = true, type = Boolean.class, nillable = true)
    protected Boolean includeQuantityInfo;
    @XmlElement(name = "IncludePriceInfo", required = true, type = Boolean.class, nillable = true)
    protected Boolean includePriceInfo;
    @XmlElement(name = "IncludeClassificationInfo", required = true, type = Boolean.class, nillable = true)
    protected Boolean includeClassificationInfo;

    /**
     * Gets the value of the includeQuantityInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeQuantityInfo() {
        return includeQuantityInfo;
    }

    /**
     * Sets the value of the includeQuantityInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeQuantityInfo(Boolean value) {
        this.includeQuantityInfo = value;
    }

    /**
     * Gets the value of the includePriceInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludePriceInfo() {
        return includePriceInfo;
    }

    /**
     * Sets the value of the includePriceInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludePriceInfo(Boolean value) {
        this.includePriceInfo = value;
    }

    /**
     * Gets the value of the includeClassificationInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeClassificationInfo() {
        return includeClassificationInfo;
    }

    /**
     * Sets the value of the includeClassificationInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeClassificationInfo(Boolean value) {
        this.includeClassificationInfo = value;
    }

}
