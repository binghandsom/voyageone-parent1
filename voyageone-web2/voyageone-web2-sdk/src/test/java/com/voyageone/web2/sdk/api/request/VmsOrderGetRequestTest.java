package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductForWmsGetResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderAddGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

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
        Date date = new Date();
        VmsOrderAddGetRequest request = new VmsOrderAddGetRequest();
        request.setChannelId("088");
        request.setReservationId("10001");
        request.setConsolidationOrderId("cons_order_10001");
        request.setOrderId("order_10001");
        request.setClientSku("sku10001");
        request.setBarcode("barcode10001");
        request.setConsolidationOrderTime(date);
        request.setDescription("description10001");
        request.setOrderTime(date);
        request.setCartId(23);
        request.setClientMsrp(new BigDecimal(70.00));
        request.setClientNetPrice(new BigDecimal(70.00));
        request.setClientRetailPrice(new BigDecimal(70.00));
        request.setMsrp(new BigDecimal(70.00));
        request.setRetailPrice(new BigDecimal(70.00));
        request.setSalePrice(new BigDecimal(70.00));

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderAddGetResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }
}