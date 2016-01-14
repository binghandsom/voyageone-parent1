package com.voyageone.cms.controller;

import com.jd.open.api.sdk.internal.util.JsonUtil;
import com.voyageone.base.BaseController;
import com.voyageone.cms.formbean.MasterCatPropBatchUpdateBean;
import com.voyageone.cms.service.MasterCatComPropBatchUpdateService;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cms/masterCategory/comPropUpdate")
public class MasterCatComPropBatchUpdateController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterCatComPropBatchUpdateController.class);

	@Autowired
	MasterCatComPropBatchUpdateService masterCatComPropBatchUpdateService;

	/**
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/getPropertyOptions", method = RequestMethod.POST)
	public void getPropertyOptions(HttpServletResponse response) {

		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
//			logger.info(JsonUtil.toJson(formData));
			logger.info("");

			Map<String, Object> responseObject = new HashMap<>();

			List<Map<String,String>> options = this.masterCatComPropBatchUpdateService.getPropOptions("商品状态");

			responseObject.put("options", options);

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
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public void batchUpdate(HttpServletResponse response, @RequestBody MasterCatPropBatchUpdateBean formData) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {

			logger.info(JsonUtil.toJson(formData));

			boolean isUpdateSuccess = masterCatComPropBatchUpdateService.batchUpdate(formData,getUser());

			// 设置返回画面的值
			responseBean.setResultInfo(isUpdateSuccess).writeTo(super.getRequest(), response);

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
