package com.voyageone.bi.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.StringUtils;

/**
 * 用于返回 Ajax 请求的结果数据
 * 
 * @author Jonas_Gao
 *
 */
public class AjaxResponseBean {
	
	private Object reqResult;
	
	private Object reqResultInfo;
	
	private String exception;
	
	public Object getReqResult() {
		return reqResult;
	}

	public void setReqResult(Object reqResult) {
		this.reqResult = reqResult;
	}

	public Object getReqResultInfo() {
		return reqResultInfo;
	}

	public void setReqResultInfo(Object reqResultInfo) {
		this.reqResultInfo = reqResultInfo;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	@Override
	public String toString(){
		return new Gson().toJson(this);
	}
	
	/**
     * 将内容写到Response的输出流中, 默认 UTF-8
     * @param response
     */
	public void WriteTo(HttpServletResponse response) throws BiException {
		WriteTo(response, "UTF-8");
	}
	
	/**
     * 将内容写到Response的输出流中
     */
	public void WriteTo(HttpServletResponse response, String encoding) throws BiException {
		if (!StringUtils.isEmpty(encoding))
			response.setCharacterEncoding(encoding);
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(toString());
			out.close();
		} catch (IOException e) {
			throw new BiException(e, "AjaxResponseBean.WriteTo");
		}
	}
	
	/**
	 * 帮助方法，用于设置reqResult的通用结果
	 * @param result
	 * 		返回的具体结果
	 * @param msg
	 * 		附带的相关信息
	 */
	public void setResult(boolean result, String msg){
		setReqResult(result ? Contants.AJAX_RESULT_OK : Contants.AJAX_RESULT_FALSE);
		if (!StringUtils.isEmpty(msg))
			setReqResultInfo(msg);
	}
	
}
