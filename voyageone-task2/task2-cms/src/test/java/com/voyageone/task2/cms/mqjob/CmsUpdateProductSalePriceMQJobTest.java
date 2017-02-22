package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import com.voyageone.task2.cms.mqjob.advanced.search.CmsUpdateProductSalePriceMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2017/1/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUpdateProductSalePriceMQJobTest {

    @Autowired
    private CmsUpdateProductSalePriceMQJob cmsUpdateProductSalePriceMQJob;

    @Test
    public void onStartup() throws Exception {


        UpdateProductSalePriceMQMessageBody mqMessageBody = JacksonUtil.json2Bean("{\"productCodes\":[\"1FMA3324Y11-edward\"],\"cartId\":23,\"channelId\":\"010\",\"params\":{\"cartId\":23,\"_option\":\"saleprice\",\"productIds\":[\"1FMA3324Y11-edward\"],\"isSelAll\":0,\"priceType\":\"priceRetail\",\"optionType\":\"*\",\"priceValue\":\"0.88\",\"roundType\":3,\"skuUpdType\":2},\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"edward\"}", UpdateProductSalePriceMQMessageBody.class);
//        MQConfigInitTestUtil.startMQ(cmsUpdateProductSalePriceMQJob);
        cmsUpdateProductSalePriceMQJob.onStartup(mqMessageBody);
    }

}