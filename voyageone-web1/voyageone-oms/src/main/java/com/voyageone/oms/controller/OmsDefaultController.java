package com.voyageone.oms.controller;

import java.util.HashMap;
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

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.service.OmsDefaultService;

/**
 * OMS默认画面
 * 
 * @author jacky
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = OmsUrlConstants.URL_OMS_DEFAULT_INDEX)
public class OmsDefaultController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsDefaultController.class);

	@Autowired
	private OmsDefaultService omsDefaultService;

	/**
	 * 初始化（获得统计数据）
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
	public void doInit(HttpServletRequest request, HttpServletResponse response) {
		// 订单数
		int orderCount = omsDefaultService.getTotalOrderCount();
		
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		Map<String, Integer> orderCountInfoMap = new HashMap<String, Integer>();
		orderCountInfoMap.put("orderCountInfo", orderCount);
		responseBean.setResultInfo(orderCountInfoMap);
		
		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 输出结果出力
		logger.info(responseBean.toString());
		
		return;
	}
	
}
