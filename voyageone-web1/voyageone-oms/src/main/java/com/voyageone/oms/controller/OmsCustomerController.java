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

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.util.PageUtil;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormCustomer;
import com.voyageone.oms.formbean.OutFormCustomerNotes;
import com.voyageone.oms.formbean.OutFormCustomerOrders;
import com.voyageone.oms.formbean.OutFormCustomerTransactions;
import com.voyageone.oms.modelbean.NotesBean;
import com.voyageone.oms.service.OmsCustomerService;

/**
 * OMS CustomerSearch、CustomerDetailSearch画面
 * @author sky
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = OmsUrlConstants.URL_OMS_CUSTOMER_DETAILS_SEARCH)
public class OmsCustomerController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsCustomerController.class);
	@Autowired
	private OmsCustomerService omsCustomerService;
	
	/**
	 * 初始化数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/doCustomerInit", method=RequestMethod.POST)
	public void doInit(HttpServletRequest request,HttpServletResponse response){
		
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
		
		// 获得检索条件下拉信息
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("propertyList", propertyList);				
		AjaxResponseBean responseBean = new AjaxResponseBean();
		
		// 设置返回结果
		responseBean.setResult(true);
		responseBean.setResultInfo(resultDataMap);
		
		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 输出结果出力
		logger.info(responseBean.toString());
		
		return;
	}
	/**
	 * 检索客户信息
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doCustomerSearch", method = RequestMethod.POST)
	public void doSearch(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {

		AjaxResponseBean result = new AjaxResponseBean();
		// 从session中获得该用户的检索条件
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		// 门店对应渠道列表

		List<String> order_channel_id = new ArrayList<String>();
		if(inFormAddNewOrderCustomer.getChannelIdSelected() == null || inFormAddNewOrderCustomer.getChannelIdSelected().equals("")) {
			
			order_channel_id = user.getChannelList();
			
		} else {	
			order_channel_id.add(inFormAddNewOrderCustomer.getChannelIdSelected());
		}

		inFormAddNewOrderCustomer.setOrder_channel_id(order_channel_id);
		int row = inFormAddNewOrderCustomer.getRows();
		inFormAddNewOrderCustomer.setRows(0);
		List<OutFormCustomer> customerList = new ArrayList<OutFormCustomer>();
		//获取总记录数
		int customerCount = omsCustomerService.getCustomersCount(inFormAddNewOrderCustomer);
		
		inFormAddNewOrderCustomer.setRows(row);
		// 分页处理
		if (PageUtil.pageInit(inFormAddNewOrderCustomer, customerCount)) {
			customerList = omsCustomerService.getCustomersList(inFormAddNewOrderCustomer);;
		}
		Map<String, Object> customerListMap = new HashMap<String, Object>();
		result.setResult(true);
		customerListMap.put("customerList", customerList);
		customerListMap.put("total", customerCount);
		result.setResultInfo(customerListMap);
		result.writeTo(request, response);
		logger.info(result.toString());
		return;
	}
	
	/**
	 * 检索客户明细信息
	 * 
	 * @param request
	 * @param response
	 * @param String
	 */
	@RequestMapping(value = "/doCustomerDetailSearch", method = RequestMethod.POST)
	public void doOrderDetailSearch(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String,String> paramMap) {

		String customerId = (String)paramMap.get("customerId");
		AjaxResponseBean result = new AjaxResponseBean();
		//配置传入参数bean
		InFormAddNewOrderCustomer ifoc = new InFormAddNewOrderCustomer();
		ifoc.setCustomerId(customerId);
		//获取具体某个客户详细信息
		List<OutFormCustomer> customerInfo = omsCustomerService.getCustomersList(ifoc);
		// 客户订单信息
		List<OutFormCustomerOrders> customerOrderList = omsCustomerService.getCustomerOrderList(customerId);
		// 客户交易信息
		List<OutFormCustomerTransactions> customerTransactionList = omsCustomerService.getCustomerTransactionList(customerId);
		// 客户Notes
		List<OutFormCustomerNotes> customerNotesList = omsCustomerService.getCustomerNotesList(customerId);
		//返回结果收集map 
		Map<String, Object> customerDetailListMap = new HashMap<String, Object>();
		result.setResult(true);
		customerDetailListMap.put("customerInfo", customerInfo);
		customerDetailListMap.put("customerNotes", customerNotesList);
		customerDetailListMap.put("customerOrderList", customerOrderList);
		customerDetailListMap.put("customerTransactionList", customerTransactionList);
		result.setResultInfo(customerDetailListMap);
		result.writeTo(request, response);
		logger.info(result.toString());
	}
	
	/**
	 * 添加或者更改客户Notes
	 * 
	 * @param request
	 * @param response
	 * @param String
	 */
	@RequestMapping(value = "/doCustomerNotesAddOrEdit", method = RequestMethod.POST)
	public void doNotesAddOrEdit(HttpServletRequest request, HttpServletResponse response,
			@RequestBody NotesBean notesBean) {

		AjaxResponseBean result = new AjaxResponseBean();
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		if(user != null && !user.equals("")){
			notesBean.setEnteredBy(user.getUserName());
		}else{
			logger.info("无法获取用户信息！");
			result.setResult(false);
			return;
		}
		boolean flag = false;
		if(!"null".equals(notesBean.getId()) && !"".equals(notesBean.getId())){
			flag = omsCustomerService.updateCustomerNotes(notesBean);
		}else{
			flag = omsCustomerService.saveCustomerNotes(notesBean);
		}
		result.setResult(flag);
		result.writeTo(request, response);
		logger.info(result.toString());
	}
}
