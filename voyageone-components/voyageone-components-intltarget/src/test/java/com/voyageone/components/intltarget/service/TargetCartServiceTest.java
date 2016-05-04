package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.bean.cart.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/5/4.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TargetCartServiceTest {

    @Autowired
    private TargetCartService targetCartService;

    @Autowired
    private TargetOrderService targetOrderService;

    @Test
    public void load() throws Exception {
        /* step 1 添加产品到购物车 */
        String data = "{\"productId\":\"205002726\",\"quantity\":\"1\"}";
        System.out.println(JacksonUtil.bean2Json(targetCartService.addProductToCart(JacksonUtil.json2Bean(data, TargetCartAddProductRequest.class))));
        /* step 2 获取使用的运输地址 */
        TargetCartUsableShippingAddressResponse response = targetCartService.getUsableShippingAddress();
        System.out.println(JacksonUtil.bean2Json(response));
        /* step 3 申请使用的运输地址 */
        String orderItemId = response.getOrderItem().get(0).getOrderItemId();
        String data1 = "{\"address\":[{\"firstName\":\"Richard\",\"middleName\":\"L\",\"lastName\":\"Thompson\",\"addressLine\":[\"416 water st\",\"Apt Ny 10002\"],\"city\":\"New York\",\"state\":\"NY\",\"zipCode\":\"10002\",\"phone\":\"1234512345\",\"phoneType\":\"Home\",\"skipAddressValidation\":\"Y\"}],\"orderItem\":[{  \"orderItemId\":\"" + orderItemId + "\",\"shipModeId\":\"12051\"}]}\n";
        System.out.println(JacksonUtil.bean2Json(targetCartService.applyShippingDetails(JacksonUtil.json2Bean(data1, TargetCartApplyShippingDetailRequest.class))));
        /* step 4 添加支付卡 */
        String data2 = "{\"phone1\":\"95626104220\",\"expiryMonth\":\"4\",\"expiryYear\":\"2020\",\"country\":\"IN\",\"billingStateProvince\":\"Kerala\",\"zipCode\":\"68581\",\"cardName\":\"Name on card\",\"address1\":\"Kazhakottam\",\"address2\":\"Attipara Village\",\"lastName\":\"Tunish\",\"firstName\":\"Neethu\",\"cvv\":\"123\",\"cardType\":\"Visa\",\"city\":\"Trivandrum\",\"cardNumber\":\"4352740415972154\"}\t\n";
        System.out.println(JacksonUtil.bean2Json(targetCartService.addTenders(JacksonUtil.json2Bean(data2, TargetCartAddTenderRequest.class))));
        /* step 5 订单review */
        TargetCartOrderReviewResponse reviewResponse=targetCartService.orderReview();
        System.out.println(JacksonUtil.bean2Json(reviewResponse));
        /* step 6 检出订单 */
        String data3 = "{\"emailId\":\"bingbing.gao@voyageone.com\"}";
        System.out.println(JacksonUtil.bean2Json(targetCartService.checkout(JacksonUtil.json2Bean(data3, TargetCartCheckoutRequest.class))));
        /* step 7 查询订单状态 */
        System.out.println(JacksonUtil.bean2Json(targetOrderService.getOrderDetails(reviewResponse.getOrderId())));
    }

    @Test
    public void testAddProductToCart() throws Exception {
        String data = "{\"productId\":\"205002726\",\"quantity\":\"1\"}";
        System.out.println(JacksonUtil.bean2Json(targetCartService.addProductToCart(JacksonUtil.json2Bean(data, TargetCartAddProductRequest.class))));
        //response
        //{"totalQuantity":"1","orderItem":[{"usableShippingMode":[{"fulfillmentType":"ShipToHome"},{"fulfillmentType":"StorePickup"}],"attachments":[{"path":"https://scene7-secure.targetimg1.com/is/image/Target/14151825?wid=75&hei=75","usage":"THUMBNAIL_75"}],"giftWrappable":"Y","itemAttributes":[{"attrValue":"Y","attrName":"isOnlineEligible"},{"attrValue":"Y","attrName":"isPickUpEligible"}],"orderItemPriceCurrency":"USD","giftWrapChargePerUnitCurrency":"USD","DPCI":"081-04-0343","isSTS":"false","giftWrapChargePerUnit":"5.99","orderItemPrice":"2.99","offers":null,"unitPrice":"2.99","quantity":"1.0","orderItemId":"51790021","inventoryStatus":"0","isAO":"false","maxPurchaseLimit":"99.0","frequencyInterval":"4 weeks|4-WEE,6 weeks|6-WEE,8 weeks|8-WEE,10 weeks|10-WEE,12 weeks|12-WEE,16 weeks|16-WEE,20 weeks|20-WEE,26 weeks|26-WEE","isShipToHomeEligible":"false","purchaseChannelId":"0","isAOEligible":"false","name":"Crayola Crayons - 64ct","catalogEntryId":"205002726","attributes":[{"identifier":null,"name":null,"description":null}],"partNumber":"14151825","giftWrapStatus":"false","isSFSEligible":"true"}],"responseTime":"1689"}
    }

    @Test
    public void testGetUsableShippingAddress() throws Exception {
        System.out.println(JacksonUtil.bean2Json(targetCartService.getUsableShippingAddress()));
    }

    @Test
    public void testApplyShippingDetails() throws Exception {

        TargetCartUsableShippingAddressResponse response = targetCartService.getUsableShippingAddress();
        String orderItemId = response.getOrderItem().get(0).getOrderItemId();

        String data = "{\"address\":[{\"firstName\":\"Richard\",\"middleName\":\"L\",\"lastName\":\"Thompson\",\"addressLine\":[\"416 water st\",\"Apt Ny 10001\"],\"city\":\"New York\",\"state\":\"NY\",\"zipCode\":\"10002\",\"phone\":\"1234512345\",\"phoneType\":\"Home\",\"skipAddressValidation\":\"N\"}],\"orderItem\":[{  \"orderItemId\":\"" + orderItemId + "\",\"shipModeId\":\"12051\"}]}\n";
        System.out.println(JacksonUtil.bean2Json(targetCartService.applyShippingDetails(JacksonUtil.json2Bean(data, TargetCartApplyShippingDetailRequest.class))));
    }

    @Test
    public void testAddTenders() throws Exception {
        String data = "{\"phone1\":\"9562604220\",\"expiryMonth\":\"4\",\"expiryYear\":\"2020\",\"country\":\"IN\",\"billingStateProvince\":\"Kerala\",\"zipCode\":\"68581\",\"cardName\":\"Name on card\",\"address1\":\"Kazhakottam\",\"address2\":\"Attipara Village\",\"lastName\":\"Tunish\",\"firstName\":\"Neethu\",\"cvv\":\"123\",\"cardType\":\"Visa\",\"city\":\"Trivandrum\",\"cardNumber\":\"4352740415972154\"}\t\n";
        System.out.println(JacksonUtil.bean2Json(targetCartService.addTenders(JacksonUtil.json2Bean(data, TargetCartAddTenderRequest.class))));
    }

    @Test
    public void testOrderReview() throws Exception {
        System.out.println(JacksonUtil.bean2Json(targetCartService.orderReview()));
    }

    @Test
    public void testCheckout() throws Exception {
        String data = "{\"emailId\":\"bingbing.gao@voyageone.cn\"}";
        System.out.println(JacksonUtil.bean2Json(targetCartService.checkout(JacksonUtil.json2Bean(data, TargetCartCheckoutRequest.class))));
    }
}