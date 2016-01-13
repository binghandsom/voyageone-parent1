package com.voyageone.oms.service;

import java.util.List;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.InFormAddNewOrderOrder;
import com.voyageone.oms.formbean.OutFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormAddNewOrderOrderHistory;
import com.voyageone.oms.modelbean.OrderDetailsBean;
import com.voyageone.oms.modelbean.OrdersBean;

/**
 * OMS 订单追加service
 * 
 * @author jacky
 *
 */
public interface OmsOrdersAddNewOrderService {

	/**
	 * 获得历史订单信息
	 * 
	 * @return
	 */
	public List<OutFormAddNewOrderOrderHistory> getOrdersHistoryInfo(String orderNumber);

	/**
	 * 获得当前订单信息
	 * 
	 * @return
	 */
	public OutFormAddNewOrderOrderHistory getCurOrderShipInfo(List<OutFormAddNewOrderOrderHistory> orderHistoryList, String orderNumber);
	
	/**
	 * 获得客户信息
	 * 
	 * @return
	 */
	public OutFormAddNewOrderCustomer getCustomerInfo(String orderNumber);
	
	/**
	 * 订单信息保存
	 * 
	 * @return
	 */
	public boolean doSaveOrderInfo(OrdersBean orderInfo);
	
	/**
	 * 订单信息保存
	 * 
	 * @return
	 */
	public boolean doSaveOrderDetailsInfo(List<OrderDetailsBean> orderDetailsList);
	
	/**
	 * 订单信息保存（含明细）
	 * 
	 * @return
	 */
	public void doSaveOrderAndDetailsInfo(InFormAddNewOrderOrder inFormAddNewOrderOrder, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 检索客户信息
	 * 
	 * @return
	 */
	public List<OutFormAddNewOrderCustomer> getCustomersList(InFormAddNewOrderCustomer inFormAddNewOrderCustomer);
}	

