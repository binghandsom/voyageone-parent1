package com.voyageone.oms.formbean;

import java.util.List;

import com.voyageone.core.ajax.AjaxRequestBean;
import com.voyageone.oms.modelbean.CustomerBean;
import com.voyageone.oms.modelbean.OrderDetailsBean;
import com.voyageone.oms.modelbean.OrdersBean;

/**
 * 画面传入订单明细bean
 * 
 * @author jacky
 *
 */
public class InFormAddNewOrderOrder extends AjaxRequestBean {

	// 订单信息
	private OrdersBean orderInfo;

	// 顾客信息
	private CustomerBean customerInfo;

	// Ship信息
	private CustomerBean shipToInfo;
	
	// 订单详情
	private List<OrderDetailsBean> orderDetailsList;

	//	本次删除 20150510
//	//	订单Transactions信息
//	private List<TransactionsBean> transactionsList;
	
	public OrdersBean getOrderInfo() {
		return this.orderInfo;
	}
	
	public void setOrderInfo(OrdersBean orderInfo) {
		this.orderInfo = orderInfo;
	}

	public List<OrderDetailsBean> getOrderDetailsList() {
		return this.orderDetailsList;
	}

	public void setOrderDetailsList(List<OrderDetailsBean> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}
	
//	public List<TransactionsBean> getTransactionsList() {
//		return this.transactionsList;
//	}
//
//	public void setTransactionsList(List<TransactionsBean> transactionsList) {
//		this.transactionsList = transactionsList;
//	}

	public CustomerBean getCustomerInfo() {
		return this.customerInfo;
	}
	
	public void setCustomerInfo(CustomerBean customerInfo) {
		this.customerInfo = customerInfo;
	}

	public CustomerBean getShipToInfo() {
		return shipToInfo;
	}
	
	public void setShipToInfo(CustomerBean shipToInfo) {
		this.shipToInfo = shipToInfo;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
