package com.voyageone.common.components.jumei.Request;
import java.io.IOException;
import java.util.Map;
public interface JMRequest {
    String getUrl();
    Map<String, Object> getParameter() throws IOException;
}
