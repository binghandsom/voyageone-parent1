package com.voyageone.oms.modelbean;

/**
 * 订单价格计算bean
 * 
 * @author jerry
 *
 */
public class OrderPrice {
	
	/**
	 * 元订单号
	 */
	private String sourceOrderId;
	
	/**
	 * 订单号
	 */
	private String orderNumber;
	
	/**
	 * product Total
	 */
	private float productTotal;
	
	/**
	 * final_product_total（revised product Total）
	 */
	private float finalProductTotal;
	
	/**
	 * surcharge
	 */
	private float surcharge;
	
	/**
	 * revised surcharge
	 */
	private float revisedSurcharge;
	
	/**
	 * discount
	 */
	private float discount;
	
	/**
	 * revised discount
	 */
	private float revisedDiscount;
	
	/**
	 * coupon discount
	 */
	private float couponDiscount;
	
	/**
	 * revised_coupon_discount
	 */
	private float revisedCouponDiscount;
	
	/**
	 * shipping Total
	 */
	private float shippingTotal;
	
	/**
	 * revised shipping
	 */
	private float finalShippingTotal;
	
	/**
	 * 修正前revised shipping（退运费时用）
	 */
	private float finalShippingTotal_beforeReturnShipping;
	
	/**
	 * grand total
	 */
	private float grandTotal;
	
	/**
	 * revised grand total
	 */
	private float finalGrandTotal;
	
	//	字段删除
//	/**
//	 * expected_net
//	 */
//	private float expectedNet;
//	
//	/**
//	 * actual_net
//	 */
//	private float actualNet;
	
//	/**
//	 * balancedue
//	 */
//	private float balanceDue;
	
	/**
	 * expected
	 */
	private float expected;
	
	/**
	 * item 调整金额
	 */
	private float itemAdjustPrice;
	
	//	修正前价格
	/**
	 * final_grand_total
	 */
	private float origFinalGrandTotal;
	
	/**
	 * final_shipping_total
	 */
	private float origFinalShippingTotal;
	
	/**
	 * revised_surcharge
	 */
	private float origRevisedSurcharge;
	
	/**
	 * revised_discount
	 */
	private float origRevisedDiscount;
	
	/**
	 * final_product_total
	 */
	private float origFinalProductTotal;
	
	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public float getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}

	public float getFinalProductTotal() {
		return finalProductTotal;
	}

	public void setFinalProductTotal(float finalProductTotal) {
		this.finalProductTotal = finalProductTotal;
	}

	public float getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(float surcharge) {
		this.surcharge = surcharge;
	}

	public float getRevisedSurcharge() {
		return revisedSurcharge;
	}

	public void setRevisedSurcharge(float revisedSurcharge) {
		this.revisedSurcharge = revisedSurcharge;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public float getRevisedDiscount() {
		return revisedDiscount;
	}

	public void setRevisedDiscount(float revisedDiscount) {
		this.revisedDiscount = revisedDiscount;
	}

	public float getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(float couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public float getRevisedCouponDiscount() {
		return revisedCouponDiscount;
	}

	public void setRevisedCouponDiscount(float revisedCouponDiscount) {
		this.revisedCouponDiscount = revisedCouponDiscount;
	}

	public float getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(float shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public float getFinalShippingTotal() {
		return finalShippingTotal;
	}

	public void setFinalShippingTotal(float finalShippingTotal) {
		this.finalShippingTotal = finalShippingTotal;
	}

	public float getFinalShippingTotal_beforeReturnShipping() {
		return finalShippingTotal_beforeReturnShipping;
	}

	public void setFinalShippingTotal_beforeReturnShipping(
			float finalShippingTotal_beforeReturnShipping) {
		this.finalShippingTotal_beforeReturnShipping = finalShippingTotal_beforeReturnShipping;
	}

	public float getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(float grandTotal) {
		this.grandTotal = grandTotal;
	}

	public float getFinalGrandTotal() {
		return finalGrandTotal;
	}

	public void setFinalGrandTotal(float finalGrandTotal) {
		this.finalGrandTotal = finalGrandTotal;
	}

//	public float getExpectedNet() {
//		return expectedNet;
//	}
//
//	public void setExpectedNet(float expectedNet) {
//		this.expectedNet = expectedNet;
//	}
//
//	public float getActualNet() {
//		return actualNet;
//	}
//
//	public void setActualNet(float actualNet) {
//		this.actualNet = actualNet;
//	}
//
//	public float getBalanceDue() {
//		return balanceDue;
//	}
//
//	public void setBalanceDue(float balanceDue) {
//		this.balanceDue = balanceDue;
//	}

	public float getExpected() {
		return expected;
	}

	public void setExpected(float expected) {
		this.expected = expected;
	}
	
	public float getItemAdjustPrice() {
		return itemAdjustPrice;
	}

	public void setItemAdjustPrice(float itemAdjustPrice) {
		this.itemAdjustPrice = itemAdjustPrice;
	}

	public float getOrigFinalGrandTotal() {
		return origFinalGrandTotal;
	}

	public void setOrigFinalGrandTotal(float origFinalGrandTotal) {
		this.origFinalGrandTotal = origFinalGrandTotal;
	}

	public float getOrigFinalShippingTotal() {
		return origFinalShippingTotal;
	}

	public void setOrigFinalShippingTotal(float origFinalShippingTotal) {
		this.origFinalShippingTotal = origFinalShippingTotal;
	}

	public float getOrigRevisedSurcharge() {
		return origRevisedSurcharge;
	}

	public void setOrigRevisedSurcharge(float origRevisedSurcharge) {
		this.origRevisedSurcharge = origRevisedSurcharge;
	}

	public float getOrigRevisedDiscount() {
		return origRevisedDiscount;
	}

	public void setOrigRevisedDiscount(float origRevisedDiscount) {
		this.origRevisedDiscount = origRevisedDiscount;
	}

	public float getOrigFinalProductTotal() {
		return origFinalProductTotal;
	}

	public void setOrigFinalProductTotal(float origFinalProductTotal) {
		this.origFinalProductTotal = origFinalProductTotal;
	}
}
