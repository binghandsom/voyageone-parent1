package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for PaymentInfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreditCardLast4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PayPalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MerchantReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaymentTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentInfo", propOrder = { "paymentType", "creditCardLast4", "payPalID", "merchantReferenceNumber",
		"paymentTransactionID" })
public class PaymentInfo {

	@XmlElement(name = "PaymentType")
	protected String paymentType;
	@XmlElement(name = "CreditCardLast4")
	protected String creditCardLast4;
	@XmlElement(name = "PayPalID")
	protected String payPalID;
	@XmlElement(name = "MerchantReferenceNumber")
	protected String merchantReferenceNumber;
	@XmlElement(name = "PaymentTransactionID")
	protected String paymentTransactionID;

	/**
	 * Gets the value of the paymentType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * Sets the value of the paymentType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPaymentType(String value) {
		this.paymentType = value;
	}

	/**
	 * Gets the value of the creditCardLast4 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCreditCardLast4() {
		return creditCardLast4;
	}

	/**
	 * Sets the value of the creditCardLast4 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCreditCardLast4(String value) {
		this.creditCardLast4 = value;
	}

	/**
	 * Gets the value of the payPalID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPayPalID() {
		return payPalID;
	}

	/**
	 * Sets the value of the payPalID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPayPalID(String value) {
		this.payPalID = value;
	}

	/**
	 * Gets the value of the merchantReferenceNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMerchantReferenceNumber() {
		return merchantReferenceNumber;
	}

	/**
	 * Sets the value of the merchantReferenceNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMerchantReferenceNumber(String value) {
		this.merchantReferenceNumber = value;
	}

	/**
	 * Gets the value of the paymentTransactionID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPaymentTransactionID() {
		return paymentTransactionID;
	}

	/**
	 * Sets the value of the paymentTransactionID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPaymentTransactionID(String value) {
		this.paymentTransactionID = value;
	}

}
