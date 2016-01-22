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
public class PromotionDetailRequestTest {

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
        ApiTestUtils.RunWithRest(request);
    }

    @Test
    public void testUpdate() {
        PromotionDetailUpdateRequest request=new PromotionDetailUpdateRequest();
        /*request.setPromotionCodeModel(promotionCodeModel);
        request.setModifier(operator);*/
        ApiTestUtils.RunWithRest(request);
    }

    @Test
    public void testDelete() {
        PromotionDetailDeleteRequest request=new PromotionDetailDeleteRequest();
        /*request.setPromotionModes(promotionModes);
        request.setChannelId(channelId);
        request.setModifier(operator);*/
        ApiTestUtils.RunWithRest(request);
    }


}
