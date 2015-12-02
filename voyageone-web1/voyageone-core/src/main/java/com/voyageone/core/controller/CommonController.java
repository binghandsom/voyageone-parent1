package com.voyageone.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.formbean.InFormCommonGetCode;
import com.voyageone.core.formbean.InFormCommonGetCodeItem;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.MasterInfoService;

/**
 * OMS 共通请求
 * 
 * @author jerry
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_COMMON)
public class CommonController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(CommonController.class);

	@Autowired
	private MasterInfoService MasterInfoService;


	@RequestMapping(value = "/doGetCode")
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
		
		// 选择的语言
		String lang = (String) request.getSession(false).getAttribute(CoreConstants.VOYAGEONE_USER_LANG);

		for (InFormCommonGetCodeItem aTypeList : typeList) {
			// masterInfoList
//			List<MasterInfoBean> masterInfoList = MasterInfoService.getMasterInfoFromId(Integer.valueOf(aTypeList.getId()), aTypeList.isShowBlank());
			List<MasterInfoBean> masterInfoList = Type.getMasterInfoFromId(aTypeList.getId(), aTypeList.isShowBlank(), lang);
			// 	masterInfoList
			ordersListMap.put(typeTitle + aTypeList.getId(), masterInfoList);
		}		
			
		//		正常
		result.setResult(true);
		
		result.setResultInfo(ordersListMap);
		
		// 结果返回输出流
		result.writeTo(request, response);
		
		// 输出结果出力
		logger.info(result.toString());

	}
}
