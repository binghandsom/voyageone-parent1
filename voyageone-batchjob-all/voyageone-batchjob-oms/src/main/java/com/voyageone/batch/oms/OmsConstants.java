package com.voyageone.batch.oms;


public class OmsConstants {
	/**
	 * 订单地址国家信息_中国
	 */
	public static final String ADDRESS_COUNTRY_CHINA = "中国";
	
	/**
	 * 差价订单
	 */
	public static final String PRICES_GAP = "price-difference-001";
	
	/**
	 * 独立域名差价订单
	 */
	public static final String PRICES_GAP_SELF = "price-difference-001-OneSize";
	
	/**
	 * 没有sku的错误商品
	 */
	public static final String NO_SKU_PRODUCT = "no-sku-product-xxx";
	
	/**
	 * 订单来源（阿里巴巴）
	 */
	public static final String ORDER_PLATFORM_ID_ALIBABA = "1";
	
	/**
	 * targetId  sneakerhead旗舰店
	 */
	public static String TARGET_SNEAKERHEAD_TMALL = "1";
	/**
	 * targetId  sneakerhead淘宝店
	 */
	public static String TARGET_SNEAKERHEAD_TAOBAO = "2";
	/**
	 * targetId  sneakerhead海外旗舰店
	 */
	public static String TARGET_SNEAKERHEAD_TMALLG = "3";
	/**
	 * targetId  京东
	 */
	public static String TARGET_JD = "4";
	
	/**
	 * targetId  PortAmerican海外专营店
	 */
	public static String TARGET_PA_TMALLG = "5";
	/**
	 * targetId  斯伯丁官方旗舰店
	 */
	public static String TARGET_SPALDING_TMALL = "6";
	/**
	 * targetId  JuicyCouture海外旗舰店
	 */
	public static String TARGET_JC_TMALLG = "7";
	/**
	 * targetId  BHFO海外旗舰店
	 */
	public static String TARGET_BHFO_TMALLG = "13";
	/**
	 * targetId  京东国际
	 */
	public static String TARGET_JDG = "14";
	/**
	 * sneakerhead channelId
	 */
	public static String CHANNEL_SNEAKERHEAD = "001";
	/**
	 * spalding channelId
	 */
	public static String CHANNEL_SPALDING = "005";
	/**
	 * bhfo channelId
	 */
	public static String CHANNEL_BHFO = "006";
	/**
	 * CP channelId
	 */
	public static String CHANNEL_CP = "007";
	/**
	 * PA channelId
	 */
	public static String CHANNEL_PA = "002";
	/**
	 * JW channelId
	 */
	public static String CHANNEL_JW = "010";
	/**
	 * JC channelId
	 */
	public static String CHANNEL_JC = "004";
	/**
	 * RM channelId
	 */
	public static String CHANNEL_RM = "008";
	
	/**
	 * cartId 京东
	 */
	public static String CART_JD = "24";
	/**
	 * title 京东
	 */
	public static String TITLE_JD = "京东";
	/**
	 * cartId 京东国际
	 */
	public static String CART_JDG = "26";
	/**
	 * title 京东国际
	 */
	public static String TITLE_JDG = "京东国际";
	/**
	 * cartId 独立域名
	 */
	public static String CART_SELF = "25";
	/**
	 * cartId 聚美
	 */
	public static String CART_JM = "27";
	/**
	 * title 聚美
	 */
	public static String TITLE_JM = "聚美";
	
	public static int DATACOUNT_300 = 300;
	
	public static int DATACOUNT_100 = 100;
	
	public static int TIME_ZONE_8 = 8;
	
	public static String TRANSACTION_PRODUCT = "Product";
	
	public static String TRANSACTION_DISCOUNT = "Discount";
	
	public static String TRANSACTION_SHIPPING = "Shipping";
	
	public static String TRANSACTION_SURCHARGE = "Surcharge";
	/**
	 * 退款阶段:售中
	 */
	public static String REFUND_PHASE_ONSALE = "onsale";
	/**
	 * 退款阶段:售后
	 */
	public static String REFUND_PHASE_AFTERSALE = "aftersale";
	/**
	 * transactions type
	 */
	public static  final String TRANSACTIONS_TYPE_ORDER = "order";
	
	/**
	 * 时间拼接
	 */
	public static String SENDORDER_FORMTIME=":00:00:00";
	
