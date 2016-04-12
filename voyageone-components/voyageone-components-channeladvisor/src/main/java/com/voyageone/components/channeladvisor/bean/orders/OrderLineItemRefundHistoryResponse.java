package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for OrderLineItemRefundHistoryResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineItemRefundHistoryResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}RefundItem">
 *       &lt;sequence>
 *         &lt;element name="InvoiceItemID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ItemSaleSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RefundRequestStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RefundPaymentInfo" type="{http://api.channeladvisor.com/datacontracts/orders}PaymentInfo" minOccurs="0"/>
 *         &lt;element name="RestockStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RefundCreateDateGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineItemRefundHistoryResponse", propOrder = { "invoiceItemID", "itemSaleSource",
		"refundRequestStatus", "refundPaymentInfo", "restockStatus", "refundCreateDateGMT" })
public class OrderLineItemRefundHistoryResponse extends RefundItem {

	@XmlElement(name = "InvoiceItemID")
	protected int invoiceItemID;
	@XmlElement(name = "ItemSaleSource")
	protected String itemSaleSource;
	@XmlElement(name = "RefundRequestStatus")
	protected String refundRequestStatus;
	@XmlElement(name = "RefundPaymentInfo")
	protected PaymentInfo refundPaymentInfo;
	@XmlElement(name = "RestockStatus")
	protected String restockStatus;
	@XmlElement(name = "RefundCreateDateGMT", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar refundCreateDateGMT;

	/**
	 * Gets the value of the invoiceItemID property.
	 *
	 */
	public int getInvoiceItemID() {
		return invoiceItemID;
	}

	/**
	 * Sets the value of the invoiceItemID property.
	 *
	 */
	public void setInvoiceItemID(int value) {
		this.invoiceItemID = value;
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
	 * Gets the value of the refundRequestStatus property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRefundRequestStatus() {
		return refundRequestStatus;
	}

	/**
	 * Sets the value of the refundRequestStatus property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setRefundRequestStatus(String value) {
		this.refundRequestStatus = value;
	}

	/**
	 * Gets the value of the refundPaymentInfo property.
	 *
	 * @return possible object is {@link PaymentInfo }
	 *
	 */
	public PaymentInfo getRefundPaymentInfo() {
		return refundPaymentInfo;
	}

	/**
	 * Sets the value of the refundPaymentInfo property.
	 *
	 * @param value
	 *            allowed object is {@link PaymentInfo }
	 *
	 */
	public void setRefundPaymentInfo(PaymentInfo value) {
		this.refundPaymentInfo = value;
	}

	/**
	 * Gets the value of the restockStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRestockStatus() {
		return restockStatus;
	}

	/**
	 * Sets the value of the restockStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRestockStatus(String value) {
		this.restockStatus = value;
	}

	/**
	 * Gets the value of the refundCreateDateGMT property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getRefundCreateDateGMT() {
		return refundCreateDateGMT;
	}

	/**
	 * Sets the value of the refundCreateDateGMT property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setRefundCreateDateGMT(XMLGregorianCalendar value) {
		this.refundCreateDateGMT = value;
	}

}
