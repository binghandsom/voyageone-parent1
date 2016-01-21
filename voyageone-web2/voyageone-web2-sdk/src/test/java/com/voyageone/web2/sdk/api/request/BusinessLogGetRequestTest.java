package com.voyageone.web2.sdk.api.request;

import com.jd.open.api.sdk.internal.JSON.JSON;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.response.BusinessLogGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BusinessLogGetRequestTest {
    @Autowired
    VoApiDefaultClient voApiClient;

    @Autowired
    RestTemplate restTemplate;

    private String baseUri = "http://localhost:8080/rest";

    @Test
    public void testSelect() {
        BusinessLogGetRequest request = new BusinessLogGetRequest();
        //request.setProductIds(221);
        request.setProductIds(Arrays.asList(221));
        request.setErrType(String.valueOf(1));

        System.out.println(JSON.toString(request));

        System.out.println(
                voApiClient.execute(request)
        );
        //processRequest(request, BusinessLogGetResponse.class);
    }


    /**
     * URI 取得
     *
     * @return URI
     */
    private URI getURI(String apiURLPath) {
        return URI.create(baseUri + apiURLPath);
    }

    /**
     * Request 处理方法
     *
     * @param <E> 实际类型
     *
     *
     * @param request 请求体
     * @param clazz 响应类
     */
    private <E> void processRequest(VoApiRequest request, Class<E> clazz) {
        RequestEntity<VoApiRequest> requestEntity = new RequestEntity<VoApiRequest>(
                request, request.getHeaders(), request.getHttpMethod(),
                getURI(request.getApiURLPath()));
        ResponseEntity<E> responseEntity;
        try {
            responseEntity = restTemplate.exchange(requestEntity, clazz);
            System.out.println(responseEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
