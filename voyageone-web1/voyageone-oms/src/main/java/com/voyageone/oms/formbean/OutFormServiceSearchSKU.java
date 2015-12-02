package com.voyageone.oms.formbean;

public class OutFormServiceSearchSKU {
	/**
	 * sku
	 */
	private String sku;	

	/**
	 * product name
	 */
	private String product;
	
	/**
	 * description
	 */
	private String description;
	
	/**
	 * price
	 */
	private String pricePerUnit;
	
	/**
	 * inventory
	 */
	private String inventory;
	
	/**
	 * 产品图片Url
	 */
	private String imgPath;
	
	/**
	 * 产品天猫Url
	 */
	private String skuTmallUrl;

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getProduct() {
		return this.product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPricePerUnit() {
		return this.pricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
 
		this.description = description;
	}

	public String getInventory() {
		return this.inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getImgPath() {
		return this.imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getSkuTmallUrl() {
		return this.skuTmallUrl;
	}

	public void setSkuTmallUrl(String skuTmallUrl) {
		this.skuTmallUrl = skuTmallUrl;
	}
}
