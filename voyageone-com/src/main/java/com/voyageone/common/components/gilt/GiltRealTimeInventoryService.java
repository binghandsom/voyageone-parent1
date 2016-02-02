package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.base.GiltBase;
import com.voyageone.common.components.gilt.bean.GiltRealTimeInventory;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltRealTimeInventoryService extends GiltBase {


    private static final String URL = "realtime-inventory";


    /**
     *
     * @param shopBean shopBean
     * @param skuId skuId
     * @return GiltRealTimeInventory
     */
    public GiltRealTimeInventory getRealTimeInventoryBySkuId(ShopBean shopBean, String skuId) throws Exception {
        if(StringUtils.isNullOrBlank2(skuId))
        throw new IllegalArgumentException("skuId不能为空");
        String result=reqGiltApi(shopBean,URL+"/"+skuId,new HashMap<String,String>());
        return JacksonUtil.json2Bean(result,GiltRealTimeInventory.class);
    }

}
