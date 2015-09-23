package com.voyageone.oms.formbean;

import java.util.List;

import com.voyageone.core.ajax.AjaxRequestBean;
import com.voyageone.oms.modelbean.OrderDetailsBean;

/**
 * 画面传入订单明细bean（Cancel，Return 共用）
 * 
 * @author jacky
 *
 */
public class InFormOrderdetailReturn extends AjaxRequestBean {
	// 元订单号
	private String sourceOrderId;
	
	// 订单号
	private String orderNumber;
	
	// 需return订单明细一览
	private List<OrderDetailsBean> orderDetailsList;

	// Shipping return 标志
	private boolean returnShipping;
	
	// 原因（与Cancel多条订单明细，共用）
	private String reason;
	
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

	public List<OrderDetailsBean> getOrderDetailsList() {
		return orderDetailsList;
	}

	public void setOrderDetailsList(List<OrderDetailsBean> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}
	
	public boolean isReturnShipping() {
		return returnShipping;
	}

	public void setReturnShipping(boolean returnShipping) {
		this.returnShipping = returnShipping;
	}	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
