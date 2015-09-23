package com.voyageone.batch.oms;

public final class SynShipContants {
	public static final String encoding = "UTF-8";
	
	public static final String PRICE_RMB = "RMB";
	
	public static final String PRICE_USD = "USD";
	
	public static final String EXPRESS_TYPE_STAND = "标准快递";
	public static final String EXPRESS_TYPE_ECONOMY = "经济快递";
	public static final String EXPRESS_TYPE_COD = "代收货款";
	public static final String EXPRESS_TYPE_COLLECT = "收件人付费";
	
	//订单状态
	//导入SE 等待客服审批
	public static final String ORDER_STATUS_IN_PROCESSING = "00";
	//客服审批通过
	public static final String ORDER_STATUS_IN_APPROVED = "01";
	//物流系统订单数据准备中
	public static final String ORDER_STATUS_DATA_IN_PROCESSING = "10";
	//库存分配完毕等待工人拿货
	public static final String ORDER_STATUS_OPEN = "11";
	//工人找到货物
	public static final String ORDER_STATUS_RESERVED= "12";
	//工人装箱完成（包含打印面单 ）
	public static final String ORDER_STATUS_PACKAGED= "13";
	//国际段发货完成
	public static final String ORDER_STATUS_SHIPPED= "14";
	//客人提出退货申请
	public static final String ORDER_STATUS_RETURN_REQUEST = "21";
	//客人提出退货 被拒绝
	public static final String ORDER_STATUS_RETURN_REFUSED = "22";
	//客人提出退货 审批通过
	public static final String ORDER_STATUS_RETURN_APPROVED = "23";
	//客人提出退货 物品已经退回
	public static final String ORDER_STATUS_RETURNED = "24";
	//客人收到货物并且确认了订单
	public static final String ORDER_STATUS_CONFIRMED= "10";
	//客人取消订单
	public static final String ORDER_STATUS_CANCEL = "99";
	
	//标签打印状态
	public static final String LABEL_STATUS_PRINTED = "1";
	public static final String LABEL_STATUS_NOT_PRINTED = "0";
	
	//Shipment状态
	public static final String SHIPMENT_STATUS_OPEN = "1";
	public static final String SHIPMENT_STATUS_CLOSE = "2";
	
	//API返回结果
	public static final String RESULT_OK = "OK";
	public static final String RESULT_ERR = "ERR";
	
	public static final String CARRIER_SYNSHIP = "SynShip";
	
	//API异常
	public static final String ERR_CODE_MISS_PARAMETER = "1001";
	public static final String ERR_DESCRIPTION_MISS_PARAMETER = "miss-parameter";
	public static final String ERR_DESCRIPTION_CN_MISS_PARAMETER = "参数为空";
	
	public static final String ERR_CODE_KEYVERIFICATION_UNSUCCESS = "1002";
	public static final String ERR_DESCRIPTION_KEYVERIFICATION_UNSUCCESS = "key-verification-unsuccess";
	public static final String ERR_DESCRIPTION_CN_KEYVERIFICATION_UNSUCCESS = "key校验失败";
	
	public static final String ERR_CODE_MISS_ORDERDATA = "1003";
	public static final String ERR_DESCRIPTION_MISS_ORDERDATA = "miss-orderdata";
	public static final String ERR_DESCRIPTION_CN_MISS_ORDERDATA = "没有订单数据";
	
	public static final String ERR_CODE_ORDERDATA_OVER200 = "1004";
	public static final String ERR_DESCRIPTION_ORDERDATA_OVER200 = "orderdata-over-200";
	public static final String ERR_DESCRIPTION_CN_ORDERDATA_OVER200 = "订单数据超过200";
	
	public static final String ERR_CODE_ORDER_INSERT_FAILURE = "1005";
	public static final String ERR_DESCRIPTION_ORDER_INSERT_FAILURE = "order-insert-failure";
	public static final String ERR_DESCRIPTION_CN_ORDER_INSERT_FAILURE = "订单插入失败";
	
	public static final String ERR_CODE_MISS_RESERVATION_DATA = "1006";
	public static final String ERR_DESCRIPTION_MISS_RESERVATION_DATA = "miss-reservation-data";
	public static final String ERR_DESCRIPTION_CN_MISS_RESERVATION_DATA = "没有reservation状态变更数据";
	
