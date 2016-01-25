package com.voyageone.bi.filter;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.tranbean.UserInfoBean;
 

public class MemberInterceptor implements HandlerInterceptor {
	protected final Log logger = LogFactory.getLog(getClass());
	
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
            Exception arg3) throws Exception {
    }
 
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
            ModelAndView arg3) throws Exception {
    }
 
    /*
     * (non-Javadoc)
     * 拦截mvc.xml配置的/manage/**路径的请求
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        //请求的路径
        String contextPath = request.getContextPath();
        String  url = request.getRequestURL().toString();
        String loginUrl = "/manage/login.html";
		logger.info(url);
		
		//取得用户信息		
		HttpSession session = request.getSession();
		UserInfoBean user = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		
        //根据情况做对应处理
		if (url.indexOf("/manage/dologin.html") >= 0 || url.indexOf("/manage/login.html") >= 0) {
			return true;
		} else if (user == null || user.getUid()== null || user.getUid().trim().length()<1){
			logger.error("用户未登录!");
			//如果是Ajax请求响应头会有，x-requested-with  
			if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
				//response.setCharacterEncoding("UTF-8");
				response.setHeader("sessionstatus", "timeout");//在响应头设置session状态
				try {
					HashMap<String,String> res = new HashMap<String,String>();
					res.put("result", "session_timeout");
					PrintWriter out = response.getWriter();
					String output = new Gson().toJson(res);
					out.print(output);
					out.close();
				} catch (Exception e) {
					logger.error(e.getStackTrace());
				}
			} else {
				//非Ajax请求 直接跳转到登录页面
	            response.sendRedirect(contextPath+loginUrl);
			}
			return false;
		} else {
			return true;
		}
    }
}
