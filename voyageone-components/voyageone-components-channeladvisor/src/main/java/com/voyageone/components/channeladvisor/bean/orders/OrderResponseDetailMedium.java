package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderResponseDetailMedium complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderResponseDetailMedium">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}OrderResponseDetailLow">
 *       &lt;sequence>
 *         &lt;element name="ResellerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BuyerEmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailOptIn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="PaymentInfo" type="{http://api.channeladvisor.com/datacontracts/orders}PaymentInfoResponse" minOccurs="0"/>
 *         &lt;element name="ShippingInfo" type="{http://api.channeladvisor.com/datacontracts/orders}ShippingInfoResponse" minOccurs="0"/>
 *         &lt;element name="BillingInfo" type="{http://api.channeladvisor.com/datacontracts/orders}BillingInfo" minOccurs="0"/>
 *         &lt;element name="FlagDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderResponseDetailMedium", propOrder = { "resellerID", "buyerEmailAddress", "emailOptIn",
		"paymentInfo", "shippingInfo", "billingInfo", "flagDescription" })
public class OrderResponseDetailMedium extends OrderResponseDetailLow {

	@XmlElement(name = "ResellerID")
	protected String resellerID;
	@XmlElement(name = "BuyerEmailAddress")
	protected String buyerEmailAddress;
	@XmlElement(name = "EmailOptIn")
	protected boolean emailOptIn;
	@XmlElement(name = "PaymentInfo")
	protected PaymentInfoResponse paymentInfo;
	@XmlElement(name = "ShippingInfo")
	protected ShippingInfoResponse shippingInfo;
	@XmlElement(name = "BillingInfo")
	protected BillingInfo billingInfo;
	@XmlElement(name = "FlagDescription")
	protected String flagDescription;

	/**
	 * Gets the value of the resellerID property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getResellerID() {
		return resellerID;
	}

	/**
	 * Sets the value of the resellerID property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setResellerID(String value) {
		this.resellerID = value;
	}

	/**
	 * Gets the value of the buyerEmailAddress property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getBuyerEmailAddress() {
		return buyerEmailAddress;
	}

	/**
	 * Sets the value of the buyerEmailAddress property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setBuyerEmailAddress(String value) {
		this.buyerEmailAddress = value;
	}

	/**
	 * Gets the value of the emailOptIn property.
	 *
	 */
	public boolean isEmailOptIn() {
		return emailOptIn;
	}

	/**
	 * Sets the value of the emailOptIn property.
	 *
	 */
	public void setEmailOptIn(boolean value) {
		this.emailOptIn = value;
	}

	/**
	 * Gets the value of the paymentInfo property.
	 *
	 * @return possible object is {@link PaymentInfoResponse }
	 *
	 */
	public PaymentInfoResponse getPaymentInfo() {
		return paymentInfo;
	}

	/**
	 * Sets the value of the paymentInfo property.
	 *
	 * @param value
	 *            allowed object is {@link PaymentInfoResponse }
	 *
	 */
	public void setPaymentInfo(PaymentInfoResponse value) {
		this.paymentInfo = value;
	}

	/**
	 * Gets the value of the shippingInfo property.
	 * 
	 * @return possible object is {@link ShippingInfoResponse }
	 * 
	 */
	public ShippingInfoResponse getShippingInfo() {
		return shippingInfo;
	}

	/**
	 * Sets the value of the shippingInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link ShippingInfoResponse }
	 * 
	 */
	public void setShippingInfo(ShippingInfoResponse value) {
		this.shippingInfo = value;
	}

	/**
	 * Gets the value of the billingInfo property.
	 * 
	 * @return possible object is {@link BillingInfo }
	 * 
	 */
	public BillingInfo getBillingInfo() {
		return billingInfo;
	}

	/**
	 * Sets the value of the billingInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link BillingInfo }
	 * 
	 */
	public void setBillingInfo(BillingInfo value) {
		this.billingInfo = value;
	}

	/**
	 * Gets the value of the flagDescription property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlagDescription() {
		return flagDescription;
	}

	/**
	 * Sets the value of the flagDescription property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFlagDescription(String value) {
		this.flagDescription = value;
	}

}
