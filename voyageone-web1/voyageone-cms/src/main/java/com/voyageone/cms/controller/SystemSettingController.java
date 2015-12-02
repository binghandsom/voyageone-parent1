package com.voyageone.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.formbean.CategoryProductCNBean;
import com.voyageone.cms.formbean.PageParamBean;
import com.voyageone.cms.modelbean.MtType;
import com.voyageone.cms.service.SystemSettingService;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;


@Controller
@RequestMapping(value=UrlConstants.URL_CMS_EDIT_SYSTEMSETTING)
public class SystemSettingController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(SystemSettingController.class);
	
    @Resource
    private SystemSettingService systemSettingService;
    
	@RequestMapping(value = "/doGetMtTypeList")
	public void doGetCNProductList(HttpServletRequest request, HttpServletResponse response, @RequestBody DtRequest<Map<String,Object>> dtRequest) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			
			// 输入参数出力
			logger.info("dtRequest: "+ JsonUtil.getJsonString(dtRequest));

			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("offset", dtRequest.getStart());
			data.put("pageCount", dtRequest.getLength());
			DtResponse<List<MtType>> dtResponse = new DtResponse<List<MtType>>();
			dtResponse.setData(systemSettingService.getCmsMtType(data));
			dtResponse.setDraw(dtRequest.getDraw());
			int count = systemSettingService.getMtTypeCount();
			dtResponse.setRecordsFiltered(count);
			dtResponse.setRecordsTotal(count);
			isSuccess = true;
			responseBean.setResultInfo(dtResponse);
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
