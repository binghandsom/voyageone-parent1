package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.PlatformSpecialFieldsGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2016/1/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PlatformSpecialFieldsGetRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGet() {

        PlatformSpecialFieldsGetRequest requestModel = new PlatformSpecialFieldsGetRequest();
        requestModel.setCartId(1);
        requestModel.setCatId("11");
        requestModel.setType("1");

        //SDK取得Product 数据
        PlatformSpecialFieldsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}
