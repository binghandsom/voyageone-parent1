package com.voyageone.web2.sdk.api.service;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductGroupsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductGroupsGetClient {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 获取商品 根据ID List
     */
    public ProductGroupsGetResponse getMainGroups(ProductGroupsGetRequest request) {
        //SDK取得Product 数据
        return voApiClient.execute(request);
    }


}
