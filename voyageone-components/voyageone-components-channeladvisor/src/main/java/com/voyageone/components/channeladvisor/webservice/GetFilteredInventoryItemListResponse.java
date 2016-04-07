
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfInventoryItemResponse;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetFilteredInventoryItemListResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfInventoryItemResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getFilteredInventoryItemListResult"
})
@XmlRootElement(name = "GetFilteredInventoryItemListResponse")
public class GetFilteredInventoryItemListResponse {

    @XmlElement(name = "GetFilteredInventoryItemListResult")
    protected com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfInventoryItemResponse getFilteredInventoryItemListResult;

    /**
     * Gets the value of the getFilteredInventoryItemListResult property.
     * 
     * @return
     *     possible object is
     *     {@link com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfInventoryItemResponse }
     *     
     */
    public com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfInventoryItemResponse getGetFilteredInventoryItemListResult() {
        return getFilteredInventoryItemListResult;
    }

    /**
     * Sets the value of the getFilteredInventoryItemListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfInventoryItemResponse }
     *     
     */
    public void setGetFilteredInventoryItemListResult(com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfInventoryItemResponse value) {
        this.getFilteredInventoryItemListResult = value;
    }

}