	public static final String ERR_CODE_RESERVATIONID_NOT_EXIST = "1007";
	public static final String ERR_DESCRIPTION_RESERVATIONID_NOT_EXIST = "reservationid-not-exist";
	public static final String ERR_DESCRIPTION_CN_RESERVATIONID_NOT_EXIST = "reservation状态变更的数据在物流系统没找到";
	
	public static final String ERR_CODE_RESERVATIONID_UPDATE_FAILURE = "1008";
	public static final String ERR_DESCRIPTION_RESERVATIONID_UPDATE_FAILURE = "reservationid-update-failure";
	public static final String ERR_DESCRIPTION_CN_RESERVATIONID_UPDATE_FAILURE = "reservationid数据更新失败";
	
	public static final String ERR_CODE_ORDER_UPDATE_FAILURE = "1009";
	public static final String ERR_DESCRIPTION_ORDER_UPDATE_FAILURE = "order-update-failure";
	public static final String ERR_DESCRIPTION_CN_ORDER_UPDATE_FAILURE = "order数据更新失败";
	
	public static final String ERR_CODE_ORDERINFO_NOT_EXIST = "1010";
	public static final String ERR_DESCRIPTION_ORDERINFO_NOT_EXIST = "orderInfo-not-exist";
	public static final String ERR_DESCRIPTION_CN_ORDERINFO_NOT_EXIST = "order信息变更的数据在物流系统没找到";
	
	public static final String ERR_CODE_ORDERINFO_UPDATE_FAILURE = "1011";
	public static final String ERR_DESCRIPTION_ORDERINFO_UPDATE_FAILURE = "orderInfo-update-failure";
	public static final String ERR_DESCRIPTION_CN_ORDERINFO_UPDATE_FAILURE = "order信息变更数据更新失败";
	
	public static final String ERR_CODE_ORDERINFO_UPDATE_IGNORE = "1012";
	public static final String ERR_DESCRIPTION_ORDERINFO_UPDATE_IGNORE = "orderInfo-update-ignore";
	public static final String ERR_DESCRIPTION_CN_ORDERINFO_UPDATE_IGNORE = "该订单已打印，信息变更数据更新忽略";
	
	public static final String ERR_CODE_ORDER_UPDATE_IGNORE = "1013";
	public static final String ERR_DESCRIPTION_ORDER_UPDATE_IGNORE = "order-update-ignore";
	public static final String ERR_DESCRIPTION_CN_ORDER_UPDATE_IGNORE = "该订单状态是{0},不能重新上传";
	
	public static final String ERR_CODE_TRACKINGID_BLANK = "1014";
	public static final String ERR_DESCRIPTION_TRACKINGID_BLANK = "trackingId-blank";
	public static final String ERR_DESCRIPTION_CN_TRACKINGID_BLANK = "运单号为空";
	
	public static final String CHANNELID_SNEAKERHEAD = "001";
	
	public static final String TRACKING_INFO_01 = "01";//您提交的订单，客户已经审批通过
	public static final String TRACKING_INFO_02 = "02";//您的订单已经进入【洛杉矶仓库】准备出库，预定与美国洛杉矶时间早晨10点左右（中国时间凌晨1点）开始处理
	public static final String TRACKING_INFO_03 = "03";//您的订单已经拣货完成
	public static final String TRACKING_INFO_04 = "04";//您的订单已经打印并且打包装箱完毕                  Print/Package
	public static final String TRACKING_INFO_05 = "05";//您的订单已交由航空货运开始国际段运送        shipped
	public static final String TRACKING_INFO_06 = "06";//您的订单已到达海关，等待机构清关                  Received
	public static final String TRACKING_INFO_07 = "07";//您的订单已交由【s1%】进行国内段运送 运单号：【s2%】       Clearance
	
	public static final String SOURCE_SNEAKERHEAD = "Sneakerhead";
	public static final String SOURCE_PORTAMERICAN = "PortAmerican";
	public static final String SOURCE_JUICYCOUTURE = "JuicyCouture";
	public static final String SOURCE_SPALDING = "Spalding";
	public static final String SOURCE_BHFO = "BHFO";
	
	// 更新类型
	public static final String UPDATE_TYPE_SHIP = "1";
	public static final String UPDATE_TYPE_LOCK = "2";
	public static final String UPDATE_TYPE_STATUS = "3";
	public static final String UPDATE_TYPE_REFORDERNUM = "4";
	
	// 斯伯丁仓库ID
	public static final String SPALDING_WAREHOURSE_ID = "0003";
}
