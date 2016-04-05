package com.voyageone.common.components.jd;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.OrderSearchRequest;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;
import com.voyageone.common.components.jd.base.JdBase;
import com.voyageone.common.configs.beans.ShopBean;

/**
 * 京东订单查询类
 * 
 * @author james
 *
 */
@Component
public class JdOrderService extends JdBase {

	/**
	 * 取得京东新订单
	 * 
	 * @param startdate String
	 * @param enddate String
	 * @param shop ShopBean
	 * @return List<OrderSearchInfo>
	 * @throws Exception
	 */
	public List<OrderSearchInfo> getNewOrderPage(String startdate, String enddate, ShopBean shop) throws Exception {
		
		List<OrderSearchInfo> retData = new ArrayList<>();

		OrderSearchRequest request = new OrderSearchRequest();
		// 开始时间
		request.setStartDate(startdate);
		// 结束时间
		request.setEndDate(enddate);

		// WAIT_SELLER_STOCK_OUT : 等待卖家发货
		// LOCKED : 顾客没有点击确认收货之前取消订单
		StringBuilder sb = new StringBuilder();
		sb.append("WAIT_SELLER_STOCK_OUT");
		sb.append(",LOCKED");

		request.setOrderState(sb.toString());
		
		StringBuilder fields = new StringBuilder();
		fields.append("order_id,");				// 订单id 
		fields.append("order_source,");			// 订单来源 
		fields.append("vender_id,");			// 商家id 
		fields.append("pay_type,");				// 支付方式（1货到付款, 2邮局汇款, 3自提, 4在线支付, 5公司转账, 6银行卡转账） 
		fields.append("order_total_price,");	// 订单总金额 
		fields.append("order_seller_price,");	// 订单货款金额（订单总金额-商家优惠金额） 
		fields.append("order_payment,");		// 用户应付金额 
		fields.append("freight_price,");		// 商品的运费 
		fields.append("seller_discount,");		// 商家优惠金额 
		fields.append("order_state,");			// 订单状态（英文） 
		fields.append("order_state_remark,");	// 订单状态说明（中文） 
		fields.append("delivery_type,");		// 送货（日期）类型（1-只工作日送货(双休日、假日不用送);2-只双休日、假日送货(工作日不用送);3-工作日、双休日与假日均可送货;其他值-返回“任意时间”） 
		fields.append("invoice_info,");			// 发票信息 “invoice_info: 不需要开具发票”下无需开具发票；其它返回值请正常开具发票 
		fields.append("order_remark,");			// 买家下单时订单备注 
		fields.append("order_start_time,");		// 下单时间 
		fields.append("order_end_time,");		// 结单时间 如返回信息为“0001-01-01 00:00:00”和“1970-01-01 00:00:00”，可认为此订单为未完成状态。 
		fields.append("modified,");				// 订单更新时间 
		fields.append("consignee_info,");		// 收货人基本信息 
		fields.append("item_info_list,");		// 
		fields.append("coupon_detail_list,");	// 优惠详细信息 
		fields.append("vender_remark,");		// 商家订单备注（不大于500字符） 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("balance_used,");			// 余额支付金额 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("payment_confirm_time,");	// 付款确认时间 如果没有付款时间 默认返回0001-01-01 00:00:00 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("waybill,");				// 运单号(当厂家自送时运单号可为空，不同物流公司的运单号用|分隔，如果同一物流公司有多个运单号，则用英文逗号分隔) 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("logistics_id,");			// 物流公司ID 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("vat_invoice_info,");		// 增值税发票 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("parent_order_id,");		// 父订单号 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("pin");					// 买家的账号信息 可选字段，需要在输入参数optional_fields中写入才能返回 

		request.setOptionalFields(fields.toString());

		// 每页件数(最大100件)
		request.setPageSize("99");
		
		// 当前页:第一页
		int intPageNow = 1;
		
		try {
			while (true) {
				request.setPage(String.valueOf(intPageNow));
				OrderSearchResponse response = reqApi(shop, request);
				if (response != null) {
					// 京东返回正常的场合
					if ("0".equals(response.getCode())) {
						// 没有订单信息
						if (response.getOrderInfoResult().getOrderInfoList().isEmpty()) {
							break;
						}
						// 当前页的订单数据获得
						retData.addAll(response.getOrderInfoResult().getOrderInfoList());
						
						// 下一页
						intPageNow++;
					} else {
						logger.info(response.getMsg());
						
						throw new Exception(shop.getShop_name() + "取得新订单失败 " + response.getMsg());
					}
				} else {
					throw new Exception(shop.getShop_name() + "取得新订单失败 ");
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			throw new Exception(shop.getShop_name() + "取得新订单失败 " + ex.getMessage());
		}
		
		return retData;
	}
	
	/**
	 * 获得京东交易成功和锁定订单
	 * 
	 * @param startdate String
	 * @param enddate String
	 * @param shop ShopBean
	 * @return List<OrderSearchInfo>
	 * @throws Exception
	 */
	public List<OrderSearchInfo> getChangedOrderPage(String startdate, String enddate, ShopBean shop) throws Exception {
		
		List<OrderSearchInfo> retData = new ArrayList<OrderSearchInfo>();

		OrderSearchRequest request = new OrderSearchRequest();
		// 开始时间
		request.setStartDate(startdate);
		// 结束时间
		request.setEndDate(enddate);

		// FINISHED_L:完成订单（交易成功）
		// LOCKED : 顾客没有点击确认收货之前取消订单
		StringBuilder sb = new StringBuilder();
		sb.append("FINISHED_L");
		sb.append(",LOCKED");
		
		request.setOrderState(sb.toString());
		
		StringBuilder fields = new StringBuilder();
		fields.append("order_id,");				// 订单id 
		fields.append("order_source,");			// 订单来源 
		fields.append("vender_id,");			// 商家id 
		fields.append("pay_type,");				// 支付方式（1货到付款, 2邮局汇款, 3自提, 4在线支付, 5公司转账, 6银行卡转账） 
		fields.append("order_total_price,");	// 订单总金额 
		fields.append("order_seller_price,");	// 订单货款金额（订单总金额-商家优惠金额） 
		fields.append("order_payment,");		// 用户应付金额 
		fields.append("freight_price,");		// 商品的运费 
		fields.append("seller_discount,");		// 商家优惠金额 
		fields.append("order_state,");			// 订单状态（英文） 
		fields.append("order_state_remark,");	// 订单状态说明（中文） 
		fields.append("delivery_type,");		// 送货（日期）类型（1-只工作日送货(双休日、假日不用送);2-只双休日、假日送货(工作日不用送);3-工作日、双休日与假日均可送货;其他值-返回“任意时间”） 
		fields.append("invoice_info,");			// 发票信息 “invoice_info: 不需要开具发票”下无需开具发票；其它返回值请正常开具发票 
		fields.append("order_remark,");			// 买家下单时订单备注 
		fields.append("order_start_time,");		// 下单时间 
		fields.append("order_end_time,");		// 结单时间 如返回信息为“0001-01-01 00:00:00”和“1970-01-01 00:00:00”，可认为此订单为未完成状态。 
		fields.append("modified,");				// 订单更新时间 
		fields.append("consignee_info,");		// 收货人基本信息 
		fields.append("item_info_list,");		// 
		fields.append("coupon_detail_list,");	// 优惠详细信息 
		fields.append("vender_remark,");		// 商家订单备注（不大于500字符） 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("balance_used,");			// 余额支付金额 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("payment_confirm_time,");	// 付款确认时间 如果没有付款时间 默认返回0001-01-01 00:00:00 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("waybill,");				// 运单号(当厂家自送时运单号可为空，不同物流公司的运单号用|分隔，如果同一物流公司有多个运单号，则用英文逗号分隔) 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("logistics_id,");			// 物流公司ID 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("vat_invoice_info,");		// 增值税发票 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("parent_order_id,");		// 父订单号 可选字段，需要在输入参数optional_fields中写入才能返回 
		fields.append("pin");					// 买家的账号信息 可选字段，需要在输入参数optional_fields中写入才能返回 

		request.setOptionalFields(fields.toString());

		// 每页件数(最大100件)
		request.setPageSize("99");
		
		// 当前页:第一页
		int intPageNow = 1;
		
		try {
			while (true) {
				request.setPage(String.valueOf(intPageNow));
				OrderSearchResponse response = reqApi(shop, request);
				if (response != null) {
					// 京东返回正常的场合
					if ("0".equals(response.getCode())) {
						// 没有订单信息
						if (response.getOrderInfoResult().getOrderInfoList().isEmpty()) {
							break;
						}
						// 当前页的订单数据获得
						retData.addAll(response.getOrderInfoResult().getOrderInfoList());
						
						// 下一页
						intPageNow++;
					} else {
						logger.info(response.getMsg());
						
						throw new Exception(shop.getShop_name() + "取得状态变化订单失败 " + response.getMsg());
					}
				} else {
					throw new Exception(shop.getShop_name() + "取得状态变化订单失败 ");
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			throw new Exception(shop.getShop_name() + "取得状态变化订单失败 " + ex.getMessage());
		}
		
		return retData;
	}
	
//	public static void main(String[] args) throws Exception {
//		JdOrderService service = new JdOrderService();
//		ShopBean shop = new ShopBean();
//		List<OrderSearchInfo> orderList = service.getChangedOrderPage("2015-06-15 10:25:16", "2015-07-03 10:25:16", shop);
//		System.out.println();
//	}
	
}
