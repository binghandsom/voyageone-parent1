package com.voyageone.wsdl.exception;

/**
 * Created by sn3 on 2015-08-10.
 */
public class ApiRuleException extends ApiException{
    public ApiRuleException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
