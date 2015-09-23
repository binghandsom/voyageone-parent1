package com.voyageone.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.GsonBuilder;
import com.jd.open.api.sdk.internal.util.JsonUtil;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.emum.UserEditEnum;
import com.voyageone.core.formbean.InFormUser;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.LoginService;

/**
 * OMS 用户管理画面
 * 
 * @author james
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_SETTING_USER)
public class SettingUserController extends SettingBaseController {

    @Autowired
    private LoginService loginService;
    
	@RequestMapping(value = "/doInit")
	public void doUserInit(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> data = settingService.getAllRolePermissionName();

		AjaxResponseBean responseBean = new AjaxResponseBean();

		// 设置返回结果
		responseBean.setResult(true);
		// 设置返回内容
		responseBean.setResultInfo(data);
		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());
	}

	/**
	 * 初始化（获取用户列表）
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserList")
	public void getUserList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {

		List<Map<String, Object>> resultDataMap = settingService.getUserList(requestMap);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalCnt", resultDataMap.size());
		if (requestMap.containsKey("offset")) {
			int o = (int) requestMap.get("offset");
			if(o > resultDataMap.size()){
				o = resultDataMap.size()-1;
			}
			int r = (o + (int) requestMap.get("rows"));
			if (r > resultDataMap.size()) {
				r = resultDataMap.size();
			}
			resultDataMap = resultDataMap.subList(o, r);
		}

		result.put("data", resultDataMap);
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		responseBean.setResultInfo(result);

		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());

		return;
	}

	/**
	 * 追加用户
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addUser")
	public void addUser(HttpServletRequest request, HttpServletResponse response, @RequestBody InFormUser user) {
		// 输入参数出力
		logger.info(user.toString());
		// 获取编辑者用户名
		UserSessionBean modifier = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		user.setModifier(modifier.getUserName());

		String cryptoPassword = new Md5Hash(user.getPassword(),
				user.getUsername().toLowerCase() + CoreConstants.MD5_FIX_SALT,
				 CoreConstants.MD5_HASHITERATIONS).toHex();
		user.setPassword(cryptoPassword);
		int ret = settingService.addUser(user);

		AjaxResponseBean responseBean = new AjaxResponseBean();

		// 设置返回结果
		responseBean.setResult(ret > 0);
		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());
	}

	/**
	 * 更新用户
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateUser")
	public void updateUser(HttpServletRequest request, HttpServletResponse response, @RequestBody InFormUser user) {
		// 输入参数出力
		logger.info(user.toString());
		// 获得session中用户信息
		UserSessionBean modifier = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		user.setModifier(modifier.getUserName());
		if(user.getPassword() != null && user.getPassword().length()>0)
		{
			String cryptoPassword = new Md5Hash(user.getPassword(),
				user.getUsername().toLowerCase() + CoreConstants.MD5_FIX_SALT,
				 CoreConstants.MD5_HASHITERATIONS).toHex();
			user.setPassword(cryptoPassword);
		}
		int ret = settingService.updateUser(user);

		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(ret > 0);
		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delUser")
	public void delUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		int userId = (int) requestMap.get("userId");
		// 输入参数出力
		logger.info("userId=" + userId);

		int ret = settingService.delUser(userId);
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(ret > 0);
		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());
	}

	/**
	 * 获取用户的详细信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserInfo")
	public void getUserInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		// 获得入力参数MAP
		// int userId=Integer.parseInt(requestMap.get("user_id").toString());
		// int userId=3;
		List<Map<String, Object>> userRoleInfo = settingService.getUserRoleInfo((int) requestMap.get("userId"));
		List<Map<String, Object>> userPermissionInfo = settingService.getUserPermissionInfo((int) requestMap
				.get("userId"));

		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("user_role_property", userRoleInfo);
		resultDataMap.put("user_permission", userPermissionInfo);

		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		// 设置查询结果
		responseBean.setResultInfo(resultDataMap);
		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());
	}

	/**
	 * 插入一条User_Role数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/insertRoleInfo")
	public void insertRoleInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		commonEdit(request, response, requestMap, UserEditEnum.UserRole, 0);
	}

	/**
	 * 更新一条User_Role数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateRoleInfo")
	// public void updateRoleInfo(HttpServletRequest request,
	// HttpServletResponse response,@RequestBody Map requestMap)
	public void updateRoleInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		commonEdit(request, response, requestMap, UserEditEnum.UserRole, 1);
	}

	@RequestMapping(value = "/delRoleInfo")
	public void delRoleInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		commonEdit(request, response, requestMap, UserEditEnum.UserRole, 2);
	}

	/**
	 * 插入一条User_Permission数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/insertPermissionInfo")
	public void insertPermissionInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		if ((requestMap.get("module_id") != null && "-1".equalsIgnoreCase(requestMap.get("module_id").toString()))
				|| (requestMap.get("controller_id") != null && "-1".equalsIgnoreCase(requestMap.get("controller_id")
						.toString()))
				|| (requestMap.get("action_id") != null && "-1"
						.equalsIgnoreCase(requestMap.get("action_id").toString()))) {

			logger.info(new GsonBuilder().serializeNulls().create().toJson(requestMap));
			AjaxResponseBean responseBean = new AjaxResponseBean();
			UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
			requestMap.put("modifier", user.getUserName());
			int ret = settingService.insertUserAllPermission(requestMap);
			responseBean.setResult(ret > 0);
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		} else {
			commonEdit(request, response, requestMap, UserEditEnum.UserPermission, 0);
		}
	}

	/**
	 * 更新一条User_Permission数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updatePermissionInfo")
	public void updatePermissionInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		commonEdit(request, response, requestMap, UserEditEnum.UserPermission, 1);
	}

	/**
	 * 删除一条User_Permission数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delPermissionInfo")
	public void delPermissionInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		commonEdit(request, response, requestMap, UserEditEnum.UserPermission, 2);
	}
	
	/**
	 * 用户密码修改
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doChgPassword")
	public void doChgPassword(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		
		
        // 输入参数出力
        logger.info(requestMap.toString());
        
        AjaxResponseBean responseBean = new AjaxResponseBean();
        
		// 获得session中用户信息
		UserSessionBean modifier = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		String userName=modifier.getUserName();
        
        // 取得用户密码
        List<Map<String, Object>> userResult = loginService.getUserInfoByName(userName);
        // 用户输入密码加密
        String cryptoPassword = new Md5Hash(requestMap.get("old_password").toString(),
        		userName.toLowerCase() + CoreConstants.MD5_FIX_SALT,
                CoreConstants.MD5_HASHITERATIONS).toHex();
        if (userResult.size() > 0 && cryptoPassword.equals(userResult.get(0).get("password").toString())) {
        	
        	String newPassword=new Md5Hash(requestMap.get("password").toString(),
            		userName.toLowerCase() + CoreConstants.MD5_FIX_SALT,
                    CoreConstants.MD5_HASHITERATIONS).toHex();
        	InFormUser formUser = new InFormUser();
        	formUser.setUsername(userName);
        	formUser.setUser_id(Integer.parseInt(userResult.get(0).get("userId").toString()));
        	formUser.setPassword(newPassword);
        	formUser.setActive(true);
        	formUser.setIs_superuser((Boolean)userResult.get(0).get("is_superuser"));
        	int ret = settingService.updateUser(formUser);
        	
    		// 设置返回结果
    		responseBean.setResult(ret > 0);

        }else{
    		// 设置返回结果
    		responseBean.setResult(false, MessageConstants.MESSAGE_CODE_200001,
                    MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
        }
        
		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());
	}
	@RequestMapping(value = "/doUpdateUserConfig")
	public void doUpdateUserConfig(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.toJson(requestMap));

			UserSessionBean user = getUser();
			settingService.updateUserConfig(requestMap, user);
			
            // 用户信息放入session
            HttpSession session = request.getSession(true);
            session.setAttribute(CoreConstants.VOYAGEONE_USER_INFO, user);
            
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
