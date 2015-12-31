package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import com.voyageone.web2.sdk.api.response.TagRemoveResponse;
import com.voyageone.web2.sdk.api.response.TagsGetByChannelIdResponse;
import com.voyageone.web2.sdk.api.response.TagsGetByParentTagIdResponse;
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
        requestModel.setTagName("6.7折");
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(101);
        requestModel.setSortOrder(0);
        requestModel.setCreater("jerry");

        //SDK取得Product 数据
        TagAddResponse res = voApiClient.execute(requestModel);
        CmsBtTagModel tag = res.getTag();

        System.out.println("res Code = " + res.getCode());
        System.out.println("res Message = " + res.getMessage());
        System.out.println("Tag = " + tag.getTagId());
    }

    @Test
    public void testRemoveTag() {
        TagRemoveRequest requestModel = new TagRemoveRequest("100");
        requestModel.setTagId(31);
        requestModel.setModifier("jerry");

        //SDK取得Product 数据
        TagRemoveResponse res = voApiClient.execute(requestModel);
        boolean removeResult = res.isRemoveResult();

        System.out.println("res Code = " + res.getCode());
        System.out.println("res Message = " + res.getMessage());
        System.out.println("removeResult = " + removeResult);
    }

    @Test
    public void testGetByParentTagId() {
        TagsGetByParentTagIdRequest requestModel = new TagsGetByParentTagIdRequest("100");
        requestModel.setParentTagId(11);

        //SDK取得Product 数据
        TagsGetByParentTagIdResponse res = voApiClient.execute(requestModel);
        List<CmsBtTagModel> tagModelList = res.getTags();

        System.out.println("res Code = " + res.getCode());
        System.out.println("res Message = " + res.getMessage());
        System.out.println("tagList.size = " + tagModelList.size());

        if (tagModelList.size() > 0) {
            for (int i = 0; i < tagModelList.size(); i++) {
                System.out.println("tag id = " + tagModelList.get(i).getTagId());
            }
        }
    }

    @Test
    public void testGetByChannelId() {
        TagsGetByChannelIdRequest requestModel = new TagsGetByChannelIdRequest("100");
        requestModel.setChannelId("100");

        //SDK取得Product 数据
        TagsGetByChannelIdResponse res = voApiClient.execute(requestModel);
        List<CmsBtTagModel> tagModelList = res.getTags();

        System.out.println("res Code = " + res.getCode());
        System.out.println("res Message = " + res.getMessage());
        System.out.println("tagList.size = " + tagModelList.size());

        if (tagModelList.size() > 0) {
            for (int i = 0; i < tagModelList.size(); i++) {
                System.out.println("tag id = " + tagModelList.get(i).getTagId());
            }
        }
    }
}
