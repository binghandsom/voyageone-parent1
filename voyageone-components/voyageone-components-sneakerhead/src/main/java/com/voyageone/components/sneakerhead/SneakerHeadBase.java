package com.voyageone.components.sneakerhead;

import com.voyageone.components.ComponentBase;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * sneakerhead 提供的 api 路径等基本信息
 * Created by gjl on 2016/11/15.
 */
public class SneakerHeadBase extends ComponentBase {

    protected static final String SNEAKERHEAD_BASE_URL = "http://47.180.64.158:52233";

    protected static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";

    protected RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(60000);
        simpleClientHttpRequestFactory.setReadTimeout(60000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }
}
