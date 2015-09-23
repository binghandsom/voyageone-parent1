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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.service.OmsOrdersSearchService;
/**
 * Oms order Index
 * @author eric
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value=OmsUrlConstants.URL_OMS_ORDERS_INDEX)
public class OmsOrderIndexController {
	@Autowired
	private OmsOrdersSearchService omsOrdersSearchService;
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsOrderIndexController.class);
	/**
	 * 初始化页面
	 * @param requset
	 * @param response
	 */
	@RequestMapping(value="/doInit" , method=RequestMethod.POST)
    public void doInit(HttpServletRequest  request,HttpServletResponse response){
		
		
		// 从session中获得该用户的检索条件
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		//获取用户时区
		int zone=user.getTimeZone();
		String strdate=DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), zone).substring(0, 10);
		//获取时区对应的当前时间
		String fromTime=DateTimeUtil.getGMTTimeFrom(strdate, zone);
		String endTime=DateTimeUtil.getGMTTimeTo(strdate, zone);
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		Iterator<String> it = propertyPermissions.keySet().iterator();
		// 门店对应渠道列表
		List<String> channelId = new ArrayList<String>();
		
		while (it.hasNext()) {
			
			channelId.add(it.next());
			
		}
		//返回结果
		Map<String, Object> orderCountInfoMap = new HashMap<String, Object>();
		orderCountInfoMap=omsOrdersSearchService.getOrderIndexCount(channelId, fromTime, endTime);
		//传到search页面的时间 
		orderCountInfoMap.put("dateTime", strdate);
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();
		ordersListMap.put("orderIndexInfo", orderCountInfoMap);

		// 设置返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		result.setResult(true);
		result.setResultInfo(ordersListMap);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
    	return;
    }
	
}
