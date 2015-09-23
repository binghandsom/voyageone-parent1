package com.voyageone.core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.common.Constants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.GsonBuilder;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.emum.UserEditEnum;
import com.voyageone.core.modelbean.UserSessionBean;


@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_SETTING_ROLE)
public class SettingRoleController extends SettingBaseController{
	
	@RequestMapping(value = "/getRoleList")
	public void getRoleList(HttpServletRequest request, HttpServletResponse response) {
		
		commonEdit(request, response, null, UserEditEnum.Role,3);
	}
	@RequestMapping(value = "/addRole")
	public void addRole(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map <String,Object> requestMap) {
		commonEdit(request, response, requestMap, UserEditEnum.Role,0);
	}
	@RequestMapping(value = "/updateRole")
	public void updateRole(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map <String,Object> requestMap) {
		
		commonEdit(request, response, requestMap, UserEditEnum.Role,1);
	}
	@RequestMapping(value = "/getRoleInfoById")
	public void getRoleInfoById(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map <String,Object> requestMap) {
		
		commonEdit(request,response,requestMap,UserEditEnum.RolePermission,3);
	}
	@RequestMapping(value = "/addRolePermission")
	public void addRolePermission(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map <String,Object> requestMap) {
		// －1表示该类目下所有的权限 添加该类目下所有的权限
		if((requestMap.get("module_id") != null && "-1".equalsIgnoreCase(requestMap.get("module_id").toString())) 
			||(requestMap.get("controller_id") != null && "-1".equalsIgnoreCase(requestMap.get("controller_id").toString()))
			||(requestMap.get("action_id") != null && "-1".equalsIgnoreCase(requestMap.get("action_id").toString()))){
			
			logger.info(new GsonBuilder().serializeNulls().create().toJson(requestMap));
			AjaxResponseBean responseBean = new AjaxResponseBean();
			UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
			requestMap.put("modifier", user.getUserName());
			int ret=settingService.insertRoleAllPermission(requestMap);
			responseBean.setResult(ret>0);
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}else{
			commonEdit(request, response, requestMap, UserEditEnum.RolePermission,0);
		}
		
	}
	@RequestMapping(value = "/updateRolePermission")
	public void updateRolePermission(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map <String,Object> requestMap) {
		
		commonEdit(request, response, requestMap, UserEditEnum.RolePermission,1);
	}
}
