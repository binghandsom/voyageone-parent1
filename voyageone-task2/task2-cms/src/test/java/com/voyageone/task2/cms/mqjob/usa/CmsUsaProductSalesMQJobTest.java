package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaProductSalesMQMessageBody;
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
public class CmsUsaProductSalesMQJobTest {
    @Autowired
    CmsUsaProductSalesMQJob cmsUsaProductSalesMQJob;
    @Test
    public void testOnStartup() throws Exception {
        CmsUsaProductSalesMQMessageBody body = new CmsUsaProductSalesMQMessageBody();
        CmsUsaProductSalesMQMessageBody.Param param = body.new Param();

        body.setSender("xusong");

        param.setCartId(23);
        param.setQty(20);
        body.setChannelId("001");
        param.setStatus(1);
        param.setOrderDate(1460736000000L);
        param.setSku("aj6961-m");
        ArrayList<CmsUsaProductSalesMQMessageBody.Param> params = new ArrayList<>();
        params.add(param);
        body.setItems(params);
        cmsUsaProductSalesMQJob.onStartup(body);
    }
}