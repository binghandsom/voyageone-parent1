package com.voyageone.web2.sdk.api.exception;

/**
 * vo客户端异常。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = -238091758285157331L;

	private String errCode;
	private String errMsg;

	private String url;

	public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException(String errCode, String errMsg) {
		super(errCode + ":" + errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public ApiException(String errCode, String errMsg, String url) {
		super(errCode + ":" + errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.url = url;
	}

	public String getErrCode() {
		return this.errCode;
	}

	public String getErrMsg() {
		return this.errMsg;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
