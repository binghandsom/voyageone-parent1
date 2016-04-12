
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.ArrayOfShippingRateInfo;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DistributionCenterInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributionCenterInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DistributionCenterCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AvailableQuantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OpenAllocatedQuantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OpenAllocatedPooledQuantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="WarehouseLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReceivedInInventory" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ShippingRateList" type="{http://api.channeladvisor.com/webservices/}ArrayOfShippingRateInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistributionCenterInfoResponse", propOrder = {
    "distributionCenterCode",
    "availableQuantity",
    "openAllocatedQuantity",
    "openAllocatedPooledQuantity",
    "warehouseLocation",
    "receivedInInventory",
    "shippingRateList"
})
public class DistributionCenterInfoResponse {

    @XmlElement(name = "DistributionCenterCode")
    protected String distributionCenterCode;
    @XmlElement(name = "AvailableQuantity", required = true, type = Integer.class, nillable = true)
    protected Integer availableQuantity;
    @XmlElement(name = "OpenAllocatedQuantity", required = true, type = Integer.class, nillable = true)
    protected Integer openAllocatedQuantity;
    @XmlElement(name = "OpenAllocatedPooledQuantity", required = true, type = Integer.class, nillable = true)
    protected Integer openAllocatedPooledQuantity;
    @XmlElement(name = "WarehouseLocation")
    protected String warehouseLocation;
    @XmlElement(name = "ReceivedInInventory", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receivedInInventory;
    @XmlElement(name = "ShippingRateList")
    protected ArrayOfShippingRateInfo shippingRateList;

    /**
     * Gets the value of the distributionCenterCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistributionCenterCode() {
        return distributionCenterCode;
    }

    /**
     * Sets the value of the distributionCenterCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistributionCenterCode(String value) {
        this.distributionCenterCode = value;
    }

    /**
     * Gets the value of the availableQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    /**
     * Sets the value of the availableQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAvailableQuantity(Integer value) {
        this.availableQuantity = value;
    }

    /**
     * Gets the value of the openAllocatedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOpenAllocatedQuantity() {
        return openAllocatedQuantity;
    }

    /**
     * Sets the value of the openAllocatedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOpenAllocatedQuantity(Integer value) {
        this.openAllocatedQuantity = value;
    }

    /**
     * Gets the value of the openAllocatedPooledQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOpenAllocatedPooledQuantity() {
        return openAllocatedPooledQuantity;
    }

    /**
     * Sets the value of the openAllocatedPooledQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOpenAllocatedPooledQuantity(Integer value) {
        this.openAllocatedPooledQuantity = value;
    }

    /**
     * Gets the value of the warehouseLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    /**
     * Sets the value of the warehouseLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarehouseLocation(String value) {
        this.warehouseLocation = value;
    }

    /**
     * Gets the value of the receivedInInventory property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReceivedInInventory() {
        return receivedInInventory;
    }

    /**
     * Sets the value of the receivedInInventory property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReceivedInInventory(XMLGregorianCalendar value) {
        this.receivedInInventory = value;
    }

    /**
     * Gets the value of the shippingRateList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfShippingRateInfo }
     *     
     */
    public ArrayOfShippingRateInfo getShippingRateList() {
        return shippingRateList;
    }

    /**
     * Sets the value of the shippingRateList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfShippingRateInfo }
     *     
     */
    public void setShippingRateList(ArrayOfShippingRateInfo value) {
        this.shippingRateList = value;
    }

}
