package com.voyageone.web2.sdk.api.service;

import com.mongodb.BulkWriteResult;
import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class PostProductSelectOneClient {
    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductById(String channelId, long prodId) {
        //设置参数
        PostProductSelectOneRequest requestModel = new PostProductSelectOneRequest(channelId);
        requestModel.setProductId(prodId);
        //SDK取得Product 数据
        return voApiClient.execute(requestModel).getProduct();
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        //设置参数
        PostProductSelectOneRequest requestModel = new PostProductSelectOneRequest(channelId);
        requestModel.setProductCode(code);
        //SDK取得Product 数据
        return voApiClient.execute(requestModel).getProduct();
    }


}
