package com.voyageone.oms.controller;

import java.util.HashMap;
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
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsMessageConstants;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.formbean.InFormOrderdetailAddLineItem;
import com.voyageone.oms.formbean.InFormOrderdetailAdjustmentItem;
import com.voyageone.oms.formbean.InFormOrderdetailRefunds;
import com.voyageone.oms.formbean.InFormOrderdetailReturn;
import com.voyageone.oms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.oms.formbean.OutFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormOrderdetailNotes;
import com.voyageone.oms.formbean.OutFormOrderdetailOrderHistory;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormOrderdetailPayments;
import com.voyageone.oms.formbean.OutFormOrderdetailRefunds;
import com.voyageone.oms.formbean.OutFormOrderdetailTransactions;
import com.voyageone.oms.modelbean.CustomerBean;
import com.voyageone.oms.modelbean.OrderDetailsBean;
import com.voyageone.oms.modelbean.OrdersBean;
import com.voyageone.oms.service.OmsMasterInfoService;
import com.voyageone.oms.service.OmsOrderDetailsSearchService;

/**
 * OMS OrdersDetail画面
 * 
 * @author Jerry
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = OmsUrlConstants.URL_OMS_ORDER_DETAILS_SEARCH)
public class OmsOrderDetailsSearchController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsOrderDetailsSearchController.class);
	@Autowired
	private OmsOrderDetailsSearchService omsOrderDetailsSearchService;
	@Autowired
	private OmsMasterInfoService omsMasterInfoService;
	
//	/**
//	 * 检索
//	 * 
//	 * @param request
//	 * @param response
//	 * @param bean
//	 */
//	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
//	public void doInit(HttpServletRequest request, HttpServletResponse response,
//			@RequestBody Map orderNumberMap) {
//		
//		String orderNumber = (String)orderNumberMap.get("orderNumber");		
//
//		// 从session中获得该用户的信息		
//		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
//		
//		// ajax 返回结果
//		AjaxResponseBean result = new AjaxResponseBean();
//		
//		// 输入参数出力
//		logger.info("orderNumber = " + orderNumber);
//		
//		// 订单信息
//		OutFormOrderdetailOrders orderInfo = omsOrderDetailsSearchService.getOrdersInfo(orderNumber, user);
//		
//		if (orderInfo != null) {
//			// 权限校验
//			if (omsOrderDetailsSearchService.isAuthorized(user, orderInfo)){
//				//	数据取得
//				// 		订单历史信息
//				List<OutFormOrderdetailOrderHistory> orderHistoryList = omsOrderDetailsSearchService.getOrdersHistoryInfo(orderNumber);		
//				// 		订单Notes信息
//				List<OutFormOrderdetailNotes> orderNotesList = omsOrderDetailsSearchService.getOrderNotesInfo(orderNumber);
////				// 		订单Tracking信息
////				List<OutFormOrderdetailTracking> orderTrackingList = omsOrderDetailsSearchService.getOrderTrackingInfo(orderNumber);
//				// 		订单Tracking信息
//				List<OutFormOrderdetailShipping> orderShippingList = omsOrderDetailsSearchService.getOrderShippingInfo(orderNumber);
//				// 		订单Transaction信息
//				List<OutFormOrderdetailTransactions> orderTransactionsList = omsOrderDetailsSearchService.getOrderTransactionsInfo(orderNumber);
//				// 		orderStatusList
//				List<MasterInfoBean> orderStatusList = omsMasterInfoService.getMasterInfoFromId(OmsConstants.TYPE_ORDER_STATUS, false);
//				// 		invoiceStatusList		
//				List<MasterInfoBean> invoiceStatusList = omsMasterInfoService.getMasterInfoFromId(OmsConstants.TYPE_INVOICE, true);
//				
//				// 设置返回结果
//				Map<String, Object> ordersListMap = new HashMap<String, Object>();			
//				//		正常
//				result.setResult(true);
//				// 		订单
//				ordersListMap.put("orderInfo", orderInfo);		
//				// 		订单历史
//				ordersListMap.put("orderHistoryList", orderHistoryList);
//				// 		订单NotesList
//				ordersListMap.put("orderNotesList", orderNotesList);
//				// 		订单TrackingList
////				ordersListMap.put("orderTrackingList", orderTrackingList);
//				ordersListMap.put("orderShippingList", orderShippingList);
//				// 		订单TransactionList
//				ordersListMap.put("orderTransactionsList", orderTransactionsList);
//				
//				// 		订单状态List（combbox 用）
//				ordersListMap.put("orderStatusList", orderStatusList);
//				// 		发票状态List（combbox 用）
//				ordersListMap.put("invoiceStatusList", invoiceStatusList);
//				
//				result.setResultInfo(ordersListMap);
//			} else {
//				// 异常返回（该订单访问没有权限）
//				result.setResult(false, MessageConstants.MESSAGE_CODE_400004, 
//						MessageConstants.MESSAGE_TYPE_DIALOG);	
//			}
//		} else {
//			// 异常返回（订单号不存在）
//			result.setResult(false, MessageConstants.MESSAGE_CODE_200002, 
//					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
//		}
//		
//		// 结果返回输出流
//		result.writeTo(request, response);
//		
//		// 输出结果出力
//		logger.info(result.toString());
//		
//		return;
//	}

	/**
	 * 检索
	 * 
	 * @param request
	 * @param response
	 * @param orderNumberMap
	 */