	public static String SENDORDER_ENDTIME=":23:59:59";
	/**
	 * 店铺文件名
	 */
	
	public static String SENDORDER_FILENAME_TG="orderlist_tg.csv";
	
	public static String SENDORDER_FILENAME_TB="orderlist_tb.csv";
	
	public static String SENDORDER_FILENAME_TM="orderlist_tm.csv";
	
	public static String SENDORDER_FILENAME_JD="orderlist_jd.csv";
	
	public static String SENDORDER_FILENAME_YM="orderlist_ym.csv";
	
	public static String SENDORDER_FILENAME="orderlist_tg.csv";
	
	public static String SENDORDER_FILENAMERE="orderlist_tg";
	/**
	 * 邮件格式
	 */
	public final static String SUBJECT_N = "OMS无重复订单记录";
	
	public final static String SUBJECT_Y = "请确认OMS订单重复数据";

	public final static String SAME_ORDERMAIL_TABLE=
			"<div><span>%s</span>" +
					"<table><tr>" +
					"<th>OrderNumber</th>" +
                    "<th>SourceOrderId</th>" +
					"</tr>%s</table></div>";

	public final static String SAME_ORDERMAIL_HEAD = "OMS订单重复列表";

	public static final String SAME_ORDERMAIL_ROW =
			"<tr>" +
					"<td>%s</td>" +
					"<td>%s</td>" +
			"</tr>";
	 public static final String EMAIL_STYLE_STRING = "<style>"
	            +  "body{font-family:'微软雅黑',sans-serif}"
	            + "table,th,td{border:1px solid silver;border-collapse:collapse;font-size:15px}"
	            + "th,td{padding:3px 5px}"
	            +  "th{background:gray;color:white;border:1px solid #514e3a}"
	            + "</style>";
	/**
	 * 收件人信息检查邮件TABLE格式
	 */
	public static final String SHIP_INFO_CHECK_TABLE =
		"<div><span>%s</span>" +
		"<table><tr>" +
		"<th>店铺</th>" +
		"<th>订单渠道</th>" +
		"<th>源订单号</th>" +
		
		"<th>收件人电话</th>" +
		"<th>国家</th>" +
		"<th>省</th>" +
		"<th>市</th>" +
		"<th>邮编</th>" +
		"<th>地址</th>" +
		"<th>快递方式</th>" +
		"</tr>%s</table></div>";
	
	/**
	 * 收件人信息检查邮件ROW格式
	 */
	public static final String SHIP_INFO_CHECK_ROW =
		"<tr>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
		"</tr>";
	
	/**
	 * 收件人信息检查邮件HEAD
	 */
	public static final String SHIP_INFO_CHECK_HEAD = "收件人信息不全，请客服及时处理";
	
	/**
	 * 收件人信息检查邮件SUBJECT
	 */
	public static final String SHIP_INFO_CHECK_SUBJECT = "订单收件人信息不全，请客服及时处理";
	/**
	 * 日报发送邮件格式
	 */
	public static final String SEND_ORDER_ROW=
		"<tr>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
		"</tr>";
   public static final String SEND_ORDER_TABLE=
		   "<div><span>%s</span>"+
		    "<table><tr>" +
		    "<th>渠道</th>" +
			"<th>订单数</th>" +
			"<th>sku总数</th>" +
			"<th>sku种类数</th>" +
			"<th>顾客数</th>" +
			"<th>总销售额</th>" +
			"</tr>%s</table></div>";
   public static final String SEND_ORDER_TABLE_NULL=
		   "<div><span>%s</span>";
		   
   
   public static final String SEND_ORDER_HEAD = "订单统计详情如下!";
   
   public static final String SEND_ORDER_HEAD_NULL = "昨日无订单!";
   
   public static final String SEND_ORDER_GRAND_TOTAL_ZREO = "0";
   
   public static  final String SEND_ORDER_SERVICE = "searchsku.path";
   
   public static  final String SEND_ORDER_FOLDER = "CSV_FOLDER";
   
   public static final int SEND_ORDER_ZONE = 8;
   
   public static final String SEND_ORDER_CARTTG = "23";
   
   public static final String SEND_ORDER_CARTTB = "21";
   
   public static final String SEND_ORDER_CARTTM = "20";
   
   public static final String SEND_ORDER_CARTJD = "24";
   
