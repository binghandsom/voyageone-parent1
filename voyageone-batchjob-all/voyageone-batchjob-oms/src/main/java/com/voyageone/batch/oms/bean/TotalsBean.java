package com.voyageone.batch.oms.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "grandTotal", "subTotal","shippingTotal","itemDiscountTotal","shippingDiscountTotal","taxTotal","itemTaxTotal","shippingTax"})
public class TotalsBean {
	private double grandTotal;
	private double subTotal;
	private double shippingTotal;
	private double itemDiscountTotal;
	private int shippingDiscountTotal = 0;
	private int taxTotal = 0;
	private int itemTaxTotal = 0;
	private int shippingTax = 0;
	/**
	 * @return the grandTotal
	 */
	public double getGrandTotal() {
		return grandTotal;
	}
	/**
	 * @param grandTotal the grandTotal to set
	 */
	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}
	/**
	 * @return the subTotal
	 */
	public double getSubTotal() {
		return subTotal;
	}
	/**
	 * @param subTotal the subTotal to set
	 */
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	/**
	 * @return the shippingTotal
	 */
	public double getShippingTotal() {
		return shippingTotal;
	}
	/**
	 * @param shippingTotal the shippingTotal to set
	 */
	public void setShippingTotal(double shippingTotal) {
		this.shippingTotal = shippingTotal;
	}
	/**
	 * @return the itemDiscountTotal
	 */
	public double getItemDiscountTotal() {
		return itemDiscountTotal;
	}
	/**
	 * @param itemDiscountTotal the itemDiscountTotal to set
	 */
	public void setItemDiscountTotal(double itemDiscountTotal) {
		this.itemDiscountTotal = itemDiscountTotal;
	}
	/**
	 * @return the shippingDiscountTotal
	 */
	public int getShippingDiscountTotal() {
		return shippingDiscountTotal;
	}
	/**
	 * @param shippingDiscountTotal the shippingDiscountTotal to set
	 */
	public void setShippingDiscountTotal(int shippingDiscountTotal) {
		this.shippingDiscountTotal = shippingDiscountTotal;
	}
	/**
	 * @return the taxTotal
	 */
	public int getTaxTotal() {
		return taxTotal;
	}
	/**
	 * @param taxTotal the taxTotal to set
	 */
	public void setTaxTotal(int taxTotal) {
		this.taxTotal = taxTotal;
	}
	/**
	 * @return the itemTaxTotal
	 */
	public int getItemTaxTotal() {
		return itemTaxTotal;
	}
	/**
	 * @param itemTaxTotal the itemTaxTotal to set
	 */
	public void setItemTaxTotal(int itemTaxTotal) {
		this.itemTaxTotal = itemTaxTotal;
	}
	/**
	 * @return the shippingTax
	 */
	public int getShippingTax() {
		return shippingTax;
	}
	/**
	 * @param shippingTax the shippingTax to set
	 */
	public void setShippingTax(int shippingTax) {
		this.shippingTax = shippingTax;
	}
	
}
