package com.voyageone.components.dt.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.dt.DtBase;
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
     * 分销新增商品
     *
     * @param shop 店铺信息
     * @param code 产品code
     * @return String 返回结果
     */
    public String addProduct(ShopBean shop, String code) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("channelID", shop.getOrder_channel_id());
        request.put("code", code);

        // 调用分销新增商品API
        result = reqApi(shop, request);

        return result;
    }

}
