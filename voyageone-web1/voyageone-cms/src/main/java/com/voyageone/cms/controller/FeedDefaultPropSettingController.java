package com.voyageone.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jd.open.api.sdk.internal.util.JsonUtil;
import com.voyageone.base.BaseController;
import com.voyageone.cms.formbean.FeedDefaultPropBean;
import com.voyageone.cms.formbean.FeedDefaultPropForm;
import com.voyageone.cms.service.FeedDefaultPropSettingService;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;

@Controller
@RequestMapping("/cms/feedDefaultProperty/setting")
public class FeedDefaultPropSettingController extends BaseController {
	
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(FeedDefaultPropSettingController.class);
	
	@Autowired
	FeedDefaultPropSettingService feedDefaultPropSettingService;
	
	/**
	 * 
	 * @param response
	 * @param formData
	 */
	@RequestMapping(value = "/getDefaultProps", method = RequestMethod.POST)
	public void getDefaultProps(HttpServletResponse response) {

		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {

			Map<String, Object> responseObject = new HashMap<>();
			UserSessionBean userSession = getUser();

			String channelId = userSession.getSelChannel();

			List<FeedDefaultPropBean> defaultPropBeans = this.feedDefaultPropSettingService.getFeedProps(channelId);

			// 设定默认属性列表
			responseObject.put("defaultPropList", defaultPropBeans);
			responseObject.put("isUpdate", this.feedDefaultPropSettingService.isUpdate());
			responseObject.put("channelId", channelId);

			// 设置返回画面的值
			responseBean.setResultInfo(responseObject).writeTo(super.getRequest(), response);
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
	
	/**
	 * 
	 * @param response
	 * @param formData
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public void submit(HttpServletResponse response, @RequestBody List<FeedDefaultPropBean> feedDefaultPropBeans) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			logger.info(JsonUtil.toJson(feedDefaultPropBeans));

			Object res = feedDefaultPropSettingService.submit(feedDefaultPropBeans,getUser());

			
			// 设置返回画面的值
			responseBean.setResultInfo(res).writeTo(super.getRequest(), response);
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
