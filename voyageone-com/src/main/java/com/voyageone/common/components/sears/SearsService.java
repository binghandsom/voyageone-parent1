package com.voyageone.common.components.sears;

import com.voyageone.common.components.sears.base.SearsBase;
import com.voyageone.common.components.sears.bean.*;
import com.voyageone.common.util.JaxbUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearsService extends SearsBase {

    /**
     * 取得指定SKU的信息
     *
     * @param skuList      需要查询的SKU列表
     * @param details      是否需要详细数据
     * @param price        是否需要价格数据
     * @param availability 是否需要库存数据
     */
    public ProductResponse getProductsBySku(List<String> skuList, Boolean details, Boolean price, Boolean availability) throws Exception {
        String skus = "";

        StringBuilder param = new StringBuilder();

        if (!skuList.isEmpty()) {
            skus = "/" + skuList.stream().collect(Collectors.joining(","));
        }
        param.append("product_details=").append(details);
        param.append("&price=").append(price);
        param.append("&availability=").append(availability);

        String responseXml = reqSearsApi(searsUrl + "products" + skus, param.toString());

        //logger.info("Sears response: " + responseXml);

        return JaxbUtil.converyToJavaBean(responseXml, ProductResponse.class);
    }

    /**
     * 获取库存数据
     */
    public AvailabilitiesResponse getInventory(Integer page, Integer pageSize,String since) throws Exception {
        String param = "page=" + page + "&per_page=" + pageSize + "&since=" + java.net.URLEncoder.encode(since, "utf-8");

        String responseXml = reqSearsApi(searsUrl + "availabilities", param);

        //logger.info("Sears response: " + responseXml);

        return JaxbUtil.converyToJavaBean(responseXml, AvailabilitiesResponse.class);
    }

    /**
     * 取得Products总数
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
     */
    public ProductResponse getAllProducts(Integer page, Integer pageSize) throws Exception {

        String responseXml = reqSearsApi(searsUrl + "products", ("page=" + page + "&per_page=" + pageSize));

        logger.info("Sears response: " + responseXml);

        return JaxbUtil.converyToJavaBean(responseXml, ProductResponse.class);
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
     */
    public OrderLookupResponse getOrderInfo(String orderId) throws Exception {
        String responseXml = reqSearsApi(searsOrderUrlByOrderId + orderId);

        return JaxbUtil.converyToJavaBean(responseXml, OrderLookupResponse.class);
    }

    /**
     * Order LookUp
     */
    public OrderLookupsResponse getOrderInfoByOrderReference(String orderReference) throws Exception {

        String responseXml = reqSearsApi(searsOrderByOrderReferenceUrl + orderReference);

        return JaxbUtil.converyToJavaBean(responseXml, OrderLookupsResponse.class);
    }

    /**
     * 给Sears推订单
     */
    public OrderResponse CreateOrder(OrderBean order) throws Exception {
        return searsHttpPost(searsUrl + "orders", "utf-8", JaxbUtil.convertToXml(order));
    }

    /**
     * 更新订单状态
     */
    public OrderResponse UpdateStatus(UpdateStatusBean order) throws Exception {
        return searsHttpPost(String.format(updateStatusUrl, order.getOrderId()), "utf-8", JaxbUtil.convertToXml(order));
    }
}
