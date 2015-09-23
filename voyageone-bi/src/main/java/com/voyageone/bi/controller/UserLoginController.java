package com.voyageone.bi.controller;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.bean.UserMenuBean;
import com.voyageone.bi.commonutils.CookieKey;
import com.voyageone.bi.commonutils.SessionKey;
//import com.voyageone.bigdata.commonutils.TTContants;
import com.voyageone.bi.disbean.MemberLoginDisBean;
import com.voyageone.bi.task.UserLoginTask;
//import com.voyageone.bi.task.MemberLoginTask;
import com.voyageone.bi.tranbean.UserInfoBean;

@Controller
@SessionAttributes
public class UserLoginController {
	
	@Autowired
	private UserLoginTask memberLoginTask;

    @RequestMapping(value = "index")
    public String goManageLogin2() {
    	return "redirect:manage/login.html";
    }

    @RequestMapping(value = "/manage/login")
    public String goLogin(HttpServletRequest request,
    		@ModelAttribute("user")MemberLoginDisBean bean) {
       
    	//强行设置 使用英语环境
    	Locale loc = new Locale("en");
    	request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, loc);
    	
    	Cookie[]cookies=request.getCookies();
    	int meet = 0;
    	if (cookies != null && cookies.length > 0 ){
        	for(Cookie c:cookies){
        		if(c.getName().equals(CookieKey.LOGIN_UID)){
        			bean.setUsername(c.getValue());
        			meet++;
        			continue;
        		}
        		if(c.getName().equals(CookieKey.REMEMBER_ME)){
        			bean.setIsRemember(c.getValue());
        			meet++;
        			continue;
        		}
        		if (meet == 2){
        			break;
        		}
        		
        	}
    	}

        return "manage/login";
    }
    
    @RequestMapping(value = "manage/dologin")
    public String doLogin(HttpServletRequest request,
    		HttpServletResponse response,
    		ModelMap map,
    		@ModelAttribute("user")MemberLoginDisBean bean,
    						BindingResult result) throws BiException {  
    	
    	Locale loc = new Locale("en");
    	request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, loc);
    	
    	if (!memberLoginTask.doLogin(request,response,bean,result)){
    		//返回本页面
    		return "manage/login";
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
//    	return "redirect:/manage/goSalesDetail.html";
    	
    }     
 
    @RequestMapping(value = "/manage/")
    public String goManageLogin1() {
    	return "redirect:manage/login.html";
    }
    

    
    @RequestMapping(value = "/manage/logout")
    public String doLogout(HttpServletRequest request,
    		HttpServletResponse response,
    		ModelMap map) {  
    	//liang
    	//memberLoginTask.doLogout(request, response);
    		
    	return "redirect:/manage/login.html";
                                                             
    }
}