package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.FeedMappingsGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2016/1/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class FeedMappingsGetRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGet() {

        FeedMappingsGetRequest requestModel = new FeedMappingsGetRequest();
        requestModel.setChannelId("013");
        requestModel.setFeedCategoryPath("Fitness & Sports-Fitness & Exercise-Yoga & Pilates");
        requestModel.setMainCategoryPath("饰品/流行首饰/时尚饰品新>手镯");

        //SDK取得Product 数据
        FeedMappingsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }

    @Test
    public void testGets() {

        FeedMappingsGetRequest requestModel = new FeedMappingsGetRequest();
        requestModel.setChannelId("013");

        //SDK取得Product 数据
        FeedMappingsGetResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}