package com.voyageone.web2.sdk.api.exception;

/**
 * API前置检查异常。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class ApiRuleException extends ApiException {

	private static final long serialVersionUID = -7787145910600194272L;

	public ApiRuleException(String errCode, String errMsg) {
		super(errCode, errMsg);
	}
}
