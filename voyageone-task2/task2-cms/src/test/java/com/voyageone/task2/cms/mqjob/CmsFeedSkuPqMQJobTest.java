package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedSkuPqMQMessageBody;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/3/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsFeedSkuPqMQJobTest {

    @Autowired
    FeedInfoService feedInfoService;
    @Autowired
    CmsFeedSkuPqMQJob cmsFeedSkuPqMQJob;

    @Test
    public void onStartup() throws Exception {
        CmsFeedSkuPqMQMessageBody message = new CmsFeedSkuPqMQMessageBody();

        List<CmsBtFeedInfoModel_Sku> skuList = new ArrayList<>();
        CmsBtFeedInfoModel_Sku _skuInfo = feedInfoService.getProductByCode("010", "1FMA3324Y11").getSkus().get(0);
        /**
         * 比较一下客户价格
         * priceNet:美金成本价
         * priceClientRetail:美金指导价
         * priceClientMsrp:美金专柜价
         */
        _skuInfo.setPriceNet(0.0);
        _skuInfo.setPriceClientRetail(0.0);
        _skuInfo.setPriceClientMsrp(0.0);

        skuList.add(_skuInfo);

        //拼消息体
        message.setChannelId("010");
        message.setSender("piao");
        message.setSkuList(skuList);


        cmsFeedSkuPqMQJob.onStartup(message);
    }


}
