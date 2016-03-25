package com.voyageone.common.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Component;

import java.util.List;

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

        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);

        ItemsOnsaleGetRequest req=new ItemsOnsaleGetRequest();
        req.setPageNo(lPageIndex);
        req.setPageNo(lPageIndex);
        req.setPageSize((long)200);
        req.setFields(strFieldList);

        response = reqTaobaoApi(shopInfo, req);

        return response.getItems();
    }

}
