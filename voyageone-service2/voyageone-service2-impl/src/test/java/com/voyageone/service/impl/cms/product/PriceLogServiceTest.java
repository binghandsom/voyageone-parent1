package com.voyageone.service.impl.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogFlatModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonasvlag on 16/7/5.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class PriceLogServiceTest {

    @Autowired
    private CmsBtPriceLogService priceLogService;

    @Test
    public void testLogAll() {

        List<String> skuList = new ArrayList<>();

        skuList.add("ESH98014-BJ");

//        priceLogService.addLogForSkuListAndCallSyncPriceJob(skuList, "010", null,"Unit Tester", "测试");
    }

    @Test
    public void testPage() {

        String skuOrCode = "ESH98014-BJ";

        List<CmsBtPriceLogFlatModel> logModelList = priceLogService.getPage(null, skuOrCode, "010", "23", 0, 10);

        int count = priceLogService.getCount(null, skuOrCode, "010", "23");

        System.out.println(count);
    }
}