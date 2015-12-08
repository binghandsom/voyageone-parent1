package com.voyageone.cms.controller;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jd.open.api.sdk.internal.util.JsonUtil;
import com.voyageone.base.BaseController;
import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.formbean.ModelProductCNBean;
import com.voyageone.cms.service.SearchService;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;


@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CMS_SEARCH)
public class SearchController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(SearchController.class);

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/complex/doSearch")
	public void doSearch(HttpServletRequest request, HttpServletResponse response, @RequestBody DtRequest<HashMap<String, Object>> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.toJson(requestMap));

			requestMap.getParam().put("start", requestMap.getStart());
			requestMap.getParam().put("length", requestMap.getLength());

			DtResponse<List<Map<String, Object>>> ret = new DtResponse<List<Map<String, Object>>>();
			ret.setData(searchService.doSearch(requestMap.getParam()));
			ret.setDraw(requestMap.getDraw());
			ret.setRecordsTotal(searchService.doSearchCount(requestMap.getParam()));
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

	@RequestMapping(value = "/advance/doSearch")
	public void doAdvanceSearch(HttpServletRequest request, HttpServletResponse response, @RequestBody DtRequest<HashMap<String, Object>> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.toJson(requestMap));

			requestMap.getParam().put("start", requestMap.getStart());
			requestMap.getParam().put("length", requestMap.getLength());

			DtResponse<List<Map<String, Object>>> ret = new DtResponse<List<Map<String, Object>>>();
			ret.setData(searchService.doAdvanceSearch(requestMap.getParam()));
			ret.setDraw(requestMap.getDraw());
			ret.setRecordsTotal(searchService.doAdvanceSearchCount(requestMap.getParam()));
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

	@RequestMapping(value = "/advance/doExport")
	public ResponseEntity<byte[]> doExport(HttpServletRequest request, HttpServletResponse response, @RequestParam("search") String  requestMap)
			throws IOException {
		// 输入参数出力
		logger.info(JsonUtil.toJson(requestMap));
		Map<String,Object> parameter = com.voyageone.common.util.JsonUtil.jsonToMap(requestMap);
		parameter.put("channelId", getUser().getSelChannel());
	    String  name = String.format(CmsConstants.Format.CSVNAME, DateTimeUtil.getLocalTime(getUserTimeZone()).substring(0, 10));
		byte[] data = searchService.doExport(parameter);
		return genResponseEntityFromBytes(name+".csv", data);

	}

	@RequestMapping(value = "/quick/search")
	public void doQuickSearch(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap ) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info(JsonUtil.toJson(requestMap));
//			String channelId = requestMap.get("channelId").toString();
//			if(StringUtils.isEmpty(channelId)){
			String	channelId = getUser().getSelChannel();
//			}
			responseBean.setResultInfo(searchService.doQuickSearch(channelId));
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
