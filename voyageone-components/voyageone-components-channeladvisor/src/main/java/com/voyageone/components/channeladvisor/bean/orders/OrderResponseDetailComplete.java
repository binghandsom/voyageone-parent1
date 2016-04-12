package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderResponseDetailComplete complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderResponseDetailComplete">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}OrderResponseDetailHigh">
 *       &lt;sequence>
 *         &lt;element name="CustomValueList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfCustomValue" minOccurs="0"/>
 *         &lt;element name="BuyerIpAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransactionNotes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderResponseDetailComplete", propOrder = { "customValueList", "buyerIpAddress", "transactionNotes" })
public class OrderResponseDetailComplete extends OrderResponseDetailHigh {

	@XmlElement(name = "CustomValueList")
	protected ArrayOfCustomValue customValueList;
	@XmlElement(name = "BuyerIpAddress")
	protected String buyerIpAddress;
	@XmlElement(name = "TransactionNotes")
	protected String transactionNotes;

	/**
	 * Gets the value of the customValueList property.
	 *
	 * @return possible object is {@link ArrayOfCustomValue }
	 *
	 */
	public ArrayOfCustomValue getCustomValueList() {
		return customValueList;
	}

	/**
	 * Sets the value of the customValueList property.
	 *
	 * @param value
	 *            allowed object is {@link ArrayOfCustomValue }
	 * 
	 */
	public void setCustomValueList(ArrayOfCustomValue value) {
		this.customValueList = value;
	}

	/**
	 * Gets the value of the buyerIpAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBuyerIpAddress() {
		return buyerIpAddress;
	}

	/**
	 * Sets the value of the buyerIpAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBuyerIpAddress(String value) {
		this.buyerIpAddress = value;
	}

	/**
	 * Gets the value of the transactionNotes property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTransactionNotes() {
		return transactionNotes;
	}

	/**
	 * Sets the value of the transactionNotes property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTransactionNotes(String value) {
		this.transactionNotes = value;
	}

}
