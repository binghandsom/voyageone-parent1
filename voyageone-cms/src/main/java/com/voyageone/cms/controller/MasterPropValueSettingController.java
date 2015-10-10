package com.voyageone.cms.controller;

import java.io.IOException;

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
import com.voyageone.core.modelbean.UserSessionBean;

@Controller
@RequestMapping("/cms/masterPropValue/setting")
public class MasterPropValueSettingController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterPropValueSettingController.class);

	@Resource(name = "propertyService")
	private MasterPropValueSettingService propertyService;

	/**
	 * 初始化页面.
	 * @param response
	 * @param formData
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	@RequestMapping(value = "/init", method = RequestMethod.POST)
	public void doInit(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) throws NumberFormatException, IOException {

		Object responseObject = this.propertyService.init(formData);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);
	}

	/**
	 * 检索主类目.
	 * @param response
	 * @param formData
	 * @throws IOException
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public void doSearch(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) throws IOException {

		Object responseObject = this.propertyService.search(formData);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);
	}

	/**
	 * 保存类目属性值.
	 * @param response
	 * @param formData
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public void doSubmit(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) {

		UserSessionBean userSession = getUser();
		
		Object responseObject = this.propertyService.submit(formData,userSession);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);

	}
	
	/**
	 * 获取类目导航.
	 * @param response
	 *
	 */
	@RequestMapping(value = "/getCategoryNav", method = RequestMethod.POST)
	public void doGetCategoryNav(HttpServletResponse response) {

		Object responseObject = this.propertyService.getCategoryNav(getUser());

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);

	}
	
	/**
	 * 切换类目.
	 * @param response
	 * @param formData
	 */
	@RequestMapping(value = "/switchCategory", method = RequestMethod.POST)
	public void switchCategory(HttpServletResponse response, @RequestBody MasterPropertyFormBean formData) {

		UserSessionBean userSession = getUser();
		
		boolean result = this.propertyService.switchCagetgory(userSession.getSelChannel(), Integer.valueOf(formData.getLevel()), formData.getLevelValue(),userSession);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(true).writeTo(super.getRequest(), response);

	}

}
