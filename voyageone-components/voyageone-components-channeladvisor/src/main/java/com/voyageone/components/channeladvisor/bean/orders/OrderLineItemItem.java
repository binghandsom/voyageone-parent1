package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * <p>
 * Java class for OrderLineItemItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineItemItem">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemBase">
 *       &lt;sequence>
 *         &lt;element name="LineItemID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AllowNegativeQuantity" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ItemSaleSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SKU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BuyerUserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BuyerFeedbackRating" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SalesSourceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VATRate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TaxCost" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ShippingCost" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ShippingTaxCost" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GiftWrapCost" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GiftWrapTaxCost" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GiftMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GiftWrapLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ItemPromoList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfOrderLineItemItemPromo" minOccurs="0"/>
 *         &lt;element name="RecyclingFee" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="FulfillmentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineItemItem", propOrder = { "lineItemID", "allowNegativeQuantity", "quantity", "itemSaleSource",
		"sku", "title", "buyerUserID", "buyerFeedbackRating", "salesSourceID", "vatRate", "taxCost", "shippingCost",
		"shippingTaxCost", "giftWrapCost", "giftWrapTaxCost", "giftMessage", "giftWrapLevel", "itemPromoList",
		"recyclingFee", "fulfillmentType" })
public class OrderLineItemItem extends OrderLineItemBase {

	@XmlElement(name = "LineItemID")
	protected int lineItemID;
	@XmlElement(name = "AllowNegativeQuantity")
	protected boolean allowNegativeQuantity;
	@XmlElement(name = "Quantity")
	protected int quantity;
	@XmlElement(name = "ItemSaleSource")
	protected String itemSaleSource;
	@XmlElement(name = "SKU")
	protected String sku;
	@XmlElement(name = "Title")
	protected String title;
	@XmlElement(name = "BuyerUserID")
	protected String buyerUserID;
	@XmlElement(name = "BuyerFeedbackRating")
	protected int buyerFeedbackRating;
	@XmlElement(name = "SalesSourceID")
	protected String salesSourceID;
	@XmlElement(name = "VATRate", required = true)
	protected BigDecimal vatRate;
	@XmlElement(name = "TaxCost", required = true, nillable = true)
	protected BigDecimal taxCost;
	@XmlElement(name = "ShippingCost", required = true, nillable = true)
	protected BigDecimal shippingCost;
	@XmlElement(name = "ShippingTaxCost", required = true, nillable = true)
	protected BigDecimal shippingTaxCost;
	@XmlElement(name = "GiftWrapCost", required = true, nillable = true)
	protected BigDecimal giftWrapCost;
	@XmlElement(name = "GiftWrapTaxCost", required = true, nillable = true)
	protected BigDecimal giftWrapTaxCost;
	@XmlElement(name = "GiftMessage")
	protected String giftMessage;
	@XmlElement(name = "GiftWrapLevel")
	protected String giftWrapLevel;
	@XmlElement(name = "ItemPromoList")
	protected ArrayOfOrderLineItemItemPromo itemPromoList;
	@XmlElement(name = "RecyclingFee", required = true, nillable = true)
	protected BigDecimal recyclingFee;
	@XmlElement(name = "FulfillmentType")
	protected String fulfillmentType;

	/**
	 * Gets the value of the lineItemID property.
	 * 
	 */
	public int getLineItemID() {
		return lineItemID;
	}

	/**
	 * Sets the value of the lineItemID property.
	 * 
	 */
	public void setLineItemID(int value) {
		this.lineItemID = value;
	}

	/**
	 * Gets the value of the allowNegativeQuantity property.
	 * 
	 */
	public boolean isAllowNegativeQuantity() {
		return allowNegativeQuantity;
	}

	/**
	 * Sets the value of the allowNegativeQuantity property.
	 * 
	 */
	public void setAllowNegativeQuantity(boolean value) {
		this.allowNegativeQuantity = value;
	}

	/**
	 * Gets the value of the quantity property.
	 * 
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the value of the quantity property.
	 * 
	 */
	public void setQuantity(int value) {
		this.quantity = value;
	}

