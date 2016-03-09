package com.voyageone.web2.sdk.api.request;

import org.junit.Test;

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
