package com.voyageone.web2.sdk.api.request;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.VmsOrderAddResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderCancelResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderInfoGetResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderStatusUpdateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jeff.duan
 * @version 1.0.0, 16/7/20
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
        Map<String, Object> item = new HashMap<>();
        item.put("channelId", "088");
        item.put("reservationId","10001");
        item.put("consolidationOrderId","cons_order_10001");
        item.put("orderId", "order_10001");
        item.put("clientSku", "sku10001");
        item.put("barcode","barcode10001");
        item.put("consolidationOrderTime", now);
        item.put("description", "description10001");
        item.put("orderTime", now);
        item.put("cartId",23);
        item.put("clientMsrp",70.00);
        item.put("clientNetPrice",71.00);
        item.put("clientRetailPrice",72.00);
        item.put("retailPrice", 170.00);
        List<Map<String, Object>> itemList = new ArrayList<>();
        itemList.add(item);

        Map<String, Object> item1 = new HashMap<>();
        item1.put("channelId", "088");
        item1.put("reservationId","10002");
        item1.put("consolidationOrderId","cons_order_10002");
        item1.put("orderId", "order_10002");
        item1.put("clientSku", "sku10002");
        item1.put("barcode","barcode10002");
        item1.put("consolidationOrderTime", now);
        item1.put("description", "description10002");
        item1.put("orderTime", now);
        item1.put("cartId",23);
        item1.put("clientMsrp",70.00);
        item1.put("clientNetPrice",71.00);
        item1.put("clientRetailPrice",72.00);
        item1.put("retailPrice", 170.00);
        itemList.add(item1);

        request.setItemList(itemList);

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


    @Test
    public void testGetOrderInfoByReservationId() {
        VmsOrderInfoGetRequest request = new VmsOrderInfoGetRequest();
        request.setChannelId("088");
        request.setReservationId("101");

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderInfoGetResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }

    @Test
    public void testGetOrderInfoByShipmentTime() throws ParseException {
        VmsOrderInfoGetRequest request = new VmsOrderInfoGetRequest();
        request.setChannelId("088");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
        String str="2015-01-01 10:00:00";
        Date date = sdf.parse(str);
        request.setTimeFrom(date.getTime());
        str="2016-12-01 10:00:00";
        date = sdf.parse(str);
        request.setTimeTo(date.getTime());
        request.setType("1");

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderInfoGetResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }

    @Test
    public void testGetOrderInfoByCancelTime() throws ParseException {
        VmsOrderInfoGetRequest request = new VmsOrderInfoGetRequest();
        request.setChannelId("088");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
        String str="2015-01-01 10:00:00";
        Date date = sdf.parse(str);
        request.setTimeFrom(date.getTime());
        str="2016-12-01 10:00:00";
        date = sdf.parse(str);
        request.setTimeTo(date.getTime());
        request.setType("2");

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderInfoGetResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }

    @Test
    public void testUpdateOrderStatusWithReceived() throws ParseException {
        VmsOrderStatusUpdateRequest request = new VmsOrderStatusUpdateRequest();
        request.setChannelId("088");
        request.setReservationId("101");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
        String str="2016-01-01 10:00:00";
        Date date = sdf.parse(str);
        request.setReceivedTime(date.getTime());
        request.setReceiver("djs");
        request.setStatus("5");

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderStatusUpdateResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }

    @Test
    public void testUpdateOrderStatusWithReceivedError() throws ParseException {
        VmsOrderStatusUpdateRequest request = new VmsOrderStatusUpdateRequest();
        request.setChannelId("088");
        request.setReservationId("101");
        request.setStatus("6");

        //SDK取得Product 数据
        voApiDefaultClient.setNeedCheckRequest(false);
        VmsOrderStatusUpdateResponse response = voApiDefaultClient.execute(request);

        System.out.println(response);
    }
}