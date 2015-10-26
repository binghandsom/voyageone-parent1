package com.voyageone.common.components.sears;

import com.voyageone.common.components.sears.base.SearsBase;
import com.voyageone.common.components.sears.bean.PaginationBean;
import com.voyageone.common.components.sears.bean.ProductResponse;
import com.voyageone.common.util.JaxbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2015/10/22.
 */
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
        if (details) {
            param.append("product_details=true");
        }
        if (price) {
            if (param.length() > 0) param.append("&");
            param.append("price=true");
        }
        if (availability) {
            if (param.length() > 0) param.append("&");
            param.append("availability=true");
        }

        String responseXml = reqSearsApi(searsUrl + "products" + skus, param.toString());

        logger.info("Sears response: " + responseXml);

        ProductResponse response = JaxbUtil.converyToJavaBean(responseXml, ProductResponse.class);

        return response;
    }

    /**
     * 取得Products总数和页数
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PaginationBean getProductsTotalPages(Integer pageSize) throws Exception {
        ProductResponse productResponse = getAllProducts(1, pageSize, false, false, false);
        if (productResponse != null ){
            return productResponse.getPagination();
        }
        return null;
    }
    /**
     * 取得所有Product的
     *
     * @param page         第几页
     * @param pageSize      每页多少个
     * @param details      是否需要详细数据
     * @param price        是否需要价格数据
     * @param availability 是否需要库存数据
     * @return
     * @throws Exception
     */
    public ProductResponse getAllProducts(Integer page, Integer pageSize, Boolean details, Boolean price, Boolean availability) throws Exception {

        StringBuffer param = new StringBuffer();

        param.append("page=" + page + "&per_page=" + pageSize);
        if (details) {
            param.append("&product_details=true");
        }
        if (price) {
            if (param.length() > 0) param.append("&");
            param.append("price=true");
        }
        if (availability) {
            if (param.length() > 0) param.append("&");
            param.append("availability=true");
        }


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
}
