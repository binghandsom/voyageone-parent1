package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderLineItemItemResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineItemItemResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemItem">
 *       &lt;sequence>
 *         &lt;element name="UnitWeight" type="{http://api.channeladvisor.com/datacontracts/orders}ItemWeight" minOccurs="0"/>
 *         &lt;element name="WarehouseLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DistributionCenterCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsExternallyFulfilled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ItemSaleSourceTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineItemItemResponse", propOrder = { "unitWeight", "warehouseLocation", "userName",
		"distributionCenterCode", "isExternallyFulfilled", "itemSaleSourceTransactionID" })
public class OrderLineItemItemResponse extends OrderLineItemItem {

	@XmlElement(name = "UnitWeight")
	protected ItemWeight unitWeight;
	@XmlElement(name = "WarehouseLocation")
	protected String warehouseLocation;
	@XmlElement(name = "UserName")
	protected String userName;
	@XmlElement(name = "DistributionCenterCode")
	protected String distributionCenterCode;
	@XmlElement(name = "IsExternallyFulfilled")
	protected boolean isExternallyFulfilled;
	@XmlElement(name = "ItemSaleSourceTransactionID")
	protected String itemSaleSourceTransactionID;

	/**
	 * Gets the value of the unitWeight property.
	 * 
	 * @return possible object is {@link ItemWeight }
	 * 
	 */
	public ItemWeight getUnitWeight() {
		return unitWeight;
	}

	/**
	 * Sets the value of the unitWeight property.
	 * 
	 * @param value
	 *            allowed object is {@link ItemWeight }
	 * 
	 */
	public void setUnitWeight(ItemWeight value) {
		this.unitWeight = value;
	}

	/**
	 * Gets the value of the warehouseLocation property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getWarehouseLocation() {
		return warehouseLocation;
	}

	/**
	 * Sets the value of the warehouseLocation property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setWarehouseLocation(String value) {
		this.warehouseLocation = value;
	}

	/**
	 * Gets the value of the userName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the value of the userName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserName(String value) {
		this.userName = value;
	}

	/**
	 * Gets the value of the distributionCenterCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDistributionCenterCode() {
		return distributionCenterCode;
	}

	/**
	 * Sets the value of the distributionCenterCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDistributionCenterCode(String value) {
		this.distributionCenterCode = value;
	}

	/**
	 * Gets the value of the isExternallyFulfilled property.
	 * 
	 */
	public boolean isIsExternallyFulfilled() {
		return isExternallyFulfilled;
	}

	/**
	 * Sets the value of the isExternallyFulfilled property.
	 * 
	 */
	public void setIsExternallyFulfilled(boolean value) {
		this.isExternallyFulfilled = value;
	}

	/**
	 * Gets the value of the itemSaleSourceTransactionID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getItemSaleSourceTransactionID() {
		return itemSaleSourceTransactionID;
	}

	/**
	 * Sets the value of the itemSaleSourceTransactionID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setItemSaleSourceTransactionID(String value) {
		this.itemSaleSourceTransactionID = value;
	}

}
