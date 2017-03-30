package com.voyageone.web2.sdk.api.request.cms;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.cms.ProductForWmsGetRequest;
import com.voyageone.web2.sdk.api.response.cms.ProductForWmsGetResponse;
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
public class ProductForWmsGetRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGetSkuBySkuIncludes() {

//        ProductForWmsGetRequest requestModel = new ProductForWmsGetRequest("010");
//        requestModel.setCode("00341");
        ProductForWmsGetRequest requestModel = new ProductForWmsGetRequest("017");
        requestModel.setSku("017-54871");

        //SDK取得Product 数据
        ProductForWmsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}