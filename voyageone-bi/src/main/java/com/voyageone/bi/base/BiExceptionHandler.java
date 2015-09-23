package com.voyageone.bi.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class BiExceptionHandler implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
//    	String contextPath = request.getContextPath();
        String errUrl = "/jsp/common/error/error-500";
        
		Map<String, Object> model = new HashMap<String, Object>();
		String msg = BiApplication.getMsg(BiApplication.LOCAL_EN, ex.getMessage());
		model.put("ex", msg);

		// 根据不同错误转向不同页面
		if(ex instanceof BiException) {
			return new ModelAndView("forward:" + errUrl, model);
		}else {
			return new ModelAndView("forward:" + errUrl, model);
		}
	}
}

