package com.voyageone.common.components.sears;

import com.voyageone.common.components.sears.base.SearsBase;
import com.voyageone.common.components.sears.bean.*;
import com.voyageone.common.util.JaxbUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2015/10/22.
 */
@Service
public class SearsService extends SearsBase {

    /**
     * 取得指定SKU的信息
     *
     * @param skuList      需要查询的SKU列表
     * @param details      是否需要详细数据
     * @param price        是否需要价格数据
     * @param availability 是否需要库存数据
     * @return
     * @throws Exception
     */
    public ProductResponse getProductsBySku(List<String> skuList, Boolean details, Boolean price, Boolean availability) throws Exception {
        String skus = "";

        StringBuffer param = new StringBuffer();

        if (skuList.size() > 0) {
            skus = "/" + skuList.stream().collect(Collectors.joining(","));
        }
        param.append("product_details=" + details);
        param.append("&price=" + price);
        param.append("&availability=" + availability);

        String responseXml = reqSearsApi(searsUrl + "products" + skus, param.toString());

        logger.info("Sears response: " + responseXml);

        ProductResponse response = JaxbUtil.converyToJavaBean(responseXml, ProductResponse.class);

        return response;
    }

    /**
     * 获取库存数据
     * @param page
     * @param pageSize
     * @param since
     * @return
     * @throws Exception
     */
    public AvailabilitiesResponse getInventory(Integer page, Integer pageSize,String since) throws Exception {
        StringBuffer param = new StringBuffer();

        param.append("page=" + page + "&per_page=" + pageSize + "&since=" + java.net.URLEncoder.encode(since, "utf-8"));

        String responseXml = reqSearsApi(searsUrl + "availabilities", param.toString());

        logger.info("Sears response: " + responseXml);

        AvailabilitiesResponse response = JaxbUtil.converyToJavaBean(responseXml, AvailabilitiesResponse.class);

        return response;
    }

    /**
     * 取得Products总数
     *
     * @return
     * @throws Exception
     */
    public PaginationBean getProductsTotal() throws Exception {
        ProductResponse productResponse = getAllProducts(1, 1);
        if (productResponse != null) {
            return productResponse.getPagination();
        }
        return null;
    }

    /**
     * 取得所有Product的
     *
     * @param page     第几页
     * @param pageSize 每页多少个
     * @return
     * @throws Exception
     */
    public ProductResponse getAllProducts(Integer page, Integer pageSize) throws Exception {

        StringBuffer param = new StringBuffer();

        param.append("page=" + page + "&per_page=" + pageSize);

        String responseXml = reqSearsApi(searsUrl + "products", param.toString());

        logger.info("Sears response: " + responseXml);

        ProductResponse response = JaxbUtil.converyToJavaBean(responseXml, ProductResponse.class);

        return response;
    }

    public String getLatestEntryIds() throws Exception {
        String response = reqSearsApi(searsUrl + "latest_entry_ids", "");
        logger.info("Sears response: " + response);
        return response;
    }

    /**
     * Order LookUp
     *
     * @param orderId     第几页
     * @return
     * @throws Exception
     */
    public OrderLookupResponse getOrderInfo(String orderId) throws Exception {

        StringBuffer param = new StringBuffer();

        String responseXml = reqSearsApi(searsOrderUrl + orderId);

        logger.info("Sears response: " + responseXml);

        // TODO 测试代码
        responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><order><orderId>400921</orderId><omsOrderId>1000400921</omsOrderId><customerReference>TMALL1</customerReference><orderReference>VO1</orderReference><orderDate>2015-11-20T16:30:46Z</orderDate><items>  <item>    <itemId>02901594040</itemId>    <productName>Infant &amp; Toddler Girl's Embellished Shirt &amp; Jogger Pants</productName>    <manufacturerPartNumber>252002S</manufacturerPartNumber>    <status>Scheduled</status>    <statusCode>CNF</statusCode>    <inStorePickupStatus/>    <expectedInStoreDate/>    <quantity>1</quantity>    <billableQuantity>1</billableQuantity>    <price>35.70</price>    <subTotal>35.70</subTotal>    <shipping>10.00</shipping>    <shipTax>0.00</shipTax>    <tax>0.00</tax>    <shippingAdjustment>0.00</shippingAdjustment>    <grandTotal>55.70</grandTotal>    <salesCheckNumber>400921111467745</salesCheckNumber>    <trackingNumbers>1111,2222</trackingNumbers>    <shippingDate/>    <shippingCarrier/>    <expectedDateOfArrival/>    <returnedQuantity>0</returnedQuantity>    <shippingMode>Ground</shippingMode>    <expectedShipDate>2015-11-20T23:59:59Z</expectedShipDate>    <customsDuty>10.0</customsDuty>    <fee>      <voCommission>10.0</voCommission>      <alipayFee>1.0</alipayFee>      <tmallCommission>3.0</tmallCommission>    </fee>    <delivery>      <type>mail</type>    </delivery>  </item></items><shippingAddress>  <firstName>Isxxxx</firstName>  <lastName>Sxxxxx</lastName>  <addressLine1>1N State St</addressLine1>  <addressLine2>order_confirm</addressLine2>  <addressLine3/>  <city>Chicago</city>  <state>IL</state>  <zipCode>60602</zipCode>  <countryCode>US</countryCode>  <email>isxxx.shxxxx@searshc.com</email>  <dayPhone>3125555111</dayPhone></shippingAddress><subTotal>35.70</subTotal><shipping>10.00</shipping><shipTax>0.00</shipTax><tax>0.00</tax><shippingAdjustment>0.00</shippingAdjustment><grandTotal>55.70</grandTotal></order>";

        OrderLookupResponse response = JaxbUtil.converyToJavaBean(responseXml, OrderLookupResponse.class);

        return response;
    }

    /**
     * 给Sears推订单
     * @param order
     * @return
     * @throws Exception
     */
    public OrderResponse CreateOrder(OrderBean order) throws Exception {
        return SearsHttpPost(searsUrl+"orders","utf-8",JaxbUtil.convertToXml(order));
    }
}