   public static final String SEND_ORDER_CARTYM = "25";
   
   /**
    * 独立域名平台id
    */
   public static final String PLATFORM_ID_SELF = "3";
   
   /**
    * 独立域名已取消订单判断时间间隔
    */
   public static final long CANCEL_TIME_INTERVAL = 36;
   
   // synship Webservice 校验用
   public static final String CHECK_SYNSHIP = "SynShip";
   
    //API返回结果
    public static final String RESULT_OK = "OK";
    public static final String RESULT_ERR = "ERR";
    
    public static final String HAVING_GIFTED_CUSTOMER_NAME = "customerName";
    public static final String HAVING_GIFTED_CUSTOMER_COUNT = "customerCount";
    
    public static final String GIFT_PROPERTY_PRICE_THAN = "priceThan";
    public static final String GIFT_PROPERTY_BUY_THAN = "buyThan";
    public static final String GIFT_PROPERTY_REGULAR_CUSTOMER = "regularCustomer";
    
    public static final String GIFT_PROPERTY_PRICE_THAN_REPEAT = "priceThanRepeat";
    public static final String GIFT_PROPERTY_BUY_THAN_REPEAT = "buyThanRepeat";
    public static final String GIFT_PROPERTY_REGULAR_CUSTOMER_REPEAT = "regularCustomerRepeat";
    
    public static final String GIFT_PROPERTY_PRICE_THAN_MAXSUM = "priceThanMaxSum";
    public static final String GIFT_PROPERTY_BUY_THAN_MAXSUM = "buyThanMaxSum";
    public static final String GIFT_PROPERTY_REGULAR_CUSTOMER_MAXSUM = "regularCustomerMaxSum";
    
    public static final String GIFT_PROPERTY_PRICE_THAN_SELECTONE = "priceThanSelectOne";
    public static final String GIFT_PROPERTY_BUY_THAN_SELECTONE = "buyThanSelectOne";
    public static final String GIFT_PROPERTY_REGULAR_CUSTOMER_SELECTONE = "regularCustomerSelectOne";

	public static final String GIFT_PROPERTY_PRICE_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY = "priceThanSelectAppointOneWithInventory";
	public static final String GIFT_PROPERTY_PRICE_THAN_SELECT_PLATFORM_CART = "priceThanSelectPlatformCart";
    
    public static final String GIFT_PROPERTY = "";
    public static final String GIFT_PROPERTY_REPEAT = "repeat";
    public static final String GIFT_PROPERTY_MAXSUM = "maxSum";
    public static final String GIFT_PROPERTY_SELECTONE = "selectOne";
    
	/**
	 * 链接推送失败检查邮件TABLE格式
	 */
	public static final String SHOT_URL_CHECK_FAILURE_TABLE =
		"<div><span>%s</span>" +
		"<table><tr>" +
		"<th>店铺</th>" +
		"<th>订单渠道</th>" +
		"<th>cartId</th>" +
		"<th>源订单号</th>" +
		"<th>收件人姓名</th>" +
		"<th>收件人电话</th>" +
		"<th>错误消息</th>" +
		"</tr>%s</table></div>";
	
	/**
	 * 短链接推送失败检查邮件HEAD
	 */
	public static final String SHOT_URL_CHECK_FAILURE_HEAD = "OMS中以下信息推送到Synship的短链接表失败，请确认！";
	
	/**
	 * 短链接推送失败检查邮件SUBJECT
	 */
	public static final String SHOT_URL_CHECK_FAILURE_SUBJECT = "OMS中有新订单信息推送到Synship的短链接表失败";
	
	/**
	 * 短链接推送失败检查邮件ROW格式
	 */
	public static final String SHOT_URL_CHECK_FAILURE_ROW =
		"<tr>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
		"</tr>";

	/**
	 * 扩展订单状态
	 */
	public static final class ExtOrderStatus {
		// 正常订单
		public static final String PAID = "paid";
		// 取消订单
		public static final String CANCELLING = "cancelling";
	}

	public static final class ExtOrderItem {
		public static final String SOURCE = "Tmall";
		public static final String SUBSOURCE = "Tmall0";
		public static final String SITENAME = "Tmall Real Madrid";
		public static final String SITEID = "157";
		public static final String MARKETPLACE = "CN";

