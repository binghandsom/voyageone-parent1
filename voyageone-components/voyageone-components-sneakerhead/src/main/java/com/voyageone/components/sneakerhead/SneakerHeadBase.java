package com.voyageone.components.sneakerhead;

import com.voyageone.common.configs.Codes;
import com.voyageone.components.ComponentBase;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * SneakerHeadBase
 *
 * @author gjl on 2016/11/15.
 * @version 0.0.1
 */
public class SneakerHeadBase extends ComponentBase {

    protected static final String SNEAKER_INFO_URL = "http://%s/api/feed/feed_info";
    protected static final String SNEAKER_COUNT_URL = "http://%s/api/feed/feed_sum";
    protected static final String SNEAKER_SALE_URL = "http://%s/api/sales/get_sales";
    protected static final String GET_CATEGORY_URL = "http://%s/api/category/get_all";
    protected static final String GET_US_PLATFORM_STATUS_URL = "http://%s/api/platform_status/get_us";
    protected static final String GET_CN_PLATFORM_STATUS_URL = "http://%s/api/platform_status/update_cn";

    protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    public static String DEFAULT_DOMAIN = "47.180.64.158:52233";

    public static final String SNEAKERHEAD_US_SERVICE = "SNEAKERHEAD_US_SERVICE";

    public SneakerHeadBase() {
        DEFAULT_DOMAIN = Optional.ofNullable(Codes.getCode(SNEAKERHEAD_US_SERVICE, "url"))
                .orElse("47.180.64.158:52233");
    }

    protected RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(200000);
        simpleClientHttpRequestFactory.setReadTimeout(200000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    protected String getSneakerInfoUrl(String domain) {
        return String.format(SNEAKER_INFO_URL, domain);
    }

    protected String getSneakerCountUrl(String domain) {
        return String.format(SNEAKER_COUNT_URL, domain);
    }

    protected String getSneakerSaleUrl(String domain) {
        return String.format(SNEAKER_SALE_URL, domain);
    }

    protected String getCategoryUrl(String domain) {
        return String.format(GET_CATEGORY_URL, domain);
    }

    protected String getUsPlatformStatusUrl(String domain) {
        return String.format(GET_US_PLATFORM_STATUS_URL, domain);
    }

    protected String getCnPlatformStatusUrl(String domain) {
        return String.format(GET_CN_PLATFORM_STATUS_URL, domain);
    }
}
