package com.voyageone.bi.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.bi.ajax.bean.AjaxSalesHomeBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.SalesHomeTask;
import com.voyageone.bi.tranbean.UserInfoBean;

@Controller  
public class SalesHomeController {
	
	private static Log logger = LogFactory.getLog(SalesHomeController.class);
	
	// 页面初期化
	@Autowired
	private SalesHomeTask salesHomeTask;	
	
	
	// 销售主页面
    @RequestMapping(value = "/manage/goSalesHome")
    public String goShipmentList(HttpServletRequest request,
    						Map<String, Object> map) throws BiException {
    	HttpSession session = request.getSession();
    	session.setAttribute("myArray", new ArrayList<String>());
        return "manage/saleshome/main";    	
    }
    
    /**
     * checkSalesHomeParam
     * @param response
     * @param request
     * @param bean
     * @throws BiException
     */
	@RequestMapping(value = "/manage/checkSalesHomeParam")
	public void doCheckSalesHomeParam(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesHomeBean bean) throws BiException {
		if (!bean.checkInput()) {
			bean.WriteTo(response);
		} else {
			AjaxResponseBean result = bean.getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_OK);
			bean.WriteTo(response);
		}
		return;
	}
	
	/**
	 * 昨天主要KPI数据取得
	 */
	@RequestMapping(value = "/manage/getSalesHomeYPKData")
	public void getSalesHomeYPKData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesHomeBean bean) throws BiException {
		logger.info("昨天主要KPI数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesHomeTask.ajaxSalesHomeYPKData(bean, user);
		bean.WriteTo(response);
	}
	
	/**
	 * TimeLine数据取得
	 */
	@RequestMapping(value = "/manage/getSalesHomeTimelineData")
	public void getSalesTimeLineData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesHomeBean bean) throws BiException {
		logger.info("TimeLine数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		salesHomeTask.ajaxGetSalesTimeLineData(bean, user);
		bean.WriteTo(response);
	}

	
	/**
	 * CompareData数据取得
	 */
	@RequestMapping(value = "/manage/getSalesHomeCompareData")
	public void getSalesCompareData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesHomeBean bean) throws BiException {
		logger.info("CompareData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesHomeTask.ajaxSalesHomeCompareData(bean, user);
		bean.WriteTo(response);
	}
	

	
	/**
	 * getSalesHomeBrandData数据取得
	 */
	@RequestMapping(value = "/manage/getSalesHomeBrandData")
	public void getSalesHomeBrandData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesHomeBean bean) throws BiException {
		logger.info("getSalesHomeBrandData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		
		salesHomeTask.ajaxGetSalesBrandData(bean, user);
		
		bean.WriteTo(response);
	}
	
	/**
	 * getSalesHomeCategoryData数据取得
	 */
	@RequestMapping(value = "/manage/getSalesHomeCategoryData")
	public void getSalesHomeCategoryData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesHomeBean bean) throws BiException {
		logger.info("getSalesHomeCategoryData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		
		salesHomeTask.ajaxGetSalesCategoryData(bean, user);
		
		bean.WriteTo(response);
	}
	
	/**
	 * getSalesHomeProductData数据取得
	 */
	@RequestMapping(value = "/manage/getSalesHomeProductData")
	public void getSalesBrandCategoryData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesHomeBean bean) throws BiException {
		logger.info("getSalesHomeProductData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		
		salesHomeTask.ajaxGetSalesProductData(bean, user);
		
		bean.WriteTo(response);
	}

}
