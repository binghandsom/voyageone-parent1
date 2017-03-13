package com.voyageone.web2.sdk.api.request.cms;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.cms.ProductForOmsGetRequest;
import com.voyageone.web2.sdk.api.response.cms.ProductForOmsGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testGetSkuByNameIncludes() {

        ProductForOmsGetRequest requestModel = new ProductForOmsGetRequest("013");
        requestModel.setNameIncludes("Foot Roller");

        //SDK取得Product 数据
        ProductForOmsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }

    @Test
    public void testGetSkuByDescriptionIncludes() {

        ProductForOmsGetRequest requestModel = new ProductForOmsGetRequest("013");
        requestModel.setDescriptionIncludes("英语");

        //SDK取得Product 数据
        ProductForOmsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }

    @Test
    public void testGetSkuByAllIncludes() {

        ProductForOmsGetRequest requestModel = new ProductForOmsGetRequest("013");
        requestModel.setSkuIncludes("58255");
        requestModel.setNameIncludes("Foot Roller");
        requestModel.setDescriptionIncludes("英语");

        //SDK取得Product 数据
        ProductForOmsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }

    @Test
    public void testGetSkuBySkuList() {

        ProductForOmsGetRequest requestModel = new ProductForOmsGetRequest("013");
        List<String> skuList = new ArrayList<>();
        skuList.add("05-61355-OneSize");
        skuList.add("05-58255-OneSize");

        requestModel.setSkuList(skuList);
        requestModel.setCartId("23");

        //SDK取得Product 数据
        ProductForOmsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}