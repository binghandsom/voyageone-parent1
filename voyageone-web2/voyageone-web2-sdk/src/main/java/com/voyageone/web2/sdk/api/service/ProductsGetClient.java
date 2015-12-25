package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProductsGetClient {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 获取商品 根据ID List
     */
    public ProductsGetResponse getProducts(ProductsGetRequest request) {
        //SDK取得Product 数据
        return voApiClient.execute(request);
    }

//    /**
//     * 获取商品Code List 根据CartId
//     * @param channelId channel ID
//     * @param cartId cart ID
//     * @return code list
//     */
//    public List<String> getProductCodesByCart(String channelId, int cartId) {
//        List<String> result = new ArrayList<>();
//
//        ProductsGetRequest request = new ProductsGetRequest();
//        request.setChannelId(channelId);
//        request.addProp("groups.platforms.cartId", cartId);
//        request.setPageSize(1000);
//        request.addField("fields.code");
//
//        ProductsGetResponse response = voApiClient.execute(request);
//
//        if (response != null) {
//            List<CmsBtProductModel> products = response.getProducts();
//            if (products != null && products.size() > 0) {
//                for (CmsBtProductModel product : products) {
//                    if (product.getFields() != null) {
//                        result.add(product.getFields().getCode());
//                    }
//                }
//            }
//
//            long totalCount = response.getTotalCount();
//            long pages = totalCount/1000;
//            if (totalCount%100 > 0) {
//                pages++;
//            }
//            for (int page=2; page<=pages; page++) {
//                request.setPageNo(page);
//                response = voApiClient.execute(request);
//                if (products != null && products.size() > 0) {
//                    for (CmsBtProductModel product : products) {
//                        if (product.getFields() != null) {
//                            result.add(product.getFields().getCode());
//                        }
//                    }
//                }
//                System.out.println(page);
//            }
//        }
//
//        return result;
//    }


}
