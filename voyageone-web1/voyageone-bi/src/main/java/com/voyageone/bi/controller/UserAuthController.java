package com.voyageone.bi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.voyageone.bi.ajax.bean.AjaxUserInfoBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.UserInfoTask;
import com.voyageone.bi.tranbean.UserInfoBean;

@Controller
@SessionAttributes
public class UserAuthController {
	private static Log logger = LogFactory.getLog(UserAuthController.class);
	
	@Autowired
	private UserInfoTask userInfoTask;

	/**
	 * getUserShopList
	 */
	@RequestMapping(value = "/manage/getUserShopList")
	public void getUserShopList(HttpServletResponse response,
			HttpServletRequest request,
			AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserShopList数据取得");
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserShopList(bean, user);
		bean.WriteTo(response);
	}

	/**
	 * getUserShopList
	 */
	@RequestMapping(value = "/manage/getUserChannelList")
	public void getUserChannelList(HttpServletResponse response,
								HttpServletRequest request,
								AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserChannelList");

		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserChannelList(bean, user);
		userInfoTask.ajaxGetUserPortList(bean, user);
		bean.WriteTo(response);
	}

	/**
	 * getUserChannelShopList
	 */
	@RequestMapping(value = "/manage/getUserChannelShopList")
	public void getUserChannelShopList(HttpServletResponse response,
								   HttpServletRequest request,
								   AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserChannelShopList");

		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserChannelShopList(bean, user);
		bean.WriteTo(response);
	}

	/**
	 * getUserCategoryList
	 */
	@RequestMapping(value = "/manage/getUserCategoryList")
	public void getUserCategoryList(HttpServletResponse response,
			HttpServletRequest request,
			AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserCategoryList数据取得");
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserCategoryList(bean, user);
		bean.WriteTo(response);
	}


	
	/**
	 * getUserBrandList
	 */
	@RequestMapping(value = "/manage/getUserBrandList")
	public void getUserBrandList(HttpServletResponse response,
			HttpServletRequest request,
			AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserBrandList数据取得");
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserBrandList(bean, user);
		bean.WriteTo(response);
	}
	
	/**
	 * getUserColorList
	 */
	@RequestMapping(value = "/manage/getUserColorList")
	public void getUserColorList(HttpServletResponse response,
			HttpServletRequest request,
			AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserColorList数据取得");
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserColorList(bean, user);
		bean.WriteTo(response);
	}
	
	/**
	 * getUserSizeList
	 */
	@RequestMapping(value = "/manage/getUserSizeList")
	public void getUserSizeList(HttpServletResponse response,
			HttpServletRequest request,
			AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserSizeList数据取得");
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserSizeList(bean, user);
		bean.WriteTo(response);
	}
    
	/**
	 * getUserSizeList
	 */
	@RequestMapping(value = "/manage/getUserProductList")
	public void getUserProductList(HttpServletResponse response,
			HttpServletRequest request,
			AjaxUserInfoBean bean) throws BiException {
		logger.info("getUserProductList数据取得");
		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		userInfoTask.ajaxGetUserProductList(bean, user);
		bean.WriteTo(response);
	}
    
}