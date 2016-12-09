package com.voyageone.web2.openapi;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import com.voyageone.web2.openapi.util.ResponseUtils;
import com.voyageone.web2.sdk.api.VoApiResponse;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aooer 2016/12/7.
 */
@Component
public class OpenApiExceptionHandler extends VOAbsLoggable implements HandlerExceptionResolver, Ordered {

    private static final String INCLUDE_URI = "/rest/";

    @SuppressWarnings("unchecked")
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // check isIncludeUri
        if (!isIncludeUri(request)) {
            return null;
        }

        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        ModelAndView mav = new ModelAndView(jsonView);
        String message = request.getRequestURL().toString();
        $error("Error Message:=" + message);
        $error(message, ex);

        if (request.getRequestURI().contains(CAUrlConstants.ROOT)) {
            ActionResponse responseCa = ResponseUtils.buildCAError(ex);
            jsonView.setAttributesMap(JacksonUtil.bean2Map(responseCa));
        } else {
            VoApiResponse responseVo = ResponseUtils.buildError(ex);
            jsonView.setAttributesMap(JacksonUtil.bean2Map(responseVo));
        }

        return mav;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * isExCludeUri
     */
    private boolean isIncludeUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri != null) {
            if (uri.contains(INCLUDE_URI)) {
                return true;
            }
        }

        return false;
    }
}
