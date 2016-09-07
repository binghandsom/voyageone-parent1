package com.voyageone.web2.openapi.channeladvisor;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.web2.sdk.api.channeladvisor.exception.ErrorModel;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Base Controller。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public abstract class CAOpenApiBaseController extends VOAbsLoggable {

    /**
     * error Handler
     */
    @ExceptionHandler(Exception.class)
    public ActionResponse errorHandler(HttpServletRequest request, Exception exception) {
        String message = request.getRequestURL().toString();
        $error("Error Message:=" + message);
        $error(message, exception);
        return buildError(request, exception);
    }

    /**
     * build Error Info
     */
    private ActionResponse buildError(HttpServletRequest request, Exception exception) {
        String code;
        String message;

        if (exception instanceof CAApiException) {
            CAApiException caException = (CAApiException)exception;
            code = caException.getErrCode();
            message = caException.getErrMsg();
        } else {
            code = String.valueOf(ErrorIDEnum.SystemFailure.getCode());
            message = exception.getMessage();
        }

        String messageNew = StringUtils.isEmpty(message) ? exception.getClass().getName() : message;

        ActionResponse response = ActionResponse.createEmpty(true);
        ErrorIDEnum errorIDEnum = ErrorIDEnum.getInstance(code);
        response.addError(new ErrorModel(errorIDEnum, messageNew));

        return response;
    }
}
