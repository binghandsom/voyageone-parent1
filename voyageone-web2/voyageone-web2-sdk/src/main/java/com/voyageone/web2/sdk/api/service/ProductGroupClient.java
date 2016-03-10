package com.voyageone.web2.sdk.api.service;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductGroupGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductGroupClient {
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
     * 获取main group 根据cartId & NumIId
     */
    public CmsBtProductModel_Group_Platform getProductGroupByNumIId(String channelId, int cartId, String numIId) {
        //设置参数
        ProductGroupGetRequest requestModel = new ProductGroupGetRequest(channelId);
        requestModel.setCartId(cartId);
        requestModel.setNumIId(numIId);
        //SDK取得Product 数据
        return voApiClient.execute(requestModel).getProductGroupPlatform();
    }

}
