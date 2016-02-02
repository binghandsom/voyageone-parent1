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
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltOrderService extends GiltBase{

    private static final String URL = "orders";

    /**
     *  分页获取Orders
     * @param shopBean shopBean
     * @param request request
     * @return List
     * @throws Exception
     */
    public List<GiltOrder> pageGetOrders(ShopBean shopBean, GiltPageGetOrdersRequest request) throws Exception {
        request.check();
        Map map= JacksonUtil.jsonToMap(JacksonUtil.bean2Json(request));
        Map<String,String> params=new HashMap<String,String>();
        params.putAll(map);
        String result=reqGiltApi(shopBean,URL,params);
        return JacksonUtil.jsonToBeanList(result,GiltOrder.class);
    }

    /**
     *  根据Id获取Order
     * @param shopBean shopBean
     * @param id id
     * @return List
     * @throws Exception
     *
     *  @see this returns data about a particular placed universally unique identifier (UUID). This should be a unique ID that maps to your customer's order.
     */
    public GiltOrder getOrderById(ShopBean shopBean,String id) throws Exception {
        if(StringUtils.isNullOrBlank2(id))
            throw new IllegalArgumentException("id不能为空");
        String result=reqGiltApi(shopBean,URL+"/"+id,new HashMap<String,String>());
        return JacksonUtil.json2Bean(result,GiltOrder.class);
    }


}
