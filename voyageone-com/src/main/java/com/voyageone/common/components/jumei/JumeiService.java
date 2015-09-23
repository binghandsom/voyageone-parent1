package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.GetOrderDetailRes;
import com.voyageone.common.components.jumei.Bean.GetOrderIdsReq;
import com.voyageone.common.components.jumei.Bean.GetOrderIdsRes;
import com.voyageone.common.components.jumei.Bean.SetShippingReq;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sn3 on 2015-07-16.
 */
@Component
public class JumeiService extends JmBase{

    public static final String C_URL_GET_ORDERS = "HtOrder/GetOrderIds";
    //单个订单详情获取接口
    public static final String C_URL_GET_ORDERDETAIL_BY_ORDERID = "HtOrder/GetOrderDetailByOrderId";
    public static final String C_URL_SET_SHIPPING = "HtOrder/setShipping";

    /**
     * 根据订单ID获取订单详情
     * @param shopBean
     * @param orderId
     * @return
     * @throws Exception
     */
    public GetOrderDetailRes getOrderDetailByOrderId(ShopBean shopBean, String orderId) throws Exception {

        Map<String, String> parms = new HashMap<String, String>();
        parms.put("order_id", orderId);

        String result = reqJmApi(shopBean, C_URL_GET_ORDERDETAIL_BY_ORDERID, parms);
//        result = "{    \"error\": \"0\",    \"result\": {        \"id\": \"2691712\",        \"order_id\": \"368866791315\",        \"quantity\": \"1\",        \"total_price\": \"11.00\",        \"uid\": \"67498315\",        \"timestamp\": \"1425219690\",        \"status\": \"6\",        \"delivery_fee\": \"10.00\",        \"creation_time\": \"1425219688\",        \"payment_time\": \"1425219690\",        \"delivery_time\": \"1425278780\",        \"completed_time\": \"0\",        \"payment_method\": \"Balance\",        \"trade_no\": \"5027145214303041572846\",        \"payment_amount\": \"0\",        \"logistic_preference\": \"0\",        \"target_shipping_time\": \"0\",        \"price_discount_amount\": \"0.00\",        \"price_discount_ratio\": \"1.00\",        \"promo_cards\": \"\",        \"order_status\": \"returned\",        \"payment_status\": \"paid\",        \"confirm_type\": \"\",        \"balance_paid_amount\": \"11.00\",        \"shipping_system_id\": \"24\",        \"shipping_system_type\": \"media\",        \"prefer_delivery_time_note\": \"weekday\",        \"cart_key\": \"media/24/media\",        \"logistic_id\": \"0\",        \"logistic_track_no\": \"\",        \"shipping_status\": \"\",        \"sync_version\": \"0\",        \"promo_card_discount_price\": \"0.00\",        \"order_discount_price\": \"0.00\",        \"shipping_load_confirm_time\": \"\",        \"user_privilege_group\": \"0\",        \"order_ip\": \"182.138.102.81\",        \"order_site\": \"cd\",        \"red_envelope_card_no\": \"\",        \"red_envelope_discount_price\": \"0.00\",        \"red_envelope_discount_price_real\": \"0.00\",        \"updated_at\": \"1425279311\",        \"is_deleted\": \"0\",        \"order_type\": \"普通订单\",        \"ext_info\": \"newpay\",        \"referer_site\": \"sogou_mz\",        \"attribute_selections\": \"\",        \"notify_mobile\": \"\",        \"deposit\": \"\",        \"deposit_payment_time\": \"\",        \"deposit_payment_method\": \"\",        \"deposit_payment_amount\": \"\",        \"deposit_balance_paid_amount\": \"\",        \"balance_due\": \"\",        \"balance_due_payment_time\": \"\",        \"balance_due_payment_method\": \"\",        \"balance_due_payment_amount\": \"\",        \"balance_due_balance_paid_amount\": \"\",        \"created_time\": \"\",        \"invoice_header\": \"\",        \"invoice_medium\": \"\",        \"invoice_contents\": \"\",        \"need_invoice\": \"否\",        \"product_infos\": [            {                \"deal_hash_id\": \"df140710p848085\",                \"deal_price\": \"1.00\",                \"quantity\": \"1\",                \"sku_no\": \"df2414049934417127\",                \"settlement_price\": \"1.000\",                \"deal_short_name\": \"个性百搭水墨风印花T 恤\",                \"supplier_code\": \"123123123213\",                \"upc_code\": \"5456dd\",                \"customs_product_number\": \"41565645\",                \"attribute\": \"L\",                \"is_bom\": \"false\"            },            {                \"deal_hash_id\": \"df140710p848085\",                \"deal_price\": \"100\",                \"quantity\": \"2\",                \"sku_no\": \"df2414049934417127\",                \"settlement_price\": \"1.000\",                \"deal_short_name\": \"个性百搭水墨风印花T 恤\",                \"is_bom\": \"true\",                \"virtual_data\": [                    {                        \"sku_no\": \"701009874\",                        \"supplier_code\": \"702002653\",                        \"upc_code\": \"4901301230881\",                        \"customs_product_number\": \"41565645\",                        \"attribute\": \"L\",                        \"num\": \"4\",                        \"price\": \"50\"                    }                ]            }        ],        \"receiver_infos\": {            \"receiver_name\": \"邓叶川\",            \"address\": \"四川省-成都市-武侯区益州大道1800 号G35 楼\",            \"postalcode\": \"000000\",            \"hp\": \"18782208077\",            \"phone\": \"\",            \"email\": \"yechuand@jumei.com\",            \"id_card_num\": \"513428198702020012\"        },        \"total_products_price\": \"1\",        \"refund_info\": [            {                \"refund_id\": \"6335009\",                \"refund_status\": \"已退款\"            },            {                \"refund_id\": \"6332213\",                \"refund_status\": \"等待确认收到退货\"            }        ]    }}";
        GetOrderDetailRes res = JsonUtil.jsonToBean(result, GetOrderDetailRes.class);

        return res;
    }


    /**
     * 订单发货接口
     * 接口说明：第三方ERP 通过该接口将已经发货订单的快递信息返回给聚美系统。商家只能用
     聚美合作的快递公司进行发货，否则无法发货成功。
     调用发货接口成功后,订单状态变为已发货status=3，聚美系统订单发货时间为ERP 调用接
     口成功的时间。
     * @param shopBean
     * @param req
     * @return
     * @throws Exception
     */
    public String setShipping(ShopBean shopBean, SetShippingReq req) throws Exception {

        Map<String, String> parms = new HashMap<String, String>();
        //订单id
        parms.put("order_id", req.getOrder_id());
        //快递公司id(来自聚美快递列表) TODO 从配置表 获取
        parms.put("logistic_id", req.getLogistic_id());
        //快递单号
        parms.put("logistic_track_no", req.getLogistic_track_no());

        String result = reqJmApi(shopBean, C_URL_SET_SHIPPING, parms);

        return  result;

    }

    /**
     * 批量获取订单ID 接口
     * 特别注意：status 此参数不管如何传值，请务必保证同一时间段两种状态的订单都要抓取一次。
     * （因为卖家后台的订单下载功能也会触发订单备货，使订单状态从2 变为7，若只抓2 状态的订单可能会漏单）
     * @param shopBean
     * @param req
     * @return
     * @throws Exception
     */
    public GetOrderIdsRes getOrderIds(ShopBean shopBean, GetOrderIdsReq req) throws Exception {

        Map<String, String> parms = new HashMap<String, String>();
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
        GetOrderIdsRes res = JsonUtil.jsonToBean(result, GetOrderIdsRes.class);

        return  res;

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