//	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
//	public void doInit(HttpServletRequest request, HttpServletResponse response,
//			@RequestBody Map orderNumberMap) {
//		
//		String orderNumber = (String)orderNumberMap.get("sourceOrderId");		
//
//		// 从session中获得该用户的信息		
//		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
//		
//		// ajax 返回结果
//		AjaxResponseBean result = new AjaxResponseBean();
//		
//		// 输入参数出力
//		logger.info("orderNumber = " + orderNumber);
//		
//		// 订单信息
//		OutFormOrderdetailOrders orderInfo = omsOrderDetailsSearchService.getOrdersInfo(orderNumber, user);
//		
//		if (orderInfo != null) {
//			// 权限校验
//			if (omsOrderDetailsSearchService.isAuthorized(user, orderInfo)){
//				//	数据取得
//				// 		订单历史信息
//				List<OutFormOrderdetailOrderHistory> orderHistoryList = omsOrderDetailsSearchService.getOrdersHistoryInfo(orderNumber);		
//				// 		订单Notes信息
//				List<OutFormOrderdetailNotes> orderNotesList = omsOrderDetailsSearchService.getOrderNotesInfo(orderNumber);
////				// 		订单Tracking信息
////				List<OutFormOrderdetailTracking> orderTrackingList = omsOrderDetailsSearchService.getOrderTrackingInfo(orderNumber);
//				// 		订单Tracking信息
//				List<OutFormOrderdetailShipping> orderShippingList = omsOrderDetailsSearchService.getOrderShippingInfo(orderNumber);
//				// 		订单Transaction信息
//				List<OutFormOrderdetailTransactions> orderTransactionsList = omsOrderDetailsSearchService.getOrderTransactionsInfo(orderNumber);
//				// 		orderStatusList
//				List<MasterInfoBean> orderStatusList = omsMasterInfoService.getMasterInfoFromId(OmsConstants.TYPE_ORDER_STATUS, false);
//				// 		invoiceStatusList		
//				List<MasterInfoBean> invoiceStatusList = omsMasterInfoService.getMasterInfoFromId(OmsConstants.TYPE_INVOICE, true);
//				
//				// 设置返回结果
//				Map<String, Object> ordersListMap = new HashMap<String, Object>();			
//				//		正常
//				result.setResult(true);
//				// 		订单
//				ordersListMap.put("orderInfo", orderInfo);		
//				// 		订单历史
//				ordersListMap.put("orderHistoryList", orderHistoryList);
//				// 		订单NotesList
//				ordersListMap.put("orderNotesList", orderNotesList);
//				// 		订单TrackingList
////				ordersListMap.put("orderTrackingList", orderTrackingList);
//				ordersListMap.put("orderShippingList", orderShippingList);
//				// 		订单TransactionList
//				ordersListMap.put("orderTransactionsList", orderTransactionsList);
//				
//				// 		订单状态List（combbox 用）
//				ordersListMap.put("orderStatusList", orderStatusList);
//				// 		发票状态List（combbox 用）
//				ordersListMap.put("invoiceStatusList", invoiceStatusList);
//				
//				result.setResultInfo(ordersListMap);
//			} else {
//				// 异常返回（该订单访问没有权限）
//				result.setResult(false, MessageConstants.MESSAGE_CODE_400004, 
//						MessageConstants.MESSAGE_TYPE_DIALOG);	
//			}
//		} else {
//			// 异常返回（订单号不存在）
//			result.setResult(false, MessageConstants.MESSAGE_CODE_200002, 
//					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
//		}
//		
//		// 结果返回输出流
//		result.writeTo(request, response);
//		
//		// 输出结果出力
//		logger.info(result.toString());
//		
//		return;
//	}
	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
	public void doInit(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map orderNumberMap) {
		
		String sourceOrderId = (String)orderNumberMap.get("sourceOrderId");		

		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + sourceOrderId);
		
		// 主订单信息取得
		OutFormOrderdetailOrders mainOrderInfo = omsOrderDetailsSearchService.getMainOrderInfo(sourceOrderId);

		// 订单信息
		List<OutFormOrderdetailOrders> ordersList = omsOrderDetailsSearchService.getOrdersList(sourceOrderId, user);
		
		// 主订单信息 && 订单信息 存在的场合 
		if (ordersList != null && mainOrderInfo != null) {
			// 权限校验
			if (omsOrderDetailsSearchService.isAuthorized(user, ordersList.get(0))){
				//	数据取得
				// 		订单历史信息
				List<OutFormOrderdetailOrderHistory> orderHistoryList = omsOrderDetailsSearchService.getOrdersHistoryInfoBySourceOrderId(sourceOrderId, user);		
				// 		订单Notes信息
				List<OutFormOrderdetailNotes> orderNotesList = omsOrderDetailsSearchService.getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
				// 		订单Transaction信息
				List<OutFormOrderdetailTransactions> orderTransactionsList = omsOrderDetailsSearchService.getOrderTransactionsInfo(sourceOrderId, user);
				//		订单Payments信息
				List<OutFormOrderdetailPayments> orderPaymentsList = omsOrderDetailsSearchService.getOrderPaymentsInfo(sourceOrderId, user);
				
				// 设置返回结果
				Map<String, Object> ordersListMap = new HashMap<String, Object>();
				//		正常
				result.setResult(true);
				// 		主订单
				omsOrderDetailsSearchService.setMainOrderInfo(mainOrderInfo, ordersList.get(0), orderTransactionsList);
				ordersListMap.put("mainOrderInfo", mainOrderInfo);
				// 		订单
				ordersListMap.put("ordersList", ordersList);		
				// 		订单历史
				ordersListMap.put("orderHistoryList", orderHistoryList);
				// 		订单NotesList
				ordersListMap.put("orderNotesList", orderNotesList);
				// 		订单TransactionList
				ordersListMap.put("orderTransactionsList", orderTransactionsList);
				//		订单paymentsList
				ordersListMap.put("orderPaymentsList", orderPaymentsList);
				
				result.setResultInfo(ordersListMap);
			} else {
				// 异常返回（该订单访问没有权限）
				result.setResult(false, MessageConstants.MESSAGE_CODE_400004, 
						MessageConstants.MESSAGE_TYPE_DIALOG);	
			}
		} else {
			// 异常返回（订单号不存在）
			result.setResult(false, MessageConstants.MESSAGE_CODE_200002, 
					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * Notes图片取得
	 * 
	 * @param request
	 * @param response
	 * @param imgPath 文件路径（暂时全路径）
	 */
	@RequestMapping(value = "/doGetNotesPic")
	public void doGetNotesPic(HttpServletRequest request,
								HttpServletResponse response,
						  		String imgPath) {  
	  	try {
	  		omsOrderDetailsSearchService.getNotesPic(request, response, imgPath);
	  	} catch (Exception e) {
	  		logger.error("doGetNotesPic", e);
	  	}              
	}	
 
	/**
	 * 订单明细图片显示
	 * 
	 * @param request
	 * @param response
	 * @param imgPath 文件路径（暂时全路径）
	 */
	@RequestMapping(value = "/doGetDetailPic")
	public void doGetDetailPic(HttpServletRequest request,
								HttpServletResponse response,
						  		String imgPath) {  
	  	try {
//	  		omsOrderDetailsSearchService.getSKUPic(request, response, "http://image.sneakerhead.com/is/image/sneakerhead/tmall-imgn?$460$&$img=nike-women-dunk-sky-hi-sneaker-boot-616738001-1&layer=2&originN=0,.5&pos=0,105");
	  		omsOrderDetailsSearchService.getSKUPic(request, response, imgPath);
	  	} catch (Exception e) {
	  		logger.error("doGetNotesPic", e);
	  	}              
	}
	
	/**
	 * Adjustment 保存
	 * 
	 * @param request
	 * @param response
	 * @param inFormOrderdetailAdjustmentItem
	 */
	@RequestMapping(value = "/doSaveAdjustment", method = RequestMethod.POST)
	public void doSaveAdjustment(HttpServletRequest request, HttpServletResponse response,
			@RequestBody InFormOrderdetailAdjustmentItem inFormOrderdetailAdjustmentItem) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		// 输入参数出力
		logger.info("sourceOrderId = " + inFormOrderdetailAdjustmentItem.getSourceOrderId());
		logger.info("orderNumber = " + inFormOrderdetailAdjustmentItem.getOrderNumber());
		logger.info("input bean = " + JsonUtil.getJsonString(inFormOrderdetailAdjustmentItem));
		
		// 订单修正
		if (omsOrderDetailsSearchService.saveAdjustment(inFormOrderdetailAdjustmentItem, user)) {
//			// 订单信息返回
//			OutFormOrderdetailOrders orderInfo = omsOrderDetailsSearchService.getOrdersInfo(inFormOrderdetailAdjustmentItem.getOrderNumber(), user);
//			// 	订单Notes信息
//			List<OutFormOrderdetailNotes> orderNotesList = omsOrderDetailsSearchService.getOrderNotesInfoBySourceOrderId(inFormOrderdetailAdjustmentItem.getSourceOrderId(), user);
//			
//			// 设置返回结果
//			Map<String, Object> ordersListMap = new HashMap<String, Object>();			
//			// 		订单
//			ordersListMap.put("orderInfo", orderInfo);
//			// 		订单NotesList
//			ordersListMap.put("orderNotesList", orderNotesList);
//			
//			result.setResultInfo(ordersListMap);
//			
//			//		正常
//			result.setResult(true);
			
			omsOrderDetailsSearchService.setSuccessReturn(inFormOrderdetailAdjustmentItem.getSourceOrderId(), inFormOrderdetailAdjustmentItem.getOrderNumber(), result, user);
		} else {
			// 异常返回（订单号不存在）
			result.setResult(false, MessageConstants.MESSAGE_CODE_200004, 
								MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * 明细打折信息保存
	 * 
	 * @param request
	 * @param response
	 * @param inFormOrderdetailReturn
	 */
	@RequestMapping(value = "/doSaveOrderDetailDiscount", method = RequestMethod.POST)
	public void doSaveOrderDetailDiscount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody InFormOrderdetailReturn inFormOrderdetailReturn) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();	
		
		// 输入参数出力
		logger.info("sourceOrderId = " + inFormOrderdetailReturn.getSourceOrderId());
		logger.info("orderNumber = " + inFormOrderdetailReturn.getOrderNumber());
		logger.info("input bean = " + JsonUtil.getJsonString(inFormOrderdetailReturn));
		
		omsOrderDetailsSearchService.saveOrderDetailDiscountMain(inFormOrderdetailReturn, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * 删除LineItem（废止，变更为多条记录删除  doCancelLineItems）
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doDeleteLineItem", method = RequestMethod.POST)
	public void doDeleteLineItem(HttpServletRequest request,
									HttpServletResponse response,
									@RequestBody Map<String, Object> requestMap) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		// 入力项目取得
		List<?> orderDetailSelections = (List<?>) requestMap.get("orderDetailSelections");
		if (orderDetailSelections == null || orderDetailSelections.size() == 0) {
			result.setResult(false, MessageConstants.MESSAGE_CODE_200002,
					MessageConstants.MESSAGE_TYPE_EXCEPTION);

			// 结果返回输出流
			result.writeTo(request, response);

			return;
		}		
		Map<String, Object> orderDetailSelection = (Map<String, Object>) orderDetailSelections.get(0);

		//	选中订单明细
		OutFormOrderDetailOrderDetail orderDetailItem = new OutFormOrderDetailOrderDetail();		
		// 		订单号
		String orderNumber = (String) orderDetailSelection.get("orderNumber");
		orderDetailItem.setOrderNumber((String) orderDetailSelection.get("orderNumber"));
		// 		订单明细番号
		orderDetailItem.setItemNumber((String) orderDetailSelection.get("itemNumber"));
		//		删除原因
		orderDetailItem.setReason((String) requestMap.get("reason"));

		omsOrderDetailsSearchService.delLineItemMain(orderDetailItem, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());

		return;
	}
	
	/**
	 * 取消多条订单明细LineItem
	 * 
	 * @param request
	 * @param response
	 * @param inFormOrderdetailReturn
	 */
	@RequestMapping(value = "/doCancelLineItems", method = RequestMethod.POST)
	public void doCancelLineItems(HttpServletRequest request,
									HttpServletResponse response,
									@RequestBody InFormOrderdetailReturn inFormOrderdetailReturn) {

		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + inFormOrderdetailReturn.getSourceOrderId());
		logger.info("orderNumber = " + inFormOrderdetailReturn.getOrderNumber());
		logger.info("input bean = " + JsonUtil.getJsonString(inFormOrderdetailReturn));

		omsOrderDetailsSearchService.cancelLineItemsMain(inFormOrderdetailReturn, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());

		return;
		
	}

	/**
	 * 追加LineItem（该机能已废止）
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doAddLineItem", method = RequestMethod.POST)
	public void doAddLineItem(HttpServletRequest request,
									HttpServletResponse response,
									@RequestBody Map<String, Object> requestMap) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
	
		//	输入参数取得
		InFormOrderdetailAddLineItem inFormOrderdetailAddLineItem = new InFormOrderdetailAddLineItem();
		inFormOrderdetailAddLineItem.setOrderNumber((String)requestMap.get("orderNumber"));
		inFormOrderdetailAddLineItem.setSku((String)requestMap.get("sku"));
		inFormOrderdetailAddLineItem.setPrice((String) requestMap.get("price"));
		inFormOrderdetailAddLineItem.setItemName((String) requestMap.get("itemName"));
		inFormOrderdetailAddLineItem.setDescription((String) requestMap.get("description"));
		inFormOrderdetailAddLineItem.setInventory(String.valueOf((Integer) requestMap.get("inventory")));
		
		omsOrderDetailsSearchService.addLineItemMain(inFormOrderdetailAddLineItem, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);

		return;
	}
	
	/**
	 * ReturnLineItem
	 * 
	 * @param request
	 * @param response
	 * @param inFormOrderdetailReturn
	 */
	@RequestMapping(value = "/doReturnLineItem", method = RequestMethod.POST)
	public void doReturnLineItem(HttpServletRequest request,
									HttpServletResponse response,
									@RequestBody InFormOrderdetailReturn inFormOrderdetailReturn) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + inFormOrderdetailReturn.getSourceOrderId());
		logger.info("orderNumber = " + inFormOrderdetailReturn.getOrderNumber());
		logger.info("input bean = " + JsonUtil.getJsonString(inFormOrderdetailReturn));

		omsOrderDetailsSearchService.returnOrderDetailMain(inFormOrderdetailReturn, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());

		return;
	}
	
	/**
	 * ReturnLineItem
	 * 
	 * @param request
	 * @param response
	 * @param inFormOrderdetailReturn
	 */
	@RequestMapping(value = "/doUnReturnLineItem", method = RequestMethod.POST)
	public void doUnReturnLineItem(HttpServletRequest request,
									HttpServletResponse response,
									@RequestBody InFormOrderdetailReturn inFormOrderdetailReturn) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		omsOrderDetailsSearchService.unReturnOrderDetailMain(inFormOrderdetailReturn, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);

		return;
	}
	
	/**
	 * 订单锁定状态变更 
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doLockOrUnlockOrder", method = RequestMethod.POST)
	public void doLockOrUnlockOrder(HttpServletRequest request,
										HttpServletResponse response,
										@RequestBody Map<String, Object> requestMap) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
	
		//	输入参数取得
		String sourceOrderId = (String)requestMap.get("sourceOrderId");
		boolean lockFlag = (Boolean)requestMap.get("lockFlag");
		
		// 输入参数出力
		logger.info("sourceOrderId = " + sourceOrderId);
		logger.info("lockFlag = " + lockFlag);
		
		omsOrderDetailsSearchService.changeLockStatusMain(sourceOrderId, lockFlag, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);

		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * 订单取消
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doCancelOrder", method = RequestMethod.POST)
	public void doCancelOrder(HttpServletRequest request,
										HttpServletResponse response,
										@RequestBody Map<String, Object> requestMap) {
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
	
		//	输入参数取得
		String sourceOrderId = (String)requestMap.get("sourceOrderId");
		String orderNumber = (String)requestMap.get("orderNumber");
		String reason = (String)requestMap.get("reason");
		
		// 输入参数出力
		logger.info("sourceOrderId = " + sourceOrderId);
		logger.info("orderNumber = " + orderNumber);
		logger.info("reason = " + reason);
		
		omsOrderDetailsSearchService.cancelOrderMain(sourceOrderId, orderNumber, reason, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);

		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}

	/**
	 * 订单恢复（已废止）
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doRevertOrder", method = RequestMethod.POST)
	public void doRevertOrder(HttpServletRequest request,
										HttpServletResponse response,
										@RequestBody Map<String, Object> requestMap) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
	
		//	输入参数取得
		String orderNumber = (String)requestMap.get("orderNumber");
		
		omsOrderDetailsSearchService.revertOrderMain(orderNumber, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);

		return;
	}

	/**
	 * 订单状态更新（已废止）
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doSetOrderStatus", method = RequestMethod.POST)
	public void doSetOrderStatus(HttpServletRequest request,
										HttpServletResponse response,
										@RequestBody Map<String, Object> requestMap) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
	
		//	输入参数取得
		//		订单号
		String orderNumber = (String)requestMap.get("orderNumber");		
		//		订单状态
		String orderStatus = (String)requestMap.get("orderStatus");
		
		OrdersBean orderInfo = new OrdersBean();
		orderInfo.setOrderNumber(orderNumber);
		orderInfo.setOrderStatus(orderStatus);
		orderInfo.setModifier(user.getUserName());
		
		omsOrderDetailsSearchService.setOrderStatus(orderInfo, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);

		return;
	}
	
	/**
	 * 订单明细状态更新（已废止）
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doSetOrderDetailStatus", method = RequestMethod.POST)
	public void doSetOrderDetailStatus(HttpServletRequest request,
										HttpServletResponse response,
										@RequestBody Map<String, Object> requestMap) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
	
		//	输入参数取得
		//		订单号
		String orderNumber = (String)requestMap.get("orderNumber");
		//		订单明细番号（该订单明细全体更新时，itemNumber为空）
		String itemNumber = (String)requestMap.get("itemNumber");
		//		订单明细状态
		String status = (String)requestMap.get("status");
		
		OrderDetailsBean orderDetailInfo = new OrderDetailsBean();
		orderDetailInfo.setOrderNumber(orderNumber);
		orderDetailInfo.setItemNumber(itemNumber);
		orderDetailInfo.setStatus(status);
		orderDetailInfo.setModifier(user.getUserName());
		
		omsOrderDetailsSearchService.setOrderDetailStatus(orderDetailInfo, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);

		return;
	}
	
	/**
	 * 订单其他属性更新[wait real refund]，[price difference no pay]，[Use Tmall Point Fee]
	 * 已废止
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doSetOrderOtherProp", method = RequestMethod.POST)
	public void doSetOrderOtherProp(HttpServletRequest request,
										HttpServletResponse response,
										@RequestBody Map<String, Object> requestMap) {
		
//		// 从session中获得该用户的信息		
//		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
//		
//		// ajax 返回结果
//		AjaxResponseBean result = new AjaxResponseBean();
//	
//		//	输入参数取得
//		//		订单号
//		String orderNumber = (String)requestMap.get("orderNumber");
//		boolean waitRealRefund = (boolean)requestMap.get("waitRealRefund");
//		boolean priceDifferenceNoPay = (boolean)requestMap.get("priceDifferenceNoPay");
//		String useTmallPointFee = (String)requestMap.get("useTmallPointFee");
//		
//		OrdersBean orderInfo = new OrdersBean();
//		orderInfo.setOrderNumber(orderNumber);
//		orderInfo.setWaitRealRefund(waitRealRefund);
//		orderInfo.setPriceDifferenceNoPay(priceDifferenceNoPay);
//		orderInfo.setUseTmallPointFee(useTmallPointFee);
//		orderInfo.setModifier(user.getUserName());
//		
//		omsOrderDetailsSearchService.setOrderOtherProp(orderInfo, result, user);
//		
//		// 结果返回输出流
//		result.writeTo(request, response);
//		
//		// 输出结果出力
//		logger.info(result.toString());

		return;
	}
	
	/**
	 * 订单其他属性更新[freight_collect]
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doUpdateOrderOtherProp", method = RequestMethod.POST)
	public void doUpdateOrderOtherProp(HttpServletRequest request,
										HttpServletResponse response,
										@RequestBody OutFormOrderdetailOrders bean) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("freightCollect = " + bean.isFreightCollect());
		
		omsOrderDetailsSearchService.updateOrderOtherPropMain(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());

		return;
	}

	/**
	 * 订单其他属性更新[customer_refused]
	 *
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doUpdateOrderCustomerRefused", method = RequestMethod.POST)
	public void doUpdateOrderCustomerRefused(HttpServletRequest request,
									   HttpServletResponse response,
									   @RequestBody OutFormOrderdetailOrders bean) {

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("customerRefused = " + bean.isCustomerRefused());

		omsOrderDetailsSearchService.updateCustomerRefusedMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);

		// 输出结果出力
		logger.info(result.toString());

		return;
	}

	/**
	 * 第三方订单取消[ext_flg1]
	 *
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doCancelClientOrder", method = RequestMethod.POST)
	public void doCancelClientOrder(HttpServletRequest request,
									   HttpServletResponse response,
									   @RequestBody OutFormOrderdetailOrders bean) {

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());

		omsOrderDetailsSearchService.cancelClientOrderMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);

		// 输出结果出力
		logger.info(result.toString());

		return;
	}
	
	/**
	 * Approve 订单
	 * 
	 * @param request
	 * @param response
	 * @param orderNumberMap
	 */
	@RequestMapping(value = "/doApprove", method = RequestMethod.POST)
	public void doApprove(HttpServletRequest request, HttpServletResponse response,
							@RequestBody Map orderNumberMap) {
		
		String sourceOrderId = (String)orderNumberMap.get("sourceOrderId");
		String orderNumber = (String)orderNumberMap.get("orderNumber");	
		
		// 输入参数出力
		logger.info("sourceOrderId = " + sourceOrderId);
		logger.info("orderNumber = " + orderNumber);
		

		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("orderNumber = " + orderNumber);
		
		// 订单信息
		OutFormOrderdetailOrders orderInfo = omsOrderDetailsSearchService.getOrdersInfo(orderNumber, user);
		
		if (orderInfo != null) {
			
			omsOrderDetailsSearchService.approveOrderMain(sourceOrderId, orderNumber, result, user);				

		} else {
			// 异常返回（订单号不存在）
			result.setResult(false, MessageConstants.MESSAGE_CODE_200002, 
					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * 保存Notes
	 * 
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping(value = "/doSaveNotes")
	public void saveNotes(HttpServletRequest request, HttpServletResponse response, 
							@RequestBody Map<String, Object> map) {
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// 元订单号
		String sourceOrderIdForLog = (String)map.get("sourceOrderId");
		// 订单号
		String orderNumberForLog = (String) map.get("orderNumber");
		// Notes
		String notesForLog = (String) map.get("notes");
		
		// 输入参数出力
		logger.info("sourceOrderId = " + sourceOrderIdForLog);
		logger.info("orderNumber = " + orderNumberForLog);
		logger.info("notesForLog = " + notesForLog);
		
		
		// 保存Notes
		boolean bRet = omsOrderDetailsSearchService.saveNotes(map, user);
		if (bRet) {
			String orderNumber = (String) map.get("orderNumber");
			String sourceOrderId = (String) map.get("sourceOrderId");
			// 		订单Notes信息
			List<OutFormOrderdetailNotes> orderNotesList = omsOrderDetailsSearchService.getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
			
			// 设置返回结果
			Map<String, Object> ordersListMap = new HashMap<String, Object>();	
			//	 		订单NotesList
			ordersListMap.put("orderNotesList", orderNotesList);
			result.setResult(true);
			result.setResultInfo(ordersListMap);
		}
		else {
			// 异常Message返回
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210026,
					 MessageConstants.MESSAGE_TYPE_EXCEPTION);
		}
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * 更改Sold to Address
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/updateAddress")
	public void updateAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OutFormOrderdetailOrders bean){
		
		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("input bean = " + JsonUtil.getJsonString(bean));
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		omsOrderDetailsSearchService.updateAddressMain(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 更改Ship to Address
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateShipAddress")
	public void updateShipAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OutFormOrderdetailOrders bean){

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("input bean = " + JsonUtil.getJsonString(bean));
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		omsOrderDetailsSearchService.updateShipAddressMain(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}

	/**
	 * 查询Customer
	 * @param request
	 * @param response
	 * @param customerMap
	 */
	@RequestMapping(value="/doGetCustomerInfoForSold")
	public void doGetCustomerInfoForSold(HttpServletRequest request,
					HttpServletResponse response,
					@RequestBody Map<String, Object> customerMap){
		AjaxResponseBean result = new AjaxResponseBean();
		
		String customerId = (String)customerMap.get("customerId");
		
		// 输入参数出力
		logger.info("customerId = " + customerId);
		
		OutFormAddNewOrderCustomer cuntomerInfo = omsOrderDetailsSearchService.getCustomerInfoforSold(customerId);
		
		if(cuntomerInfo!=null){
			result.setResult(true);
//			result.setResultInfo(cuntomerInfo);

			// 设置返回结果
			Map<String, Object> ordersListMap = new HashMap<String, Object>();
			ordersListMap.put("customerInfo", cuntomerInfo);
			
			result.setResultInfo(ordersListMap);
		}
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * 更改Customer	(废止)
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateCustomerInfoForSold")
	public void doUpdateCustomerInfoForSold(HttpServletRequest request, HttpServletResponse response,@RequestBody CustomerBean bean){
		AjaxResponseBean result = new AjaxResponseBean();
		boolean ret=true;
		ret=omsOrderDetailsSearchService.updateCustomerInfo(bean);
		if (ret) {
			result.setResult(true);
		}
		else {
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210027,
					 MessageConstants.MESSAGE_TYPE_EXCEPTION);
		}
		// 结果返回输出流
		result.writeTo(request, response);
		
		return;
	}
	
	/**
	 * 更改InternalMessage
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateInternalMessage")
	public void doUpdateInternalMessage(HttpServletRequest request, HttpServletResponse response,
	    @RequestBody OutFormOrderdetailOrders bean){
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("internalMessage = " + bean.getInternalMessage());
		
		omsOrderDetailsSearchService.updateInternalMessage(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}

	/**
	 * 更改GiftMessage
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateGiftMessage")
	public void doUpdateGiftMessage(HttpServletRequest request, HttpServletResponse response,
	    @RequestBody OutFormOrderdetailOrders bean){
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("giftMessage = " + bean.getGiftMessage());
		
		omsOrderDetailsSearchService.updateGiftMessage(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 更改Shipping
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateShipping")
	public void doUpdateShipping(HttpServletRequest request, HttpServletResponse response,
	    @RequestBody OutFormOrderdetailOrders bean){
		
		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("shipping = " + bean.getShipping());
		logger.info("origShipping = " + bean.getOrigShipping());
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		omsOrderDetailsSearchService.updateShippingMain(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 更改CustomerComment
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateCustomerComment")
	public void doUpdateCustomerComment(HttpServletRequest request, HttpServletResponse response,
	    @RequestBody OutFormOrderdetailOrders bean){

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("customerComment = " + bean.getCustomerComment());
		
		omsOrderDetailsSearchService.updateCustomerComment(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 更改Invoice
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateInvoice")
	public void doUpdateInvoice(HttpServletRequest request, HttpServletResponse response,
	    @RequestBody OutFormOrderdetailOrders bean){

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("invoice = " + bean.getInvoice());
		
		omsOrderDetailsSearchService.updateInvoiceMain(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 更改InvoiceInfo
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value="/doUpdateInvoiceInfo")
	public void doUpdateInvoiceInfo(HttpServletRequest request, HttpServletResponse response,
	    @RequestBody OutFormOrderdetailOrders bean){

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + bean.getSourceOrderId());
		logger.info("orderNumber = " + bean.getOrderNumber());
		logger.info("invoiceInfo = " + bean.getInvoiceInfo());
		
		omsOrderDetailsSearchService.updateInvoiceInfoMain(bean, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	
	/**
	 * 差价订单Approved 预处理操作
	 * @param request
	 * @param response
	 * @param orderNumberMap
	 */
	@RequestMapping(value="/doPreApprovePriceDiffOrder")
	public void doPreApprovePriceDiffOrder(HttpServletRequest request, HttpServletResponse response,
												@RequestBody Map orderNumberMap){
		
		//	待处理订单号
		String orderNumber = (String)orderNumberMap.get("orderNumber");
		//	绑定订单号（source_order_id/ordernumber）
		String bindNumber = (String)orderNumberMap.get("bindNumber");
		//	绑定订单号类型区分
		String bindNumberKind = (String)orderNumberMap.get("bindNumberKind");
		
		// 输入参数出力
		logger.info("orderNumber = " + orderNumber);
		logger.info("bindNumber = " + bindNumber);
		logger.info("bindNumberKind = " + bindNumberKind);
		
		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		omsOrderDetailsSearchService.preApprovePriceDiffOrderMain(orderNumber, bindNumber, bindNumberKind, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	
	/**
	 * 差价订单Approved 操作
	 * @param request
	 * @param response
	 * @param orderNumberMap
	 */
	@RequestMapping(value="/doApprovePriceDiffOrder")
	public void doApprovePriceDiffOrder(HttpServletRequest request, HttpServletResponse response,
											@RequestBody Map orderNumberMap){
		
		//	待处理订单号
		String orderNumber = (String)orderNumberMap.get("orderNumber");
		//	绑定订单号（source_order_id/ordernumber）
		String bindNumber = (String)orderNumberMap.get("bindNumber");
		//	绑定订单号类型区分
		String bindNumberKind = (String)orderNumberMap.get("bindNumberKind");
		
		// 输入参数出力
		logger.info("orderNumber = " + orderNumber);
		logger.info("bindNumber = " + bindNumber);
		logger.info("bindNumberKind = " + bindNumberKind);
		
		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		omsOrderDetailsSearchService.approvePriceDiffOrderMain(orderNumber, bindNumber, bindNumberKind, result, user);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}

	/**
	 * 取得退款信息
	 * 
	 * @param request
	 * @param response
	 * @param orderNumberMap
	 */
	@RequestMapping(value = "/doInitRefund", method = RequestMethod.POST)
	public void doInitRefund(HttpServletRequest request, HttpServletResponse response,
								@RequestBody Map orderNumberMap) {
		// 网络订单号
		String sourceOrderId = (String)orderNumberMap.get("sourceOrderId");
		// cartId
		String cartId = (String)orderNumberMap.get("cartId");
		// 仅显示履历
		boolean isShowHistoryOnly = (boolean)orderNumberMap.get("isShowHistoryOnly");

		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("sourceOrderId = " + sourceOrderId);
		logger.info("cartId = " + cartId);
		logger.info("isShowHistoryOnly = " + isShowHistoryOnly);

		omsOrderDetailsSearchService.getOrderRefundsMain(sourceOrderId, cartId, isShowHistoryOnly, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 取得退款Message信息
	 * 
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doGetRefundMessages", method = RequestMethod.POST)
	public void doGetRefundMessages(HttpServletRequest request, HttpServletResponse response,
										@RequestBody InFormOrderdetailRefunds reqBean) {
		
		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("refundPhase = " + bean.getRefundPhase());
		
		omsOrderDetailsSearchService.getOrderRefundMessagesMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 追加退款Message信息（目前只支持淘宝，暂不对应）
	 * 
	 * @param request
	 * @param response
	 * @param reqParaMap
	 */
	@RequestMapping(value = "/doAddRefundMessage", method = RequestMethod.POST)
	public void doAddRefundMessage(HttpServletRequest request, HttpServletResponse response,
										@RequestBody Map reqParaMap) {
		//	Key
		String refundId = (String)reqParaMap.get("refundId");
		//	内容
		String content = (String)reqParaMap.get("content");
		//	图片
		String image = (String)reqParaMap.get("image");
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + refundId);
		logger.info("content = " + content);
		
		omsOrderDetailsSearchService.addOrderRefundMessageMain(refundId, content, image, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 拒绝退款
	 * 
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doRefundRefuse", method = RequestMethod.POST)
	public void doRefundRefuse(HttpServletRequest request, HttpServletResponse response,
												@RequestBody InFormOrderdetailRefunds reqBean) {
		
		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("refuseMessage = " + bean.getContent());
		logger.info("refundPhase = " + bean.getRefundPhase());

		omsOrderDetailsSearchService.refundRefuseMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 同意退款
	 * 
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doRefundsAgree", method = RequestMethod.POST)
	public void doRefundsAgree(HttpServletRequest request, HttpServletResponse response,
								@RequestBody InFormOrderdetailRefunds reqBean) {

		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("code = " + bean.getCode());
		logger.info("refundPhase = " + bean.getRefundPhase());

		omsOrderDetailsSearchService.refundsAgreeMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}

	/**
	 * 同意退款（同步OMS）
	 *
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doRefundsAgreeSynOMS", method = RequestMethod.POST)
	public void doRefundsAgreeSynOMS(HttpServletRequest request, HttpServletResponse response,
							   @RequestBody InFormOrderdetailRefunds reqBean) {

		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("code = " + bean.getCode());
		logger.info("refundPhase = " + bean.getRefundPhase());

		omsOrderDetailsSearchService.refundsAgreeMainSynOMS(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);

		// 输出结果出力
		logger.info(result.toString());
	}

	/**
	 * 同意退款（独立域名）
	 *
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doRefundsAgreeCN", method = RequestMethod.POST)
	public void doRefundsAgreeCN(HttpServletRequest request, HttpServletResponse response,
							   @RequestBody InFormOrderdetailRefunds reqBean) {

		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();

		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("code = " + bean.getCode());
		logger.info("refundPhase = " + bean.getRefundPhase());

		omsOrderDetailsSearchService.refundsAgreeMainCN(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);

		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 同意退货
	 * 
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doReturnGoodsAgree", method = RequestMethod.POST)
	public void doReturnGoodsAgree(HttpServletRequest request, HttpServletResponse response,
									@RequestBody InFormOrderdetailRefunds reqBean) {

		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("content = " + bean.getContent());
		logger.info("refundPhase = " + bean.getRefundPhase());
		
		omsOrderDetailsSearchService.returnGoodsAgreeMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 拒绝退货
	 * 
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doReturnGoodsRefuse", method = RequestMethod.POST)
	public void doReturnGoodsRefuse(HttpServletRequest request, HttpServletResponse response,
										@RequestBody InFormOrderdetailRefunds reqBean) {
		
		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("refundPhase = " + bean.getRefundPhase());
		
		omsOrderDetailsSearchService.returnGoodsRefuseMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 卖家回填物流信息
	 * 
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doReturnGoodsRefill", method = RequestMethod.POST)
	public void doReturnGoodsRefill(HttpServletRequest request, HttpServletResponse response,
										@RequestBody InFormOrderdetailRefunds reqBean) {
		
		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("refundPhase = " + bean.getRefundPhase());
		
		omsOrderDetailsSearchService.returnGoodsRefillMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
	
	/**
	 * 审核退款单 
	 * 
	 * @param request
	 * @param response
	 * @param reqBean
	 */
	@RequestMapping(value = "/doRefundReview", method = RequestMethod.POST)
	public void doRefundReview(HttpServletRequest request, HttpServletResponse response,
										@RequestBody InFormOrderdetailRefunds reqBean) {
		
		OutFormOrderdetailRefunds bean = reqBean.getRefundInfo();
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		
		// 输入参数出力
		logger.info("refundId = " + bean.getRefundId());
		logger.info("refundPhase = " + bean.getRefundPhase());

		omsOrderDetailsSearchService.refundReviewMain(bean, result, user);

		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
	}
}
