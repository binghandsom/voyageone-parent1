package com.voyageone.core.controller;

import com.google.gson.GsonBuilder;
import com.jd.open.api.sdk.response.wms.HashMap;
import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.emum.UserEditEnum;
import com.voyageone.core.modelbean.UserSessionBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * OMS 用户管理画面
 * 
 * @author james
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_SETTING_APP)
public class SettingAppController extends SettingBaseController {

	/**
	 * 追加application
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/addApp", "/addModule", "/addController", "/addAction" })
	public void add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		if (request.getServletPath().lastIndexOf("/addApp") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Application, 0);
		} else if (request.getServletPath().lastIndexOf("/addModule") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Module, 0);
		} else if (request.getServletPath().lastIndexOf("/addController") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Controller, 0);
		} else if (request.getServletPath().lastIndexOf("/addAction") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Action, 0);
		}
	}

	/**
	 * 更新application
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/updateApp", "/updateModule", "/updateController", "/updateAction" })
	public void update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		if (request.getServletPath().lastIndexOf("/updateApp") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Application, 1);
		} else if (request.getServletPath().lastIndexOf("/updateModule") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Module, 1);
		} else if (request.getServletPath().lastIndexOf("/updateController") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Controller, 1);
		} else if (request.getServletPath().lastIndexOf("/updateAction") > -1) {
			commonEdit(request, response, requestMap, UserEditEnum.Action, 1);
		}
	}
	@RequestMapping(value = "/batchAdd")
	public void batchAdd(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			settingService.batchAdd(requestMap, getUser().getUserName());
			isSuccess = true;
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}
		
	}
}
