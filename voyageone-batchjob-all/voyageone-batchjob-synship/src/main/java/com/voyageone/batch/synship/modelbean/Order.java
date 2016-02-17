package com.voyageone.batch.synship.modelbean;

import java.math.BigDecimal;
import java.util.List;

public class Order implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3313364598006416940L;
	private String orderNumber;
	private String warehouseId;
	private String sourceOrderId;
	private String orderStatus;
	private String orderChannelId;
	private String shipChannel;
	private String shipName;
	private String shipCompany;
	private String shipAddress;
	private String shipDistrict;
	private String shipCity;
	private String shipState;
	private String shipZip;
	private String shipCountry;
	private String shipPhone;
	private String shipping;
	private String isLocked;
	private BigDecimal priceTotal;
	private String priceUnit;
	private BigDecimal originalPriceTotal;
	private String originalPriceUnit;
	private String comments;
	private String description;
	private String descriptionInner;
	private String labelStatus;
	private String expressType;
	private String productNum;
	private String shippedWeight;
	private String actualShippedWeightKg;
	private String actualShippedWeightLb;
	private String idCard;
	private String cartID;
	private String updateTime;
	private String orderDateTime;
	private String brand;
	private String sku;
	private BigDecimal salePriceTotal;
	private String salePriceUnit;
	private String refOrderNumber;
	private String alipayTransNo;
	private String alipayAccount;
	private String wangwangId;
	private String seorderStatus;
	private String orderInstructions;
	private String customerComments;
	private String giftMessage;
	private String noteToCustomer;
	private String freightCollect;
	private String exchangedWebId;
	private String isExchanged;
	private BigDecimal grandTotal;
	private BigDecimal finalGrandTotal;
	
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}
	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	/**
	 * @return the sourceOrderId
	 */
	public String getSourceOrderId() {
		return sourceOrderId;
	}
	/**
	 * @param sourceOrderId the sourceOrderId to set
	 */
	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}
	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}
	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * @return the shipName
	 */
	public String getShipName() {
		return shipName;
	}
	/**
	 * @param shipName the shipName to set
	 */
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	/**
	 * @return the shipCompany
	 */
	public String getShipCompany() {
		return shipCompany;
	}
	/**
	 * @param shipCompany the shipCompany to set
	 */
	public void setShipCompany(String shipCompany) {
		this.shipCompany = shipCompany;
	}
	/**
	 * @return the shipAddress
	 */
	public String getShipAddress() {
		return shipAddress;
	}
	/**
	 * @param shipAddress the shipAddress to set
	 */
	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}
	/**
	 * @return the shipDistrict
	 */
	public String getShipDistrict() {
		return shipDistrict;
	}
	/**
	 * @param shipDistrict the shipDistrict to set
	 */
	public void setShipDistrict(String shipDistrict) {
		this.shipDistrict = shipDistrict;
	}
	/**
	 * @return the shipCity
	 */
	public String getShipCity() {
		return shipCity;
	}
	/**
	 * @param shipCity the shipCity to set
	 */
	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}
	/**
	 * @return the shipState
	 */
	public String getShipState() {
		return shipState;
	}
	/**
	 * @param shipState the shipState to set
	 */
	public void setShipState(String shipState) {
		this.shipState = shipState;
	}
	/**
	 * @return the shipZip
	 */
	public String getShipZip() {
		return shipZip;
	}
	/**
	 * @param shipZip the shipZip to set
	 */
	public void setShipZip(String shipZip) {
		this.shipZip = shipZip;
	}
	/**
	 * @return the shipCountry
	 */
	public String getShipCountry() {
		return shipCountry;
	}
	/**
	 * @param shipCountry the shipCountry to set
	 */
	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}
	/**
	 * @return the shipPhone
	 */
	public String getShipPhone() {
		return shipPhone;
	}
	/**
	 * @param shipPhone the shipPhone to set
	 */
	public void setShipPhone(String shipPhone) {
		this.shipPhone = shipPhone;
	}
	/**
	 * @return the shipping
	 */
	public String getShipping() {
		return shipping;
	}
	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(String shipping) {
		this.shipping = shipping;
	}
	/**
	 * @return the isLocked
	 */
	public String getIsLocked() {
		return isLocked;
	}
	/**
	 * @param isLocked the isLocked to set
	 */
	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}
	/**
	 * @return the priceTotal
	 */
	public BigDecimal getPriceTotal() {
		return priceTotal;
	}
	/**
	 * @param priceTotal the priceTotal to set
	 */
	public void setPriceTotal(BigDecimal priceTotal) {
		this.priceTotal = priceTotal;
	}
	/**
	 * @return the priceUnit
	 */
	public String getPriceUnit() {
		return priceUnit;
	}
	/**
	 * @param priceUnit the priceUnit to set
	 */
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	/**
	 * @return the originalPriceTotal
	 */
	public BigDecimal getOriginalPriceTotal() {
		return originalPriceTotal;
	}
	/**
	 * @param originalPriceTotal the originalPriceTotal to set
	 */
	public void setOriginalPriceTotal(BigDecimal originalPriceTotal) {
		this.originalPriceTotal = originalPriceTotal;
	}
	/**
	 * @return the originalPriceUnit
	 */
	public String getOriginalPriceUnit() {
		return originalPriceUnit;
	}
	/**
	 * @param originalPriceUnit the originalPriceUnit to set
	 */
	public void setOriginalPriceUnit(String originalPriceUnit) {
		this.originalPriceUnit = originalPriceUnit;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the descriptionInner
	 */
	public String getDescriptionInner() {
		return descriptionInner;
	}
	/**
	 * @param descriptionInner the descriptionInner to set
	 */
	public void setDescriptionInner(String descriptionInner) {
		this.descriptionInner = descriptionInner;
	}
	/**
	 * @return the labelStatus
	 */
	public String getLabelStatus() {
		return labelStatus;
	}
	/**
	 * @param labelStatus the labelStatus to set
	 */
	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
	}
	/**
	 * @return the expressType
	 */
	public String getExpressType() {
		return expressType;
	}
	/**
	 * @param expressType the expressType to set
	 */
	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}
	/**
	 * @return the productNum
	 */
	public String getProductNum() {
		return productNum;
	}
	/**
	 * @param productNum the productNum to set
	 */
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	/**
	 * @return the shippedWeight
	 */
	public String getShippedWeight() {
		return shippedWeight;
	}
	/**
	 * @param shippedWeight the shippedWeight to set
	 */
	public void setShippedWeight(String shippedWeight) {
		this.shippedWeight = shippedWeight;
	}
	/**
	 * @return the actualShippedWeightKg
	 */
	public String getActualShippedWeightKg() {
		return actualShippedWeightKg;
	}
	/**
	 * @param actualShippedWeightKg the actualShippedWeightKg to set
	 */
	public void setActualShippedWeightKg(String actualShippedWeightKg) {
		this.actualShippedWeightKg = actualShippedWeightKg;
	}
	/**
	 * @return the actualShippedWeightLb
	 */
	public String getActualShippedWeightLb() {
		return actualShippedWeightLb;
	}
	/**
	 * @param actualShippedWeightLb the actualShippedWeightLb to set
	 */
	public void setActualShippedWeightLb(String actualShippedWeightLb) {
		this.actualShippedWeightLb = actualShippedWeightLb;
	}
	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the orderChannelId
	 */
	public String getOrderChannelId() {
		return orderChannelId;
	}
	/**
	 * @param orderChannelId the orderChannelId to set
	 */
	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}
	/**
	 * @return the shipChannel
	 */
	public String getShipChannel() {
		return shipChannel;
	}
	/**
	 * @param shipChannel the shipChannel to set
	 */
	public void setShipChannel(String shipChannel) {
		this.shipChannel = shipChannel;
	}
	/**
	 * @return the orderDateTime
	 */
	public String getOrderDateTime() {
		return orderDateTime;
	}
	/**
	 * @param orderDateTime the orderDateTime to set
	 */
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	/**
	 * @return the cartID
	 */
	public String getCartID() {
		return cartID;
	}
	/**
	 * @param cartID the cartID to set
	 */
	public void setCartID(String cartID) {
		this.cartID = cartID;
	}
	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}
	/**
	 * @param sku the sku to set
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}
	/**
	 * @return the salePriceTotal
	 */
	public BigDecimal getSalePriceTotal() {
		return salePriceTotal;
	}
	/**
	 * @param salePriceTotal the salePriceTotal to set
	 */
	public void setSalePriceTotal(BigDecimal salePriceTotal) {
		this.salePriceTotal = salePriceTotal;
	}
	/**
	 * @return the salePriceUnit
	 */
	public String getSalePriceUnit() {
		return salePriceUnit;
	}
	/**
	 * @param salePriceUnit the salePriceUnit to set
	 */
	public void setSalePriceUnit(String salePriceUnit) {
		this.salePriceUnit = salePriceUnit;
	}
	/**
	 * @return the refOrderNumber
	 */
	public String getRefOrderNumber() {
		return refOrderNumber;
	}
	/**
	 * @param refOrderNumber the refOrderNumber to set
	 */
	public void setRefOrderNumber(String refOrderNumber) {
		this.refOrderNumber = refOrderNumber;
	}
	/**
	 * @return the alipayTransNo
	 */
	public String getAlipayTransNo() {
		return alipayTransNo;
	}
	/**
	 * @param alipayTransNo the alipayTransNo to set
	 */
	public void setAlipayTransNo(String alipayTransNo) {
		this.alipayTransNo = alipayTransNo;
	}
	/**
	 * @return the alipayAccount
	 */
	public String getAlipayAccount() {
		return alipayAccount;
	}
	/**
	 * @param alipayAccount the alipayAccount to set
	 */
	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	/**
	 * @return the wangwangId
	 */
	public String getWangwangId() {
		return wangwangId;
	}
	/**
	 * @param wangwangId the wangwangId to set
	 */
	public void setWangwangId(String wangwangId) {
		this.wangwangId = wangwangId;
	}
	/**
	 * @return the seorderStatus
	 */
	public String getSeorderStatus() {
		return seorderStatus;
	}
	/**
	 * @param seorderStatus the seorderStatus to set
	 */
	public void setSeorderStatus(String seorderStatus) {
		this.seorderStatus = seorderStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOrderInstructions() {
		return orderInstructions;
	}
	public void setOrderInstructions(String orderInstructions) {
		this.orderInstructions = orderInstructions;
	}
	public String getCustomerComments() {
		return customerComments;
	}
	public void setCustomerComments(String customerComments) {
		this.customerComments = customerComments;
	}
	public String getGiftMessage() {
		return giftMessage;
	}
	public void setGiftMessage(String giftMessage) {
		this.giftMessage = giftMessage;
	}
	public String getNoteToCustomer() {
		return noteToCustomer;
	}
	public void setNoteToCustomer(String noteToCustomer) {
		this.noteToCustomer = noteToCustomer;
	}
	public String getFreightCollect() {
		return freightCollect;
	}
	public void setFreightCollect(String freightCollect) {
		this.freightCollect = freightCollect;
	}
	public String getExchangedWebId() {
		return exchangedWebId;
	}
	public void setExchangedWebId(String exchangedWebId) {
		this.exchangedWebId = exchangedWebId;
	}
	public String getIsExchanged() {
		return isExchanged;
	}
	public void setIsExchanged(String isExchanged) {
		this.isExchanged = isExchanged;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public BigDecimal getFinalGrandTotal() {
		return finalGrandTotal;
	}
	public void setFinalGrandTotal(BigDecimal finalGrandTotal) {
		this.finalGrandTotal = finalGrandTotal;
	}			
 }