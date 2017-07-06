package com.voyageone.service.impl.cms.usa;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class UsaFeedInfoServiceTest {
    @Autowired
    UsaFeedInfoService usaFeedInfoService;

    @Test
    public void testGetFeedList() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum",1);
        map.put("pageSize",10);
        map.put("name","Kids");
        List<CmsBtFeedInfoModel> feedList = usaFeedInfoService.getFeedList(map,"C001");

    }

    @Test
    public void testGetFeedCount() throws Exception {

    }
}