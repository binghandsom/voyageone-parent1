package com.voyageone.components.jumei.request;
import java.io.IOException;
import java.util.Map;

/**
 * BaseJMRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public interface BaseJMRequest {
    String getUrl();
    Map<String, Object> getParameter() throws IOException;
}
