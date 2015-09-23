package com.voyageone.bi.task;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.bean.UserMenuBean;
import com.voyageone.bi.commonutils.CookieKey;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.dao.UserInfoDao;
import com.voyageone.bi.disbean.MemberChangePasswordDisBean;
import com.voyageone.bi.disbean.MemberLoginDisBean;
import com.voyageone.bi.tranbean.UserInfoBean;
import com.voyageone.bi.tranbean.UserShopBean;

@Scope("prototype")
@Service
public class UserLoginTask {
	private static Log logger = LogFactory.getLog(UserLoginTask.class);
	
	@Autowired
	private UserInfoDao userInfoDao;
	
	
	//用户login
	public boolean doLogin(HttpServletRequest request,
									HttpServletResponse response,
									MemberLoginDisBean bean,
									BindingResult result) throws BiException{
		
		boolean ret = false;
		
		logger.info("doLogin start!");		
		try{
	    	//cookie
			response.addCookie(new Cookie(CookieKey.REMEMBER_ME, bean.getIsRemember()));
	    	if (bean.getIsRemember() !=null && "0".equals(bean.getIsRemember())){
	    		//把用户名写入cookie
	    		response.addCookie(new Cookie(CookieKey.LOGIN_UID, bean.getUsername()));
	    	}else{
	    		response.addCookie(new Cookie(CookieKey.LOGIN_UID, ""));
	    	}
	    	
			UserInfoBean userInfo = userInfoDao.selectUserById(bean);
			if (userInfo.getUid() != null && !"".equals(userInfo.getUid())){
				//设置 门店
				List<UserShopBean> userShopList = userInfoDao.selectUserShopIdById(userInfo.getUid());
				userInfo.setUserShopList(userShopList);
				//Channel DB
				List<String> channelDBList =userInfoDao.selectUserChannelDBByUserId(userInfo.getUid());
				userInfo.setUserChannelDBList(channelDBList);
				//menu
				Map<String, List<UserMenuBean>> userMenu =userInfoDao.selectUserMenuByUserId(userInfo.getUid());
				userInfo.setUserMenuMap(userMenu);
				//session赋值
				request.getSession().setAttribute(SessionKey.LOGIN_INFO, userInfo);
				ret = true;
			}else{
				//用户不存在时设置错误信息
				result.rejectValue("password", "check.user.notexist");
				ret = false;
			}
		}catch (Exception e) {
			throw new BiException(e,"MemberLoginTask.doLogin");
		}	
		
		logger.info("doLogin end!");
		return ret;
	}
	
	//用户logout
	public void doLogout(HttpServletRequest request,
									HttpServletResponse response){		
		
		logger.info("doLogout start!");		
		//session清空
		request.getSession().removeAttribute(SessionKey.LOGIN_INFO);
		
		logger.info("doLogout end!");
	}
	
	//用户login
	public boolean changePassword(UserInfoBean userInfoBean,
									MemberChangePasswordDisBean bean,
									BindingResult result) throws BiException{
		
		boolean ret = false;
		
		logger.info("changePassword start!");
		
		try{
			if (bean.getOrgPassword() == null || "".equals(bean.getOrgPassword().trim())) {
				result.rejectValue("orgPassword", "Null.user.password");
			} else if (bean.getNewPassword1() == null || "".equals(bean.getNewPassword1().trim())) {
				result.rejectValue("newPassword1", "Null.user.password");
			} else if (bean.getNewPassword2() == null || "".equals(bean.getNewPassword2().trim())) {
				result.rejectValue("newPassword2", "Null.user.password");
			} else if (!bean.getNewPassword1().equals(bean.getNewPassword2().trim())) {
				result.rejectValue("newPassword2", "check.password.incorrect.try");
			} else {
				if (bean.getNewPassword1().length()<6 || bean.getNewPassword1().length()>10) {
					result.rejectValue("newPassword1", "check.user.password.length");
				} else {
					UserInfoBean userInfo = userInfoDao.selectUserById(userInfoBean.getUid(), bean.getOrgPassword());
					if (userInfo.getUid() == null || "".equals(userInfo.getUid())){
						result.rejectValue("orgPassword", "check.password.incorrect");
					} else {
						userInfoDao.updateUserPassword(userInfoBean.getUid(), bean.getNewPassword1());
						ret = true;
					}
				}
			}
		}catch (Exception e) {
			throw new BiException(e,"MemberLoginTask.doLogin");
		}	
		
		logger.info("changePassword end!");
		return ret;
	}

}
