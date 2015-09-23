package com.voyageone.batch.oms.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "upc", "productId","colorId","sizeId","itemCost","itemDiscount","itemTotal","itemTax"})
public class OrderItemBean {
	private String upc;
	private String productId;
	private String colorId;
	private String sizeId;
	private String itemCost;
	private String itemDiscount;
	private Double itemTotal;
	private int itemTax = 0;
	/**
	 * @return the upc
	 */
	public String getUpc() {
		return upc;
	}
	/**
	 * @param upc the upc to set
	 */
	public void setUpc(String upc) {
		this.upc = upc;
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
	 * @return the colorId
	 */
	public String getColorId() {
		return colorId;
	}
	/**
	 * @param colorId the colorId to set
	 */
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	/**
	 * @return the sizeId
	 */
	public String getSizeId() {
		return sizeId;
	}
	/**
	 * @param sizeId the sizeId to set
	 */
	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}
	/**
	 * @return the itemCost
	 */
	public String getItemCost() {
		return itemCost;
	}
	/**
	 * @param itemCost the itemCost to set
	 */
	public void setItemCost(String itemCost) {
		this.itemCost = itemCost;
	}
	/**
	 * @return the itemDiscount
	 */
	public String getItemDiscount() {
		return itemDiscount;
	}
	/**
	 * @param itemDiscount the itemDiscount to set
	 */
	public void setItemDiscount(String itemDiscount) {
		this.itemDiscount = itemDiscount;
	}
	/**
	 * @return the itemTotal
	 */
	public Double getItemTotal() {
		return itemTotal;
	}
	/**
	 * @param itemTotal the itemTotal to set
	 */
	public void setItemTotal(Double itemTotal) {
		this.itemTotal = itemTotal;
	}
	/**
	 * @return the itemTax
	 */
	public int getItemTax() {
		return itemTax;
	}
	/**
	 * @param itemTax the itemTax to set
	 */
	public void setItemTax(int itemTax) {
		this.itemTax = itemTax;
	}
	
}
