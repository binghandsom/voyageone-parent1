package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2015/12/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TagAddClientTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testAddTag() {
        TagAddRequest requestModel = new TagAddRequest("100");
        requestModel.setTagName("5折");
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(11);
        requestModel.setSortOrder(0);
        requestModel.setCreater("jerry");

        //SDK取得Product 数据
        CmsBtTagModel tag = voApiClient.execute(requestModel).getTag();

        System.out.println(tag.getTagId());

    }
}
