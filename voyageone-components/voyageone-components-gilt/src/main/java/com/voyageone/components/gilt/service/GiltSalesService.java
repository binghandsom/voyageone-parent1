package com.voyageone.components.gilt.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.gilt.GiltBase;
import com.voyageone.components.gilt.bean.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltSalesService extends GiltBase {

    private static final String URI = "sales";

    /**
     * 返回所有活动和即将到来的未来七天的销售数据
     * Returns data for all active and upcoming sales for the next seven days.
     * @return List<GiltSale>
     */
    public List<GiltSale> getAllSales() throws Exception {
        return getApiListResult(GiltSale.class, URI,new HashMap<String,String>());
    }

    /**
     * 返回单个活动和即将到来的未来七天的销售数据
     * Returns data for a particular Sale ID if it is active or upcoming in the next seven days.
     * @return List<GiltSale>
     */
    public GiltSale getSaleById(String saleId) throws Exception {
        if(StringUtils.isNullOrBlank2(saleId))
            throw new IllegalArgumentException("saleId不能为空");
        return JacksonUtil.json2Bean(reqGiltApi(URI +"/"+saleId,new HashMap<String,String>()),GiltSale.class);
    }

    /**
     * 根据id获取sales的inventory (restful API:  /sales/:id/inventory)
       * @param request request
     * @return List<GiltInventory>
     * @throws Exception
     */
    public List<GiltInventory> getInventorysBySaleId(GiltPageGetSaleAttrRequest request) throws Exception {
        request.check();
        return getApiListResult(GiltInventory.class, URI +"/"+request.getId()+"/inventory",request.getBeanMap());
    }

    /**
     * 根据id获取sales的realtime-inventory
        * @param request request
     * @return List<GiltRealTimeInventory>
     * @throws Exception
     */
    public List<GiltRealTimeInventory> getRealTimeInventoriesBySaleId(GiltPageGetSaleAttrRequest request) throws Exception {
        request.check();
        return getApiListResult(GiltRealTimeInventory.class, URI +"/"+request.getId()+"/realtime-inventory",request.getBeanMap());
    }

    /**
     * 根据id获取sales的skus
      * @param request request
     * @return List<GiltSku>
     * @throws Exception
     */
    public List<GiltSku> getSkusBySaleId(GiltPageGetSaleAttrRequest request) throws Exception {
        request.check();
        return getApiListResult(GiltSku.class, URI +"/"+request.getId()+"/skus",request.getBeanMap());
    }

    /**
     * 获取Api结果

     * @param e e
     * @param url url
     * @param param param
     * @param <E> <E>
     * @return List<E>
     * @throws Exception
     */
    private <E> List<E> getApiListResult(Class<E> e,String url,Map<String,String> param) throws Exception {
        return JacksonUtil.jsonToBeanList(reqGiltApi(url,param),e);
    }

}
