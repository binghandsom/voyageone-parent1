package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductGroupsDeleteResponse;
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
public class ProductGroupsDeleteRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testDelete() {

        ProductGroupsDeleteRequest requestModel = new ProductGroupsDeleteRequest("300");
        requestModel.addGroupId(969L);
        requestModel.addGroupId(125L);

        //SDK取得Product 数据
        ProductGroupsDeleteResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}
