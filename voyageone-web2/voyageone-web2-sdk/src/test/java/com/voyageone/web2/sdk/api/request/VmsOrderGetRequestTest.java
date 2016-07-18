package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductForWmsGetResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderAddGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Edward
 * @version 2.0.0, 16/2/3
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class VmsOrderGetRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testAddOrderInfo() {

        VmsOrderAddGetRequest request = new VmsOrderAddGetRequest();
        request.setChannelId("088");

        //SDK取得Product 数据
        VmsOrderAddGetResponse response = voApiClient.execute(request);

        System.out.println(response);
    }
}