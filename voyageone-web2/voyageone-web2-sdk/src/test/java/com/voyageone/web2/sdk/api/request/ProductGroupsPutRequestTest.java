package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductGroupsPutResponse;
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
public class ProductGroupsPutRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testProductSkusPut() {
        ProductGroupsPutRequest requestModel = new ProductGroupsPutRequest("001");

        CmsBtProductModel_Group_Platform platformModel = new CmsBtProductModel_Group_Platform();
        //{ "groupId" : 391 , "cartId" : 21 , "numIId" : "2000482" , "isMain" : 0 , "displayOrder" : 68 , "publishTime" : "2015-11-12 16:19:00" , "instockTime" : "2015-11-18 16:19:00"}
        platformModel.setGroupId(100L);
        platformModel.setCartId(22);
        platformModel.setNumIId("0011b");
        platformModel.setIsMain(true);
        platformModel.setDisplayOrder(4);
        platformModel.setPublishTime("2015-11-12 17:19:00");
        platformModel.setInstockTime("2015-11-18 17:19:00");
        requestModel.setPlatform(platformModel);

        requestModel.addProductId(1L);

        //SDK取得Product 数据
        ProductGroupsPutResponse response = voApiClient.execute(requestModel);
        System.out.println(response);
    }
}
