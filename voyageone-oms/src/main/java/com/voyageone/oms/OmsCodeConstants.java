package com.voyageone.oms;


import com.jd.open.api.sdk.request.afsservice.AfsserviceFinishedtaskGetRequest;

public interface OmsCodeConstants {
	
	/**
	 * order status （type_id = 5）
	 */
	public static final class OrderStatus {
//		public final static String APPROVED = "01";
//		public final static String RETURNED = "02";
//		public final static String CANCELED = "03";
//		public final static String CONFIRMED = "04";
//		public final static String SHIPPED = "05";
//		public final static String INPROCESSING = "06";
//		public final static String ONHOLD = "07";
		
		public final static String APPROVED = "Approved";
		public final static String RETURNED = "Returned";
		public final static String CANCELED = "Canceled";
		public final static String CONFIRMED = "Confirmed";
		public final static String SHIPPED = "Shipped";
		public final static String INPROCESSING = "In Processing";
		public final static String ONHOLD = "On Hold";
		
		public final static String RETURN_UNSUCCESS_CLOSED = "Return Unsuccess Closed";
		public final static String RETURN_REQUESTED = "Return Requested";
		public final static String RETURN_APPROVED = "Return Approved";
		public final static String RETURN_REFUSED = "Return Refused";
	}
	
//	/**
//	 * order detail status（与order status 合并）
//	 */
//	public static final class OrderDetailStatus {
//		public static final String APPROVED = "01";
//		public static final String CANCELLED = "02";
//		public static final String CONFIRMED = "03";
//		public final static String INPROCESSING = "04";
//		public final static String ONHOLD = "05";
//		public final static String RETURNED = "06";
//	}
	
	/**
	 * order 类型
	 */
	public static final class OrderKind {
		//	原始订单
		public final static String ORIGINAL_ORDER = "0";
		//	拆分订单
		public final static String SPLIT_ORDER = "1";
		//	赠品订单
		public final static String PRESENT_ORDER = "2";
		//	换货订单
		public final static String RETURN_ORDER = "3";
		//	价差订单
		public final static String PRICE_DIFF_ORDER = "4";
	}
	
	/**
	 * 付款信息
	 */
	public static final int PayInfoType = 22;
	public static final class PayInfo {
		public final static String PAY_IN_FULL = "0";
		public final static String CREDIT_DUE = "1";
		public final static String BALANCE_DUE = "2";
	}
	
	/**
	 * CartId信息
	 */
	public static final class CartId {
		//	现下订单
		public final static String OFFLINE = "22";
	}
	
	/**
	 * invoice type_id 6
	 */	
	public static final int INVOICE = 6;
	/**
	 * invoiceKind type_id 27
	 */	
	public static final int INVOICE_KIND = 27;
	/**
	 * shippingMethod type_id 2
	 */	
	public static final int SHIPPINGMETHOD_TYPE = 2;
	/**
	 * tmallRefundStatus type_id 29
	 */
	public static final int TMALL_REFUND_STATUS = 29;
	/**
	 * tmallRefundStatus type_id 29
	 */
	public static final int TMALL_REFUND_KIND = 30;
	public static final class TmallRefundKindInfo {
		//	仅退款
		public final static String HAS_GOODS_RETURN_NO = "0";
		//	退款，退货
		public final static String HAS_GOODS_RETURN_YES = "1";
	}
	
	/**
	 * Tmall 订单状态
	 */	
	public static final class TmallOrderStatus {
		// 没有创建支付宝交易
		public final static String TRADE_NO_CREATE_PAY = "TRADE_NO_CREATE_PAY";
		// 等待买家付款
		public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
		// 等待卖家发货,即:买家已付款
		public final static String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";
		// 等待买家确认收货,即:卖家已发货
		public final static String WAIT_BUYER_CONFIRM_GOODS = "WAIT_BUYER_CONFIRM_GOODS";
		// 买家已签收,货到付款专用
		public final static String TRADE_BUYER_SIGNED = "TRADE_BUYER_SIGNED";
		// 交易成功
		public final static String TRADE_FINISHED = "TRADE_FINISHED";
		// 交易关闭
		public final static String TRADE_CLOSED = "TRADE_CLOSED";
		// 交易被淘宝关闭
		public final static String TRADE_CLOSED_BY_TAOBAO = "TRADE_CLOSED_BY_TAOBAO";
		// 包含：WAIT_BUYER_PAY、TRADE_NO_CREATE_PAY
		public final static String ALL_WAIT_PAY = "ALL_WAIT_PAY";
		// 包含：TRADE_CLOSED、TRADE_CLOSED_BY_TAOBAO
		public final static String ALL_CLOSED = "ALL_CLOSED";
	}

	/**
	 * 退款申请状态
	 */
	public static final class RefundStatus {
		// 退款关闭
		public final static String CLOSE = "CLOSE";
		// 退款成功
		public final static String SUCCESS = "SUCCESS";
		// 退款申请建立
		public final static String RETURN_REQUESTED = "Return Requested";
	}

	/**
	 * 退款申请阶段
	 */
	public static final class RefundPhase {
		// 售中
		public final static String ONSALE = "onsale";
		// 售后
		public final static String AFTERSALE = "aftersale";
	}

	public static final class PaymentDescription {
		public final static String FINISHED = "Finished";
	}

	/**
	 * 付款方式
	 */
	public static final int PaymentMethodType = 3;
	public static final class PaymentMethod {
		public final static String CASH = "03";
	}

	/**
	 * 渠道ID
	 */
	public static final class OrderChannelId {
		// 斯伯丁官方旗舰店
		public final static String SP = "005";
		// Champion海外旗舰店
		public final static String CHAMPION = "007";
		// 皇马海外旗舰店
		public final static String RM = "008";
		// 珠宝旗舰店
		public final static String JW = "010";
		// BCBG
		public final static String BCBG = "012";
	}
}
