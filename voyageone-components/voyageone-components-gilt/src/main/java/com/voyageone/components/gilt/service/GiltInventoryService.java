package com.voyageone.components.gilt.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.gilt.GiltBase;
import com.voyageone.components.gilt.bean.GiltInventory;
import com.voyageone.components.gilt.bean.GiltPageInventoryRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltInventoryService extends GiltBase {


    private static final String URI = "inventory";


    /**
     * 根据Id获取实时库存
      * @param skuId skuId
     * @return GiltRealTimeInventory
     */
    public GiltInventory getInventoryBySkuId(String skuId) throws Exception {
        if(StringUtils.isNullOrBlank2(skuId)) throw new IllegalArgumentException("skuId不能为空");

        String result = reqGiltApi(URI +"/"+skuId,new HashMap<>());
        return JacksonUtil.json2Bean(result,GiltInventory.class);
    }
    /**
     *  分页获取Inventory

     * @param request request
     * @return List
     * @throws Exception
     */
    public List<GiltInventory> pageGetInventories(GiltPageInventoryRequest request) throws Exception {
        request.check();
        String result = reqGiltApi(URI,request.getBeanMap());
        return JacksonUtil.jsonToBeanList(result,GiltInventory.class);
    }
}
