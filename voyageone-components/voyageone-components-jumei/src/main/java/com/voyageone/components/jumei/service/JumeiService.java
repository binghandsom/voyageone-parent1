package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.Bean.*;
import com.voyageone.components.jumei.JmBase;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sn3 on 2015-07-16.
 */
@Component
public class JumeiService extends JmBase {

    public static final String C_URL_GET_ORDERS = "HtOrder/GetOrderIds";
    //单个订单详情获取接口
    public static final String C_URL_GET_ORDERDETAIL_BY_ORDERID = "HtOrder/GetOrderDetailByOrderId";
    public static final String C_URL_SET_SHIPPING = "HtOrder/setShipping";
    //SKU库存同步接口
    public static final String C_URL_STOCK_SYNC = "HtStock/StockSync";

    /**
     * 根据订单ID获取订单详情
     * @param shopBean ShopBean
     * @param orderId String
     * @return
     * @throws Exception
     */
    public GetOrderDetailRes getOrderDetailByOrderId(ShopBean shopBean, String orderId) throws Exception {

        Map<String, Object> parms = new HashMap<>();
        parms.put("order_id", orderId);

        String result = reqJmApi(shopBean, C_URL_GET_ORDERDETAIL_BY_ORDERID, parms);

        return JsonUtil.jsonToBean(result, GetOrderDetailRes.class);
    }


    /**
     * 订单发货接口
     * 接口说明：第三方ERP 通过该接口将已经发货订单的快递信息返回给聚美系统。商家只能用
     聚美合作的快递公司进行发货，否则无法发货成功。
     调用发货接口成功后,订单状态变为已发货status=3，聚美系统订单发货时间为ERP 调用接
     口成功的时间。
     * @param shopBean ShopBean
     * @param req SetShippingReq
     * @return String
     * @throws Exception
     */
    public String setShipping(ShopBean shopBean, SetShippingReq req) throws Exception {

        Map<String, Object> parms = new HashMap<>();
        //订单id
        parms.put("order_id", req.getOrder_id());
        //快递公司id(来自聚美快递列表) TODO 从配置表 获取
        parms.put("logistic_id", req.getLogistic_id());
        //快递单号
        parms.put("logistic_track_no", req.getLogistic_track_no());

        return reqJmApi(shopBean, C_URL_SET_SHIPPING, parms);
    }

    /**
     * 批量获取订单ID 接口
     * 特别注意：status 此参数不管如何传值，请务必保证同一时间段两种状态的订单都要抓取一次。
     * （因为卖家后台的订单下载功能也会触发订单备货，使订单状态从2 变为7，若只抓2 状态的订单可能会漏单）
     * @param shopBean ShopBean
     * @param req GetOrderIdsReq
     * @return GetOrderIdsRes
     * @throws Exception
     */
    public GetOrderIdsRes getOrderIds(ShopBean shopBean, GetOrderIdsReq req) throws Exception {

        Map<String, Object> parms = new HashMap<>();
        //开始时间
        if (!StringUtils.isEmpty(req.getStart_date())){
            parms.put("start_date", req.getStart_date());
        }
        //开始时间
        if (!StringUtils.isEmpty(req.getEnd_date())){
            parms.put("end_date", req.getEnd_date());
        }
        //订单状态
        // 2：已付款订单 7：备货中订单
        //特别注意：此参数不管如何传值，请务必保证同一时间段两种状态的订单都要抓取一次。
        //（因为卖家后台的订单下载功能也会触发订单备货，使订单状态从2 变为7，若只抓2 状态的订单可能会漏单）
        parms.put("status", req.getStatus());

        String result = reqJmApi(shopBean, C_URL_GET_ORDERS, parms);
        return JsonUtil.jsonToBean(result, GetOrderIdsRes.class);
    }

    /**
     * SKU库存同步接口
     * 接口说明：商家可以通过该接口修改自己的商品库存数量
     * @param shopBean ShopBean
     * @param req StockSyncReq
     * @return String
     * @throws Exception
     */
    public String stockSync(ShopBean shopBean, StockSyncReq req) throws Exception {

        Map<String, Object> parms = new HashMap<>();
        //商家编码
        parms.put("businessman_code", req.getBusinessman_code());
        //该SKU的最新可售库存量
        parms.put("enable_num", req.getEnable_num());

        return reqJmApi(shopBean, C_URL_STOCK_SYNC, parms);

    }
    
//    public static void main(String[] args) throws Exception {
//    	ShopBean shopBean = new ShopBean();
//    	shopBean.setApp_url("http://openapi.ext.jumei.com/");
//    	shopBean.setAppKey("131");
//    	shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
//    	shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
//    	
//    	JumeiService service = new JumeiService();
//    	GetOrderIdsReq reqIds = new GetOrderIdsReq();
//    	
//    	GetOrderDetailRes orderDetailRes = service.getOrderDetailByOrderId(shopBean, "568962961");
//		
//		// 订单开始时间
//		reqIds.setStart_date("2015-08-30 10:00:00");
//		// 订单结束时间
//		reqIds.setEnd_date("2015-08-31 01:00:00");
//		// 已付款、备货中（都是新订单）
//		reqIds.setStatus("2,7");
//		GetOrderIdsRes orderIdsRes = service.getOrderIds(shopBean, reqIds);
//		List<String> orderNumberList = orderIdsRes.getResult();
//		if (orderNumberList != null && orderNumberList.size() > 0) {
//			for (String orderId : orderNumberList) {
//				// 获取新订单详情
////				GetOrderDetailRes orderDetailRes = service.getOrderDetailByOrderId(shopBean, orderId);
//				System.out.println();
//			}
//		}
//    }
}

