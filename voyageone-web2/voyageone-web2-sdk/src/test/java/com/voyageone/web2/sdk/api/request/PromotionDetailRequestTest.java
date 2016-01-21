package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.response.PromotionDetailPutResponse;
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

/**
 * @author aooer 2016/1/19.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PromotionDetailRequestTest {

    @Autowired
    VoApiDefaultClient voApiClient;

    @Autowired
    RestTemplate restTemplate;

    private static final String baseUri = "http://localhost:8080/rest";

    @Test
    public void testAdd() {

        PromotionDetailAddRequest request=new PromotionDetailAddRequest();
        /*request.setModifier("testor");
        request.setChannelId("001");
        request.setCartId(1);
        request.setProductCode("100001");
        request.setPromotionId(1);
        request.setPromotionPrice(1.1);
        request.setTagId(1);
        request.setTagPath("http://1");*/

        processRequest(request, PromotionDetailPutResponse.class);
    }

    @Test
    public void testUpdate() {
        PromotionDetailUpdateRequest request=new PromotionDetailUpdateRequest();
        /*request.setPromotionCodeModel(promotionCodeModel);
        request.setModifier(operator);*/

        processRequest(request, PromotionDetailPutResponse.class);
    }

    @Test
    public void testDelete() {
        PromotionDetailDeleteRequest request=new PromotionDetailDeleteRequest();
        /*request.setPromotionModes(promotionModes);
        request.setChannelId(channelId);
        request.setModifier(operator);*/

        processRequest(request, PromotionDetailPutResponse.class);
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
