package com.voyageone.components.cnn.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.cnn.CnnBase;
import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.request.ProductAddRequest;
import com.voyageone.components.cnn.response.ProductAddResponse;
import org.springframework.stereotype.Component;

/**
 * sn app
 * Created by morse.lu on 2017/08/01
 */
@Component
public class CnnWareNewService extends CnnBase {

    /**
     * 新独立域名新增商品
     */
    public ProductAddResponse addProduct(ShopBean shop, ProductAddRequest request) throws Exception {
        return reqApi(shop, CnnConstants.CnnApiAction.PRODUCT_ADD, request);
    }

}
