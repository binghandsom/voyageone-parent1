package com.voyageone.oms;


public interface OmsConstants {
	/**
	 * quickFilter type
	 */
	public static final int TYPE_QUICK_FILTER = 1;
	
	/**
	 * shippingMethod type
	 */
	public static final int TYPE_SHIPPING_METHOD = 2;
	
	/**
	 * paymentMethod type
	 */
	public static final int TYPE_PAYMENT_METHOD = 3;
	
	/**
	 * itemStatus type
	 */
	public static final int TYPE_ITEM_STATUS = 4;
	
	/**
	 * orderStatus type
	 */
	public static final int TYPE_ORDER_STATUS = 5;
	
	/**
	 * invoice type
	 */
	public static final int TYPE_INVOICE = 6;
	
	/**
	 * local ship on hold type
	 */
	public static final int TYPE_LOCAL_SHIP_ON_HOLD = 7;
	
	/**
	 * freight by customer type
	 */
	public static final int TYPE_FREIGHT_BY_CUSTOMER = 8;
	
	/**
	 * shop cart
	 */
	public static final int SHOP_CART = 9;
	
	/**
	 * OrderDetail product display info
	 */
	public static final class OrderDetailProductDsp {
		//	Original 信息
		/**
		 * 		Surcharge 显示信息
		 */
		public static final String SURCHARGE_TITLE = "Misc. Surcharge";
		
		/**
		 * 		Discount 显示信息
		 */
		public static final String DISCOUNT_TITLE = "Misc. Discount";
		
		/**
		 * 		shipping charge 显示信息
		 */
		public static final String SHIPPING_CHARGE_TITLE = "Shipping Charge - 2";
		
		
		//	adjustment 信息
		/**
		 * 		Surcharge 显示信息（含原因）
		 */
		public static final String SURCHARGE_ADJUSTMENT_TITLE = "Surcharge: ";
		
		/**
		 * 		Discount 显示信息（修正）
		 */
		public static final String DISCOUNT_DJUSTMENT_TITLE = "Discount adjustment - ";
		/**
		 * 		Discount 显示信息（打折取消）
		 */
		public static final String DISCOUNT_CANCELLED_TITLE = "Discount cancelled - ";
		/**
		 * 		Discount 显示信息（删除项目）
		 */
		public static final String DISCOUNT_DJUSTMENT_TITLE_DELlINEITEM = "Discount adjustment";
		
		/**
		 * 		shipping adjustment 显示信息（本次对应，运费修改原因加上）
		 */
		public static final String SHIPPING_ADJUSTMENT_TITLE = "Shipping Adjustment - ";
		
		/**
		 * 		shipping charge adjustment 显示信息
		 */
		public static final String SHIPPING_CHARGE_ADJUSTMENT_TITLE = "Shipping charge adjustment";
		
		/**
		 * 		returned 显示信息（%s sku
		 *							%s product）
		 */
		public static final String RETURN_TITLE = "Returned(1)%s:%s";
	}
	
	/**
	 * OrderDetail sku 显示信息（transaction 共用）
	 */
	public static final class OrderDetailSkuDsp {
		/**
		 * Surcharge 显示信息
		 */
		public static final String SURCHARGE_TITLE = "Surcharge";
		
		/**
		 * Discount 显示信息
		 */
		public static final String DISCOUNT_TITLE = "Discount";
		
		/**
		 * shipping 显示信息
		 */
		public static final String SHIPPING_TITLE = "Shipping";
		
		/**
		 * Product 显示信息
		 */
		public static final String PRODUCT_TITLE = "Product";
		
		/**
		 * 价差 显示信息（价差专用）
		 */
		public static final String PRICE_DIFF_TITLE = "Price Difference";
		
		/**
		 * 新订单生成（子订单专用）
		 */
		public static final String CREATED_TITLE = "Created";
	}
	
	/**
	 * Oms prop file key 信息
	 */
	public static final class PropKey {
		
		/**
		 * Note 图片地址
		 */ 
		public static final String NOTE_IMG_PATH = "note.img.path";
		
		/**
		 * Tracking 地址
		 */
		public static final String TRACKING_PATH = "tracking.path";
		/**
		 * SearchSku webservice地址（C# 美国服务器）
		 */
		public static final String SEARCHSKU_PATH = "searchsku.path";
		/**
		 * SearchSku webservice地址（C# 美国服务器）
		 */
		public static final String SEARCHSKUList_PATH = "searchskulist.path";

		/**
		 * SearchSku webservice地址（C# 中国服务器）
		 */
		public static final String SEARCHSKUCN_PATH = "searchskuCN.path";
		/**
		 * SearchSku webservice地址（C# 中国服务器）
		 */
		public static final String SEARCHSKUListCN_PATH = "searchskulistCN.path";

