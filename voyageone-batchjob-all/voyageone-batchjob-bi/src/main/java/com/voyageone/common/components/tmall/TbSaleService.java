package com.voyageone.common.components.tmall;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsListGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsListGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;

/**
 * Created by Kylin on 2015/7/15.
 */
@Component
public class TbSaleService extends TbBase {


    /**
     * 获取当前会话用户出售中的商品列表
     * @param strOrderChannelId
     * @param strCardId
     * @param strFieldList
     * @return
     * @throws ApiException
     */
    public List<Item> getOnsaleProduct(String strOrderChannelId, String strCardId, Long lPageIndex, String strFieldList)throws ApiException{

        ItemsOnsaleGetResponse response = null;

        ShopBean shopInfo = ShopConfigs.getShop(strOrderChannelId, strCardId);

        ItemsOnsaleGetRequest req=new ItemsOnsaleGetRequest();
        req.setPageNo(lPageIndex);
        req.setPageSize((long)200);
        req.setFields(strFieldList);
        
        response = reqTaobaoApi(shopInfo, req);

        return response.getItems();
    }
    
    /**
     * 获取当前会话用户在库的商品列表
     * @param strOrderChannelId
     * @param strCardId
     * @param strFieldList
     * @return
     * @throws ApiException
     */
    public List<Item> getInventoryProduct(String strOrderChannelId, String strCardId, Long lPageIndex, String strFieldList, String banner)throws ApiException{
        ShopBean shopInfo = ShopConfigs.getShop(strOrderChannelId, strCardId);
        ItemsInventoryGetRequest req=new ItemsInventoryGetRequest();
        req.setPageNo(lPageIndex);
        req.setPageSize((long)200);
        req.setFields(strFieldList);
        req.setBanner(banner);
        ItemsInventoryGetResponse response = reqTaobaoApi(shopInfo, req);
        return response.getItems();
    }

    /**
     * 获取当前会话用户在库的商品列表
     * @param strOrderChannelId
     * @param strCardId
     * @param strFieldList
     * @return
     * @throws ApiException
     */
    public List<Item> getListProduct(String strOrderChannelId, String strCardId, Long lPageIndex, String strFieldList, String numIids)throws ApiException{

        ItemsListGetResponse response = null;

        ShopBean shopInfo = ShopConfigs.getShop(strOrderChannelId, strCardId);

        ItemsListGetRequest req=new ItemsListGetRequest();
        req.setFields(strFieldList);
        req.setNumIids(numIids);
        
        response = reqTaobaoApi(shopInfo, req);

        return response.getItems();
    }
}
