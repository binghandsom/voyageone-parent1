package com.voyageone.core.controller;

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

import com.voyageone.core.UrlConstants;
/**
 * Core Index
 * @author eric
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value=UrlConstants.URL_CORE_DEFAULT_INDEX)
public class CoreDefaultIndexController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(CoreDefaultIndexController.class);
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