		/**
		 * SearchSku webservice地址（Java voyageone）
		 */
		public static final String SEARCHSKUINFO_PATH = "searchskuinfo.path";

		/**
		 * 临时 图片地址
		 */
		public static final String TEMP_IMG_PATH = "temp.img.path";
		
		/**
		 * 退款同意（聚石塔调用url）
		 */
		public static final String REFUNDS_AGREE_PATH = "refunds.agree.path";
	}
	
	/**
	 * 订单明细调整类型
	 */
	public static final class AdjustmentType {
		
		/**
		 * Surcharge
		 */ 
		public static final String SURCHARGE = "1";
		
		/**
		 * Discount
		 */ 
		public static final String DISCOUNT = "2";
		
		/**
		 * Coupon
		 */ 
		public static final String COUPON = "3";		
		
		/**
		 * Shipping
		 */ 
		public static final String SHIPPING = "4";
	}
	
	/**
	 * 订单Notes类型
	 */
	public static final class NotesType {
		/**
		 * 系统
		 */ 
		public static final String SYSTEM = "0";
		
		/**
		 * 人工
		 */
		public static final String MANUAL = "1";
	}
	
	/**
	 * 系统用户ID
	 */
	public static final String SYS_USER = "system";
	
	/**
	 * 打折类型
	 */
	public static final class DiscountType {
		/**
		 * 不打折
		 */
		public static final String NODISCOUNT = "1";
		
		/**
		 * 手工
		 */
		public static final String MANUAL = "2";
		
		/**
		 * 百分比
		 */
		public static final String PERCENT = "3";
	}
	
	// 操作许可 
	public static final String PERMIT_OK = "1";
	// 操作不许可
	public static final String PERMIT_NG = "0";
	
	//	调整 有
	public static final String ADJUSTMENT_YES = "1";
	//	未调整（真实物品）
	public static final String ADJUSTMENT_NO = "0";
	
	//	锁单状态
	public static final class LockShip {
		public static final String YES = "YES";
		public static final String No = "NO";
	}
 
	/**
	 * 产品打折标题
	 */
	public static final String PRODUCT_CANCELLED_TITLE = "CANCELED: ";
	
	/**
	 * 项目删除Notes格式（product）
	 */
	public static final String DELETE_LINEITEM_NOTES_FORMAT_FORPRODUCT = "item #%s SKU=%s Status changed to: Canceled. Reason: %s";
	
	/**
	 * 项目删除Notes格式（surcharge, discount ,shipping）
	 */
//	public static final String DELETE_LINEITEM_NOTES_FORMAT_FOROTHER = "item #%s SKU=%s Delete. Reason: %s";
	//	本次不做物理删除。
	public static final String DELETE_LINEITEM_NOTES_FORMAT_FOROTHER = "item #%s SKU=%s Canceled. Reason: %s";

	/**
	 * 项目追加Notes格式（product）
	 */
	public static final String ADD_LINEITEM_NOTES_FORMAT_FORPRODUCT = "item #%s SKU=%s add. Quantity: %s";
	
	/**
	 * 订单Lock状态变更Notes格式
	 */
	public static final String CHANGE_ORDER_LOCK_STATUS = "Changed lock status to %s.";
	
	/**
	 * 订单Approve状态变更Notes格式
	 */
	public static final String CHANGE_ORDER_APPROVED_STATUS = "Change approved status to Approved.";
	
	/**
	 * 新追加订单Notes格式
	 */
	public static final String NEW_ORDER_CREATED = "New order created.";
	
	/**
	 * 订单Cancel状态变更Notes格式
	 */
	public static final String CANCEL_ORDER = "Order Status changed to: Canceled. Reason: %s";
	
	/**
	 * 订单Restore状态变更Notes格式
	 */
	public static final String RESTORED_ORDER = "Canceled order restored. Reason: %s";
 
	/**
	 * 订单Cancel 关联明细删除Notes样式
	 */
	public static final String CANCEL_ORDER_LINEITEM_NOTES_FORMAT_FORPRODUCT = "item #%s SKU=%s Status changed to: Canceled.";
	
	/**
	 * 订单恢复 Notes样式
	 */
	public static final String CANCEL_ORDER_RESTORE = "Canceled order restored.";
	
	/**
	 * 订单Return Notes样式
	 */
	public static final String RETURN_ORDER = "Order Status changed to: Returned";
	
	/**
	 * 订单明细Return Notes样式
	 */
	public static final String RETURN_ORDERDETAIL_FORMAT = "Item #%S SKU=%S Status changed to: Returned. Reason: %s";
	
