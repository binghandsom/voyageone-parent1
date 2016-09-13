package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.common.util.HttpExcuteUtils;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/9/7.
 * @version 2.0.0
 * @since 2.0.0
 */
@ActiveProfiles("standbox")
public class ProductControllerTest {

    private static final String BASE_URL = "http://localhost:9090/rest/channeladvisor/";

    private static final Map HEADER=new HashMap<>();

    static {
        HEADER.put("SellerID","123sdfwewqqw");
    }

    @Test
    public  void run() throws Exception {
        for (int i=0;i<200;i++){

            productTest();
            orderTest();
        }

    @Test
    public void productTest() throws Exception {
        //获取产品
        String url = BASE_URL + CAUrlConstants.PRODUCTS.GET_PRODUCTS;
        Map<String, String> header = new HashMap<>();
        header.put("SellerID", "049beea8-bdd1-48f0-a930-e56e42f85458");
        header.put("SellerToken", "caf8e5ed-16c4-40d8-92ce-1ce86e03cac5");
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url);
        System.out.println(result);
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.concat("?groupFields=123,4324,345"));
        System.out.println(result);
        //添加或更新产品
        url = BASE_URL + CAUrlConstants.PRODUCTS.UPDATE_PRODUCTS;
        String reqJson = "[{\"SellerSKU\":\"REBEL X-WING\",\"Fields\":[{\"Name\":\"title\",\"Value\":\"Rebel X-Wing\"},{\"Name\":\"description\",\"Value\":\"Got a Death Star porblem at your base? Take it out with this small craft (PS, aim for the 2-meter exhaust port). Buy now! Republic credits not accepted.\"},{\"Name\":\"category\",\"Value\":\"Transportation : Spacecraft : Single-Passenger\"},{\"Name\":\"brand\",\"Value\":\"Rebel Forces, Inc.\"},{\"Name\":\"productgroupimageurl1\",\"Value\":\"http://courses.cs.washington.edu/courses/cse455/08wi/projects/project3/web/artifacts/jonobird-ncall/artifact/xw1.jpg\"}],\"BuyableProducts\":[{\"SellerSKU\":\"REBEL X-WING\",\"Quantity\":23,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"100.00\"},{\"Name\":\"color\",\"Value\":\"gray\"},{\"Name\":\"size\",\"Value\":\"one-man\"}]}]},{\"SellerSKU\":\"LIGHTSABER\",\"Fields\":[{\"Name\":\"title\",\"Value\":\"Lightsaber\"},{\"Name\":\"description\",\"Value\":\"This is the weapon of a Jedi Knight. Not as clumsy or random as a blaster; an elegant weapon for a more civilized age.\"},{\"Name\":\"category\",\"Value\":\"Civilized Weapons\"},{\"Name\":\"brand\",\"Value\":\"Jedi Knights\"},{\"Name\":\"productgroupimageurl1\",\"Value\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/d/df/Dueling_lightsabers.svg/2000px-Dueling_lightsabers.svg.png\"}],\"BuyableProducts\":[{\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"Quantity\":14,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"14.56\"},{\"Name\":\"color\",\"Value\":\"red\"},{\"Name\":\"size\",\"Value\":\"medium\"}]},{\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"Quantity\":9,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"15.56\"},{\"Name\":\"color\",\"Value\":\"red\"},{\"Name\":\"size\",\"Value\":\"large\"}]},{\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"Quantity\":17,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"11.56\"},{\"Name\":\"color\",\"Value\":\"blue\"},{\"Name\":\"size\",\"Value\":\"medium\"}]},{\"SellerSKU\":\"LIGHTSABER_BLUE2_LG\",\"Quantity\":15,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"12.56\"},{\"Name\":\"color\",\"Value\":\"blue\"},{\"Name\":\"size\",\"Value\":\"large\"}]}]}]";
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url, reqJson,header);
        System.out.println(result);
        //更新价格或数量
        url = BASE_URL + CAUrlConstants.PRODUCTS.UPDATE_QUANTITY_PRICE;
        reqJson = "[{\"SellerSKU\":\"REBEL X-WING\",\"Fields\":[],\"BuyableProducts\":[{\"SellerSKU\":\"REBEL X-WING\",\"Quantity\":32,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"100.00\"}]}]},{\"SellerSKU\":\"LIGHTSABER\",\"Fields\":[],\"BuyableProducts\":[{\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"Quantity\":14,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"14.56\"}]},{\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"Quantity\":9,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"15.56\"}]},{\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"Quantity\":17,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"11.56\"}]},{\"SellerSKU\":\"LIGHTSABER_BLUE2_LG\",\"Quantity\":30,\"ListingStatus\":\"Live\",\"Fields\":[{\"Name\":\"price\",\"Value\":\"22.56\"}]}]}]";
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url, reqJson, header);
        System.out.println(result);
        //更新状态
        url = BASE_URL + CAUrlConstants.PRODUCTS.UPDATE_STATUS;
        reqJson = "[{\"SellerSKU\":\"REBEL X-WING\",\"Fields\":[],\"BuyableProducts\":[{\"SellerSKU\":\"REBEL X-WING\",\"Quantity\":0,\"ListingStatus\":\"NotLive\",\"Fields\":null}]},{\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"Fields\":[],\"BuyableProducts\":[{\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"Quantity\":0,\"ListingStatus\":\"NotLive\",\"Fields\":null}]},{\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"Fields\":[],\"BuyableProducts\":[{\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"Quantity\":0,\"ListingStatus\":\"NotLive\",\"Fields\":null}]},{\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"Fields\":[],\"BuyableProducts\":[{\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"Quantity\":0,\"ListingStatus\":\"NotLive\",\"Fields\":null}]},{\"SellerSKU\":\"LIGHTSABER_BLUE_LG\",\"Fields\":[],\"BuyableProducts\":[{\"SellerSKU\":\"LIGHTSABER_BLUE_LG\",\"Quantity\":0,\"ListingStatus\":\"NotLive\",\"Fields\":null}]}]";
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url, reqJson);
        System.out.println(result);
    }

    private static void orderTest() throws Exception {
        //获取批量订单
        String url = BASE_URL + CAUrlConstants.ORDERS.GET_ORDERS;
        String result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url,null,HEADER);
        System.out.println(result);
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.concat("?limit=10"),null,HEADER);
        System.out.println(result);
        //根据id查询订单
        url = BASE_URL + CAUrlConstants.ORDERS.GET_ORDER;
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.GET, url.replace("{id}","546856"),null,HEADER);
        System.out.println(result);
        //确认订单
        url = BASE_URL + CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER;
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}","546856"),null,HEADER);
        System.out.println(result);
        //运输订单
        url = BASE_URL + CAUrlConstants.ORDERS.SHIP_ORDER;
        String reqJson = "{\"ShippedDateUtc\":\"2016-09-07T04:52:32.9431095Z\",\"TrackingNumber\":\"Z123456789J\",\"ShippingCarrier\":\"Bantha Union\",\"ShippingClass\":\"Express\",\"Items\":{\"LIGHTSABER_BLUE_MED\":1,\"LIGHTSABER_RED_LG\":4,\"REBEL X-WING\":5}}";
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}","546856"), reqJson,HEADER);
        System.out.println(result);
        //取消订单
        url = BASE_URL + CAUrlConstants.ORDERS.CANCEL_ORDER;
        reqJson = "{\"OrderID\":\"M456W\",\"Items\":[{\"ID\":\"M333W\",\"SellerSku\":\"LIGHTSABER_BLUE_MED\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"},{\"ID\":\"M222W\",\"SellerSku\":\"LIGHTSABER_RED_LG\",\"Quantity\":2,\"Reason\":\"CustomerExchange\"},{\"ID\":\"M000W\",\"SellerSku\":\"REBEL X-WING\",\"Quantity\":3,\"Reason\":\"MerchandiseNotReceived\"}]}";
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}","546856"), reqJson,HEADER);
        System.out.println(result);
        //退返订单
        url = BASE_URL + CAUrlConstants.ORDERS.REFUND_ORDER;
        reqJson = "{\"OrderID\":\"M456W\",\"Items\":[{\"ID\":\"M333W\",\"SellerSku\":\"LIGHTSABER_BLUE_MED\",\"Quantity\":1,\"Reason\":\"BuyerCanceled\"},{\"ID\":\"M222W\",\"SellerSku\":\"LIGHTSABER_RED_LG\",\"Quantity\":2,\"Reason\":\"CustomerExchange\"},{\"ID\":\"M000W\",\"SellerSku\":\"REBEL X-WING\",\"Quantity\":3,\"Reason\":\"MerchandiseNotReceived\"}]}";
        result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url.replace("{id}","546856"), reqJson,HEADER);
        System.out.println(result);

    }
}