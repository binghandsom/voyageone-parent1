
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.GetInventorySkuList;

import javax.xml.bind.annotation.*;
import java.util.List;


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
 *         &lt;element name="accountID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="itemCriteria" type="{http://api.channeladvisor.com/webservices/}InventoryItemCriteria"/>
 *         &lt;element name="detailLevel" type="{http://api.channeladvisor.com/webservices/}InventoryItemDetailLevel" minOccurs="0"/>
 *         &lt;element name="sortField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sortDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "accountID",
        "skuList"
})
@XmlRootElement(name = "GetInventoryItemList")
public class GetInventoryItemList {

    @XmlElement(required = true, nillable = true)
    protected String accountID;
    @XmlElement(required = true, nillable = true)
    protected GetInventorySkuList skuList;

    /**
     * Gets the value of the accountID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     * Sets the value of the accountID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccountID(String value) {
        this.accountID = value;
    }

    /**
     * Gets the value of the skuList property.
     *
     * @return
     *     possible object is
     *     {@link GetInventorySkuList }
     *
     */
    public GetInventorySkuList getSkuList() {
        return skuList;
    }

    /**
     * Sets the value of the skuList property.
     *
     * @param skuList
     *     allowed object is
     *     {@link GetInventorySkuList }
     *
     */
    public void setSkuList(GetInventorySkuList skuList) {
        this.skuList = skuList;
    }

}
