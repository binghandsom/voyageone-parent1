package com.voyageone.oms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.voyageone.oms.OmsUrlConstants;
/**
 * Oms customer Index
 * @author eric
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value=OmsUrlConstants.URL_OMS_CUSTOMER_INDEX)
public class OmsCustomerIndexController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsCustomerIndexController.class);
	/**
	 * 初始化页面
	 * @param requset
	 * @param response
	 */
	@RequestMapping(value="/doInit" , method=RequestMethod.POST)
    public void doInit(HttpServletRequest  requset,HttpServletResponse response){
    	return;
    }
}
