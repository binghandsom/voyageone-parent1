package com.voyageone.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jd.open.api.sdk.internal.util.JsonUtil;
import com.voyageone.base.BaseController;
import com.voyageone.cms.dao.SearchDao;
import com.voyageone.cms.formbean.*;
import com.voyageone.common.Constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.service.ModelEditService;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CMS_EDIT_MODEL)
public class ModelController extends BaseController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(ModelController.class);

	@Autowired
	private ModelEditService modelEditService;

	@RequestMapping(value = "/doGetUSModelInfo")
	public void doGetUSModelInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String modelId = (String) requestMap.get("modelId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("modelId: " + modelId + " channelId: " + channelId);

			ModelUSBean ret;

			ret = modelEditService.doGetUSModelInfo(modelId, channelId, true);
			isSuccess = ret != null;
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

	@RequestMapping(value = "/doGetCNModelInfo")
	public void doGetCNModelInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String modelId = (String) requestMap.get("modelId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("modelId: " + modelId + " channelId: " + channelId);

			ModelCNBean ret;

			ret = modelEditService.doGetCNModelInfo(modelId, channelId, true);
			isSuccess = ret != null;
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

	@RequestMapping(value = "/doGetModelCNPriceSettingInfo")
	public void doGetModelCNPriceSettingInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String modelId = (String) requestMap.get("modelId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("modelId: " + modelId + " channelId: " + channelId);

			ModelPriceSettingBean ret;

			ret = modelEditService.doGetModelCNPriceSettingInfo(modelId, channelId, true);
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

	@RequestMapping(value = "/doUpdateModelCNPriceSettingInfo")
	public void doUpdateModelCNPriceSettingInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModelPriceSettingBean requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = modelEditService.doUpdateModelCNPriceSettingInfo(requestMap, getUser().getUserName());
			responseBean.setResultInfo(modelEditService.doGetModelCNPriceSettingInfo(requestMap.getModelId().toString(), requestMap.getChannelId(), true));
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

	@RequestMapping(value = { "/doGetUSCategoryList", "/doGetCNCategoryList" })
	public void doGetCategoryList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String modelId = (String) requestMap.get("modelId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("modelId: " + modelId + " channelId: " + channelId);

			if (request.getServletPath().lastIndexOf("/doGetUSCategoryList") > -1) {
				List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
				ret = modelEditService.doGetUSCategoryList(modelId, channelId);
				responseBean.setResultInfo(ret);
			} else if (request.getServletPath().lastIndexOf("/doGetCNCategoryList") > -1) {
				List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
				ret = modelEditService.doGetCNCategoryList(modelId, channelId);
				responseBean.setResultInfo(ret);
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

	@RequestMapping(value = "/doGetUSProductList")
	public void doGetUSProductList(HttpServletRequest request, HttpServletResponse response, @RequestBody DtRequest<Map<String, Object>> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("参数：" + JsonUtil.toJson(requestMap));
			
			requestMap.getParam().put("start", requestMap.getStart());
			requestMap.getParam().put("length", requestMap.getLength());
			if(requestMap.getOrder().size()>0){
				requestMap.getParam().put("order", requestMap.getOrder().get(0).getColumn());
				requestMap.getParam().put("dir", requestMap.getOrder().get(0).getDir());
			}

			DtResponse<List<ModelProductUSBean>> ret = new DtResponse<List<ModelProductUSBean>>();
			ret.setData(modelEditService.doGetUSProductList(requestMap.getParam()));
			ret.setDraw(requestMap.getDraw());
			ret.setRecordsTotal(modelEditService.doGetUSProductListCnt(requestMap.getParam()));
			ret.setRecordsFiltered(ret.getRecordsTotal());
			
			responseBean.setResultInfo(ret);
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

	@RequestMapping(value = "/doGetCNProductList")
	public void doGetCNProductList(HttpServletRequest request, HttpServletResponse response, @RequestBody DtRequest<Map<String, Object>> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			requestMap.getParam().put("start", requestMap.getStart());
			requestMap.getParam().put("length", requestMap.getLength());
			if(requestMap.getOrder().size()>0){
				requestMap.getParam().put("order", requestMap.getOrder().get(0).getColumn());
				requestMap.getParam().put("dir", requestMap.getOrder().get(0).getDir());
			}
			
			DtResponse<List<ModelProductCNBean>> ret = new DtResponse<List<ModelProductCNBean>>();
			ret.setData(modelEditService.doGetCNProductList(requestMap.getParam()));
			ret.setDraw(requestMap.getDraw());
			ret.setRecordsTotal(modelEditService.doGetCNProductListCnt(requestMap.getParam()));
			ret.setRecordsFiltered(ret.getRecordsTotal());
			
			responseBean.setResultInfo(ret);
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

	@RequestMapping(value = "/doUpdateCNModelInfo")
	public void doUpdateCNModelInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModelCNBaseBean requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));
			isSuccess = modelEditService.doUpdateCNModelInfo(requestMap);
			responseBean.setResultInfo(modelEditService.doGetCNModelInfo(requestMap.getModelId().toString(), requestMap.getChannelId().toString(), true));

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

	@RequestMapping(value = "/doUpdateCNModelTmallInfo")
	public void doUpdateCNModelTmallInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModelTMBean requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = modelEditService.doUpdateCNModelTmallInfo(requestMap);
			responseBean.setResultInfo(modelEditService.doGetCNModelInfo(requestMap.getModelId().toString(), requestMap.getChannelId().toString(), true));

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

	@RequestMapping(value = "/doUpdateCNModelJingDongInfo")
	public void doUpdateCNModelJingDongInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModelJDBean requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = modelEditService.doUpdateCNModelJingDongInfo(requestMap);
			responseBean.setResultInfo(modelEditService.doGetCNModelInfo(requestMap.getModelId().toString(), requestMap.getChannelId().toString(), true));

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

	@RequestMapping(value = "/doUpdateUSModelInfo")
	public void doUpdateUSModelInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModelUSBean requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = modelEditService.doUpdateUSModelInfo(requestMap);
			responseBean.setResultInfo(modelEditService.doGetCNModelInfo(requestMap.getModelId().toString(), requestMap.getChannelId().toString(), true));

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

	@RequestMapping(value = "/doUpdateUSModelPrimaryCategory")
	public void doUpdateUSModelPrimaryCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.put("modifier", getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			// requestMap.setModifier(getUser().getUserName());
			isSuccess = modelEditService.doUpdateUSModelPrimaryCategory(requestMap);
			if ("1".equals(requestMap.get("typeId"))) {
				responseBean.setResultInfo(modelEditService.doGetUSCategoryList(requestMap.get("modelId").toString(), requestMap.get("channelId").toString()));
			} else {
				responseBean.setResultInfo(modelEditService.doGetCNCategoryList(requestMap.get("modelId").toString(), requestMap.get("channelId").toString()));
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

	@RequestMapping(value = "/doUpdateRemoveCategoryModel")
	public void doUpdateRemoveCategoryModel(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.put("modifier", getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = (modelEditService.doUpdateRemoveCategoryModel(requestMap) > 0);
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

	@RequestMapping(value = "/doSearchCategoryByModel")
	public void doSearchCategoryByModel(HttpServletResponse response, @RequestBody Map<String, Object> requestMap){
		AjaxResponseBean responseBean = new AjaxResponseBean();
		Map<String, Object> resMap = new HashMap<>();
		List<Map<String, Object>> result = modelEditService.doSearchCategoryByModel(requestMap);
		resMap.put("categoryList", result);
		responseBean.setResultInfo(resMap).setResult(true).writeTo(request, response);
	}

	@RequestMapping(value = "/doChangeModel")
	public void doChangeModel(HttpServletResponse response, @RequestBody Map<String, Object> requestMap){
		AjaxResponseBean responseBean = new AjaxResponseBean();
		Map<String, Object> resMap = new HashMap<>();
		boolean sucFlg = modelEditService.doChangeModel(requestMap, getUser());
		resMap.put("successFlg", sucFlg);
		responseBean.setResultInfo(resMap).setResult(true).writeTo(request, response);
	}
}
