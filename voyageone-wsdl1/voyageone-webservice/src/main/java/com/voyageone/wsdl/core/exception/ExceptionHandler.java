package com.voyageone.wsdl.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.voyageone.wsdl.core.Constants;
import com.voyageone.wsdl.core.modelbean.ExceptionLogBean;
import com.voyageone.wsdl.core.response.WsdlResponseBean;
import com.voyageone.wsdl.core.service.LogService;
import com.voyageone.wsdl.core.util.DateTimeUtil;

public class ExceptionHandler implements HandlerExceptionResolver {
	
	private static Log logger = LogFactory.getLog(ExceptionHandler.class);

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Exception exception;
	
	@Autowired
	private LogService logService;
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		
		this.request = request;
		this.response = response;
		this.exception = exception;
		
		try {
			// log4j打印出详细信息，包括堆栈信息
			logger.error(exception.getMessage(), exception);
			
			// 异常信息记录至数据库
			insertLogToDB();
			
			// 业务异常记错误日志及堆栈信息，迁移到共通错误页面
			if (exception instanceof BussinessException) {
				return catchBussinessException();
				
			// 系统异常记错误日志及堆栈信息，迁移到共通错误页面
			} else if (exception instanceof SystemException) {
				return catchSystemException();
			
			// 其他未知异常
			} else {
				return catchDefault();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			
			return catchDefault();
		}
	}
	
	/**
	 * 捕获业务异常
	 * @return
	 */
	private ModelAndView catchBussinessException() {
		BussinessException exception = (BussinessException) this.exception;
		
		// 尝试根据信息获取指定的错误提示
//		String msg = exception.getMessage().split(Constants.EXCEPTION_MESSAGE_PREFIX)[0];
		String msg = "发生业务异常";
		
		return bussinessExceptionDeal(msg);
	}
	
	/**
	 * 捕获系统异常处理
	 * 
	 * @return
	 */
	private ModelAndView catchSystemException() {
		SystemException exception = (SystemException) this.exception;
		
		// 尝试根据信息获取指定的错误提示
//		String msg = exception.getMessage().split(Constants.EXCEPTION_MESSAGE_PREFIX)[0];
		String msg = "发生系统异常处理";
		
		return exceptionDeal(msg);
	}
	
	/**
	 * 捕获其他异常处理
	 * 
	 * @return
	 */
	private ModelAndView catchDefault() {
		// 尝试根据信息获取指定的错误提示
//		String msg = exception.getMessage().split(Constants.EXCEPTION_MESSAGE_PREFIX)[0];
		String msg = "发生异常";
		
		return exceptionDeal(msg);
	}
	
	/**
	 * 业务异常时ajax返回处理
	 * 
	 * @param msg
	 * @return
	 */
	private ModelAndView bussinessExceptionDeal(String msg) {
		WsdlResponseBean bean = new WsdlResponseBean();
		bean.setResult(false, -1, msg);
		
		bean.writeTo(response);
		
		return null;
	}
	
	/**
	 * 业务以外异常时ajax返回处理
	 * 
	 * @param msg
	 * @return
	 */
	private ModelAndView exceptionDeal(String msg) {
		WsdlResponseBean bean = new WsdlResponseBean();
		bean.setResult(false, -1, msg);
		
		bean.writeTo(response);
		
		return null;
	}
	
	private void insertLogToDB() {
		// 异常发生时间
		String dateTime = DateTimeUtil.getStringDateTime(DateTimeUtil.DATE_TIME_FORMAT_1);
		
		// 请求URL
		String url = request.getServletPath();
		// 异常类型
		String type = exception.getClass().getName();
		// 堆栈信息
		//String stackInfo = getExceptionStack(exception);
		// 异常描述
		String message = exception.getMessage() == null ? 
				"": exception.getMessage().split(Constants.EXCEPTION_MESSAGE_PREFIX)[0];
		if (message != null && message.length() > Constants.EXCEPTION_MESSAGE_LENGTH) {
			message = message.substring(0, Constants.EXCEPTION_MESSAGE_LENGTH);
		}
		
		ExceptionLogBean exceptionBean = new ExceptionLogBean();
		exceptionBean.setDateTime(dateTime);
		exceptionBean.setExceptionType(type);
		exceptionBean.setDescription(message);
		exceptionBean.setUrl(url);
		exceptionBean.setCreated(dateTime.split("\\.")[0]);
		
		// 异常信息记录至数据库
		logService.insertExceptionLog(exceptionBean);
	}
	
	/**
	 * 获得异常堆栈信息
	 * 
	 * @param e
	 * @return
	 */
	private String getExceptionStack(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			e.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
}

