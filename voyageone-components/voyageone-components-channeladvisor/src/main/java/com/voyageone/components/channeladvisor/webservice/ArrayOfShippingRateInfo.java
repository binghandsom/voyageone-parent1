
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.ShippingRateInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfShippingRateInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfShippingRateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShippingRateInfo" type="{http://api.channeladvisor.com/webservices/}ShippingRateInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfShippingRateInfo", propOrder = {
    "shippingRateInfo"
})
public class ArrayOfShippingRateInfo {

    @XmlElement(name = "ShippingRateInfo", nillable = true)
    protected List<ShippingRateInfo> shippingRateInfo;

    /**
     * Gets the value of the shippingRateInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shippingRateInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShippingRateInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ShippingRateInfo }
     * 
     * 
     */
    public List<ShippingRateInfo> getShippingRateInfo() {
        if (shippingRateInfo == null) {
            shippingRateInfo = new ArrayList<ShippingRateInfo>();
        }
        return this.shippingRateInfo;
    }

}
