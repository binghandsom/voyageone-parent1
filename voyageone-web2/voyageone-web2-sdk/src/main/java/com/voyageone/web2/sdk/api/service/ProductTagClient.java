package com.voyageone.web2.sdk.api.service;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductsTagDeleteRequest;
import com.voyageone.web2.sdk.api.request.ProductsTagPutRequest;
import com.voyageone.web2.sdk.api.response.ProductsTagDeleteResponse;
import com.voyageone.web2.sdk.api.response.ProductsTagPutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product Tag Client Service。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
@Service
public class ProductTagClient {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 增加商品的Tag
     */
    public Map<String, Object> addTagProducts(String channelId, String tagPath, List<Long> productIds, String modifier) {
        Map<String, Object> ret = new HashMap<>();
        ProductsTagPutRequest request = new ProductsTagPutRequest(channelId);
        request.setModifier(modifier);

        for (Long productId : productIds) {
            request.addProductIdTagPathsMap(productId, tagPath);
        }

        ProductsTagPutResponse response = voApiClient.execute(request);
        ret.put("result", "success");
        ret.put("response", response);
        return ret;
    }

    /**
     * 删除商品的Tag
     */
    public Map<String, Object> removeTagProducts(String channelId, String tagPath, List<Long> productIds, String modifier) {
        Map<String, Object> ret = new HashMap<>();
        ProductsTagDeleteRequest request = new ProductsTagDeleteRequest(channelId);
        request.setModifier(modifier);

        for (Long productId : productIds) {
            request.addProductIdTagPathsMap(productId, tagPath);
        }

        ProductsTagDeleteResponse response = voApiClient.execute(request);
        ret.put("result", "success");
        ret.put("response", response);
        return ret;
    }

    /**
     * 删除商品的Tags
     */
    public Map<String, Object> removeTagsProduct(String channelId, List<String> tagPaths, Long productId, String modifier) {
        Map<String, Object> ret = new HashMap<>();
        ProductsTagDeleteRequest request = new ProductsTagDeleteRequest(channelId);
        request.setModifier(modifier);

        for (String tagPath : tagPaths) {
            request.addProductIdTagPathsMap(productId, tagPath);
        }

        ret.put("result", "success");
        return ret;
    }

}