	/**
	 * Gets the value of the itemSaleSource property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getItemSaleSource() {
		return itemSaleSource;
	}

	/**
	 * Sets the value of the itemSaleSource property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setItemSaleSource(String value) {
		this.itemSaleSource = value;
	}

	/**
	 * Gets the value of the sku property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSKU() {
		return sku;
	}

	/**
	 * Sets the value of the sku property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSKU(String value) {
		this.sku = value;
	}

	/**
	 * Gets the value of the title property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the value of the title property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	/**
	 * Gets the value of the buyerUserID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBuyerUserID() {
		return buyerUserID;
	}

	/**
	 * Sets the value of the buyerUserID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBuyerUserID(String value) {
		this.buyerUserID = value;
	}

	/**
	 * Gets the value of the buyerFeedbackRating property.
	 * 
	 */
	public int getBuyerFeedbackRating() {
		return buyerFeedbackRating;
	}

	/**
	 * Sets the value of the buyerFeedbackRating property.
	 * 
	 */
	public void setBuyerFeedbackRating(int value) {
		this.buyerFeedbackRating = value;
	}

	/**
	 * Gets the value of the salesSourceID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSalesSourceID() {
		return salesSourceID;
	}

	/**
	 * Sets the value of the salesSourceID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSalesSourceID(String value) {
		this.salesSourceID = value;
	}

	/**
	 * Gets the value of the vatRate property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getVATRate() {
		return vatRate;
	}

	/**
	 * Sets the value of the vatRate property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setVATRate(BigDecimal value) {
		this.vatRate = value;
	}

	/**
	 * Gets the value of the taxCost property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getTaxCost() {
		return taxCost;
	}

	/**
	 * Sets the value of the taxCost property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setTaxCost(BigDecimal value) {
		this.taxCost = value;
	}

	/**
	 * Gets the value of the shippingCost property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getShippingCost() {
		return shippingCost;
	}

	/**
	 * Sets the value of the shippingCost property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setShippingCost(BigDecimal value) {
		this.shippingCost = value;
	}

	/**
	 * Gets the value of the shippingTaxCost property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getShippingTaxCost() {
		return shippingTaxCost;
	}

	/**
	 * Sets the value of the shippingTaxCost property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setShippingTaxCost(BigDecimal value) {
		this.shippingTaxCost = value;
	}

	/**
	 * Gets the value of the giftWrapCost property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getGiftWrapCost() {
		return giftWrapCost;
	}

	/**
	 * Sets the value of the giftWrapCost property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setGiftWrapCost(BigDecimal value) {
		this.giftWrapCost = value;
	}

	/**
	 * Gets the value of the giftWrapTaxCost property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getGiftWrapTaxCost() {
		return giftWrapTaxCost;
	}

	/**
	 * Sets the value of the giftWrapTaxCost property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setGiftWrapTaxCost(BigDecimal value) {
		this.giftWrapTaxCost = value;
	}

	/**
	 * Gets the value of the giftMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGiftMessage() {
		return giftMessage;
	}

	/**
	 * Sets the value of the giftMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGiftMessage(String value) {
		this.giftMessage = value;
	}

	/**
	 * Gets the value of the giftWrapLevel property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGiftWrapLevel() {
		return giftWrapLevel;
	}

	/**
	 * Sets the value of the giftWrapLevel property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGiftWrapLevel(String value) {
		this.giftWrapLevel = value;
	}

	/**
	 * Gets the value of the itemPromoList property.
	 * 
	 * @return possible object is {@link ArrayOfOrderLineItemItemPromo }
	 * 
	 */
	public ArrayOfOrderLineItemItemPromo getItemPromoList() {
		return itemPromoList;
	}

	/**
	 * Sets the value of the itemPromoList property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfOrderLineItemItemPromo }
	 * 
	 */
	public void setItemPromoList(ArrayOfOrderLineItemItemPromo value) {
		this.itemPromoList = value;
	}

	/**
	 * Gets the value of the recyclingFee property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getRecyclingFee() {
		return recyclingFee;
	}

	/**
	 * Sets the value of the recyclingFee property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setRecyclingFee(BigDecimal value) {
		this.recyclingFee = value;
	}

	/**
	 * Gets the value of the fulfillmentType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFulfillmentType() {
		return fulfillmentType;
	}

	/**
	 * Sets the value of the fulfillmentType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFulfillmentType(String value) {
		this.fulfillmentType = value;
	}

}
