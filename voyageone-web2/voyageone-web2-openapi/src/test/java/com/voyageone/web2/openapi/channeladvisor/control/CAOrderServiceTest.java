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
        String url = BASE_URL + CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER;
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "010201605161600023232230005"), null, HEADER);
        System.out.println(result);
        //id在表中存在
        url = BASE_URL + CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER;
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), null, HEADER);
        System.out.println(result);
    }

    @Test
    public void testShipOrder() throws Exception {
        //id在表中不存在
        String url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-19T04:52:32.9431095Z\"," +
                "\"TrackingNumber\":\"Z123456789J\"," +
                "\"ShippingCarrier\":\"Bantha Union\"," +
                "\"ShippingClass\":\"Express\"," +
                "\"Items\":{\"3117503\":1}}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919114353441115001"), reqJson, HEADER);
        System.out.println(result);
        //订单A：所有物品使用同一运单全部发货
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);
    }

    @Test
    public void testCancelOrder() throws Exception {
        //同一取消理由取消订单
        String url = BASE_URL + CAUrlConstants.ORDERS.CANCEL_ORDER;
        /*String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3321289\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "028201609123119111115001"), reqJson, HEADER);
        System.out.println(result);
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);*/
        //不同取消理由
        /*String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3321289\",\"Quantity\":1,\"Reason\":\"Other\"}" +
                "]}";

        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);*/

        //不同取消理由
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}"+
                "]}";

        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);
    }

    @Test
    public void testRefundOrder() throws Exception {
        //退返订单
        /*String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3321289\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "010201605161512312000003"), reqJson, HEADER);
        System.out.println(result);*/

        /*//同一理由退货
        String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3321289\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);*/

        /*//不同一理由退货
        String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3321289\",\"Quantity\":1,\"Reason\":\"Other\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"AlternateItemProvided\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);*/

        /*//部分退货
        String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3108487\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}," +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3321289\",\"Quantity\":1,\"Reason\":\"Other\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);
*/

        //部分退货
        String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[" +
                "{\"ID\":\"M333W\",\"SellerSku\":\"3117503\",\"Quantity\":1,\"Reason\":\"AlternateItemProvided\"}" +
                "]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "02820160919111115001"), reqJson, HEADER);
        System.out.println(result);

    }
}