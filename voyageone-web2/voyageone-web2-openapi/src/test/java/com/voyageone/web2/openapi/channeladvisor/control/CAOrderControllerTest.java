package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.common.util.HttpExcuteUtils;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author aooer 2016/9/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@ActiveProfiles("product")
public class CAOrderControllerTest {

    private static final String BASE_URL = "http://localhost:8080/rest/channeladvisor/";

    private static final Map<String, String> HEADER = new HashMap<>();

    static {
        /**
         * userAccount:                 vms_channeladvisor
         * clientId(SellerID):          channeladvisor
         * clientSecret(SellerToken):   Y2hhbm5lbGFkdmlzb3I=
         * channelId:                   028
         */
        HEADER.put("SellerID", "049beea8-bdd1-48f0-a930-e56e42f85458");
        HEADER.put("SellerToken", "caf8e5ed-16c4-40d8-92ce-1ce86e03cac5");
    }

    @Test
    public void rateLimiter() throws Exception {
        for (int i = 0; i < 1100; i++) {
            testGetOrders();
        }
    }


    @Test
    public void testGetOrders() throws Exception {
        //获取批量订单
        String url = BASE_URL + "orders";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url, null, HEADER);
        System.out.println(result);
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.concat("?status=Pending"), null, HEADER);
        System.out.println(result);
    }

    @Test
    public void testGetOrderById() throws Exception {
        //根据id查询订单
        String url = BASE_URL + "orders/01020160516160000005";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url, null, HEADER);
        System.out.println(result);
    }

    @Test
    public void testAcknowledgeOrder() throws Exception {
        //确认订单
        String url = BASE_URL + CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER;
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "01020160516160000005"), null, HEADER);
        System.out.println(result);
    }

    @Test
    public void testShipOrder() throws Exception {
        //运输订单
        String url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-07T04:52:32.9431095Z\",\"TrackingNumber\":\"Z123456789J\",\"ShippingCarrier\":\"Bantha Union\",\"ShippingClass\":\"Express\",\"Items\":{\"C900555SRML1\":1,\"C900555SRML2\":1,\"C900555SRML3\":1}}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "01020160516152000003"), reqJson, HEADER);
        System.out.println(result);
    }

    @Test
    public void testCancelOrder() throws Exception {
        //取消订单
        String url = BASE_URL + CAUrlConstants.ORDERS.CANCEL_ORDER;
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[{\"ID\":\"M333W\",\"SellerSku\":\"C900555SRML1\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"}]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "01020160516152000003"), reqJson, HEADER);
        System.out.println(result);
    }

    @Test
    public void testRefundOrder() throws Exception {
        //退返订单
        String url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        String reqJson = "{\"OrderID\":\"M456W\",\"Items\":[{\"ID\":\"M333W\",\"SellerSku\":\"C900555SRML1\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"},{\"ID\":\"M222W\",\"SellerSku\":\"LIGHTSABER_RED_LG\",\"Quantity\":2,\"Reason\":\"CustomerExchange\"},{\"ID\":\"M000W\",\"SellerSku\":\"REBEL X-WING\",\"Quantity\":3,\"Reason\":\"MerchandiseNotReceived\"}]}";
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}", "01020160516152000003"), reqJson, HEADER);
        System.out.println(result);
    }
}