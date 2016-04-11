package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderCart complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderCart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CartID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CheckoutSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VATTaxCalculationOption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VATShippingOption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VATGiftWrapOption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LineItemSKUList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfOrderLineItemItem" minOccurs="0"/>
 *         &lt;element name="LineItemInvoiceList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfOrderLineItemInvoice" minOccurs="0"/>
 *         &lt;element name="LineItemPromoList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfOrderLineItemPromo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderCart", propOrder = { "cartID", "checkoutSource", "vatTaxCalculationOption", "vatShippingOption",
		"vatGiftWrapOption", "lineItemSKUList", "lineItemInvoiceList", "lineItemPromoList" })
public class OrderCart {

	@XmlElement(name = "CartID")
	protected int cartID;
	@XmlElement(name = "CheckoutSource")
	protected String checkoutSource;
	@XmlElement(name = "VATTaxCalculationOption")
	protected String vatTaxCalculationOption;
	@XmlElement(name = "VATShippingOption")
	protected String vatShippingOption;
	@XmlElement(name = "VATGiftWrapOption")
	protected String vatGiftWrapOption;
	@XmlElement(name = "LineItemSKUList")
	protected ArrayOfOrderLineItemItem lineItemSKUList;
	@XmlElement(name = "LineItemInvoiceList")
	protected ArrayOfOrderLineItemInvoice lineItemInvoiceList;
	@XmlElement(name = "LineItemPromoList")
	protected ArrayOfOrderLineItemPromo lineItemPromoList;

	/**
	 * Gets the value of the cartID property.
	 * 
	 */
	public int getCartID() {
		return cartID;
	}

	/**
	 * Sets the value of the cartID property.
	 * 
	 */
	public void setCartID(int value) {
		this.cartID = value;
	}

	/**
	 * Gets the value of the checkoutSource property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCheckoutSource() {
		return checkoutSource;
	}

	/**
	 * Sets the value of the checkoutSource property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCheckoutSource(String value) {
		this.checkoutSource = value;
	}

	/**
	 * Gets the value of the vatTaxCalculationOption property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVATTaxCalculationOption() {
		return vatTaxCalculationOption;
	}

	/**
	 * Sets the value of the vatTaxCalculationOption property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVATTaxCalculationOption(String value) {
		this.vatTaxCalculationOption = value;
	}

	/**
	 * Gets the value of the vatShippingOption property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVATShippingOption() {
		return vatShippingOption;
	}

	/**
	 * Sets the value of the vatShippingOption property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVATShippingOption(String value) {
		this.vatShippingOption = value;
	}

	/**
	 * Gets the value of the vatGiftWrapOption property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVATGiftWrapOption() {
		return vatGiftWrapOption;
	}

	/**
	 * Sets the value of the vatGiftWrapOption property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVATGiftWrapOption(String value) {
		this.vatGiftWrapOption = value;
	}

	/**
	 * Gets the value of the lineItemSKUList property.
	 * 
	 * @return possible object is {@link ArrayOfOrderLineItemItem }
	 * 
	 */
	public ArrayOfOrderLineItemItem getLineItemSKUList() {
		return lineItemSKUList;
	}

	/**
	 * Sets the value of the lineItemSKUList property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfOrderLineItemItem }
	 * 
	 */
	public void setLineItemSKUList(ArrayOfOrderLineItemItem value) {
		this.lineItemSKUList = value;
	}

	/**
	 * Gets the value of the lineItemInvoiceList property.
	 * 
	 * @return possible object is {@link ArrayOfOrderLineItemInvoice }
	 * 
	 */
	public ArrayOfOrderLineItemInvoice getLineItemInvoiceList() {
		return lineItemInvoiceList;
	}

	/**
	 * Sets the value of the lineItemInvoiceList property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfOrderLineItemInvoice }
	 * 
	 */
	public void setLineItemInvoiceList(ArrayOfOrderLineItemInvoice value) {
		this.lineItemInvoiceList = value;
	}

	/**
	 * Gets the value of the lineItemPromoList property.
	 * 
	 * @return possible object is {@link ArrayOfOrderLineItemPromo }
	 * 
	 */
	public ArrayOfOrderLineItemPromo getLineItemPromoList() {
		return lineItemPromoList;
	}

	/**
	 * Sets the value of the lineItemPromoList property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfOrderLineItemPromo }
	 * 
	 */
	public void setLineItemPromoList(ArrayOfOrderLineItemPromo value) {
		this.lineItemPromoList = value;
	}

}
