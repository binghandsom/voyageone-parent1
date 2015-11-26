package com.voyageone.cms.service;

import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class FeedToCmsServiceTest {
    @Autowired
    FeedToCmsService feedToCmsService;
    @Test
    public void testGetFeedCategory() throws Exception {
        List<Map> ret = feedToCmsService.getFeedCategory("010");
        System.out.println(JsonUtil.getJsonString(ret));
    }

    @Test
    public void testSetFeedCategory() throws Exception {
        List<Map> tree = new ArrayList<>();
        Map o = new HashMap<>();

        List<Map> child = new ArrayList<>();
        Map b = new HashMap();
        b.put("category","dd");
        child.add(b);
        Map c = new HashMap();
        c.put("category", "c");
        child.add(c);
        o.put("ategory","a");
        o.put("child",child);
        tree.add(o);

        feedToCmsService.setFeedCategory("012",tree);
    }

    @Test
    public void testFindCategory() throws Exception {
        List<Map> ret = feedToCmsService.getFeedCategory("010");
//        Map child =  feedToCmsService.findCategory(ret, "Home-Kitchen");
        Map child =  feedToCmsService.findCategory(ret, "Home");
    }

    @Test
    public void testAddCategory() throws Exception {
        List<Map> tree = feedToCmsService.getFeedCategory("010");
        feedToCmsService.addCategory(tree,"Home-voyageone-james");
        feedToCmsService.setFeedCategory("010",tree);
    }
}