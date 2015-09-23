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
import com.voyageone.cms.formbean.CmsCategoryBean;
import com.voyageone.cms.formbean.MasterPropertyFormBean;
import com.voyageone.cms.service.MasterCategoryMatchService;
import com.voyageone.common.bussiness.platformInfo.model.PlatformInfoModel;
import com.voyageone.common.configs.ImsCategoryConfigs;
import com.voyageone.common.configs.beans.ImsCategoryBean;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;

@Controller
@RequestMapping("/cms/masterCategory/match")
public class MasterCategoryMatchController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterCategoryMatchController.class);

	@Autowired
	MasterCategoryMatchService masterCategoryMatchService;

	/**
	 * 
	 * @param response
	 * @param formData
	 */
	@RequestMapping(value = "/getAllCategory", method = RequestMethod.POST)
	public void getAllCategory(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) {

		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			logger.info(JsonUtil.toJson(formData));

			Map<String, Object> responseObject = new HashMap<>();
			UserSessionBean userSession = getUser();

			String channelId = userSession.getSelChannel();

			List<CmsCategoryBean> cmsCategoryBeans = this.masterCategoryMatchService.getCmsCategoryList(channelId);

			// 获取cms类目
			responseObject.put("cmsCategoryList", cmsCategoryBeans);
			// 获取所有的主类目
			List<ImsCategoryBean> masterCategoryList = ImsCategoryConfigs.getMtCategoryBeanById(0).getSubCategories();
			responseObject.put("masterCategoryList", masterCategoryList);

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
	@RequestMapping(value = "/getPlatformInfo", method = RequestMethod.POST)
	public void doGetPlatformInfo(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			Map<String, Object> responseObject = new HashMap<>();

			List<PlatformInfoModel> platformInfoList = this.masterCategoryMatchService
					.getPlatformInfo(Integer.valueOf(formData.getCategoryId()));

			// 获取cms类目
			responseObject.put("platformInfo", platformInfoList);

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

}
