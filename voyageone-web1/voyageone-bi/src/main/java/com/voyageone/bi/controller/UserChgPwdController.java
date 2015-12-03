package com.voyageone.bi.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.bean.UserMenuBean;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.disbean.MemberChangePasswordDisBean;
import com.voyageone.bi.task.UserLoginTask;
//import com.voyageone.bi.task.MemberLoginTask;
import com.voyageone.bi.tranbean.UserInfoBean;
//import com.voyageone.bigdata.commonutils.TTContants;

@Controller
@SessionAttributes
public class UserChgPwdController {
	
	@Autowired
	private UserLoginTask memberLoginTask;
    
    @RequestMapping(value = "/manage/changePassword")
    public String changePassword(HttpServletRequest request, @ModelAttribute("password")MemberChangePasswordDisBean bean) {
    	return "/manage/user/changePassword";
    }
    
    @RequestMapping(value = "manage/doChangePwd")
    public String doChangePwd(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap map, @ModelAttribute("password")MemberChangePasswordDisBean bean, BindingResult result) throws BiException {  
    	
    	HttpSession session = request.getSession();
		if (session == null) {
			return "manage/login";
		}
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		if (user == null) {
			return "manage/login";
		}
    	
    	if (!memberLoginTask.changePassword(user,bean,result)){
    		//返回本页面
    		return "/manage/user/changePassword";
    	}
    	
    	
    	String homeUrl = "/manage/goSalesHome.html";
    	UserInfoBean userinfo = (UserInfoBean)request.getSession().getAttribute(SessionKey.LOGIN_INFO);
    	Collection<List<UserMenuBean>> menus = userinfo.getUserMenuMap().values();
    	if (menus != null && menus.size()>0) {
    		List<UserMenuBean> supMenus = menus.iterator().next();
    		if (supMenus != null && supMenus.size()>0) {
    			UserMenuBean menuBean = supMenus.get(0);
    			homeUrl = menuBean.getLink();
    		}
    	}
    	return "redirect:"+homeUrl;
    	
    }     

}