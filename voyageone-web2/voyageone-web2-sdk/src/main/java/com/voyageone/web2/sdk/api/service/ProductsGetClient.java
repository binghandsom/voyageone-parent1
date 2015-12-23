package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//     * 获取商品 根据Code
//     */
//    public CmsBtProductModel getProductByCode(String channelId, String code) {
//        //设置参数
//        ProductGetRequest requestModel = new ProductGetRequest(channelId);
//        requestModel.setProductCode(code);
//        //SDK取得Product 数据
//        return voApiClient.execute(requestModel).getProduct();
//    }
//
//    /**
//     * 获取主商品 根据groupId
//     */
//    public CmsBtProductModel getMainProductByGroupId(String channelId, long groupId) {
//        //设置参数
//        ProductGetRequest requestModel = new ProductGetRequest(channelId);
//        //String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"groupId\":%s, \"isMain\":1}}}";
//        requestModel.addProp("groups.platforms.groupId", groupId);
//        requestModel.addProp("groups.platforms.isMain", 1);
//
////        requestModel.addField("prodId");
////        requestModel.addField("catPath");
////
////        requestModel.addField("fields.code");
////        requestModel.addField("fields.brand");
////        requestModel.addField("fields.productName");
////        requestModel.addField("fields.middleTitle");
////
////        requestModel.addSort("fields.code", true);
////        requestModel.addSort("fields.brand", false);
//
//        //SDK取得Product 数据
//        return voApiClient.execute(requestModel).getProduct();
//    }


}
