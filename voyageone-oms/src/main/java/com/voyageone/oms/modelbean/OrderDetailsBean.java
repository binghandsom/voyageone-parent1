package com.voyageone.oms.modelbean;

public class OrderDetailsBean {

	/**
	 * 
	 */
	private String orderNumber;	
	
	/**
	 * 
	 */
	private String itemNumber;
	
	/**
	 * 
	 */
	private boolean adjustment;

	/**
	 * 
	 */
	private String product;
	
	/**
	 * 
	 */
	private String subItemNumber;
	
	/**
	 * 
	 */
	private String pricePerUnit;
	
	/**
	 * 
	 */
	private String quantityOrdered;
	
	/**
	 * 
	 */
	private String quantityShipped;
	
	/**
	 * 
	 */
	private String quantityReturned;
	
	/**
	 * 
	 */
	private String sku;
	
	/**
	 * 
	 */
	private String dateShipped;

	/**
	 * 
	 */
	private String status;
	
	/**
	 * US-Pricing (extraCurrency5)
	 */
	private String usPricing;
	
	/**
	 * 库存分配用 
	 */
	private boolean resAllotFlg;
	
	/**
	 * syncSynShip (integer4)
	 */
	private boolean syncSynship;
	
	/**
	 * Reservation-ID (integer5)
	 */
	private String resId;
	
	/**
	 * modified（更新时间） 
	 */
	private String modified;
	
	private String creater;
	
	/**
	 * modifier 
	 */
	private String modifier;
	
	/**
	 * 产品图片路径（WS取得）
	 */
	private String imgPath;
	
	/**
	 * 分配仓库（WS取得）
	 */
	private String storehouse;
	
	/**
	 * 库存（WS取得）
	 */
	private String inventory;
	
	/**
	 * 打折信息（AddNewOrder）
	 */
	private String discount;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	public boolean isAdjustment() {
		return adjustment;
	}

	public void setAdjustment(boolean adjustment) {
		this.adjustment = adjustment;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}	
	
	public String getSubItemNumber() {
		return subItemNumber;
	}

	public void setSubItemNumber(String subItemNumber) {
		this.subItemNumber = subItemNumber;
	}

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(String quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public String getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(String quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	public String getQuantityReturned() {
		return quantityReturned;
	}

	public void setQuantityReturned(String quantityReturned) {
		this.quantityReturned = quantityReturned;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDateShipped() {
		return dateShipped;
	}

	public void setDateShipped(String dateShipped) {
		this.dateShipped = dateShipped;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsPricing() {
		return usPricing;
	}

	public void setUsPricing(String usPricing) {
		this.usPricing = usPricing;
	}

	public boolean isResAllotFlg() {
		return resAllotFlg;
	}

	public void setResAllotFlg(boolean resAllotFlg) {
		this.resAllotFlg = resAllotFlg;
	}

	public boolean isSyncSynship() {
		return syncSynship;
	}

	public void setSyncSynship(boolean syncSynship) {
		this.syncSynship = syncSynship;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getStorehouse() {
		return storehouse;
	}

	public void setStorehouse(String storehouse) {
		this.storehouse = storehouse;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}
}
