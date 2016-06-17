package com.voyageone.components.overstock.service;

import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.api.exception.ClientException;
import com.overstock.mp.mpc.externalclient.model.ProductType;
import com.overstock.mp.mpc.externalclient.model.ProductsType;
import com.voyageone.components.overstock.OverstockBase;
import com.voyageone.components.overstock.bean.OverstockMultipleRequest;
import com.voyageone.components.overstock.bean.product.OverstockProductOneQueryRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

/**
 * @author aooer 2016/6/7.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class OverstockProductService extends OverstockBase {

    /**
     * 批量查询商品
     *
     * @param request request
     * @return 结果集
     * @throws MalformedURLException
     * @throws ClientException
     */
    @Retryable
    public Result<ProductsType> queryForMultipleProducts(OverstockMultipleRequest request) throws Exception {
        return getClientFactory()
                .forProducts()
                .getMany()
                .withLimit(request.getLimit())
                .withOffset(request.getOffset())
                .build()
                .execute(getCredentials());
    }

    /**
     * 查询单个商品
     *
     * @param request request
     * @return 结果集
     * @throws MalformedURLException
     * @throws ClientException
     */
    @Retryable
    public Result<ProductType> queryForOneProduct(OverstockProductOneQueryRequest request) throws Exception {
        return getClientFactory()
                .forProducts()
                .getOne(request.getProductId())
                .build()
                .execute(getCredentials());
    }

}
