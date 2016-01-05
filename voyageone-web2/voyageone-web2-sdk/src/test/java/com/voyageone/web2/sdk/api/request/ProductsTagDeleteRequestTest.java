package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductsTagDeleteResponse;
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
public class ProductsTagDeleteRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testDelete() {
        ProductsTagDeleteRequest request = new ProductsTagDeleteRequest("001");
        request.setModifier("testcc");
        String tagPath = "-10-";
        request.addProductIdTagPathsMap(1L, tagPath);

        ProductsTagDeleteResponse response = voApiClient.execute(request);
        System.out.println(response);
    }
}
