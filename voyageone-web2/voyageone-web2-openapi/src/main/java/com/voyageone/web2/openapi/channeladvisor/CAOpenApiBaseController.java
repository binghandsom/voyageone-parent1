package com.voyageone.web2.openapi.channeladvisor;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.web2.openapi.channeladvisor.util.ResponseUtils;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
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
        return ResponseUtils.buildError(exception);
    }


}
