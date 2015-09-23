package com.voyageone.cms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.voyageone.base.BaseController;
import com.voyageone.cms.formbean.MasterPropertyFormBean;
import com.voyageone.cms.service.MasterPropValueSettingService;
import com.voyageone.core.ajax.AjaxResponseBean;

@Controller
@RequestMapping("/cms/masterPropValue/setting")
public class MasterPropValueSettingController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterPropValueSettingController.class);

	@Resource(name = "propertyService")
	private MasterPropValueSettingService propertyService;

	@RequestMapping(value = "/init", method = RequestMethod.POST)
	public void doInit(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) {

		Object responseObject = this.propertyService.init(formData);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public void doSearch(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) {

		Object responseObject = this.propertyService.search(formData);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public void doSubmit(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) {

		Object responseObject = this.propertyService.submit(formData);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);

	}

}
