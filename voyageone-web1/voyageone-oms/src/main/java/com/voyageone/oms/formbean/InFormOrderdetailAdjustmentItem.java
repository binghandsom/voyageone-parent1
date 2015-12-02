package com.voyageone.oms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

/**
 * 画面传入订单价格bean
 * 
 * @author jerry
 *
 */
public class InFormOrderdetailAdjustmentItem extends AjaxRequestBean {
	
	/**
	 * 元订单号
	 */
	private String sourceOrderId;
	
	/**
	 * 订单号
	 */
	private String orderNumber;
	
	/**
	 * 调整类型（1:Charges，2:Discounts，3:Coupon，4:Shipping）
	 */
	private String adjustmentType;
	
	/**
	 * 调整数值
	 */
	private String adjustmentNumber;
	
	/**
	 * 调整原因
	 */
	private String adjustmentReason;
	
	/**
	 * 打折类型
	 */
	private String adjustmentDiscountType;

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

	public String getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	
	public String getAdjustmentReason() {
		return adjustmentReason;
	}

	public void setAdjustmentReason(String adjustmentReason) {
		this.adjustmentReason = adjustmentReason;
	}

	public String getAdjustmentNumber() {
		return adjustmentNumber;
	}

	public void setAdjustmentNumber(String adjustmentNumber) {
		this.adjustmentNumber = adjustmentNumber;
	}	
	
	public String getAdjustmentDiscountType() {
		return adjustmentDiscountType;
	}

	public void setAdjustmentDiscountType(String adjustmentDiscountType) {
		this.adjustmentDiscountType = adjustmentDiscountType;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
