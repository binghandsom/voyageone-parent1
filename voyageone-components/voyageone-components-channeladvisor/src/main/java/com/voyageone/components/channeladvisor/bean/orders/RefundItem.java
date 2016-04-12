package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * <p>
 * Java class for RefundItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RefundItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SKU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ShippingAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ShippingTaxAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TaxAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GiftWrapAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GiftWrapTaxAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="RecyclingFee" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RefundRequestID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RefundRequested" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RestockQuantity" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="AdjustmentReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SellerRefundID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LineItemID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefundItem", propOrder = { "sku", "amount", "shippingAmount", "shippingTaxAmount", "taxAmount",
		"giftWrapAmount", "giftWrapTaxAmount", "recyclingFee", "quantity", "refundRequestID", "refundRequested",
		"restockQuantity", "adjustmentReason", "sellerRefundID", "lineItemID" })
public class RefundItem {

	@XmlElement(name = "SKU")
	protected String sku;
	@XmlElement(name = "Amount", required = true)
	protected BigDecimal amount;
	@XmlElement(name = "ShippingAmount", required = true)
	protected BigDecimal shippingAmount;
	@XmlElement(name = "ShippingTaxAmount", required = true)
	protected BigDecimal shippingTaxAmount;
	@XmlElement(name = "TaxAmount", required = true)
	protected BigDecimal taxAmount;
	@XmlElement(name = "GiftWrapAmount", required = true)
	protected BigDecimal giftWrapAmount;
	@XmlElement(name = "GiftWrapTaxAmount", required = true)
	protected BigDecimal giftWrapTaxAmount;
	@XmlElement(name = "RecyclingFee", required = true)
	protected BigDecimal recyclingFee;
	@XmlElement(name = "Quantity")
	protected int quantity;
	@XmlElement(name = "RefundRequestID")
	protected int refundRequestID;
	@XmlElement(name = "RefundRequested")
	protected boolean refundRequested;
	@XmlElement(name = "RestockQuantity", required = true, type = Boolean.class, nillable = true)
	protected Boolean restockQuantity;
	@XmlElement(name = "AdjustmentReason")
	protected String adjustmentReason;
	@XmlElement(name = "SellerRefundID")
	protected String sellerRefundID;
	@XmlElement(name = "LineItemID", required = true, type = Integer.class, nillable = true)
	protected Integer lineItemID;

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
	 * Gets the value of the amount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Sets the value of the amount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setAmount(BigDecimal value) {
		this.amount = value;
	}

	/**
	 * Gets the value of the shippingAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getShippingAmount() {
		return shippingAmount;
	}

	/**
	 * Sets the value of the shippingAmount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setShippingAmount(BigDecimal value) {
		this.shippingAmount = value;
	}

	/**
	 * Gets the value of the shippingTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getShippingTaxAmount() {
		return shippingTaxAmount;
	}

	/**
	 * Sets the value of the shippingTaxAmount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setShippingTaxAmount(BigDecimal value) {
		this.shippingTaxAmount = value;
	}

	/**
	 * Gets the value of the taxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	/**
	 * Sets the value of the taxAmount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setTaxAmount(BigDecimal value) {
		this.taxAmount = value;
	}

	/**
	 * Gets the value of the giftWrapAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getGiftWrapAmount() {
		return giftWrapAmount;
	}

	/**
	 * Sets the value of the giftWrapAmount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setGiftWrapAmount(BigDecimal value) {
		this.giftWrapAmount = value;
	}

	/**
	 * Gets the value of the giftWrapTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getGiftWrapTaxAmount() {
		return giftWrapTaxAmount;
	}

	/**
	 * Sets the value of the giftWrapTaxAmount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setGiftWrapTaxAmount(BigDecimal value) {
		this.giftWrapTaxAmount = value;
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
	 * Gets the value of the refundRequestID property.
	 * 
	 */
	public int getRefundRequestID() {
		return refundRequestID;
	}

	/**
	 * Sets the value of the refundRequestID property.
	 * 
	 */
	public void setRefundRequestID(int value) {
		this.refundRequestID = value;
	}

	/**
	 * Gets the value of the refundRequested property.
	 * 
	 */
	public boolean isRefundRequested() {
		return refundRequested;
	}

	/**
	 * Sets the value of the refundRequested property.
	 * 
	 */
	public void setRefundRequested(boolean value) {
		this.refundRequested = value;
	}

	/**
	 * Gets the value of the restockQuantity property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isRestockQuantity() {
		return restockQuantity;
	}

	/**
	 * Sets the value of the restockQuantity property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setRestockQuantity(Boolean value) {
		this.restockQuantity = value;
	}

	/**
	 * Gets the value of the adjustmentReason property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAdjustmentReason() {
		return adjustmentReason;
	}

	/**
	 * Sets the value of the adjustmentReason property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAdjustmentReason(String value) {
		this.adjustmentReason = value;
	}

	/**
	 * Gets the value of the sellerRefundID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSellerRefundID() {
		return sellerRefundID;
	}

	/**
	 * Sets the value of the sellerRefundID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSellerRefundID(String value) {
		this.sellerRefundID = value;
	}

	/**
	 * Gets the value of the lineItemID property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getLineItemID() {
		return lineItemID;
	}

	/**
	 * Sets the value of the lineItemID property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setLineItemID(Integer value) {
		this.lineItemID = value;
	}

}
