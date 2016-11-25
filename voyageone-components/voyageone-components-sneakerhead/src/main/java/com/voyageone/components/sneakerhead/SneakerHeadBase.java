package com.voyageone.components.sneakerhead;

import com.voyageone.components.ComponentBase;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by gjl on 2016/11/15.
 */
public class SneakerHeadBase extends ComponentBase {

    protected static final String sneakerInfoUrl = "http://10.0.0.91:52233/api/feed/feed_info";
    protected static final String sneakerCountUrl = "http://10.0.0.91:52233/api/feed/feed_sum";
    protected static final String sneakerSaleUrl = "http://10.0.0.91:52233/api/sales/get_sales";
    protected static final String contentType = "application/json;charset=UTF-8";

    protected RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(6000);
        simpleClientHttpRequestFactory.setReadTimeout(6000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }
}
