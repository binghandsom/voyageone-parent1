package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
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

        ProductsGetRequest requestModel = new ProductsGetRequest("013");
//        String queryString = "{\"groups.platforms\":{$elemMatch: {\"groupId\":589, \"isMain\":1}}}";
//        requestModel.setQueryString(queryString);

        //requestModel.setIsPage(false);
        requestModel.setPageNo(1);
        requestModel.setPageSize(2);

//        List<Long> listProductIds= new ArrayList<Long>();
//        listProductIds.add(163L);
//        Set<Long> productIds = new HashSet<Long>(listProductIds);
//
//        requestModel.setProductIds(productIds);

        requestModel.setQueryString("{\"modifier\":\"will\"}");

        requestModel.addField("prodId");
        requestModel.addField("fields.code");
        requestModel.addField("fields.brand");
        requestModel.addField("groups.platforms.groupId");
        requestModel.addField("groups.platforms.cartId");
        requestModel.addField("feed.orgAtts.modelCode");

        //requestModel.addSort("fields.brand", true);
        requestModel.setSorts("\"fields.brand\": -1, \"fields.code\": 1");

        //SDK取得Product 数据
        ProductsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}
