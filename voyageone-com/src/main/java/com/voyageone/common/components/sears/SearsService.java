package com.voyageone.common.components.sears;

import com.voyageone.common.components.sears.base.SearsBase;
import com.voyageone.common.components.sears.bean.*;
import com.voyageone.common.util.JaxbUtil;
import org.springframework.stereotype.Service;

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

        if (!skuList.isEmpty()) {
            skus = "/" + skuList.stream().collect(Collectors.joining(","));
        }
        param.append("product_details=" + details);
        param.append("&price=" + price);
        param.append("&availability=" + availability);

        String responseXml = reqSearsApi(searsUrl + "products" + skus, param.toString());

        //logger.info("Sears response: " + responseXml);

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

        //logger.info("Sears response: " + responseXml);

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
     * @throws Exception
     */
    public OrderLookupResponse getOrderInfo(String orderId) throws Exception {

        StringBuffer param = new StringBuffer();

        String responseXml = reqSearsApi(searsOrderUrlByOrderId + orderId);

        //logger.info("Sears response: " + responseXml);

        OrderLookupResponse response = JaxbUtil.converyToJavaBean(responseXml, OrderLookupResponse.class);

        return response;
    }

    /**
     * Order LookUp
     *
     * @param orderReference
     * @return
     * @throws Exception
     */
    public OrderLookupsResponse getOrderInfoByOrderReference(String orderReference) throws Exception {

        StringBuffer param = new StringBuffer();

        String responseXml = reqSearsApi(searsOrderByOrderReferenceUrl + orderReference);

        //logger.info("Sears response: " + responseXml);

        OrderLookupsResponse response = JaxbUtil.converyToJavaBean(responseXml, OrderLookupsResponse.class);

        return response;
    }

    /**
     * 给Sears推订单
     * @param order
     * @return
     * @throws Exception
     */
    public OrderResponse CreateOrder(OrderBean order) throws Exception {
        return searsHttpPost(searsUrl + "orders", "utf-8", JaxbUtil.convertToXml(order));
    }

    /**
     * 更新订单状态
     * @param order
     * @return
     * @throws Exception
     */
    public OrderResponse UpdateStatus(UpdateStatusBean order) throws Exception {
        return searsHttpPost(String.format(updateStatusUrl, order.getOrderId()), "utf-8", JaxbUtil.convertToXml(order));
    }
}
