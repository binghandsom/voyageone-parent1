package com.voyageone.service.impl.cms.usa;

import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import org.apache.commons.collections4.CollectionUtils;
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
 * Created by rex.wu on 2017/7/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class UsaFeedInfoServiceTest {

    @Autowired
    UsaFeedInfoService usaFeedInfoService;
    @Autowired
    FeedInfoService feedInfoService;
    @Test
    public void setPrice() throws Exception {
        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode("001", "68220-gem");
        usaFeedInfoService.setPrice(cmsBtFeedInfoModel);
    }

    @Test
    public void testGetFeedList() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum",1);
        map.put("pageSize",10);
        map.put("name","Patagonia Down Sweater Vest (Kids)");
        map.put("searchContent","68220-gem-xl");
        map.put("barcode","885657051304");
        map.put("lastReceivedOnStart","2014-09-21 00:55:49");
        map.put("lastReceivedOnEnd","2014-09-23 00:55:49");
        map.put("sort","barcode_1");
        List<CmsBtFeedInfoModel> feedList = usaFeedInfoService.getFeedList(map,"001");

    }

    @Test
    public void testGetFeedCount() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 10);
        map.put("name", "Patagonia Down Sweater Vest");
        map.put("searchContent", "68220-gem-xl");
        map.put("barcode", "885657051304");
        map.put("lastReceivedOnStart", "2014-09-21 00:55:49");
        map.put("lastReceivedOnEnd", "2014-09-23 00:55:49");
        map.put("sort", "barcode_1");
        Long feedCount = usaFeedInfoService.getFeedCount(map, "001");
    }

    @Test
    public void testUpDateFeedInfo() throws Exception {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("code","68220-gem");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name","1111111111111");
        usaFeedInfoService.upDateFeedInfo("001",queryMap,resultMap);

    }
}