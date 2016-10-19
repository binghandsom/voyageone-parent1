package com.voyageone.task2.cms.service;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by james on 2016/10/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSynJmPromotionDealPriceMQServiceTest {
    @Autowired
    CmsSynJmPromotionDealPriceMQService cmsSynJmPromotionDealPriceMQService;
    @Autowired
    CmsSynJmPromotionDealPriceService cmsSynJmPromotionDealPriceService;
    @Test
    public void onStartup() throws Exception {
        cmsSynJmPromotionDealPriceService.onStartup(new ArrayList<>());
        Map<String,Object> param = new HashedMap();
        param.put("jmPromotionId",111);
        cmsSynJmPromotionDealPriceMQService.onStartup(param);
    }

}