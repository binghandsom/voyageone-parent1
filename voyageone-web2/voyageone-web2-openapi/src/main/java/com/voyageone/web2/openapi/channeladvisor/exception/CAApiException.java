package com.voyageone.web2.openapi.channeladvisor.exception;

import com.voyageone.service.bean.vms.channeladvisor.enums.ErrorIDEnum;

/**
 * vo客户端异常。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class CAApiException extends RuntimeException {

	private String errCode;
	private String errMsg;

	private String url;

	public CAApiException() {
		super();
	}

	public CAApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public CAApiException(String message) {
		super(message);
	}

	public CAApiException(Throwable cause) {
		super(cause);
	}

    public CAApiException(ErrorIDEnum errorIDEnum) {
        super(errorIDEnum.getCode() + ":" + errorIDEnum.name());
        this.errCode = String.valueOf(errorIDEnum.getCode());
        this.errMsg = errorIDEnum.getDefaultMessage();
    }

	public CAApiException(ErrorIDEnum errorIDEnum, String message) {
		super(errorIDEnum.getCode() + ":" + errorIDEnum.name());
		this.errCode = String.valueOf(errorIDEnum.getCode());
		this.errMsg = message;
	}

	public CAApiException(String errCode, String errMsg) {
		super(errCode + ":" + errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public CAApiException(String errCode, String errMsg, String url) {
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
