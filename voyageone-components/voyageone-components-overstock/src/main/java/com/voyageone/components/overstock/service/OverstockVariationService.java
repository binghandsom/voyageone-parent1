package com.voyageone.components.overstock.service;

import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.api.builder.VariationGetManyBuilder;
import com.overstock.mp.mpc.externalclient.model.VariationsType;
import com.voyageone.components.overstock.OverstockBase;
import com.voyageone.components.overstock.bean.variation.OverstockVariationMultipleQueryRequest;
import com.voyageone.components.overstock.bean.variation.OverstockVariationMultipleSkuQueryRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author aooer 2016/6/7.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class OverstockVariationService extends OverstockBase {

    /**
     * 查询变化的库存信息
     *
     * @param request request
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<VariationsType> queryingForMultipleVariations(OverstockVariationMultipleQueryRequest request) throws Exception {
        VariationGetManyBuilder builder = getClientFactory().forVariations()
                .getMany()
                .withOffset(request.getOffset())
                .withLimit(request.getLimit());
        if (!StringUtils.isEmpty(request.getListingId()))
            builder = builder.withQueryParameter("retailerProperties.ListingId", request.getListingId());
        if (!StringUtils.isEmpty(request.getInventoryOverride()))
            builder = builder.withQueryParameter("retailerProperties.InventoryOverride", request.getInventoryOverride());
        return builder.build().execute(getCredentials());
    }

    /**
     * 根据sku查询库存信息
     *
     * @param request request
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<VariationsType> queryingForMultipleSkuVariations(OverstockVariationMultipleSkuQueryRequest request) throws Exception {
        return getClientFactory().forVariations()
                .getMany()
                .withQueryParameter("sku",request.getSku())
                .withOffset(request.getOffset())
                .withLimit(request.getLimit())
                .build()
                .execute(getCredentials());
    }

}
