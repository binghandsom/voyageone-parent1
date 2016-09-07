package com.voyageone.web2.openapi.channeladvisor.service;

import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.sdk.api.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.web2.sdk.api.channeladvisor.request.ShipRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CAOrderService extends CAOpenApiBaseService {

    public ActionResponse getOrders(String status, String limit) {
        /* 用来限制查询订单的状态，默认任何状态 */
        if (StringUtils.isEmpty(status)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        /* 用来限制查询订单的条数，默认全部条数 */
        if (StringUtils.isEmpty(limit)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        String jsonData = "{\"ResponseBody\":[{\"ID\":\"M123W\",\"OrderStatus\":\"ReleasedForShipment\",\"OrderDateUtc\":\"2016-09-07T04:32:32.9275092Z\",\"BuyerAddress\":{\"EmailAddress\":\"tatooine_sales@tosche-stations.com\",\"FirstName\":\"Watto\",\"LastName\":\"Toydarian\",\"AddressLine1\":\"321 Landing St.\",\"City\":\"Mos Eisley\",\"Country\":\"US\",\"PostalCode\":\"ME-123\",\"StateOrProvince\":\"NC\",\"AddressLine2\":\"Ste. 104\",\"CompanyName\":\"Tosche Stations\",\"DaytimePhone\":\"919-123-4567\",\"EveningPhone\":null,\"NameSuffix\":\"Jr.\"},\"ShippingAddress\":{\"EmailAddress\":\"orphanpilot1996@hotmail.com\",\"FirstName\":\"Luke\",\"LastName\":\"Skywalker\",\"AddressLine1\":\"654 Binary Sundet Dr.\",\"City\":\"Tuskan Territory\",\"Country\":\"US\",\"PostalCode\":\"TT-456\",\"StateOrProvince\":\"NC\",\"AddressLine2\":\"C/O \\\"The chosen one\\\"\",\"CompanyName\":\"Skywalker Evaporator Farms\",\"DaytimePhone\":\"480-987-6543\",\"EveningPhone\":null,\"NameSuffix\":null},\"RequestedShippingMethod\":\"Bantha Union Express\",\"DeliverByDateUtc\":\"2016-09-09T05:02:32.9275092Z\",\"ShippingLabelURL\":\"http://www.asapsystems.com/instructions/FAQ_FedEx_2D_Barcode_Information.pdf\",\"TotalPrice\":514.25,\"TotalTaxPrice\":5.00,\"TotalShippingPrice\":5.00,\"TotalShippingTaxPrice\":0.75,\"TotalGiftOptionPrice\":1.00,\"TotalGiftOptionTaxPrice\":0.25,\"TotalFees\":3.00,\"Currency\":\"USD\",\"VatInclusive\":false,\"Items\":[{\"ID\":\"M000W\",\"SellerSku\":\"REBEL X-WING\",\"UnitPrice\":100.00,\"Quantity\":5}]},{\"ID\":\"M456W\",\"OrderStatus\":\"ReleasedForShipment\",\"OrderDateUtc\":\"2016-09-07T04:32:32.9275092Z\",\"BuyerAddress\":{\"EmailAddress\":\"tatooine_sales@tosche-stations.com\",\"FirstName\":\"Watto\",\"LastName\":\"Toydarian\",\"AddressLine1\":\"321 Landing St.\",\"City\":\"Mos Eisley\",\"Country\":\"US\",\"PostalCode\":\"ME-123\",\"StateOrProvince\":\"NC\",\"AddressLine2\":\"Ste. 104\",\"CompanyName\":\"Tosche Stations\",\"DaytimePhone\":\"919-123-4567\",\"EveningPhone\":null,\"NameSuffix\":\"Jr.\"},\"ShippingAddress\":{\"EmailAddress\":\"orphanpilot1996@hotmail.com\",\"FirstName\":\"Luke\",\"LastName\":\"Skywalker\",\"AddressLine1\":\"654 Binary Sundet Dr.\",\"City\":\"Tuskan Territory\",\"Country\":\"US\",\"PostalCode\":\"TT-456\",\"StateOrProvince\":\"NC\",\"AddressLine2\":\"C/O \\\"The chosen one\\\"\",\"CompanyName\":\"Skywalker Evaporator Farms\",\"DaytimePhone\":\"480-987-6543\",\"EveningPhone\":null,\"NameSuffix\":null},\"RequestedShippingMethod\":\"Bantha Union Express\",\"DeliverByDateUtc\":\"2016-09-09T05:02:32.9275092Z\",\"ShippingLabelURL\":\"http://www.asapsystems.com/instructions/FAQ_FedEx_2D_Barcode_Information.pdf\",\"TotalPrice\":588.05,\"TotalTaxPrice\":5.00,\"TotalShippingPrice\":5.00,\"TotalShippingTaxPrice\":0.75,\"TotalGiftOptionPrice\":1.00,\"TotalGiftOptionTaxPrice\":0.25,\"TotalFees\":3.00,\"Currency\":\"USD\",\"VatInclusive\":false,\"Items\":[{\"ID\":\"M333W\",\"SellerSku\":\"LIGHTSABER_BLUE_MED\",\"UnitPrice\":11.56,\"Quantity\":1},{\"ID\":\"M222W\",\"SellerSku\":\"LIGHTSABER_RED_LG\",\"UnitPrice\":15.56,\"Quantity\":4},{\"ID\":\"M000W\",\"SellerSku\":\"REBEL X-WING\",\"UnitPrice\":100.00,\"Quantity\":5}]}],\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

    public ActionResponse getOrderById(String orderID) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        String jsonData = "{\"ResponseBody\":{\"ID\":\"M456W\",\"OrderStatus\":\"ReleasedForShipment\",\"OrderDateUtc\":\"2016-09-07T04:32:32.9275092Z\",\"BuyerAddress\":{\"EmailAddress\":\"tatooine_sales@tosche-stations.com\",\"FirstName\":\"Watto\",\"LastName\":\"Toydarian\",\"AddressLine1\":\"321 Landing St.\",\"City\":\"Mos Eisley\",\"Country\":\"US\",\"PostalCode\":\"ME-123\",\"StateOrProvince\":\"NC\",\"AddressLine2\":\"Ste. 104\",\"CompanyName\":\"Tosche Stations\",\"DaytimePhone\":\"919-123-4567\",\"EveningPhone\":null,\"NameSuffix\":\"Jr.\"},\"ShippingAddress\":{\"EmailAddress\":\"orphanpilot1996@hotmail.com\",\"FirstName\":\"Luke\",\"LastName\":\"Skywalker\",\"AddressLine1\":\"654 Binary Sundet Dr.\",\"City\":\"Tuskan Territory\",\"Country\":\"US\",\"PostalCode\":\"TT-456\",\"StateOrProvince\":\"NC\",\"AddressLine2\":\"C/O \\\"The chosen one\\\"\",\"CompanyName\":\"Skywalker Evaporator Farms\",\"DaytimePhone\":\"480-987-6543\",\"EveningPhone\":null,\"NameSuffix\":null},\"RequestedShippingMethod\":\"Bantha Union Express\",\"DeliverByDateUtc\":\"2016-09-09T05:02:32.9275092Z\",\"ShippingLabelURL\":\"http://www.asapsystems.com/instructions/FAQ_FedEx_2D_Barcode_Information.pdf\",\"TotalPrice\":588.05,\"TotalTaxPrice\":5.00,\"TotalShippingPrice\":5.00,\"TotalShippingTaxPrice\":0.75,\"TotalGiftOptionPrice\":1.00,\"TotalGiftOptionTaxPrice\":0.25,\"TotalFees\":3.00,\"Currency\":\"USD\",\"VatInclusive\":false,\"Items\":[{\"ID\":\"M333W\",\"SellerSku\":\"LIGHTSABER_BLUE_MED\",\"UnitPrice\":11.56,\"Quantity\":1},{\"ID\":\"M222W\",\"SellerSku\":\"LIGHTSABER_RED_LG\",\"UnitPrice\":15.56,\"Quantity\":4},{\"ID\":\"M000W\",\"SellerSku\":\"REBEL X-WING\",\"UnitPrice\":100.00,\"Quantity\":5}]},\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

    public ActionResponse acknowledgeOrder(String orderID) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        String jsonData = "{\"ResponseBody\":{},\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

    public ActionResponse shipOrder(String orderID, ShipRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)

        String jsonData = "{\"ResponseBody\":{},\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

    public ActionResponse cancelOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)

        String jsonData = "{\"ResponseBody\":{},\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

    public ActionResponse refundOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)

        String jsonData = "{\"ResponseBody\":{},\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }


}
