package com.voyageone.batch.oms.formbean;

public class OutFormOrderDetailOrderDetail {
	
	/**
	 * 订单号
	 */
	private String orderNumber;
	
	/**
	 * 订单明细连番
	 */
	private String itemNumber;
	
//	/**
//	 * 是否调整项目
//	 */
//	private String adjustment;
	/**
	 * 
	 */
	private boolean adjustment;

	/**
	 * 
	 */
	private String product;
	
	/**
	 * 关联子订单信息
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
	 * 订单明细状态（Code）
	 */
	private String status;

	/**
	 * 订单明细状态（名称）
	 */
	private String statusName;
	
	/**
	 * reservation 状态（Code）
	 */
	private String resStatus;
	
	/**
	 * reservation 状态（名称）
	 */
	private String resStatusName;
	
	/**
	 * US-Pricing (extraCurrency5)
	 */
	private String usPricing;
	
	/**
	 * syncSynship (integer4)
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
	
	/**
	 * 产品图片路径（WS取得）
	 */
	private String imgPath;
	
	/**
	 * 分配仓库（synship取得）
	 */
	private String storehouse;
	
	/**
	 * 库存（WS取得）
	 */
	private String inventory;

	/**
	 * 原因（删除明细时用）
	 */
	private String reason;
	
	/**
	 * 发货运单号
	 */
	private String trackingNo;
	
	/**
	 * syn_ship_no
	 */
	private String synShipNo;
	
	/**
	 * 发货url
	 */
	private String synShipPath;
	
	/**
	 * 天猫url
	 */
	private String skuTmallPath;
	
	/**
	 * 发货方式
	 */
	private String shipChannel;
	
	/**
	 * 发货日期
	 */
	private String shipTime;
	
	/**
	 * 发货地区（1：国际，0：国内）
	 */
	private String trackingArea;
	
	/**
	 * 发货类型
	 */
	private String trackingType;
	
	/**
	 * 显示标志位
	 */
	private boolean showFlag;
	
	/**
	 * 打折信息
	 */
	private String discount;
	
	/**
	 * 最终售价
	 */
	private String price;

	/**
	 * 第三方SKU
	 */
	private String clientSku;
	
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

//	public String getAdjustment() {
//		return adjustment;
//	}
//
//	public void setAdjustment(String adjustment) {
//		this.adjustment = adjustment;
//	}
	
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getResStatus() {
		return resStatus;
	}

	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}

	public String getResStatusName() {
		return resStatusName;
	}

	public void setResStatusName(String resStatusName) {
		this.resStatusName = resStatusName;
	}

	public String getUsPricing() {
		return usPricing;
	}

	public void setUsPricing(String usPricing) {
		this.usPricing = usPricing;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public String getSynShipNo() {
		return synShipNo;
	}

	public void setSynShipNo(String synShipNo) {
		this.synShipNo = synShipNo;
	}

	public String getSynShipPath() {
		return synShipPath;
	}

	public void setSynShipPath(String synShipPath) {
		this.synShipPath = synShipPath;
	}

	public String getSkuTmallPath() {
		return skuTmallPath;
	}

	public void setSkuTmallPath(String skuTmallPath) {
		this.skuTmallPath = skuTmallPath;
	}

	public String getShipChannel() {
		return shipChannel;
	}

	public void setShipChannel(String shipChannel) {
		this.shipChannel = shipChannel;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	public String getTrackingArea() {
		return trackingArea;
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getTrackingType() {
		return trackingType;
	}

	public void setTrackingType(String trackingType) {
		this.trackingType = trackingType;
	}

	public boolean isShowFlag() {
		return showFlag;
	}

	public void setShowFlag(boolean showFlag) {
		this.showFlag = showFlag;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getClientSku() {
		return clientSku;
	}

	public void setClientSku(String clientSku) {
		this.clientSku = clientSku;
	}
}
