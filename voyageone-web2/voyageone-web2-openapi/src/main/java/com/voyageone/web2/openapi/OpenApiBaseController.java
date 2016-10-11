package com.voyageone.web2.openapi;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import com.voyageone.web2.openapi.util.ResponseUtils;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.response.SimpleResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

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
    public Object errorHandler(HttpServletRequest request, Exception exception) {
        String message = request.getRequestURL().toString();
        $error("Error Message:=" + message);
        $error(message, exception);

        // channelAdvisor ExceptionHandler
        if (request.getRequestURI().contains(CAUrlConstants.ROOT)) {
            return ResponseUtils.buildCAError(exception);
        }
        return ResponseUtils.buildError(exception);
    }

    protected VoApiResponse simpleResponse(Object resultData) {
        return new SimpleResponse(resultData);
    }
}
