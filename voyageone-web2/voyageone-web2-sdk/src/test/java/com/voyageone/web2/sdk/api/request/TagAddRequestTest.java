package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
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
public class TagAddRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testAddTag() {
        TagAddRequest requestModel = new TagAddRequest();

        requestModel.setChannelId("100");
        requestModel.setTagName("6.82折");
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(0);
        requestModel.setSortOrder(0);
        requestModel.setModifier("jerry");

        //SDK取得Product 数据
        TagAddResponse res = voApiClient.execute(requestModel);
        CmsBtTagModel tag = res.getTag();

        System.out.println("res Code = " + res.getCode());
        System.out.println("res Message = " + res.getMessage());
        System.out.println("Tag = " + tag.getTagId());
    }

    @Test
    public void testRemoveTag() {
        TagRemoveRequest requestModel = new TagRemoveRequest();
        requestModel.setChannelId("100");
        requestModel.setTagId(37);
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
        TagsGetRequest requestModel = new TagsGetRequest();
        requestModel.setParentTagId(11);

        //SDK取得Product 数据
        TagsGetResponse res = voApiClient.execute(requestModel);
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
        TagsGetRequest requestModel = new TagsGetRequest();
        requestModel.setChannelId("100");
        //SDK取得Product 数据
        TagsGetResponse res = voApiClient.execute(requestModel);
        List<CmsBtTagModel> tagModelList = res.getTags();

        System.out.println("res Code = " + res.getCode());
        System.out.println("res Message = " + res.getMessage());
        System.out.println("tagList.size = " + tagModelList.size());

        if (tagModelList.size() > 0) {
            for (CmsBtTagModel aTagModelList : tagModelList) {
                System.out.println("tag id = " + aTagModelList.getTagId());
            }
        }
    }
}
