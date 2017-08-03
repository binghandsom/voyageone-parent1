package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.common.util.JacksonUtil;
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

        String json ="{\"delaySecond\":0,\"sender\":null,\"mqTranSequence\":null,\"items\":[{\"channelId\":\"001\",\"cartId\":10,\"orderDate\":1501707096000,\"sku\":\"814443-001-4.5\",\"clientSku\":\"814443-001-4.5\",\"qty\":1,\"status\":1}]}";
        CmsUsaProductSalesMQMessageBody body = JacksonUtil.json2Bean(json, CmsUsaProductSalesMQMessageBody.class);
//        CmsUsaProductSalesMQMessageBody body = new CmsUsaProductSalesMQMessageBody();
//        CmsUsaProductSalesMQMessageBody.Param param = new CmsUsaProductSalesMQMessageBody.Param();

//        body.setSender("xusong");
//
//        param.setCartId(23);
//        param.setQty(20);
//        body.setChannelId("001");
//        param.setStatus(1);
//        param.setOrderDate(1460736000000L);
//        param.setSku("aj6961-m");
//        ArrayList<CmsUsaProductSalesMQMessageBody.Param> params = new ArrayList<>();
//        params.add(param);
//        body.setItems(params);
        cmsUsaProductSalesMQJob.onStartup(body);
    }
}