package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.ware.WareSkuStockUpdateRequest;
import com.jd.open.api.sdk.response.ware.WareSkuStockUpdateResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.JdBase;
import org.springframework.stereotype.Component;

/**
 * 京东库存类 api 调用服务
 * <p/>
 * Created by jonas on 15/6/5.
 */
@Component
public class JdInventoryService extends JdBase {

    /**
     * 更新京东商品的库存
     *
     * @param shopBean 店铺信息
     * @param sku      商品 SKU
     * @param qty      数量
     * @return 响应
     * @throws JdException
     */
    public WareSkuStockUpdateResponse skuStockUpdate(ShopBean shopBean, String sku, String qty) throws JdException {
        WareSkuStockUpdateRequest request = new WareSkuStockUpdateRequest();

        request.setOuterId(sku);
        request.setQuantity(qty);

        return reqApi(shopBean, request);
    }
}
