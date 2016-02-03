package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.base.GiltBase;
import com.voyageone.common.components.gilt.bean.*;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
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
public class GiltSalesService extends GiltBase{

    private static final String URL = "sales";

    /**
     * 返回所有活动和即将到来的未来七天的销售数据
     * Returns data for all active and upcoming sales for the next seven days.
     * @return List<GiltSale>
     */
    public List<GiltSale> getAllSales(ShopBean shopBean) throws Exception {
        return getApiListResult(shopBean,GiltSale.class,URL,new HashMap<String,String>());
    }

    /**
     * 返回单个活动和即将到来的未来七天的销售数据
     * Returns data for a particular Sale ID if it is active or upcoming in the next seven days.
     * @return List<GiltSale>
     */
    public GiltSale getSaleById(ShopBean shopBean,String saleId) throws Exception {
        if(StringUtils.isNullOrBlank2(saleId))
            throw new IllegalArgumentException("saleId不能为空");
        return JacksonUtil.json2Bean(reqGiltApi(shopBean,URL+"/"+saleId,new HashMap<String,String>()),GiltSale.class);
    }

    /**
     * 根据id获取sales的inventory
     * @param shopBean shopBean
     * @param request request
     * @return List<GiltInventory>
     * @throws Exception
     */
    public List<GiltInventory> getSaleInventorysById(ShopBean shopBean, GiltPageGetSaleAttrRequest request) throws Exception {
        request.check();
        return getApiListResult(shopBean,GiltInventory.class,URL+"/"+request.getId()+"/inventory",request.getBeanMap());
    }

    /**
     * 根据id获取sales的realtime-inventory
     * @param shopBean shopBean
     * @param request request
     * @return List<GiltRealTimeInventory>
     * @throws Exception
     */
    public List<GiltRealTimeInventory> getSaleRealTimeInventorysById(ShopBean shopBean, GiltPageGetSaleAttrRequest request) throws Exception {
        request.check();
        return getApiListResult(shopBean,GiltRealTimeInventory.class,URL+"/"+request.getId()+"/realtime-inventory",request.getBeanMap());
    }

    /**
     * 根据id获取sales的skus
     * @param shopBean shopBean
     * @param request request
     * @return List<GiltSku>
     * @throws Exception
     */
    public List<GiltSku> getSaleSkusById(ShopBean shopBean, GiltPageGetSaleAttrRequest request) throws Exception {
        request.check();
        return getApiListResult(shopBean,GiltSku.class,URL+"/"+request.getId()+"/skus",request.getBeanMap());
    }

    /**
     * 获取Api结果
     * @param shopBean shopBean
     * @param e e
     * @param url url
     * @param param param
     * @param <E> <E>
     * @return List<E>
     * @throws Exception
     */
    private <E> List<E> getApiListResult(ShopBean shopBean,Class<E> e,String url,Map<String,String> param) throws Exception {
        return JacksonUtil.jsonToBeanList(reqGiltApi(shopBean,url,param),e);
    }

}
