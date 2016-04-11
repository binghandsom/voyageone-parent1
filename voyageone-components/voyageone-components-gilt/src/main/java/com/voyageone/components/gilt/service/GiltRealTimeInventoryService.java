package com.voyageone.components.gilt.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.gilt.GiltBase;
import com.voyageone.components.gilt.bean.GiltRealTimeInventory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltRealTimeInventoryService extends GiltBase {


    private static final String URI = "realtime-inventory";


    /**
     * 根据Id获取实时库存
      * @param skuId skuId
     * @return GiltRealTimeInventory
     */
    public GiltRealTimeInventory getRealTimeInventoryBySkuId(String skuId) throws Exception {
        if(StringUtils.isNullOrBlank2(skuId)) throw new IllegalArgumentException("skuId不能为空");

        String result=reqGiltApi(URI  + "/" + skuId, new HashMap<>());
        return JacksonUtil.json2Bean(result, GiltRealTimeInventory.class);
    }

}