	/**
	 * 订单UnReturn Notes样式
	 */
	public static final String UNRETURN_ORDER = "Canceled the Return of (1) %s:%s";
	
	/**
	 * 订单地址变更 Notes样式
	 */
	public static final String SOLD_TO_ADDRESS_CHANGED = "Sold To Address Changed :%s";
	
	/**
	 * 订单Comment变更 Notes样式
	 */
	public static final String INTERNAL_MESSAGE_CHANGED = "InternalMessage Changed :%s";

	/**
	 * 订单GiftMessage变更 Notes样式
	 */
	public static final String GIFT_MESSAGE_CHANGED = "GiftMessage Changed :%s";
	
	/**
	 * 订单Shipping变更 Notes样式
	 */
	public static final String SHIPPING_CHANGED = "Shipping Changed :%s";
	
	/**
	 * 订单FreightCollect变更 Notes样式
	 */
	public static final String FREIGHT_COLLECT = "Freight Collect Changed :%s";

	/**
	 * 订单CustomerRefused变更 Notes样式
	 */
	public static final String CUSTOMER_REFUSED = "Customer Refused Changed :%s";

	/**
	 * 第三方订单取消 Notes样式
	 */
	public static final String THIRD_PARTY_ORDER_CANCELLED = "Third-party order change to cancelled";
	
	/**
	 * 订单CustomerComment变更Notes样式
	 */
    public static final String CUSTOMER_COMMENT_CHANGED="CustomerComment Changed :%s";
    /**
	 * 订单Invoice变更Notes样式
	 */
    public static final String INVOICE_CHANGED="Invoice Changed :%s";
    /**
	 * 订单InvoiceInfo变更Notes样式
	 */
    public static final String INVOICEINFO_CHANGED="InvoiceInfo Changed :%s";
    /**
	 * 订单地址变更 Notes样式
	 */
	public static final String SHIP_TO_ADDRESS_CHANGED = "Ship To Address Changed :%s";
	
	/**
	 * 价差订单Approved Notes格式
	 */
	public static final String PRICE_DIFFERENCE_ORDER_APPROVED = "Price Difference Order Approved.";
	
	/**
	 * 项目追加Notes格式（refundRefuse）
	 */
	public static final String REFUND_REFUSE = "卖家拒绝退款 taobao.refund.refuse.";
	
	/**
	 * 项目追加Notes格式（refundAgree）
	 */
	public static final String REFUND_AGREE = "同意退款 taobao.rp.refunds.agree.";
	
	/**
	 * 项目追加Notes格式（refundAgree local DB 更新）
	 */
	public static final String REFUND_AGREE_INSIDE = "同意退款 Local DB update success.";
	
	/**
	 * 项目追加Notes格式（returnGoodsAgree）
	 */
	public static final String RETURN_GOODS_AGREE = "卖家同意退货 taobao.rp.returngoods.agree.";
	
	/**
	 * 项目追加Notes格式（returnGoodsRefuse）
	 */
	public static final String RETURN_GOODS_REFUSE = "卖家拒绝退货 taobao.rp.returngoods.refuse.";
	
	/**
	 * 项目追加Notes格式（returngoodsrefill）
	 */
	public static final String RETURN_GOODS_REFILL = "卖家回填物流信息 taobao.rp.returngoods.refill.";
	
	/**
	 * 项目追加Notes格式（refundReview）
	 */
	public static final String REFUND_REVIEW = "审核退款单 taobao.rp.refund.review.";

	/**
	 * 卖家同意退货（taobao.rp.returngoods.agree remark）
	 */
	public static final String RETURN_GOODS_AGREE_CONTENT = "没有问题，同意退货。";
	
	/**
	 * sku调用接口type
	 */
	public static final String SKU_TYPE_ADDNEWORDER="1";
	
	public static final String SKU_TYPE_ORDERDETAIL="2";
	
	/**
	 * 更新前Note输出信息
	 */
	public static final class BEFORE_EDIT_TITLE {
		public static final String NAME = "Name = %s";
		public static final String COMPANY = "Company = %s";
		public static final String ADDRESS = "Address = %s";
		public static final String ADDRESS2 = "Address2 = %s";
		public static final String CITY = "City = %s";
		public static final String COUNTRY = "Country = %s";
		public static final String EMAIL = "Email = %s";
		public static final String STATE = "State = %s";
		public static final String ZIP = "Zip = %s";
		public static final String PHONE = "Phone = %s";
	}
	
