package com.voyageone.core.ajax;

import com.google.gson.GsonBuilder;
import com.voyageone.base.exception.SystemException;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.utilbean.FormValidateBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于接收 Ajax 请求中的数据Bean
 * 
 * @author jacky
 *
 */
public abstract class AjaxRequestBean {
	
	protected static Log logger = LogFactory.getLog(AjaxRequestBean.class);
	
	protected abstract String[] getValidateSorts();
	
	private AjaxResponseBean result = new AjaxResponseBean();
	
	/**
	 * 当前页码
	 */
	private int page;
	
	/**
	 * 每页显示行
	 */
	private int rows;
	
	/**
	 * 当前页起始行
	 */
	private int offset;

	@SuppressWarnings("unchecked")
	public <T extends AjaxResponseBean> T getResponseBean() {
//		if (result == null) {
//			result = initResponseBean();
//		}
		
		return (T) result;
	}
	
	/**
	 * 生成一个返回实例
	 */
	protected AjaxResponseBean initResponseBean() {
		return new AjaxResponseBean();
	};
	
	/**
	 * 检查从请求中获取的参数的校验是否正确
	 * 
	 * @param bean
	 * @param bindingResult
	 * @return
	 */
	public boolean hasInputError(AjaxRequestBean bean, BindingResult bindingResult) {
		// 是否有校验错误
		boolean isError = bindingResult.hasErrors();
		// 校验不通过
		if (isError) {
			setInputErrorInfo(bean.getValidateSorts(), bindingResult);
		}
		return isError;
	}

	/**
	 * 校验不通过时获得校验的详细信息及设置相应返回值
	 */
	private void setInputErrorInfo(String[] fieldSorts, BindingResult bindingResult) {
		getResponseBean().setResult(false, MessageConstants.MESSAGE_CODE_100001, 
				MessageConstants.MESSAGE_TYPE_VALIDATE);
		
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			if (fieldSorts != null && fieldSorts.length > 0) {
				List<FormValidateBean> listForm = new ArrayList<FormValidateBean>();
				for (String filedSort : fieldSorts) {
					for (FieldError field : fieldErrors) {
						if (field.getField().equals(filedSort)) {
							FormValidateBean form = new FormValidateBean();
							// 字段名
							form.setField(field.getField());
							// 错误消息
							form.setMessage(field.getDefaultMessage());
							
							listForm.add(form);
						}
					}
				}
				getResponseBean().setFormValidateList(listForm);
			}
		}
		
	}
	
	/**
	 * 返回输出流
	 * 
	 * @param request
	 * @param response
	 * @throws SystemException
	 */
	public void writeTo(HttpServletRequest request, HttpServletResponse response) throws SystemException {
		getResponseBean().writeTo(request, response);
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	}
	
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