		public static final String COUNTRY_CODE = "CN";
		public static final String COUNTRY = "China";

		public static final String POSTAL_SERVICE = "EMS";

		public static final String CURRENCY = "CNY";

	}

	/**
	 * 收件人信息检查邮件SUBJECT
	 */
	public static final String RM_OUT_OF_STOCK_CHECK_SUBJECT = "皇马订单无法发货，请客服及时处理";
	/**
	 * 收件人信息检查邮件SUBJECT
	 */
	public static final String RM_ERROR_CHECK_SUBJECT = "皇马订单导入异常，请IT及时处理";

	/**
	 * Sears 订单推送异常SUBJECT
	 */
	public static final String SEARS_PUSH_ORDER_ERROR_SUBJECT = "Sears 订单推送异常，请客服及时处理";

	/**
	 * Sears 订单超卖SUBJECT
	 */
	public static final String SEARS_OUT_OF_STOCK_CHECK_SUBJECT  = "Sears 订单无法发货，请客服及时处理";

	/**
	 * Sears 订单超卖信息
	 */
	public static final String SEARS_OUT_OF_STOCK_MESSAGE = "Sears 订单超卖";

	/**
	 * Sears 订单部分取消SUBJECT
	 */
	public static final String SEARS_PARTIAL_CANCELLED_CHECK_SUBJECT  = "Sears 订单部分取消，请客服及时处理";

	/**
	 * Sears 订单部分取消信息
	 */
	public static final String SEARS_PARTIAL_CANCELLED_MESSAGE = "Sears 订单部分取消";

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
	 * 皇马订单状态
	 */
	public static final class RMOrderStatus {
		public static final String None = "None";
		public static final String FailedValidation = "FailedValidation";
		public static final String FailedOrderCreation = "FailedOrderCreation";
		public static final String PartiallyFulfillable = "PartiallyFulfillable";
		public static final String TotallyUnfulfillable = "TotallyUnfulfillable";
		public static final String BackOrder = "BackOrder";
		public static final String InProgress = "InProgress";
		public static final String Cancelled = "Cancelled";
		public static final String Despatched = "Despatched";
		public static final String DespatchedWithTracking = "DespatchedWithTracking";
		public static final String Refunded = "Refunded";
		public static final String ReturnedAndRefunded = "ReturnedAndRefunded";
		public static final String InPicking = "InPicking";
		public static final String ChargedNotShipped = "ChargedNotShipped";
		public static final String AwaitingRefund = "AwaitingRefund";
		public static final String AwaitingChequeRefund = "AwaitingChequeRefund";

	}

	/**
	 * Sears 明细项目状态
	 */
	public static final class SearsOrderItemStatus {
		public static final String Cancelled = "Cancelled";
		public static final String Returned = "Returned";
	}

	/**
	 * Sears 明细项目CancelReasonCode
	 */
	public static final class SearsOrderItemCancelReasonCode {
		// Sears item out of stock.
		public static final String OutOfStock = "16";
	}

	/**
	 * 未自动Approved订单检查邮件TABLE格式
	 */
	public static final String NOT_APPROVED_CHECK_TABLE =
		"<div><span>%s</span>" +
		"<table><tr>" +
		"<th>店铺</th>" +
		"<th>OMS订单号</th>" +
		"<th>源订单号</th>" +
		"</tr>%s</table></div>";
	
	/**
	 * 未自动Approved订单检查邮件ROW格式
	 */
	public static final String NOT_APPROVED_CHECK_ROW =
		"<tr>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
		"</tr>";
	
	/**
	 * 未自动Approved检查邮件HEAD
	 */
	public static final String NOT_APPROVED_CHECK_HEAD = "订单没有自动Approved，请客服及时处理";
	
	/**
	 * 未自动Approved检查邮件SUBJECT
	 */
	public static final String NOT_APPROVED_CHECK_SUBJECT = "订单没有自动Approved，请客服及时处理";

	/**
	 * 第三方订单处理
	 */
	public static final String PATERN_TABLE =
			"<div><span>%s</span>" +
					"<table><tr>" +
					"<th>SourceOrderId</th>" +
					"</tr>%s</table></div>";

	/**
	 * 第三方订单处理ROW格式
	 */
	public static final String PATERN_TABLE_ROW =
			"<tr>" +
					"<td>%s</td>" +
					"</tr>";

