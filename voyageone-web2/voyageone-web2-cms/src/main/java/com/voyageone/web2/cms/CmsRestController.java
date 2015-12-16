package com.voyageone.web2.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.response.VoResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest Controller。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public abstract class CmsRestController extends BaseController {

    /**
     * error Handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity errorHandler(HttpServletRequest request, Exception exception) {
        ApiException errorDetail = buildError(request, exception);

        VoResponseEntity result = new VoResponseEntity(null, HttpStatus.OK);
        result.setError(errorDetail);
        return result;
    }

    /**
     * build Error Info
     */
    private ApiException buildError(HttpServletRequest request, Exception exception) {
        String msg = exception.getMessage();
        msg = StringUtils.isEmpty(msg) ? exception.getClass().getName() : msg;

        ApiException error = null;
        if (exception instanceof ApiException) {
            error = (ApiException)exception;
        } else if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException)exception;
            error = new ApiException(businessException.getCode(), msg);
        } else if (exception instanceof SystemException) {
            SystemException systemException = (SystemException)exception;
            error = new ApiException(systemException.getCode(), msg);
        } else {
            error = new ApiException("5", msg);
        }

        error.setUrl(request.getRequestURL().toString());
        return error;
    }

    /**
     * build success Entity
     */
    public VoResponseEntity successEntity(Object model) {
        return new VoResponseEntity(model, HttpStatus.OK);
    }


}
