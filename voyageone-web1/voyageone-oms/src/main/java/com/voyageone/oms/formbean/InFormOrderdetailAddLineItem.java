package com.voyageone.oms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

/**
 * 画面传入Sku Bean
 * 
 * @author jerry
 *
 */
public class InFormOrderdetailAddLineItem extends AjaxRequestBean {
	
	/**
	 * orderNumber
	 */
	private String orderNumber;
	
	/**
	 * sku
	 */
	private String sku;
	
	/**
	 * itemName
	 */
	private String itemName;
	
	/**
	 * descripton
	 */
	private String description;
	
	/**
	 * price
	 */
	private String price;
	
	/**
	 * inventory
	 */
	private String inventory;
	
	/**
	 * item_number
	 */
	private String detailLineItem;
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	
	public String getDetailLineItem() {
		return detailLineItem;
	}

	public void setDetailLineItem(String detailLineItem) {
		this.detailLineItem = detailLineItem;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
