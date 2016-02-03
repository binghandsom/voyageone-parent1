package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductForOmsGetResponse;
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
public class ProductForOmsGetRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGetSkuBySkuIncludes() {

        ProductForOmsGetRequest requestModel = new ProductForOmsGetRequest("013");
        requestModel.setSkuIncludes("58255");

        //SDK取得Product 数据
        ProductForOmsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}