package com.voyageone.task2.cms.mqjob.stock;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.impl.cms.vomq.vomessage.body.stock.CmsStockCartChangedStockMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/6/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsStockCartChangedStockMQJobTest {

    @Autowired
    CmsStockCartChangedStockMQJob cmsStockCartChangedStockMQJob;
    @Test
    public void onStartup() throws Exception {
        String json = "{\"cartChangedStocks\":[{\"channelId\":\"001\",\"cartId\":23,\"sku\":\"1280734-025-l\",\"itemCode\":\"1280734-025\",\"qty\":0},{\"channelId\":\"001\",\"cartId\":23,\"sku\":\"1280734-025-m\",\"itemCode\":\"1280734-025\",\"qty\":0},{\"channelId\":\"001\",\"cartId\":23,\"sku\":\"1280734-025-s\",\"itemCode\":\"1280734-025\",\"qty\":0}]}";

        CmsStockCartChangedStockMQMessageBody cmsStockCartChangedStockMQMessageBody = JacksonUtil.json2Bean(json,CmsStockCartChangedStockMQMessageBody.class);
        cmsStockCartChangedStockMQJob.onStartup(cmsStockCartChangedStockMQMessageBody);
    }

}