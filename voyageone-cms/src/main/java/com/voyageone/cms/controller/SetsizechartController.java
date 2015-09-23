package com.voyageone.cms.controller;

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

import com.voyageone.base.BaseController;
import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.formbean.BindSizeChartBean;
import com.voyageone.cms.formbean.ModelCNBaseBean;
import com.voyageone.cms.formbean.SizeChartInfo;
import com.voyageone.cms.formbean.SizeChartModelBean;
import com.voyageone.cms.modelbean.BindSizeChart;
import com.voyageone.cms.modelbean.CnCategoryExtend;
import com.voyageone.cms.modelbean.SizeChart;
import com.voyageone.cms.modelbean.SizeChartDetail;
import com.voyageone.cms.service.CategoryEditService;
import com.voyageone.cms.service.ModelEditService;
import com.voyageone.cms.service.SetsizechartService;
import com.voyageone.common.Constants;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CMS_EDIT_SETSIZECHART)
public class SetsizechartController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(SetsizechartController.class);

	@Autowired
	private SetsizechartService setsizechartService;
	@Autowired
	private CategoryEditService categoryEditService;
	@Autowired
	private ModelEditService modelEditService;

	@RequestMapping(value = "/doGetBindedSizeChartList")
	public void doGetBindedSizeChartList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.getJsonString(requestMap));

			List<SizeChartInfo> ret = null;

			ret = setsizechartService.doGetBindedSizeChartList(requestMap);
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

	@RequestMapping(value = "/doGetOtherSizeChartList")
	public void doGetOtherSizeChartList(HttpServletResponse response, @RequestBody Map<String, String> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.getJsonString(requestMap));
			List<SizeChartInfo> ret = null;
			ret = setsizechartService.doGetOtherSizeChartList(requestMap);
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
	@RequestMapping(value = "/doGetBindSizeChart")
	public void doGetBindSizeChart(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.getJsonString(requestMap));
			List<BindSizeChartBean> ret = null;
			ret = setsizechartService.doGetBindSizeChart(requestMap);
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

	@RequestMapping(value = "/doGetSizeChartDetailInfo")
	public void doGetSizeChartDetailInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.getJsonString(requestMap));
			SizeChartInfo ret = null;
			ret = setsizechartService.doGetSizeChartDetailInfo(requestMap);
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

	@RequestMapping(value = "/doSaveSizeChart")
	public void doSaveSizeChart(HttpServletResponse response, @RequestBody SizeChart sizeChart) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("sizeChart: " + JsonUtil.getJsonString(sizeChart));
			isSuccess = setsizechartService.insertSizeChart(sizeChart);
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

	@RequestMapping(value = "/doSaveSizeChartDetail")
	public void doSaveSizeChartDetail(HttpServletResponse response, @RequestBody Map<String, Object> data) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("data: " + JsonUtil.getJsonString(data));
			String id = setsizechartService.insertSizeChartAndSizeChartDetail(data, getUser());
			responseBean.setResultInfo(id);
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

	@RequestMapping(value = "/doSaveBindSizeChart")
	public void doSaveBindSizeChart(HttpServletResponse response, @RequestBody BindSizeChart bindSizeChart) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("bindSizeChart: " + JsonUtil.getJsonString(bindSizeChart));
			isSuccess = setsizechartService.insertBindSizeChart(bindSizeChart);
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

	@RequestMapping(value = "/doUpdateSizeChartDetail")
	public void doUpdateSizeChartDetail(HttpServletResponse response, @RequestBody Map<String, Object> data) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("data: " + JsonUtil.getJsonString(data));
			isSuccess = setsizechartService.updateSizeChartAndSizeChartDetail(data, getUser());
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

	@RequestMapping(value = "/doSaveCategorySizeChart")
	public void doSaveCategorySizeChart(HttpServletResponse response, @RequestBody CnCategoryExtend cnCategoryExtend) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("cnCategoryExtend: " + JsonUtil.getJsonString(cnCategoryExtend));
			isSuccess = categoryEditService.updateCnCategoryExtend(cnCategoryExtend, getUser());
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
	@RequestMapping(value = "/doSaveModelSizeChart")
	public void doSaveModelSizeChart(HttpServletResponse response, @RequestBody Map<String,Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("requestMap: " + JsonUtil.getJsonString(requestMap));
			isSuccess = setsizechartService.updateModelSizeChart(requestMap, getUser());
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
	@RequestMapping(value = "/doGetSizeChartModel")
	public void doGetSizeChartModel(HttpServletResponse response, @RequestBody String channelId) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("channelId :"  +channelId);
			List<SizeChartModelBean> ret = setsizechartService.getSizeChartModelBean(channelId);
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
}
