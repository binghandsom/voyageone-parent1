package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductGroupsPutResponse;
import com.voyageone.web2.sdk.api.response.ProductsDeleteResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2015/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductsGetRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGet() {

        ProductsGetRequest requestModel = new ProductsGetRequest("001");
        String queryString = "{\"groups.platforms\":{$elemMatch: {\"groupId\":589, \"isMain\":1}}}";
        requestModel.setQueryString(queryString);

        requestModel.addField("prodId");
        requestModel.addField("fields.code");
        requestModel.addField("fields.brand");
        requestModel.addField("groups.platforms.groupId");
        requestModel.addField("groups.platforms.cartId");

        //SDK取得Product 数据
        ProductsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}
