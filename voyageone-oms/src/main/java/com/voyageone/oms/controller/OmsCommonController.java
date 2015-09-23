package com.voyageone.oms.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMethod;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.MasterInfoBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.formbean.InFormCommonGetCode;
import com.voyageone.oms.formbean.InFormCommonGetCodeItem;
import com.voyageone.oms.formbean.InFormServiceSearchSKU;
import com.voyageone.oms.formbean.OutFormServiceSearchSKU;
import com.voyageone.oms.service.OmsCommonService;
import com.voyageone.oms.service.OmsMasterInfoService;

/**
 * OMS 共通请求
 * 
 * @author jerry
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = OmsUrlConstants.URL_OMS_ORDERS_COMMON)
public class OmsCommonController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsCommonController.class);	

	@Autowired
	private OmsMasterInfoService omsMasterInfoService;
	@Autowired
	private OmsCommonService omsCommonService;
	
	/**
	 * Code取得
	 * 
	 * @param request
	 * @param response
	 * @param typeMap	例：typeIdList : '6,9'
	 */
//	@RequestMapping(value = "/doGetCode", method = RequestMethod.POST)
//	public void doSearch(HttpServletRequest request, HttpServletResponse response,
//			@RequestBody Map typeMap) {
//		
////		String[] typeList = ((String)typeMap.get("typeIdList")).split(",");
//		List<String> typeList = ((List<String>)typeMap.get("typeIdList"));
//		
//		// 从session中获得该用户的信息		
//		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
//		// ajax 返回结果
//		AjaxResponseBean result = new AjaxResponseBean();
//		// 设置返回结果
//		Map<String, Object> ordersListMap = new HashMap<String, Object>();		
//		
//		String typeTitle = "type";
//		for (int i = 0; i < typeList.size(); i++) {
//			// masterInfoList
//			List<MasterInfoBean> masterInfoList = omsMasterInfoService.getMasterInfoFromId(Integer.valueOf(typeList.get(i)));
//			
//			// 	masterInfoList
//			ordersListMap.put(typeTitle + typeList.get(i) , masterInfoList);
//		}		
//			
//		//		正常
//		result.setResult(true);
//		
//		result.setResultInfo(ordersListMap);
//		
//		// 结果返回输出流
//		result.writeTo(request, response);
//		
//		// 输出结果出力
//		logger.info(result.toString());
//		
//		return;
//	}
	@RequestMapping(value = "/doGetCode", method = RequestMethod.POST)
	public void doGetCode(HttpServletRequest request, HttpServletResponse response,
			@RequestBody InFormCommonGetCode inFormCommonGetCode) {
		
		// 从session中获得该用户的信息		
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();
		
		List<InFormCommonGetCodeItem> typeList = inFormCommonGetCode.getTypeIdList();
		String typeTitle = "type";
		for (int i = 0; i < typeList.size(); i++) {
			// masterInfoList
			List<MasterInfoBean> masterInfoList = omsMasterInfoService.getMasterInfoFromId(Integer.valueOf(typeList.get(i).getId()), typeList.get(i).isShowBlank());
			
			// 	masterInfoList
			ordersListMap.put(typeTitle + typeList.get(i).getId() , masterInfoList);
		}		
			
		//		正常
		result.setResult(true);
		
		result.setResultInfo(ordersListMap);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
	
	/**
	 * SKU检索
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 */
	@RequestMapping(value = "/doGetSKUInfo", method = RequestMethod.POST)
	public void doGetSKUInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody InFormServiceSearchSKU searchSKUReq) {
		
		// ajax 返回结果
		AjaxResponseBean result = new AjaxResponseBean();
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();
		
		// sku 信息取得
		List<OutFormServiceSearchSKU> skuInfoList = omsCommonService.getSKUList(searchSKUReq,OmsConstants.SKU_TYPE_ADDNEWORDER);		
		
		ordersListMap.put("skuInfoList", skuInfoList);
		
		//	正常
		result.setResult(true);		
		result.setResultInfo(ordersListMap);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());
		
		return;
	}
}
