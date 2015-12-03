package com.voyageone.core.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.GsonBuilder;
import com.voyageone.base.BaseController;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.emum.UserEditEnum;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.SettingService;

public class SettingBaseController extends BaseController{

	/**
	 * 日志
	 */
	protected static Log logger = LogFactory.getLog(SettingUserController.class);

	@Autowired
	protected SettingService settingService;

	/**
	 * 数据编辑（共同）
	 * 
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param requestMap
	 *            web请求的参数
	 * @param UserEditEnum
	 *            对应需要操作的sql
	 * @param mode
	 *            操作类型 0：插入 1：更新 2：修改 3：查询
	 * 
	 */
	protected void commonEdit(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestMap,
			UserEditEnum type, int mode) {

		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {

			if (requestMap == null) {
				requestMap = new HashMap<String, Object>();
			}
			// 输入参数出力
			logger.info(new GsonBuilder().serializeNulls().create().toJson(requestMap));

			// 获得session中用户信息
			UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
			requestMap.put("modifier", user.getUserName());
			int ret = 0;

			switch (mode) {
			// 插入
			case 0:
				ret = settingService.commonInsert(requestMap, type);
				isSuccess = ret > 0;
				if (ret == -1) {
					msgCode = MessageConstants.MESSAGE_CODE_200010;
					msgType = MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION;
				}
				responseBean.setResultInfo(requestMap);
				break;
			// 更新
			case 1:
				ret = settingService.commonUpdate(requestMap, type);
				isSuccess = ret > 0;
				if (ret == -1) {
					msgCode = MessageConstants.MESSAGE_CODE_200010;
					msgType = MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION;
				}
				responseBean.setResultInfo(requestMap);
				break;
			// 删除
			case 2:
				ret = settingService.commonDel(Integer.parseInt(requestMap.get("id").toString()), type);
				isSuccess = ret > 0;
				responseBean.setResultInfo(requestMap);
				break;
			// 查询
			case 3:
				responseBean.setResultInfo(settingService.commonSelect(requestMap, type));
				isSuccess = true;
			default:
				break;
			}
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
