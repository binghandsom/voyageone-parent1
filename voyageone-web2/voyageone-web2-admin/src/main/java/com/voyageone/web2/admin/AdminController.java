package com.voyageone.web2.admin;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.core.bean.UserSessionBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
public class AdminController extends BaseController {
	
	public static final String SUCCESS = "success";
	
	public static final String MESSAGE = "message";

	/**
	 * 目前没有权限验证，此方法只作测试用。
	 */
	@Override
	public UserSessionBean getUser() {
		UserSessionBean user = new UserSessionBean();
		user.setUserName("developer");
		return user;
	}

}
