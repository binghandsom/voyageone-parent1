package com.voyageone.oms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.Enums.TypeConfigEnums.MastType;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.CoreConstants;
import com.voyageone.oms.OmsCodeConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.init.MessageHelp;
import com.voyageone.core.modelbean.MasterInfoBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.util.PageUtil;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.formbean.InFormSearch;
import com.voyageone.oms.formbean.OutFormSearch;
import com.voyageone.oms.service.OmsOrdersSearchService;

/**
 * OMS Orders检索画面
 * 
 * @author jacky
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = OmsUrlConstants.URL_OMS_ORDERS_SEARCH)
public class OmsOrdersSearchController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsOrdersSearchController.class);
	
	@Autowired
	private OmsOrdersSearchService omsOrdersSearchService;

	/**
	 * 初始化（获得查询条件）
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
	public void doInit(HttpServletRequest request, HttpServletResponse response) {
		// 从session中获得该用户的检索条件
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		Iterator<String> it = propertyPermissions.keySet().iterator();
		// 门店列表
		List<PermissionBean> propertyList = new ArrayList<PermissionBean>();
		// 门店对应渠道列表
		List<Map<String, Object>> shoppingCartInfoList = new ArrayList<Map<String, Object>>();
		while (it.hasNext()) {
			String propertyId = it.next();
			propertyList.add(propertyPermissions.get(propertyId));
			
			List<MasterInfoBean> shoppingCartList = omsOrdersSearchService.getShoppingCarts(propertyId);
			Map<String, Object> shoppingCartInfo = new HashMap<String, Object>();
			shoppingCartInfo.put("propertyId", propertyId);
			shoppingCartInfo.put("shoppingCartList", shoppingCartList);
			shoppingCartInfoList.add(shoppingCartInfo);
		}

		// 获取用户时区
		int zone = user.getTimeZone();
		// 检索结束日
		String searchDateTo = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), zone).substring(0, 10);
		String searchDateFrom = DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(OmsConstants.ORDER_SEARCH_FROM_MONTH), zone).substring(0, 10);

		// 获得检索条件下拉信息
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("propertyList", propertyList);
		resultDataMap.put("shoppingCartInfoList", shoppingCartInfoList);
		// 检索时间终了
		resultDataMap.put("searchDateTo", searchDateTo);
		// 检索时间开始
		resultDataMap.put("searchDateFrom", searchDateFrom);
		
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
	 * 检索
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doSearch", method = RequestMethod.POST)
	public void doSearch(HttpServletRequest request, HttpServletResponse response,
			@Valid @RequestBody InFormSearch bean, BindingResult bindingResult) {

		// 从session中获得该用户的信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

		// 输入参数出力
		logger.info(bean.toString());
		
		// 输入参数校验失败
		if (bean.hasInputError(bean, bindingResult)) {
			
			logger.info(MessageHelp.getMessage(MessageConstants.MESSAGE_TYPE_VALIDATE, MessageConstants.MESSAGE_CODE_100001));
			
			bean.writeTo(request, response);
			
			logger.info(bean.getResponseBean().toString());
			
			return;
		}
		
		// 业务校验通过
		if (!bussinessCheck(bean, request)) {
			// 输入快递单号且没有没找到列表
			boolean isTrackingSuccess = true;

			// 快递单号输入场合
			if (!StringUtils.isNullOrBlank2(bean.getTrackingNumber())) {
				// 根据快递单号查订单号列表
				List<String> orderNumberList = omsOrdersSearchService.getOrderNumbersByTrackingNo(bean);

				// 快递单号输入但没找到
				if (orderNumberList == null || orderNumberList.size() == 0) {
					// 下面的查询不用再继续了，肯定查不到
					isTrackingSuccess = false;

				// 追加订单号列表作为查询条件
				} else {
					bean.setOrderNumberList(orderNumberList);
				}
			}

			//TODO 这里的原先的流程 每次请求都需要取得所有的OrderNumber 这样很费时间 能不能改一下流程
//			// 符合条件的全部订单号列表
//			List<String> orderNumberList = omsOrdersSearchService.getOrderNumbers(bean);
//			// 符合条件的总订单数
//			int orderCount = 0;
//			if (orderNumberList != null) {
//				orderCount = orderNumberList.size();
//			}

			// 继续查询
			if (isTrackingSuccess) {
				//Quick Filter Today Order所需要的条件
				String currentDate = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()).substring(0, 10);
				bean.setFromTime(DateTimeUtil.getGMTTimeFrom(currentDate, user.getTimeZone()));
				bean.setEndTime(DateTimeUtil.getGMTTimeTo(currentDate, user.getTimeZone()));
				//Quick Filter refund order
				if (bean.getQuickFilter() != null && bean.getQuickFilter().equals(OmsConstants.ORDER_STATUS_QUICKFILTER_REFUND)) {
					String orderStatusReturnRequested = Type.getValue(MastType.orderStatus.getId(), OmsCodeConstants.RefundStatus.RETURN_REQUESTED, com.voyageone.common.Constants.LANGUAGE.EN);
					bean.setOrderStatusReturnRequested(orderStatusReturnRequested);
				}
				// 订单开始日期时间
				String orderDateTimeFrom = bean.getOrderDateFrom();
				if (!StringUtils.isNullOrBlank2(orderDateTimeFrom)) {
					orderDateTimeFrom = StringUtils.getDate(DateTimeUtil.parseStr(orderDateTimeFrom, DateTimeUtil.DEFAULT_DATETIME_FORMAT));
					orderDateTimeFrom = DateTimeUtil.getGMTTimeFrom(orderDateTimeFrom, user.getTimeZone());
					bean.setOrderDateFrom(orderDateTimeFrom);
				}

				// 订单结束日期时间
				String orderDateTimeTo = bean.getOrderDateTo();
				if (!StringUtils.isNullOrBlank2(orderDateTimeTo)) {
					orderDateTimeTo = StringUtils.getDate(DateTimeUtil.parseStr(orderDateTimeTo, DateTimeUtil.DEFAULT_DATETIME_FORMAT));
					orderDateTimeTo = DateTimeUtil.getGMTTimeTo(orderDateTimeTo, user.getTimeZone());
					bean.setOrderDateTo(orderDateTimeTo);
				}
				//orderKind
				List<String> orderKindList = getOrderKindList(bean);
				if (orderKindList.size() == 0) {
					orderKindList = null;
				}
				bean.setOrderKindList(orderKindList);
//			List<String> orderNumberList=new ArrayList<String>();
				int orderCount = omsOrdersSearchService.getOrderCount(bean);
				// 当前页订单数据
				List<OutFormSearch> ordersInfoList = new ArrayList<OutFormSearch>();
				// 分页处理
				if (PageUtil.pageInit(bean, orderCount)) {
					// 取得当前页的数据
					ordersInfoList = omsOrdersSearchService.getOrdersInfo(bean, user);
				}

				// 设置返回结果
				bean.getResponseBean().setResult(true);
				Map<String, Object> ordersListMap = new HashMap<String, Object>();
				ordersListMap.put("total", orderCount);
				ordersListMap.put("orderNumberList", new ArrayList<String>());
				ordersListMap.put("ordersInfoList", ordersInfoList);
				bean.getResponseBean().setResultInfo(ordersListMap);

			// 无需继续查询
			} else {
				// 设置返回结果
				bean.getResponseBean().setResult(true);
				Map<String, Object> ordersListMap = new HashMap<String, Object>();
				ordersListMap.put("total", 0);
				ordersListMap.put("orderNumberList", new ArrayList<String>());
				ordersListMap.put("ordersInfoList", new ArrayList<OutFormSearch>());
				bean.getResponseBean().setResultInfo(ordersListMap);
			}
		}
		
		// 结果返回输出流
		bean.getResponseBean().writeTo(request, response);
		
		// 输出结果出力
		logger.info(bean.getResponseBean().toString());
		
		return;
		
	}
	
	/**
	 * 业务校验
	 */
	private boolean bussinessCheck(InFormSearch bean, HttpServletRequest request) {
		// 校验失败
		boolean isFailure = true;
		
		// 店铺ID
		List<String> storeIdList = bean.getStoreId();
		if (storeIdList == null || storeIdList.size() == 0) {
			bean.getResponseBean().setResult(false, MessageConstants.MESSAGE_CODE_400005, 
					MessageConstants.MESSAGE_TYPE_DIALOG);
			
		} else {
			// 渠道ID
			List<String> channelList = bean.getChannelId();
			if (channelList == null || channelList.size() == 0) {
				bean.getResponseBean().setResult(false, MessageConstants.MESSAGE_CODE_400006, 
						MessageConstants.MESSAGE_TYPE_DIALOG);
				
			} else {
				UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
				HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
				Set<String> permissions = propertyPermissions.keySet();
				if (permissions == null ||  permissions.size() == 0) {
					bean.getResponseBean().setResult(false, MessageConstants.MESSAGE_CODE_400001, 
							MessageConstants.MESSAGE_TYPE_DIALOG);
					
				} else {
					boolean isNotInclude = false;
					for (String storeId : storeIdList) {
						if (!permissions.contains(storeId)) {
							isNotInclude = true;
							break;
							
						}
					}
					if (isNotInclude) {
						bean.getResponseBean().setResult(false, MessageConstants.MESSAGE_CODE_400007, 
								MessageConstants.MESSAGE_TYPE_DIALOG);
						
					// 校验通过
					} else {
						isFailure = false;
					}
				}
				
			}
		}
		
		return isFailure;
	}
	/**
	 * 返回orderKind封装结果
	 * @param bean
	 * @return
	 */
	private List<String>  getOrderKindList(InFormSearch bean) {
		List<String> orderKindList = new ArrayList<String>();
		if(bean.getOrderKindOriginal()!=null&&!"".equals(bean.getOrderKindOriginal())&&bean.getOrderKindOriginal().equals("true")) {
			orderKindList.add(OmsCodeConstants.OrderKind.ORIGINAL_ORDER);
		} 
		if(bean.getOrderKindSplit()!=null&&!"".equals(bean.getOrderKindSplit())&&bean.getOrderKindSplit().equals("true")) {
			orderKindList.add(OmsCodeConstants.OrderKind.SPLIT_ORDER);
		}
		if(bean.getOrderKindPriceDifference()!=null&&!"".equals(bean.getOrderKindPriceDifference())&&bean.getOrderKindPriceDifference().equals("true")) {
			orderKindList.add(OmsCodeConstants.OrderKind.PRICE_DIFF_ORDER);
		}
		if(bean.getOrderKindPresent()!=null&&!"".equals(bean.getOrderKindPresent())&&bean.getOrderKindPresent().equals("true")) {
			orderKindList.add(OmsCodeConstants.OrderKind.PRESENT_ORDER);
		}
		if(bean.getOrderKindExchange()!=null&&!"".equals(bean.getOrderKindExchange())&&bean.getOrderKindExchange().equals("true")) {
			orderKindList.add(OmsCodeConstants.OrderKind.RETURN_ORDER);
		}
		return orderKindList;
	}
}
