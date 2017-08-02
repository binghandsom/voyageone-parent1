package com.voyageone.components.cnn.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.cnn.CnnBase;
import com.voyageone.components.cnn.request.*;
import com.voyageone.components.cnn.response.*;
import com.voyageone.components.cnn.util.CnnUtil;
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
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app更新商品
     */
    public ProductUpdateResponse updateProduct(ShopBean shop, ProductUpdateRequest request) throws Exception {
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app批量更新商品价格，每次请求最多处理100个商品
     */
    public ProductBatchUpdatePriceResponse updateMultiProductPrice(ShopBean shop, ProductBatchUpdatePriceRequest request) throws Exception {
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app删除商品 group下线
     */
    public ProductDeleteResponse deleteProduct(ShopBean shop, ProductDeleteRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app删除CODE 单CODE下线
     */
    public ProductDeleteCodeResponse deleteProductCode(ShopBean shop, ProductDeleteCodeRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        request.addParam(request.getProdCode());
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app删除sku,逻辑删除
     */
    public ProductDeleteSkuResponse deleteProductSku(ShopBean shop, ProductDeleteSkuRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        request.addParam(request.getSkuCode());
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app 上架
     */
    public ProductListingResponse doProductListing(ShopBean shop, ProductListingRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app 下架
     */
    public ProductDeListingResponse doProductDeListing(ShopBean shop, ProductDeListingRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app 获取商品上下架状态
     */
    public ProductGetStatusResponse getProductPlatformStatus(ShopBean shop, ProductGetStatusRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getNumIId());
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

}