	/**
	 * 更新前Invoice输出信息
	 */
    public static final class BEFORE_INVOICE_TITLE {
    	public static final String INVOICE = "Invoice = %s";
    	public static final String INVOICE_INFO = "Invoice info = %s";
    	public static final String INVOICE_KIND = "Invoice kind = %s";
    }

	/**
	 * CartType
	 */
	public static final class CartType {
		//	国内
		public static final String CART_CN = "1";
		//	国际
		public static final String CART_OVERSEAS = "2";
	}

	/**
	 * Platform
	 */
	public static final class Platform {
		//	线下
		public static final String OFFLINE = "0";
		//	阿里
		public static final String ALI = "1";
		//	京东
		public static final String JD = "2";
		//	独立域名
		public static final String CN = "3";
	}

    public static final class CurrencyType {
    	//	人民币
    	public static final String RMB = "￥";
    	//	美元
    	public static final String DOLLAR = "$";
    }
    
	/**
	 * taobao.rp.refunds.agree msg_code
	 */
    public static final class TmallRefundAgreeMsgCode {
    	// 发送二次验证短信成功
    	public static final String MSG_CODE_10000 = "10000";
    	// 操作成功
    	public static final String MSG_CODE_40000 = "40000";
    }

	/**
	 * 交易类型
	 */
	public static final class Transaction_Type {
		public static final String ORDER = "order";
		public static final String REFUND = "refund";
	}
    
    // 淘宝API taobao.refund.messages.get 每页条数 
    public static final long GET_TMALL_REFUND_MESSAGES_PAGESIZE = 40L;
    
    // 本公司Webservice 校验用
    public static final String CHECK_SIGNATURE = "signature";    
	/**
	 * 聚石塔WebService传递用
	 */
	public static String REFUNDS_INFO_WSDL_PARAM = "refundsInfo";	
	/**
	 * WebService 调用返回
	 */
	public static final class WSDLRet {
		// 返回Key
		public static final String resultKey = "result";
		public static final String messageCodeKey = "messageCode";
		public static final String messageKey = "message";
		public static final String messageTypeKey = "messageType";
		public static final String resultInfoKey = "resultInfo";
		
		// 返回结果
		//		正常
		public static final String resultOK = "OK";
		//		异常
		public static final String resultNG = "NG";
	}
    
//    public static final String ORDER_STATUS_IN_PROCESSING = "In Processing";
//    public static final String ORDER_STATUS_RETURN_REQUESTED = "Return Requested";

	/**
	 * 未处理订单检索
	 */
    public static final String ORDER_STATUS_QUICKFILTER_REFUND = "01";

	/**
	 * 订单检索画面，3月前订单
	 */
	public static final int ORDER_SEARCH_FROM_MONTH = -3;

	//	财务文件相关
	//	账务方式 识别子
	public static final class AccountKindIdentify {
		public static final String TG = "Partner_transaction_id";
		public static final String WX = "交易时间";
		public static final String TM = "#支付宝账务明细查询";
	}

	//	业务类型
	public static final class BusinessType {
		//	交易付款/在线支付/...
		public static final String  PAYMENT = "0";
		//	转账
		public static final String  TRANSFER = "1";
		//	收费
		public static final String  CHARGE = "2";
		//  提现
		public static final String  TIXIAN = "3";
	}

	//	业务类型描述
	public static final class BusinessTypeDescription {
		public static final String  JIAOYIFUKUAN = "交易付款";
		public static final String  SHOUFEI = "收费";
		public static final String  ZHUANZHANG = "转账";
		public static final String  TIXIAN = "提现";
	}

	//	描述
	public static final class SettlementDescription {
		public static final String FINISHED = "Finished";
	}

	//	ChannelAdvisor 的返回结果
	public static final class CAResult {
		public static final String Status_Success = "Success";
	}

	//	ChannelAdvisor 的ShippingStatus
	public static final class CAShippingStatus {
		public static final String NoChange = "NoChange";
		public static final String Unshipped = "Unshipped";
		public static final String PendingShipment = "PendingShipment";
		public static final String PartiallyShipped = "PartiallyShipped";
		public static final String Shipped = "Shipped";
	}

	public static final class CACheckoutStatus {
		public static final String Cancelled  = "Cancelled";
	}

	//	ChannelAdvisor 的OrderRefundStatus
	public static final class CAOrderRefundStatus {
		public static final String NoRefunds = "NoRefunds";
		public static final String OrderLevel = "OrderLevel";
		public static final String LineItemLevel = "LineItemLevel";
		public static final String OrderAndLineItemLevel = "OrderAndLineItemLevel";
		public static final String FailedAttemptsOnly = "FailedAttemptsOnly";
	}
}
