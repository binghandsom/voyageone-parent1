package com.voyageone.bi.base;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用于接收 Ajax 请求中的数据Bean
 * 
 * @author Dell
 *
 */
public abstract class AjaxRequestBean {
	
	private AjaxResponseBean result;

	protected static Log logger = LogFactory.getLog(AjaxRequestBean.class);
	
	@SuppressWarnings("unchecked")
	public <T extends AjaxResponseBean> T getResponseBean() {
		if (result == null)
			result = initResponseBean();
		
		return (T) result;
	}
	
	/**
	 * 生成一个返回实例
	 */
	protected abstract AjaxResponseBean initResponseBean();

	/**
	 * 检查从请求中获取的参数是否正确
	 */
	public abstract boolean checkInput();
	
	public void WriteTo(HttpServletResponse response) throws BiException{
		getResponseBean().WriteTo(response);
	}

}
