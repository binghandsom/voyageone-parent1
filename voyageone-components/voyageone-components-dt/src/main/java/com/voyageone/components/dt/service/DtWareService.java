package com.voyageone.components.dt.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.dt.DtBase;
import com.voyageone.components.dt.enums.DtConstants;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 分销商品类API调用服务
 * <p/>
 * Created by desmond on 2016/12/29.
 */
@Component
public class DtWareService extends DtBase {

    /**
     * 分销上架(新增或更新)商品
     *
     * @param shop 店铺信息
     * @param code 产品code
     * @return String 返回结果
     */
    public String onShelfProduct(ShopBean shop, String code) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("channelID", shop.getOrder_channel_id());
        request.put("code", code);

        // 调用分销新增商品API
        result = reqApi(shop, DtConstants.DtApiAction.ON_SHELF_PRODUCT, request);

        return result;
    }

    /**
     * 分销下架(删除)商品
     *
     * @param shop 店铺信息
     * @param code 产品code
     * @return String 返回结果
     */
    public String offShelfProduct(ShopBean shop, String code) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("channelID", shop.getOrder_channel_id());
        request.put("code", code);

        // 调用分销下架(删除)商品API
        result = reqApi(shop, DtConstants.DtApiAction.OFF_SHELF_PRODUCT, request);

        return result;
    }

}
