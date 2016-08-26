package com.voyageone.web2.openapi;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.response.SimpleResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Base Controller。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public abstract class OpenApiBaseController extends VOAbsLoggable {

    /**
     * error Handler
     */
    @ExceptionHandler(Exception.class)
    public VoApiResponse errorHandler(HttpServletRequest request, Exception exception) {
        String message = request.getRequestURL().toString();
        $error("Error Message:=" + message);
        $error(message, exception);
        return buildError(request, exception);
    }

    /**
     * build Error Info
     */
    private VoApiResponse buildError(HttpServletRequest request, Exception exception) {
        String code;
        String message = exception.getMessage();
        message = StringUtils.isEmpty(message) ? exception.getClass().getName() : message;

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException)exception;
            code = apiException.getErrCode();
        } else if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException)exception;
            code = businessException.getCode();
        } else if (exception instanceof SystemException) {
            SystemException systemException = (SystemException)exception;
            code = systemException.getCode();
        } else {
            code = "5";
        }

        return new VoApiResponse(code, message);
    }



    protected VoApiResponse simpleResponse(Object resultData) {
        return new SimpleResponse(resultData);
    }
}
