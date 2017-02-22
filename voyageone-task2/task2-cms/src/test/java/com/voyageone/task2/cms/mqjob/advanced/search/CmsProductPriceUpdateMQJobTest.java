package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Edward
 * @version 2.0.0, 2017/2/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsProductPriceUpdateMQJobTest {

    @Autowired
    CmsProductPriceUpdateMQJob cmsProductPriceUpdateMQJob;

    @Test
    public void testOnStartup() throws Exception {

        ProductPriceUpdateMQMessageBody mqMessageBody = new ProductPriceUpdateMQMessageBody();
        mqMessageBody.setChannelId("012");
        mqMessageBody.setCartId(31);
        mqMessageBody.setProdId(3127746L);
        mqMessageBody.setSender("edward");
        cmsProductPriceUpdateMQJob.onStartup(mqMessageBody);
    }
}