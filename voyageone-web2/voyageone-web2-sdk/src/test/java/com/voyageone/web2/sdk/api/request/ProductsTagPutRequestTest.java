package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductsTagPutResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2016/1/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductsTagPutRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testPut() {
        ProductsTagPutRequest request = new ProductsTagPutRequest("001");
        request.setModifier("testcc");
        String tagPath = "-8-";
        request.addProductIdTagPathsMap(1L, tagPath);
        ProductsTagPutResponse response = voApiClient.execute(request);
        System.out.println(response);
    }
}
