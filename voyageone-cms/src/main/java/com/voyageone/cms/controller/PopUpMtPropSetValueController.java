package com.voyageone.cms.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.voyageone.base.BaseController;
import com.voyageone.cms.formbean.PopUpMtPropSetValueBean;
import com.voyageone.cms.formbean.PopUpMtPropSetValueBean.Word;
import com.voyageone.cms.service.PopUpMtPropSetValueService;
import com.voyageone.core.ajax.AjaxResponseBean;

@Controller
@RequestMapping("/cms/setMasterPropValue/popUp")
public class PopUpMtPropSetValueController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(PopUpMtPropSetValueController.class);

	@Resource(name = "propertyPopUpService")
	private PopUpMtPropSetValueService service;

	@RequestMapping(value = "/serialize", method = RequestMethod.POST)
	public void doSerialize(HttpServletRequest request,HttpServletResponse response, @RequestBody PopUpMtPropSetValueBean formData) {
		
		List<Word> words = formData.getWords();

		Object responseObject = this.service.doSerialize(words);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);
	}

	@RequestMapping(value = "/deserialize", method = RequestMethod.POST)
	public void doDeserialize(HttpServletRequest request,HttpServletResponse response, @RequestBody PopUpMtPropSetValueBean formData) {

		String wordValue = formData.getTxtValue();
		String channelId = formData.getChannelId();
		
		Object responseObject = this.service.doDeserialize(wordValue,channelId);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);
	}

	@RequestMapping(value = "/getComplexValues", method = RequestMethod.POST)
	public void getComplexValues(HttpServletRequest request,HttpServletResponse response, @RequestBody PopUpMtPropSetValueBean formData) {
		
		int wordType = formData.getWordType();
		
		String channelId = formData.getChannelId();

		Object responseObject = this.service.doGetComplexValues(wordType,channelId);

		// 设置返回画面的值
		AjaxResponseBean.newResult(true).setResultInfo(responseObject).writeTo(super.getRequest(), response);

	}

}
