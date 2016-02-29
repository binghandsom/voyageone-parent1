package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductTransDistrResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2016/2/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductTransDistrRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGet() {

        ProductTransDistrRequest requestModel = new ProductTransDistrRequest("013");
        requestModel.addSort("fields.code", true);
        requestModel.addSort("fields.brand", false);

        requestModel.setTranslator("chuanyu.liang3");
        requestModel.setTranslateTimeHDiff(24);

        requestModel.addField("fields.code");
        requestModel.addField("fields.brand");
        requestModel.addField("fields.translateStatus");
        requestModel.addField("fields.translator");
        requestModel.addField("fields.translateTime");

        //SDK取得Product 数据
        ProductTransDistrResponse response = voApiClient.execute(requestModel);



        System.out.println(response);
    }
}
