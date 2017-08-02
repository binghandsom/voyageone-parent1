package com.voyageone.components.cnn.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.MD5;
import com.voyageone.components.cnn.CnnBase;
import com.voyageone.components.cnn.request.*;
import com.voyageone.components.cnn.response.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * sn app
 * Created by morse.lu on 2017/08/01
 */
@Component
public class CnnWareNewService extends CnnBase {

    private static final String SN_APP_TOKEN = "mVaazc3R85qAU1ZTO3ezxVNtZNbIh4uU4QRtXaR04"; // 暂时写死吧，之后看看是不是放在shop的token_url字段里

    /**
     * sn app新增商品
     */
    public ProductAddResponse addProduct(ShopBean shop, ProductAddRequest request) throws Exception {
        return reqApi(shop, request, getHeadersForSnApp());
    }

    /**
     * sn app更新商品
     */
    public ProductUpdateResponse updateProduct(ShopBean shop, ProductUpdateRequest request) throws Exception {
        return reqApi(shop, request, getHeadersForSnApp());
    }

    /**
     * sn app删除商品 group下线
     */
    public ProductDeleteResponse deleteProduct(ShopBean shop, ProductDeleteRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        return reqApi(shop, request, getHeadersForSnApp());
    }

    /**
     * sn app删除CODE 单CODE下线
     */
    public ProductDeleteCodeResponse deleteProductCode(ShopBean shop, ProductDeleteCodeRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        request.addParam(request.getProdCode());
        return reqApi(shop, request, getHeadersForSnApp());
    }

    /**
     * sn app删除sku,逻辑删除
     */
    public ProductDeleteSkuResponse deleteProductSku(ShopBean shop, ProductDeleteSkuRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        request.addParam(request.getSkuCode());
        return reqApi(shop, request, getHeadersForSnApp());
    }

    /**
     * 获取sn app请求Header
     */
    private Map<String, String> getHeadersForSnApp() {
        String accessTimestamp = Long.toString(System.currentTimeMillis());
        String accessSign = MD5.getMD5(SN_APP_TOKEN + accessTimestamp);
        Map<String, String> headers = new HashMap<>();
        headers.put("access_token", SN_APP_TOKEN);
        headers.put("access_timestamp", accessTimestamp);
        headers.put("access_sign", accessSign);
        return headers;
    }
}
