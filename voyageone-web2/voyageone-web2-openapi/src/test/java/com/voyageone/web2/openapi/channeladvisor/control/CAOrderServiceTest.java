package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.common.util.HttpExcuteUtils;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/9/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@ActiveProfiles("product")
public class CAOrderServiceTest {

    private static final String BASE_URL = "http://localhost:8080/rest/channeladvisor/";

    private static final Map<String, String> HEADER = new HashMap<>();

    static {
        /**
         * userAccount:                 vms_channeladvisor
         * clientId(SellerID):          channeladvisor
         * clientSecret(SellerToken):   Y2hhbm5lbGFkdmlzb3I=
         * channelId:                   028
         */
        HEADER.put("SellerID", "channeladvisor");
        HEADER.put("SellerToken", "Y2hhbm5lbGFkdmlzb3I=");
    }


    @Test
    public void testGetOrders() throws Exception {
        //status、limit未设置
        String url = BASE_URL + "orders";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url, null, HEADER);
        System.out.println(result);
        //status=Pending、limit未设置
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.concat("?status=Pending"), null, HEADER);
        System.out.println(result);
        //status未设置、limit=10
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.concat("?limit=10"), null, HEADER);
        System.out.println(result);
        //status=ReleasedForShipment、limit未设置
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.concat("?status=ReleasedForShipment"), null, HEADER);
        System.out.println(result);
        //status=ReleasedForShipment、limit=8
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.concat("?status=ReleasedForShipment&limit=8"), null, HEADER);
        System.out.println(result);
    }

    @Test
    public void testGetOrderById() throws Exception {
        //id在表中不存在
        String url = BASE_URL + "orders/0102016051616000213210005";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url, null, HEADER);
        System.out.println(result);
        //id在表中存在
        url = BASE_URL + "orders/02820160919111115001";
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url, null, HEADER);
        System.out.println(result);
    }

    @Test
    public void testAcknowledgeOrder() throws Exception {
        //id在表中不存在
        /*String url = BASE_URL + CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER;
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "010201605161600023232230005"), null, HEADER);
        System.out.println(result);*/
        //id在表中存在
        String url = BASE_URL + CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER;
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111119012"), null, HEADER);
        System.out.println(result);
    }

    @Test
    public void testShipOrder() throws Exception {
        //id在表中不存在
        String url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        /*String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789J\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3108487\":1,\"3321289\":1,\"3117503\":1,\"3569376\":1," +
                "\"2700002\":1,\"3035462\":1,\"3338667\":1,\"3037316\":1,\"3483906\":1,\"3100191\":1,\"1737685\":1}}";
        *//*String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919114353441115001"), reqJson, HEADER);
        System.out.println(result);*//*
        //订单A：所有物品使用同一运单全部发货
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);*/

        //部分物品发货
       /* String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789J\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3562870\":1,\"3373733\":1,\"3108487\":1,\"3100066\":1," +
                "\"3007085\":1,\"3116543\":1,\"3643406\":1}}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115002"), reqJson, HEADER);
        System.out.println(result);//,"3117503":1,"2690233":1,"3470231":1*/

        //上部分物品发货
        /*String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789Q\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3109577\":1,\"3463654\":1,\"3457939\":1,\"3567694\":1,\"3116543\":1" +
                ",\"3567751\":1,\"3437358\":1}}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111116003"), reqJson, HEADER);
        System.out.println(result);//,"3645966":1,"2689852":1,"2818819":1*/

        //下部分物品发货
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789W\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3645966\":1,\"2689852\":1,\"2818819\":1}}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111116003"), reqJson, HEADER);
        System.out.println(result);//,"3645966":1,"2689852":1,"2818819":1
    }

    @Test
    public void testCancelOrder() throws Exception {
        //同一取消理由取消订单
        String url = BASE_URL + CAUrlConstants.ORDERS.CANCEL_ORDER;
        /*String reqJson = "{\"OrderID\":\"02820160919111116004\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3483906\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3665253\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W3\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W4\",\"SellerSku\":\"1840405\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W5\",\"SellerSku\":\"2541787\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W6\",\"SellerSku\":\"3337666\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W7\",\"SellerSku\":\"2524220\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W8\",\"SellerSku\":\"3117390\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W9\",\"SellerSku\":\"3229838\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W10\",\"SellerSku\":\"3451718\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}" +
                "]}";
        //String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "028201609123119111115001"), reqJson, HEADER);
        //System.out.println(result);
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111116004"), reqJson, HEADER);
        System.out.println(result);*/

        //不同取消理由
       /* String reqJson = "{\"OrderID\":\"02820160919111116005\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3681313\",\"Quantity\":1,\"Reason\":\"Other\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"1520401\",\"Quantity\":1,\"Reason\":\"GeneralAdjustment\"}," +
                "{\"ID\":\"M333W3\",\"SellerSku\":\"2711840\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W4\",\"SellerSku\":\"3114141\",\"Quantity\":1,\"Reason\":\"CustomerReturnedItem\"}," +
                "{\"ID\":\"M333W5\",\"SellerSku\":\"3097329\",\"Quantity\":1,\"Reason\":\"CouldNotShip\"}," +
                "{\"ID\":\"M333W6\",\"SellerSku\":\"3108978\",\"Quantity\":1,\"Reason\":\"AlternateItemProvided\"}," +
                "{\"ID\":\"M333W7\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W8\",\"SellerSku\":\"3624579\",\"Quantity\":1,\"Reason\":\"CustomerExchange\"}," +
                "{\"ID\":\"M333W9\",\"SellerSku\":\"3368458\",\"Quantity\":1,\"Reason\":\"MerchandiseNotReceived\"}," +
                "{\"ID\":\"M333W10\",\"SellerSku\":\"3582316\",\"Quantity\":1,\"Reason\":\"ShippingAddressUndeliverable\"}" +
                "]}";

        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111116005"), reqJson, HEADER);
        System.out.println(result);*/

        /*//部分物品取消
        String reqJson = "{\"OrderID\":\"02820160919111117006\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3402153\",\"Quantity\":1,\"Reason\":\"Other\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3525055\",\"Quantity\":1,\"Reason\":\"GeneralAdjustment\"}" +
                "]}";

        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111117006"), reqJson, HEADER);
        System.out.println(result);*/

        //上部分物品取消
       /* String reqJson = "{\"OrderID\":\"02820160919111117007\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"1965967\",\"Quantity\":1,\"Reason\":\"Other\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3624162\",\"Quantity\":1,\"Reason\":\"GeneralAdjustment\"}," +
                "{\"ID\":\"M333W3\",\"SellerSku\":\"2411328\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W4\",\"SellerSku\":\"3110659\",\"Quantity\":1,\"Reason\":\"CustomerReturnedItem\"}," +
                "{\"ID\":\"M333W5\",\"SellerSku\":\"3169246\",\"Quantity\":1,\"Reason\":\"CouldNotShip\"}" +
                "]}";

        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111117007"), reqJson, HEADER);
        System.out.println(result);*/

        //下部分物品取消
        String reqJson = "{\"OrderID\":\"02820160919111117007\",\"Items\":[" +
                "{\"ID\":\"M333W6\",\"SellerSku\":\"3117390\",\"Quantity\":1,\"Reason\":\"AlternateItemProvided\"}," +
                "{\"ID\":\"M333W7\",\"SellerSku\":\"3109577\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W8\",\"SellerSku\":\"3456083\",\"Quantity\":1,\"Reason\":\"CustomerExchange\"}," +
                "{\"ID\":\"M333W9\",\"SellerSku\":\"3108978\",\"Quantity\":1,\"Reason\":\"MerchandiseNotReceived\"}," +
                "{\"ID\":\"M333W11\",\"SellerSku\":\"3461477\",\"Quantity\":1,\"Reason\":\"MerchandiseNotReceived\"}," +
                "{\"ID\":\"M333W10\",\"SellerSku\":\"3055246\",\"Quantity\":1,\"Reason\":\"ShippingAddressUndeliverable\"}" +
                "]}";

        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111117007"), reqJson, HEADER);
        System.out.println(result);
    }

    @Test
    public void testRefundOrder() throws Exception {

        //id在表中不存在
        /*String url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789U\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"2963720\":1,\"3337520\":1,\"3117624\":1,\"2784405\":1," +
                "\"2237491\":1,\"3108487\":1,\"2868623\":1,\"1160427\":1,\"3385170\":1,\"3114141\":1}}";
        //订单A：所有物品使用同一运单全部发货
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111117008"), reqJson, HEADER);
        System.out.println(result);*/

        ///同一理由退货
       /* String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"02820160919111117008\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"2963720\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3337520\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W3\",\"SellerSku\":\"3117624\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W4\",\"SellerSku\":\"2784405\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W5\",\"SellerSku\":\"2237491\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W6\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W7\",\"SellerSku\":\"2868623\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W8\",\"SellerSku\":\"1160427\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W9\",\"SellerSku\":\"3385170\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W10\",\"SellerSku\":\"3114141\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111117008"), reqJson, HEADER);
        System.out.println(result);*/

        //不同一理由退货
        /*String url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789I\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3114141\":7,\"3564246\":1,\"3117503\":1,\"3325995\":1," +
                "\"3337245\":1}}";
        //订单A：所有物品使用同一运单全部发货
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111117009"), reqJson, HEADER);
        System.out.println(result);
*/
        /*String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"02820160919111117009\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3114141\",\"Quantity\":7,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3564246\",\"Quantity\":1,\"Reason\":\"GeneralAdjustment\"}," +
                "{\"ID\":\"M333W3\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"ItemNotAvailable\"}," +
                "{\"ID\":\"M333W4\",\"SellerSku\":\"3325995\",\"Quantity\":1,\"Reason\":\"Other\"}," +
                "{\"ID\":\"M333W5\",\"SellerSku\":\"3337245\",\"Quantity\":1,\"Reason\":\"AlternateItemProvided\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111117009"), reqJson, HEADER);
        System.out.println(result);*/

        //部分退货
       //不同一理由退货
        /*String url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789I\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3114141\":9,\"3117503\":1,\"1908350\":1,\"3091639\":1}}";
        //订单A：所有物品使用同一运单全部发货
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111118011"), reqJson, HEADER);
        System.out.println(result);*/

       /* String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"02820160919111117009\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3114141\",\"Quantity\":7,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"GeneralAdjustment\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111118011"), reqJson, HEADER);
        System.out.println(result);
*/


        //上部分退货
        //不同一理由退货
        /*String url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789O\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3114141\":7,\"3108978\":2,\"3108487\":1,\"3108666\":1}}";
        //订单A：所有物品使用同一运单全部发货
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111119012"), reqJson, HEADER);
        System.out.println(result);*/


      /*String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"02820160919111117009\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3114141\",\"Quantity\":5,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3108978\",\"Quantity\":1,\"Reason\":\"GeneralAdjustment\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111119012"), reqJson, HEADER);
        System.out.println(result);*/

        //下部分退货
        String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"02820160919111117009\",\"Items\":[" +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3114141\",\"Quantity\":2,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"MerchandiseNotReceived\"}," +
                "{\"ID\":\"M333W1\",\"SellerSku\":\"3108666\",\"Quantity\":1,\"Reason\":\"ShippingAddressUndeliverable\"}," +
                "{\"ID\":\"M333W2\",\"SellerSku\":\"3108978\",\"Quantity\":1,\"Reason\":\"GeneralAdjustment\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111119012"), reqJson, HEADER);
        System.out.println(result);

    }
}