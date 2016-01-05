package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.response.ProductsTagDeleteResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2016/1/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductsTagDeleteRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testDelete() {
        ProductsTagDeleteRequest request = new ProductsTagDeleteRequest("001");

        CmsBtTagModel tagModel = new CmsBtTagModel();


        String tagPath = "-8-";
        tagModel.setTagPath(tagPath);
        //???
        int tagId = Integer.parseInt(String.valueOf(tagPath.charAt(tagPath.length() - 2)));
        tagModel.setTagId(tagId);
        request.setModifier("testcc");
        request.addProductIdTag(1L, tagModel);

        ProductsTagDeleteResponse response = voApiClient.execute(request);
        System.out.println(response);
    }
}
