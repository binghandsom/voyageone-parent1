package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsCountGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2015/12/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductsCountGetResponseTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testProductsCountGet() {
        ProductsCountGetRequest requestModel = new ProductsCountGetRequest("300");
        requestModel.addProp("groups.platforms.groupId", 101);
        //SDK取得Product 数据
        System.out.println(voApiClient.execute(requestModel).getTotalCount());
    }



}
