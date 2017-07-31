package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.VOCmsUsaProductSalesMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by dell on 2017/7/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class VOCmsUsaProductSalesQueueTest {
    @Autowired
    VOCmsUsaProductSalesMQJob VOCmsUsaProductSalesQueue;
    @Test
    public void testOnStartup() throws Exception {
        VOCmsUsaProductSalesMQMessageBody body = new VOCmsUsaProductSalesMQMessageBody();
        VOCmsUsaProductSalesMQMessageBody.Param param = body.new Param();

        body.setSender("xusong");

        param.setCartId(23);
        param.setQty(20);
        body.setChannelId("001");
        param.setStatus(1);
        param.setOrderDate(1460736000000L);
        param.setSku("aj6961-m");
        ArrayList<VOCmsUsaProductSalesMQMessageBody.Param> params = new ArrayList<>();
        params.add(param);
        body.setItems(params);
        VOCmsUsaProductSalesQueue.onStartup(body);
    }
}