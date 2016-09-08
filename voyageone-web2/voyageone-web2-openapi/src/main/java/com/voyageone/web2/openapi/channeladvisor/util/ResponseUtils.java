package com.voyageone.web2.openapi.channeladvisor.util;

import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.sdk.api.channeladvisor.domain.ErrorModel;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ResponseStatusEnum;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * Response Utils
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public class ResponseUtils {

    /**
     * build Error Info
     */
    public static ActionResponse buildError(Exception exception) {
        String code;
        String message;

        if (exception instanceof CAApiException) {
            CAApiException caException = (CAApiException)exception;
            code = caException.getErrCode();
            message = caException.getErrMsg();
        } else {
            code = String.valueOf(ErrorIDEnum.SystemUnavailable.getCode());
            message = ErrorIDEnum.SystemUnavailable.getDefaultMessage();
        }

        String messageNew = StringUtils.isEmpty(message) ? exception.getClass().getName() : message;

        ActionResponse response = new ActionResponse();
        response.setResponseBody(null);
        response.setStatus(ResponseStatusEnum.Failed);
        response.setPendingUri(null);
        response.setHasErrors(true);
        ErrorIDEnum errorIDEnum = ErrorIDEnum.getInstance(code);
        response.addError(new ErrorModel(errorIDEnum, messageNew));

        return response;
    }
}
