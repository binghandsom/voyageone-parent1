package com.voyageone.batch.oms.modelbean;

import java.math.BigDecimal;


public class OrderDetails implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2731835154711748130L;
	private String orderNumber;
	private String itemNumber;
	private String status;
	private String reservationId;
	private String reservationStatus;
	private String shipChannel;
	private String store;
	private String pieces;
	private String descriptionInner;
	private String description;
	private String sku;
	private BigDecimal price;
	private String priceUnit;
	private BigDecimal originalPrice;
	private String originalPriceUnit;
	private String gender;
	private String productType;
	private String brand;
	private String origin;
	private String hsCode;
	private String hsDescription;
	private String unit;
	private String weightKg;
	private String weightLb;
	private String updateTime;
	private BigDecimal salePrice;
	private String salePriceUnit;
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
	 * @return the itemNumber
	 */
	public String getItemNumber() {
		return itemNumber;
	}
	/**
	 * @param itemNumber the itemNumber to set
	 */
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the reservationId
	 */
	public String getReservationId() {
		return reservationId;
	}
	/**
	 * @param reservationId the reservationId to set
	 */
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	/**
	 * @return the reservationStatus
	 */
	public String getReservationStatus() {
		return reservationStatus;
	}
	/**
	 * @param reservationStatus the reservationStatus to set
	 */
	public void setReservationStatus(String reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	/**
	 * @return the store
	 */
	public String getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}
	/**
	 * @return the pieces
	 */
	public String getPieces() {
		return pieces;
	}
	/**
	 * @param pieces the pieces to set
	 */
	public void setPieces(String pieces) {
		this.pieces = pieces;
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
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	 * @return the originalPrice
	 */
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	/**
	 * @param originalPrice the originalPrice to set
	 */
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
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
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
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
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	/**
	 * @return the hsCode
	 */
	public String getHsCode() {
		return hsCode;
	}
	/**
	 * @param hsCode the hsCode to set
	 */
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	/**
	 * @return the hsDescription
	 */
	public String getHsDescription() {
		return hsDescription;
	}
	/**
	 * @param hsDescription the hsDescription to set
	 */
	public void setHsDescription(String hsDescription) {
		this.hsDescription = hsDescription;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the weightKg
	 */
	public String getWeightKg() {
		return weightKg;
	}
	/**
	 * @param weightKg the weightKg to set
	 */
	public void setWeightKg(String weightKg) {
		this.weightKg = weightKg;
	}
	/**
	 * @return the weightLb
	 */
	public String getWeightLb() {
		return weightLb;
	}
	/**
	 * @param weightLb the weightLb to set
	 */
	public void setWeightLb(String weightLb) {
		this.weightLb = weightLb;
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
	 * @return the salePrice
	 */
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	/**
	 * @param salePrice the salePrice to set
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
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
	
}