package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/2/24.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PromotionCodeAddTejiaBaoRequestTest {
    @Autowired
    VoApiDefaultClient voApiDefaultClient;
    @Test
    public void test(){
        PromotionCodeAddTejiaBaoRequest request=new PromotionCodeAddTejiaBaoRequest();
        request.setModifier("lijun");
        request.setChannelId("010");
        request.setCartId(23);
        request.setProductCode("ESJ9293LBS-SC");
        request.setPromotionId(45);
        request.setPromotionPrice(100.0);

        voApiDefaultClient.execute(request);
    }
    @Test
    public void test2(){
        PromotionCodeUpdateTejiaBaoRequest request=new PromotionCodeUpdateTejiaBaoRequest();
        request.setModifier("lijun");
        request.setChannelId("010");
        request.setCartId(23);
        request.setProductCode("ESJ9293LBS-SC");
        request.setPromotionId(45);
        request.setPromotionPrice(90.0);

        voApiDefaultClient.execute(request);
    }
}