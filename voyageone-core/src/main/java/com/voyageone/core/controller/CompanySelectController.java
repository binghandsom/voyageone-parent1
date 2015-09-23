package com.voyageone.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.GsonBuilder;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.CompanyBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.LoginService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_ACCOUNT_COMPANY)
public class CompanySelectController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(CompanySelectController.class);

	@Autowired
	private LoginService loginService;
	/**
	 * 初始化（取得公司列表）
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/doGetCompany", method = RequestMethod.POST)
	public void doGetCompany(HttpServletRequest request, HttpServletResponse response) {

		// session中取得用户信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// 获得该用户有权限公司列表
		List<CompanyBean> companyList = user.getPermissionCompanies();
		
		List<CompanyBean> newCompanyList = loginService.getUserCompanyPermissionMap(user.getUserName());
		
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		Map<String, Object> companyListMap = new HashMap<String, Object>();
		companyListMap.put("companyList", companyList);
		companyListMap.put("newCompanyList", newCompanyList);
		companyListMap.put("selCompanyId", user.getSelectCompany().getCompanyId());
		responseBean.setResultInfo(companyListMap);
		
		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 输出结果出力
		logger.info(responseBean.toString());
		
		return;
	}
	
	/**
	 * 公司选择
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/doSelectCompany", method = RequestMethod.POST)
	public void doSelectCompany(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map requestMap) {
		
		// 输入参数出力
		logger.info(new GsonBuilder().serializeNulls().create().toJson(requestMap));
		
		// session中取得用户信息
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		
		// 设置当前用户选择的公司
		user.getSelectCompany().setCompanyId(Integer.valueOf(requestMap.get("id").toString()));

		// 取得用户信息
		user = loginService.getUserInfo(user);
		
		if(requestMap.get("systemName") != null){
			
			user.setSelMenu(((String)requestMap.get("systemName")).toUpperCase());
		}
		if(requestMap.get("channelId") != null){
			user.setSelChannel((String)requestMap.get("channelId"));
		}
		
		request.getSession(false).setAttribute(CoreConstants.VOYAGEONE_USER_INFO, user);
		
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		if("cms".equals(requestMap.get("systemName"))){
			responseBean.setForward(UrlConstants.URL_CORE_CMS_HOME);
		}else{
			responseBean.setForward(UrlConstants.URL_CORE_MENU_HOME);
		}
		
		
		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 输出结果出力
		logger.info(responseBean.toString());
		
		return;
	}
}
