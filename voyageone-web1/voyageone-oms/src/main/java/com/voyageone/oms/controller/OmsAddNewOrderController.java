package com.voyageone.oms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.InFormAddNewOrderOrder;
import com.voyageone.oms.formbean.OutFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormAddNewOrderOrderHistory;
import com.voyageone.oms.modelbean.CustomerBean;
import com.voyageone.oms.modelbean.OrdersBean;
import com.voyageone.oms.service.OmsMasterInfoService;
import com.voyageone.oms.service.OmsOrdersAddNewOrderService;

/**
 * OMS AddNewOrder画面
 * 
 * @author jacky
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = OmsUrlConstants.URL_OMS_ORDERS_ADDNEWORDER)
public class OmsAddNewOrderController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsAddNewOrderController.class);
	
	@Autowired
	private OmsOrdersAddNewOrderService omsOrdersAddNewOrderService;
	@Autowired
	private OmsMasterInfoService omsMasterInfoService;
	
	/**
	 * 初期化	检索订单信息
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
	public void doInit(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map orderNumberMap) {
		
		String orderNumber = (String)orderNumberMap.get("orderNumber");
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("orderNumber = " + orderNumber);

		//	数据取得
		// 		订单历史信息
		List<OutFormAddNewOrderOrderHistory> orderHistoryList = omsOrdersAddNewOrderService.getOrdersHistoryInfo(orderNumber);		
		// 		当前订单信息
		OutFormAddNewOrderOrderHistory curOrderShipInfo = omsOrdersAddNewOrderService.getCurOrderShipInfo(orderHistoryList, orderNumber);
		// 		当前客户信息
		OutFormAddNewOrderCustomer customerInfo = omsOrdersAddNewOrderService.getCustomerInfo(orderNumber);
		
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();			
		//		正常
		result.setResult(true);

		// 		订单历史
		ordersListMap.put("orderHistoryList", orderHistoryList);
		//		当前订单信息
		ordersListMap.put("curOrderShipInfo", curOrderShipInfo);
		// 		当前客户信息
		ordersListMap.put("customerInfo", customerInfo);
		
		result.setResultInfo(ordersListMap);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}

	/**
	 * 初期化	取得画面用Master信息
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doInitNewOrder", method = RequestMethod.POST)
	public void doInitNewOrder(HttpServletRequest request, HttpServletResponse response) {
		
		// 从session中获得该用户的检索条件
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		Iterator<String> it = propertyPermissions.keySet().iterator();
		// 门店列表
		List<PermissionBean> propertyList = new ArrayList<PermissionBean>();

		while (it.hasNext()) {
			String propertyId = it.next();
			propertyList.add(propertyPermissions.get(propertyId));
		}
		
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("propertyList", propertyList);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		// 设置返回结果
		result.setResult(true);
		result.setResultInfo(resultDataMap);
		
		// 结果返回输出流
		result.writeTo(request, response);
	}
	
	/**
	 * 保存
	 * 
	 * @param request
	 * @param response
	 * @param orderInfoReq
	 */
	@RequestMapping(value = {"/doSaveOther","/doSaveOriginal"}, method = RequestMethod.POST)
	public void doSave(HttpServletRequest request, HttpServletResponse response,
			@RequestBody InFormAddNewOrderOrder orderInfoReq) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

		// 输入参数出力
		logger.info("doSave input bean = " + JsonUtil.getJsonString(orderInfoReq));
		
		setCustomerInfo(orderInfoReq);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();		

		// 订单信息保存（含明细）
		omsOrdersAddNewOrderService.doSaveOrderAndDetailsInfo(orderInfoReq, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 检索客户信息
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doGetCustomerInfo", method = RequestMethod.POST)
	public void doGetCustomerInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("input bean = " + JsonUtil.getJsonString(inFormAddNewOrderCustomer));

		//	数据取得
		// 		客户信息
		List<OutFormAddNewOrderCustomer> customerList = omsOrdersAddNewOrderService.getCustomersList(inFormAddNewOrderCustomer);

		
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();			
		//		正常
		result.setResult(true);

		// 		客户信息
		ordersListMap.put("customerList", customerList);
		
		result.setResultInfo(ordersListMap);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	public void setCustomerInfo(InFormAddNewOrderOrder orderInfoReq){
		//从页面Bean获取所有订单信息
		OrdersBean orderInfo = orderInfoReq.getOrderInfo();
		CustomerBean customerInfo = orderInfoReq.getCustomerInfo();
		CustomerBean shipToInfo = orderInfoReq.getShipToInfo();
		//将customerInfo，shipToInfo赋值到orderInfo
		orderInfo.setCustomerId(customerInfo.getCustomerId());
		orderInfo.setName(customerInfo.getLastName());
		orderInfo.setCompany(customerInfo.getCompany());
		orderInfo.setEmail(customerInfo.getEmail());
		orderInfo.setAddress(customerInfo.getAddress());
		orderInfo.setAddress2(customerInfo.getAddress2());
		orderInfo.setCity(customerInfo.getCity());
		orderInfo.setState(customerInfo.getState());
		orderInfo.setZip(customerInfo.getZip());
		orderInfo.setCountry(customerInfo.getCountry());
		orderInfo.setPhone(customerInfo.getPhone());
		
		orderInfo.setShipName(shipToInfo.getLastName());
		orderInfo.setShipCompany(shipToInfo.getCompany());
		orderInfo.setShipEmail(shipToInfo.getEmail());
		orderInfo.setShipAddress(shipToInfo.getAddress());
		orderInfo.setShipAddress2(shipToInfo.getAddress2());
		orderInfo.setShipCity(shipToInfo.getCity());
		orderInfo.setShipState(shipToInfo.getState());
		orderInfo.setShipZip(shipToInfo.getZip());
		orderInfo.setShipCountry(shipToInfo.getCountry());
		orderInfo.setShipPhone(shipToInfo.getPhone());
		
	}
}
