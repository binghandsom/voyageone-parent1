package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for OrderCriteria complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderCreationFilterBeginTimeGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="OrderCreationFilterEndTimeGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="StatusUpdateFilterBeginTimeGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="StatusUpdateFilterEndTimeGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="JoinDateFiltersWithOr" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DetailLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExportState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderIDList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="ClientOrderIdentifierList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="OrderStateFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaymentStatusFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CheckoutStatusFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShippingStatusFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RefundStatusFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DistributionCenterCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FulfillmentTypeFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PageNumberFilter" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderCriteria", propOrder = { "orderCreationFilterBeginTimeGMT", "orderCreationFilterEndTimeGMT",
		"statusUpdateFilterBeginTimeGMT", "statusUpdateFilterEndTimeGMT", "joinDateFiltersWithOr", "detailLevel",
		"exportState", "orderIDList", "clientOrderIdentifierList", "orderStateFilter", "paymentStatusFilter",
		"checkoutStatusFilter", "shippingStatusFilter", "refundStatusFilter", "distributionCenterCode",
		"fulfillmentTypeFilter", "pageNumberFilter", "pageSize" })
public class OrderCriteria {

	@XmlElement(name = "OrderCreationFilterBeginTimeGMT", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar orderCreationFilterBeginTimeGMT;
	@XmlElement(name = "OrderCreationFilterEndTimeGMT", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar orderCreationFilterEndTimeGMT;
	@XmlElement(name = "StatusUpdateFilterBeginTimeGMT", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar statusUpdateFilterBeginTimeGMT;
	@XmlElement(name = "StatusUpdateFilterEndTimeGMT", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar statusUpdateFilterEndTimeGMT;
	@XmlElement(name = "JoinDateFiltersWithOr", required = true, type = Boolean.class, nillable = true)
	protected Boolean joinDateFiltersWithOr;
	@XmlElement(name = "DetailLevel")
	protected String detailLevel;
	@XmlElement(name = "ExportState")
	protected String exportState;
	@XmlElement(name = "OrderIDList")
	protected ArrayOfInt orderIDList;
	@XmlElement(name = "ClientOrderIdentifierList")
	protected ArrayOfString clientOrderIdentifierList;
	@XmlElement(name = "OrderStateFilter")
	protected String orderStateFilter;
	@XmlElement(name = "PaymentStatusFilter")
	protected String paymentStatusFilter;
	@XmlElement(name = "CheckoutStatusFilter")
	protected String checkoutStatusFilter;
	@XmlElement(name = "ShippingStatusFilter")
	protected String shippingStatusFilter;
	@XmlElement(name = "RefundStatusFilter")
	protected String refundStatusFilter;
	@XmlElement(name = "DistributionCenterCode")
	protected String distributionCenterCode;
	@XmlElement(name = "FulfillmentTypeFilter")
	protected String fulfillmentTypeFilter;
	@XmlElement(name = "PageNumberFilter", required = true, type = Integer.class, nillable = true)
	protected Integer pageNumberFilter;
	@XmlElement(name = "PageSize", required = true, type = Integer.class, nillable = true)
	protected Integer pageSize;

	/**
	 * Gets the value of the orderCreationFilterBeginTimeGMT property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getOrderCreationFilterBeginTimeGMT() {
		return orderCreationFilterBeginTimeGMT;
	}

	/**
	 * Sets the value of the orderCreationFilterBeginTimeGMT property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setOrderCreationFilterBeginTimeGMT(XMLGregorianCalendar value) {
		this.orderCreationFilterBeginTimeGMT = value;
	}

	/**
	 * Gets the value of the orderCreationFilterEndTimeGMT property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getOrderCreationFilterEndTimeGMT() {
		return orderCreationFilterEndTimeGMT;
	}

	/**
	 * Sets the value of the orderCreationFilterEndTimeGMT property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setOrderCreationFilterEndTimeGMT(XMLGregorianCalendar value) {
		this.orderCreationFilterEndTimeGMT = value;
	}

	/**
	 * Gets the value of the statusUpdateFilterBeginTimeGMT property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getStatusUpdateFilterBeginTimeGMT() {
		return statusUpdateFilterBeginTimeGMT;
	}

	/**
	 * Sets the value of the statusUpdateFilterBeginTimeGMT property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setStatusUpdateFilterBeginTimeGMT(XMLGregorianCalendar value) {
		this.statusUpdateFilterBeginTimeGMT = value;
	}

	/**
	 * Gets the value of the statusUpdateFilterEndTimeGMT property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getStatusUpdateFilterEndTimeGMT() {
		return statusUpdateFilterEndTimeGMT;
	}

	/**
	 * Sets the value of the statusUpdateFilterEndTimeGMT property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setStatusUpdateFilterEndTimeGMT(XMLGregorianCalendar value) {
		this.statusUpdateFilterEndTimeGMT = value;
	}

	/**
	 * Gets the value of the joinDateFiltersWithOr property.
	 *
	 * @return possible object is {@link Boolean }
	 *
	 */
	public Boolean isJoinDateFiltersWithOr() {
		return joinDateFiltersWithOr;
	}

	/**
	 * Sets the value of the joinDateFiltersWithOr property.
	 *
	 * @param value
	 *            allowed object is {@link Boolean }
	 *
	 */
	public void setJoinDateFiltersWithOr(Boolean value) {
		this.joinDateFiltersWithOr = value;
	}

	/**
	 * Gets the value of the detailLevel property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getDetailLevel() {
		return detailLevel;
	}

	/**
	 * Sets the value of the detailLevel property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setDetailLevel(String value) {
		this.detailLevel = value;
	}

	/**
	 * Gets the value of the exportState property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getExportState() {
		return exportState;
	}

	/**
	 * Sets the value of the exportState property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setExportState(String value) {
		this.exportState = value;
	}

	/**
	 * Gets the value of the orderIDList property.
	 *
	 * @return possible object is {@link ArrayOfInt }
	 *
	 */
	public ArrayOfInt getOrderIDList() {
		return orderIDList;
	}

	/**
	 * Sets the value of the orderIDList property.
	 *
	 * @param value
	 *            allowed object is {@link ArrayOfInt }
	 *
	 */
	public void setOrderIDList(ArrayOfInt value) {
		this.orderIDList = value;
	}

	/**
	 * Gets the value of the clientOrderIdentifierList property.
	 *
	 * @return possible object is {@link ArrayOfString }
	 *
	 */
	public ArrayOfString getClientOrderIdentifierList() {
		return clientOrderIdentifierList;
	}

	/**
	 * Sets the value of the clientOrderIdentifierList property.
	 *
	 * @param value
	 *            allowed object is {@link ArrayOfString }
	 * 
	 */
	public void setClientOrderIdentifierList(ArrayOfString value) {
		this.clientOrderIdentifierList = value;
	}

	/**
	 * Gets the value of the orderStateFilter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderStateFilter() {
		return orderStateFilter;
	}

	/**
	 * Sets the value of the orderStateFilter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderStateFilter(String value) {
		this.orderStateFilter = value;
	}

	/**
	 * Gets the value of the paymentStatusFilter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPaymentStatusFilter() {
		return paymentStatusFilter;
	}

	/**
	 * Sets the value of the paymentStatusFilter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPaymentStatusFilter(String value) {
		this.paymentStatusFilter = value;
	}

	/**
	 * Gets the value of the checkoutStatusFilter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCheckoutStatusFilter() {
		return checkoutStatusFilter;
	}

	/**
	 * Sets the value of the checkoutStatusFilter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCheckoutStatusFilter(String value) {
		this.checkoutStatusFilter = value;
	}

	/**
	 * Gets the value of the shippingStatusFilter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShippingStatusFilter() {
		return shippingStatusFilter;
	}

	/**
	 * Sets the value of the shippingStatusFilter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShippingStatusFilter(String value) {
		this.shippingStatusFilter = value;
	}

	/**
	 * Gets the value of the refundStatusFilter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRefundStatusFilter() {
		return refundStatusFilter;
	}

	/**
	 * Sets the value of the refundStatusFilter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRefundStatusFilter(String value) {
		this.refundStatusFilter = value;
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
	 * Gets the value of the fulfillmentTypeFilter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFulfillmentTypeFilter() {
		return fulfillmentTypeFilter;
	}

	/**
	 * Sets the value of the fulfillmentTypeFilter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFulfillmentTypeFilter(String value) {
		this.fulfillmentTypeFilter = value;
	}

	/**
	 * Gets the value of the pageNumberFilter property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getPageNumberFilter() {
		return pageNumberFilter;
	}

	/**
	 * Sets the value of the pageNumberFilter property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setPageNumberFilter(Integer value) {
		this.pageNumberFilter = value;
	}

	/**
	 * Gets the value of the pageSize property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the value of the pageSize property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setPageSize(Integer value) {
		this.pageSize = value;
	}

}
