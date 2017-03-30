package com.voyageone.web2.openapi.cms.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductForOmsBean;
import com.voyageone.service.bean.cms.product.ProductForWmsBean;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.web2.openapi.OpenApiBaseService;
import com.voyageone.web2.sdk.api.request.cms.ProductForOmsGetRequest;
import com.voyageone.web2.sdk.api.request.cms.ProductForWmsGetRequest;
import com.voyageone.web2.sdk.api.response.cms.ProductForOmsGetResponse;
import com.voyageone.web2.sdk.api.response.cms.ProductForWmsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 */
@Service
public class OpenApiProductService extends OpenApiBaseService {

    @Autowired
    private ProductService productService;

    /**
     * get the product info from wms's request
     */
    public ProductForWmsGetResponse getWmsProductsInfo(ProductForWmsGetRequest request) {
        ProductForWmsGetResponse response = new ProductForWmsGetResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        // set fields
        String[] projection = getProjection(request);

        //getProductByCode
        String productSku = request.getSku();

        if (!StringUtils.isEmpty(productSku)) {
            ProductForWmsBean bean = productService.getWmsProductsInfo(channelId, productSku, projection);
            response.setResultInfo(bean);
        }
        return response;
    }

    /**
     * get the product list from oms's request
     */
    public ProductForOmsGetResponse getOmsProductsInfo(ProductForOmsGetRequest request) {
        ProductForOmsGetResponse response = new ProductForOmsGetResponse();

        $info(JacksonUtil.bean2Json(request));
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        // set fields
        String[] projection = getProjection(request);
        // 设定sku的模糊查询
        String skuIncludes = request.getSkuIncludes();
        // 根据具体的sku取值
        List<String> skuList = request.getSkuList();
        // 设定name的模糊查询
        String nameIncludes = request.getNameIncludes();
        // 设定description的模糊查询
        String descriptionIncludes = request.getDescriptionIncludes();
        String cartId = request.getCartId();

        List<ProductForOmsBean> resultInfo = productService.getOmsProductsInfo(channelId,
                skuIncludes,
                skuList,
                nameIncludes,
                descriptionIncludes,
                cartId,
                projection
        );
        response.setResultInfo(resultInfo);

        return response;
    }


}
