package com.voyageone.core.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.service.ManagerService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_MANAGER_BATCHJOB)
public class ManagerBatchController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MenuController.class);
	
	@Autowired
	private ManagerService managerService;
	
	@RequestMapping(value = "/doGetBatchJobList")
	public void getBatchJobList(HttpServletRequest request, HttpServletResponse response) {
		
		
		List<Map<String, Object>> data = managerService.getBatchJobList();

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
	
	@RequestMapping(value = "/doUpdateJobRun")
	public void doUpdateJobRun(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		int ret=0;
		try{
			ret = managerService.updateJobRun(requestMap);
		}catch(Exception e){
			
		}

		AjaxResponseBean responseBean = new AjaxResponseBean();

		// 设置返回结果
		responseBean.setResult(ret > 0);
		// 结果返回输出流
		responseBean.writeTo(request, response);
		// 输出结果出力
		logger.info(responseBean.toString());
		
	}
	
	@RequestMapping(value = "/doInsertBatchJob")
	public void doInsertBatchJob(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> requestMap) {
		
		int ret =  managerService.insertBatchJob(requestMap);
		AjaxResponseBean responseBean = new AjaxResponseBean();

		// 设置返回结果
		responseBean.setResult(ret > 0);
		// 新的记录传回
		responseBean.setResultInfo(requestMap);
		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 结果返回输出流
		// 输出结果出力
		logger.info(responseBean.toString());
		
	}
}
