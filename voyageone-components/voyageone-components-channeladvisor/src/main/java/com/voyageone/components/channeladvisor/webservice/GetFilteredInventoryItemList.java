
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.*;
import com.voyageone.components.channeladvisor.webservice.InventoryItemDetailLevel;

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
    "itemCriteria",
    "detailLevel",
    "sortField",
    "sortDirection"
})
@XmlRootElement(name = "GetFilteredInventoryItemList")
public class GetFilteredInventoryItemList {

    @XmlElement(required = true, nillable = true)
    protected String accountID;
    @XmlElement(required = true, nillable = true)
    protected InventoryItemCriteria itemCriteria;
    protected com.voyageone.components.channeladvisor.webservice.InventoryItemDetailLevel detailLevel;
    protected String sortField;
    protected String sortDirection;

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
     * Gets the value of the itemCriteria property.
     *
     * @return
     *     possible object is
     *     {@link InventoryItemCriteria }
     *
     */
    public InventoryItemCriteria getItemCriteria() {
        return itemCriteria;
    }

    /**
     * Sets the value of the itemCriteria property.
     *
     * @param value
     *     allowed object is
     *     {@link InventoryItemCriteria }
     *
     */
    public void setItemCriteria(InventoryItemCriteria value) {
        this.itemCriteria = value;
    }

    /**
     * Gets the value of the detailLevel property.
     *
     * @return
     *     possible object is
     *     {@link com.voyageone.components.channeladvisor.webservice.InventoryItemDetailLevel }
     *
     */
    public com.voyageone.components.channeladvisor.webservice.InventoryItemDetailLevel getDetailLevel() {
        return detailLevel;
    }

    /**
     * Sets the value of the detailLevel property.
     *
     * @param value
     *     allowed object is
     *     {@link com.voyageone.components.channeladvisor.webservice.InventoryItemDetailLevel }
     *
     */
    public void setDetailLevel(com.voyageone.components.channeladvisor.webservice.InventoryItemDetailLevel value) {
        this.detailLevel = value;
    }

    /**
     * Gets the value of the sortField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * Sets the value of the sortField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSortField(String value) {
        this.sortField = value;
    }

    /**
     * Gets the value of the sortDirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSortDirection() {
        return sortDirection;
    }

    /**
     * Sets the value of the sortDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSortDirection(String value) {
        this.sortDirection = value;
    }

}
