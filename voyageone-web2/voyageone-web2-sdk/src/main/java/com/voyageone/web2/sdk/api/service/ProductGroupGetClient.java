package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductGroupGetClient {
    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 获取group 根据groupId获得
     */
    public CmsBtProductModel_Group_Platform getProductGroupByGroupId(String channelId, long groupId) {
        //设置参数
        ProductGroupGetRequest requestModel = new ProductGroupGetRequest(channelId);
        requestModel.setGroupId(groupId);
        //SDK取得Product 数据
        return voApiClient.execute(requestModel).getProductGroupPlatform();
    }

    /**
     * 获取group 根据cartId & NumIId
     */
    public CmsBtProductModel_Group_Platform getProductGroupByNumIId(String channelId, int cartId, String numIId) {
        //设置参数
        ProductGroupGetRequest requestModel = new ProductGroupGetRequest(channelId);
        requestModel.setCartId(cartId);
        requestModel.setNumIId(numIId);
        //SDK取得Product 数据
        return voApiClient.execute(requestModel).getProductGroupPlatform();
    }

    /**
     * 获取主商品 根据groupId
     */
    public CmsBtProductModel getMainProductByGroupId(String channelId, long groupId) {
        //设置参数
        ProductGetRequest requestModel = new ProductGetRequest(channelId);
        //String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"groupId\":%s, \"isMain\":1}}}";
        requestModel.addProp("groups.platforms.groupId", groupId);
        requestModel.addProp("groups.platforms.isMain", 1);

//        requestModel.addField("prodId");
//        requestModel.addField("catPath");
//
//        requestModel.addField("fields.code");
//        requestModel.addField("fields.brand");
//        requestModel.addField("fields.productName");
//        requestModel.addField("fields.middleTitle");
//
//        requestModel.addSort("fields.code", true);
//        requestModel.addSort("fields.brand", false);

        //SDK取得Product 数据
        return voApiClient.execute(requestModel).getProduct();
    }


}
