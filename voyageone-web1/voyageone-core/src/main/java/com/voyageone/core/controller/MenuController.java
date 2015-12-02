package com.voyageone.core.controller;

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

import com.google.gson.GsonBuilder;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.service.AnnouncementService;
import com.voyageone.core.service.LoginService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_MENU_HOME)
public class MenuController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MenuController.class);

	@Autowired
	private AnnouncementService announcementService;

	@Autowired
	private LoginService loginService;
	
	/**
	 * 获得公共和该公司的公告标题
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/doGetAnnouncements", method = RequestMethod.POST)
	public void doGetAnnouncements(HttpServletRequest request, HttpServletResponse response) {
		// 公共公告标题列表
		List<Map<String, String>> publicList = announcementService.getPublicAnnouncements(request);
		
		// 获得公司的公告标题
		List<Map<String, String>> companyList = announcementService.getCompanyAnnouncements(request);
		
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		
		Map<String, List<Map<String, String>>> announcementMap = new HashMap<String, List<Map<String, String>>>();
		announcementMap.put("publicAnnouncementList", publicList);
		announcementMap.put("companyAnnouncementList", companyList);
		responseBean.setResultInfo(announcementMap);
		
		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 输出结果出力
		logger.info(responseBean.toString());
		
		return;
	}
	
	/**
	 * 获得公告内容
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/doGetAnnouncementContent", method = RequestMethod.POST)
	public void doGetAnnouncementContent(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map requestMap) {
		// 输入参数出力
		logger.info(new GsonBuilder().serializeNulls().create().toJson(requestMap));
		
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		// 公告内容
		String content = announcementService.getAnnouncementContent(request, Integer.valueOf(requestMap.get("id").toString()));
		Map<String, String> contentMap = new HashMap<String, String>();
		contentMap.put("contentInfo", content);
		responseBean.setResultInfo(contentMap);
		
		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 输出结果出力
		logger.info(responseBean.toString());
		
		return;
	}
}