	/**
	 * 第三方订单处理（含原因）
	 */
	public static final String PATERN_TABLE_REASON =
			"<div><span>%s</span>" +
					"<table><tr>" +
						"<th>SourceOrderId</th>" +
						"<th>Reason</th>" +
					"</tr>%s</table></div>";

	/**
	 * 第三方订单处理ROW格式（含原因）
	 */
	public static final String PATERN_TABLE_REASON_ROW =
			"<tr>" +
					"<td>%s</td>" +
					"<td>%s</td>" +
			"</tr>";
	
	/**
	 * sneakerhead 88店庆每小时统计消费前10顾客邮件TABLE格式
	 */
	public static final String SNEAKERHEAD_TOP10_CHECK_TABLE =
		"<div><span>%s</span>" +
		"<table><tr>" +
		"<th>排名</th>" +
		"<th>旺旺ID</th>" +
		"<th>消费金额</th>" +
		"</tr>%s</table></div>";
	
	/**
	 * sneakerhead 88店庆每小时统计消费前10顾客邮件ROW格式
	 */
	public static final String SNEAKERHEAD_TOP10_CHECK_ROW =
		"<tr>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
				"<td>%s</td>" +
		"</tr>";
	
//	/**
//	 * sneakerhead 88店庆每小时统计消费前10顾客邮件HEAD
//	 */
//	public static final String SNEAKERHEAD_TOP10_CHECK_HEAD = "sneakerhead 88店庆  土豪大咖消费排行榜";

	/**
	 * 统计消费前111顾客邮件HEAD
	 */
	public static final String SNEAKERHEAD_TOP10_CHECK_HEAD = "双十一消费排行榜";
	
//	/**
//	 * sneakerhead 88店庆每小时统计消费前10顾客邮件SUBJECT
//	 */
//	public static final String SNEAKERHEAD_TOP10_CHECK_SUBJECT = "sneakerhead 88店庆  土豪大咖消费排行榜";

	/**
	 * 统计消费前111顾客邮件SUBJECT
	 */
	public static final String SNEAKERHEAD_TOP10_CHECK_SUBJECT = "双十一消费排行榜";

	public static final String html4Space = "&nbsp;&nbsp;&nbsp;&nbsp;";
	/**
	 * sneakerhead 统计消费前多少名顾客邮件ROW格式
	 */
	public static final String SNEAKERHEAD_TOP_SPENDING_RANKING_ROW =
			html4Space + html4Space + html4Space + html4Space + "&lt;tr height=\"30\"&gt;" + "<br>" +
					html4Space + html4Space + html4Space + html4Space + html4Space + "&lt;td style=\"width:160px;border: solid 1px #000;\">%s&lt;/td&gt;" + "<br>" +
					html4Space + html4Space + html4Space + html4Space + html4Space + "&lt;td style=\"width:160px;border: solid 1px #000;\">%s&lt;/td&gt;" + "<br>" +
					html4Space + html4Space + html4Space + html4Space + html4Space + "&lt;td style=\"width:160px;border: solid 1px #000;\">%s&lt;/td&gt;" + "<br>" +
			html4Space + html4Space + html4Space + html4Space + "&lt;/tr&gt;" + "<br>";

	/**
	 * sneakerhead 统计消费前多少名顾客邮件ROW格式
	 */
	public static final String SNEAKERHEAD_TOP_SPENDING_RANKING_COLOUM =
			html4Space + html4Space + "&lt;td&gt;" + "<br>" +
					html4Space + html4Space + html4Space + "&lt;table width=\"480\" style=\"border-right:solid 3px #000;\"&gt;" + "<br>" +
						"%s" +
					html4Space + html4Space + html4Space + "&lt;/table&gt;" + "<br>" +
			html4Space + html4Space + "&lt;/td&gt;" + "<br>";

	/**
	 * sneakerhead 统计消费前多少名顾客邮件ROW格式
	 */
	public static final String SNEAKERHEAD_TOP_SPENDING_RANKING_TABLE =
			"&lt;table style=\"text-align:center;font-size:14px;background-color:#333333;color:#fff;\"&gt;" + "<br>" +
				html4Space + "&lt;tr&gt;" + "<br>" +
					"%s" +
				html4Space + "&lt;/tr&gt;" + "<br>" +
			"&lt;/table&gt;" + "<br>";
}
