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

    private static final String URI = "orders";


    /**
     *  Put订单数据
    
     * @param request request
     * @return GiltOrder
     * @throws Exception
     */
    public GiltOrder putOrder(GiltPutOrderRequest request) throws Exception {
        request.check();
        Map param=JacksonUtil.jsonToMap(JacksonUtil.bean2Json(request));
        param.remove("id");
        String result=reqPutGiltApi(URI +"/"+request.getId(), JacksonUtil.bean2Json(param));
        return JacksonUtil.json2Bean(result,GiltOrder.class);
    }

    /**
     *  Patch订单数据

     * @param request request
     * @return GiltOrder
     * @throws Exception
     */
    public GiltOrder patchOrder(GiltPatchOrderRequest request) throws Exception {
        request.check();
        Map param=JacksonUtil.jsonToMap(JacksonUtil.bean2Json(request));
        param.remove("id");
        String result=reqPatchGiltApi(URI + "/" + request.getId(), JacksonUtil.bean2Json(param));
        return JacksonUtil.json2Bean(result,GiltOrder.class);
    }

    /**
     *  分页获取Orders

     * @param request request
     * @return List
     * @throws Exception
     */
    public List<GiltOrder> pageGetOrders(GiltPageGetOrdersRequest request) throws Exception {
        request.check();
        Map map= JacksonUtil.jsonToMap(JacksonUtil.bean2Json(request));
        Map<String,String> params=new HashMap<String,String>();
        params.putAll(map);
        String result=reqGiltApi(URI, params);
        return JacksonUtil.jsonToBeanList(result,GiltOrder.class);
    }

    /**
     *  根据Id获取Order

     * @param id id
     * @return List
     * @throws Exception
     *
     *  @see this returns data about a particular placed universally unique identifier (UUID). This should be a unique ID that maps to your customer's order.
     */
    public GiltOrder getOrderById(String id) throws Exception {
        if(StringUtils.isNullOrBlank2(id))
            throw new IllegalArgumentException("id不能为空");
        String result=reqGiltApi(URI +"/"+id,new HashMap<String,String>());
        return JacksonUtil.json2Bean(result,GiltOrder.class);
    }


}
