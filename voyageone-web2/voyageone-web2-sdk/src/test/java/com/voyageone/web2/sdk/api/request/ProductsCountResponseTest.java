package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2015/12/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductsCountResponseTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testProductsCountGet() {
        ProductsCountRequest requestModel = new ProductsCountRequest("300");
        requestModel.addProp("groups.platforms.groupId", 101);
        //SDK取得Product 数据
        System.out.println(voApiClient.execute(requestModel).getTotalCount());
    }

    @Test
    public void testCountWithQueryString() {
        ProductsCountRequest requestModel = new ProductsCountRequest("001");
        String queryString = "{\"groups.platforms\":{$elemMatch: {\"groupId\":589, \"isMain\":1}}}";
        requestModel.setQueryString(queryString);
        //SDK取得Product 数据
        System.out.println(voApiClient.execute(requestModel).getTotalCount());
    }



}
