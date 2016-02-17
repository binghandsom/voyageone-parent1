package com.voyageone.common.magento.api.bean;

public class InventoryStockItemBean {

	private String isInStock;
	
	private String productId;
	
	private String qty;
	
	private String sku;

	/**
	 * @return the isInStock
	 */
	public String getIsInStock() {
		return isInStock;
	}

	/**
	 * @param isInStock the isInStock to set
	 */
	public void setIsInStock(String isInStock) {
		this.isInStock = isInStock;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
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

}
