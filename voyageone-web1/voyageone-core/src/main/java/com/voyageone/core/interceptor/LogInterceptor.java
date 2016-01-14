package com.voyageone.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
 
/**
 * log处理拦截器
 * 
 * @author jacky
 */
public class LogInterceptor implements HandlerInterceptor {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object arg2, Exception arg3) throws Exception {
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView arg3) throws Exception {
		
			logger.info(request.getServletPath() + " is end.");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		
		logger.info(request.getServletPath() + " is start.");
		
		return true;
	}

}
