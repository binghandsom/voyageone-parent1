package com.voyageone.components.cnn.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.cnn.CnnBase;
import com.voyageone.components.cnn.request.ProductAddRequest;
import com.voyageone.components.cnn.request.ProductDeleteCodeRequest;
import com.voyageone.components.cnn.request.ProductDeleteRequest;
import com.voyageone.components.cnn.response.ProductAddResponse;
import com.voyageone.components.cnn.response.ProductDeleteCodeResponse;
import com.voyageone.components.cnn.response.ProductDeleteResponse;
import org.springframework.stereotype.Component;

/**
 * sn app
 * Created by morse.lu on 2017/08/01
 */
@Component
public class CnnWareNewService extends CnnBase {

    /**
     * sn app新增商品
     */
    public ProductAddResponse addProduct(ShopBean shop, ProductAddRequest request) throws Exception {
        return reqApi(shop, request);
    }

    /**
     * sn app删除商品
     */
    public ProductDeleteResponse deleteProduct(ShopBean shop, ProductDeleteRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        return reqApi(shop, request);
    }

    /**
     * sn app删除CODE
     */
    public ProductDeleteCodeResponse deleteProductCode(ShopBean shop, ProductDeleteCodeRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        request.addParam(request.getProdCode());
        return reqApi(shop, request);
    }

}
