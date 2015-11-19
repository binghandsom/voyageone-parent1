package com.voyageone.common.components.sears;

import com.voyageone.common.components.sears.base.SearsBase;
import com.voyageone.common.components.sears.bean.PaginationBean;
import com.voyageone.common.components.sears.bean.ProductResponse;
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
     * 获取库存数据
     * @param skuList 需要查询的SKU列表最多25个
     * @return
     * @throws Exception
     */
    public ProductResponse getInventory(List<String> skuList) throws Exception {
        return getProductsBySku(skuList,false,false,true);
    }
    /**
     * 取得Products总数
     * @return
     * @throws Exception
     */
    public PaginationBean getProductsTotal() throws Exception {
        ProductResponse productResponse = getAllProducts(1, 1);
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
}
