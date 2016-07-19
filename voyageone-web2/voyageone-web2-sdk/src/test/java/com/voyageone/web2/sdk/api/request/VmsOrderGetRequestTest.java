package com.voyageone.web2.sdk.api.request;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.VmsOrderAddResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderCancelResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * @author Edward
 * @version 2.0.0, 16/2/3
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class VmsOrderGetRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiDefaultClient;

    @Test
    public void testAddOrderInfo() {
        long now =  DateTimeUtil.getNowTimeStampLong();

        VmsOrderAddRequest request = new VmsOrderAddRequest();
        request.setChannelId("088");
        request.setReservationId("10001");
        request.setConsolidationOrderId("cons_order_10001");
        request.setOrderId("order_10001");
        request.setClientSku("sku10001");
        request.setBarcode("barcode10001");
        request.setConsolidationOrderTime(now);
        request.setDescription("description10001");
        request.setOrderTime(now);
        request.setCartId(23);
        request.setClientMsrp(70.00);
        request.setClientNetPrice(70.00);
        request.setClientRetailPrice(70.00);
        request.setMsrp(70.00);
        request.setRetailPrice(70.00);
        request.setSalePrice(70.00);

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderAddResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }

    @Test
    public void testCancelOrderInfo() {

        VmsOrderCancelRequest request = new VmsOrderCancelRequest();
        request.setChannelId("088");
        request.setReservationIdList(new ArrayList<String>(){{add("101");add("102");add("103");}});

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderCancelResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }
}