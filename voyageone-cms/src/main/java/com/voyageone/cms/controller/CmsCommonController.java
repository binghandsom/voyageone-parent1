package com.voyageone.cms.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jd.open.api.sdk.internal.util.JsonUtil;
import com.voyageone.base.BaseController;
import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.formbean.CategoryListBean;
import com.voyageone.cms.service.CategoryEditService;
import com.voyageone.cms.service.CommonService;
import com.voyageone.cms.service.ModelEditService;
import com.voyageone.cms.service.ProductEditService;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CMS_COMMON)
public class CmsCommonController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(CmsCommonController.class);

	@Autowired
	private CommonService commonService;
	@Autowired
	private CategoryEditService categoryEditService;
	@Autowired
	private ModelEditService modelEditService;
	@Autowired
	private ProductEditService productEditService;

	@RequestMapping(value = "/master/doGetMasterDataList")
	public void doGetMasterDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.toJson(requestMap));

			String channelId = (String) requestMap.get("channelId");

			Map<String, Object> ret = commonService.doGetMasterDataList(channelId);
			isSuccess = true;
			responseBean.setResultInfo(ret);
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

	@RequestMapping(value = "/edit/doUpdateMainCategory")
	public void doUpdateMainCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody List<HashMap<String, Object>> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.toJson(requestMap));
			for(HashMap<String, Object>item :requestMap){
				item.put("modifier", getUser().getUserName());
				
				Integer type = Integer.valueOf(item.get("type").toString());
				// 设置JD TM CategoryId
				List<Map<String,Object>>platformInfo = (List<Map<String, Object>>) item.get("platformInfo");
				item.put("tmCategoryId", null);
				item.put("jdCategoryId", null);
				if(platformInfo !=null){
					for(Map<String,Object> platform :platformInfo){
						String platformId = platform.get("platformId").toString();
						if(platformId.equals(PlatFormEnums.PlatForm.TM.getId())){
							item.put("tmCategoryId", platform.get("platformCid").toString());
						}else if(platformId.equals(PlatFormEnums.PlatForm.JD.getId())){
							item.put("jdCategoryId", platform.get("platformCid").toString());
						}
					}
				}
				
				if (type.equals(CmsConstants.IDType.TYPE_CATEGORY)) {
					isSuccess = categoryEditService.updateMainCategoryId(item, getUser());
				}else if (type.equals(CmsConstants.IDType.TYPE_MODEL)) {
					modelEditService.doUpdateMainCategory(item);
				}else if (type.equals(CmsConstants.IDType.TYPE_PRODUCT)) {
					productEditService.doUpdateMainCategory(item);
				}
			}
			isSuccess = true;
			responseBean.setResultInfo(isSuccess);
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

	@RequestMapping(value = "/navigation/doGetNavigationByCategoryModelId")
	public void doGetNavigationByCategoryModelId(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.toJson(requestMap));

			Integer type = Integer.valueOf(requestMap.get("type").toString());
			String categoryId = requestMap.get("categoryId").toString();
			String channelId = requestMap.get("channelId").toString();

			if (type.equals(CmsConstants.IDType.TYPE_CATEGORY)) {
				List<CategoryListBean> result = categoryEditService.getCategoryList(categoryId, channelId);
				Collections.reverse(result);
				responseBean.setResultInfo(result);
			}
			if (type.equals(CmsConstants.IDType.TYPE_MODEL)) {

				String modelId = requestMap.get("modelId").toString();
				Map<String, Object> result = categoryEditService.getModelInfo(modelId, categoryId, channelId);

				responseBean.setResultInfo(result);
			}

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
