package com.voyageone.service.impl.cms.usa;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rex.wu on 2017/7/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class UsaFeedInfoServiceTest {

    @Autowired
    private UsaFeedInfoService usaFeedInfoService;

    @Test
    public void getTopModelsByModel() throws Exception {

        List<CmsBtFeedInfoModel> resultFeedList = usaFeedInfoService.getTopModelsByModel("001", "patagoniadownsweatervestkids");
        if (CollectionUtils.isNotEmpty(resultFeedList)) {
            for (CmsBtFeedInfoModel feed : resultFeedList) {
                System.out.println("_id=" + feed.get_id());
                System.out.println("Model=" + feed.getModel());
                System.out.println("Code=" + feed.getCode());
                System.out.println("Brand=" + feed.getBrand());

            }
        }

    }

    @Test
    public void isUrlKeyDuplicated() throws Exception {
        System.out.println(usaFeedInfoService.isUrlKeyDuplicated("001", "68220-gem","123"));
    }

}