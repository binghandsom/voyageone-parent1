package com.voyageone.batch.core;


public interface CodeConstants {
	/**
	 * EMAIL_RECEIVER
	 */
	public static final class EmailReceiver {
        // 共通错误邮件
        public static final String VOYAGEONE_ERROR = "VOYAGEONE_ERROR";
        public static final String ITOMS = "ITOMS";
        public static final String ITWMS = "ITWMS";
        public static final String ITIMS = "ITIMS";
	}

    /**
     * TRANSFER_TYPE
     */
    public static final class TransferType {
        // 1:TransferIn
        public static final String IN = "1";
        // 2:TransferOut
        public static final String OUT = "2";
        // 3:PurchaseOrder
        public static final String PURCHASE = "3";
        // 4:退库
        public static final String WITHDRAWAL = "4";
        // 5:刷新（库存由品牌方管理）
        public static final String REFRESH = "5";
    }

    /**
     * TRANSFER_STATUS
     */
    public static final class TransferStatus {
        // 0:Open
        public static final String OPEN = "0";
        // 1:Close
        public static final String ClOSE = "1";
    }

    /**
     * TRANSFER_ORIGIN
     */
    public static final class TransferOrigin {
        // 0:正常Transfer
        public static final String TRANSFER = "0";
        // 1:CloseDay
        public static final String RESERVED = "1";
        // 2:退货
        public static final String RETURNED = "2";
        // 3:盘盈
        public static final String PLUS = "3";
        // 4:盘亏
        public static final String MINUS = "4";
        // 5:退库
        public static final String WITHDRAWAL = "5";
        // 6:刷新（库存由品牌方管理）
        public static final String REFRESH = "6";
    }

    /**
     * Reservation状态
     */
    final class Reservation_Status {
        public final static String ID = "REV_STU";
        public final static String Open = "11";
        public final static String Reserved = "12";
        public final static String Packaged = "13";
        public final static String ShippedUS = "14";
        public final static String Arrived = "15";
        public final static String Clearance = "16";
        public final static String ShippedCN = "17";
        public final static String Missed = "80";
        public final static String Lost = "96";
        public final static String BackOrderConfirmed = "96";
        public final static String Returned = "97";
        public final static String BackOrdered = "98";
        public final static String Cancelled = "99";
    }

    /**
     * Shipment状态
     */
    final class SHIPMENT_STATUS {
        public static final String OPEN = "1";
        public static final String SHIPPED = "2";
        public static final String ARRIVED = "3";
        public static final String CLEARANCE = "4";
    }

    /**
     * Packeage状态
     */
    final class PACKAGE_STATUS {
        public static final String OPEN = "1";
        public static final String CLOSE = "2";
    }

    final class TRACKING {
        public static final String INFO_010 = "010";//您提交的订单，客服已经审批通过
        public static final String INFO_020 = "020";//您的订单已经进入【洛杉矶仓库】准备出库，预定与美国洛杉矶时间早晨10点左右（中国时间凌晨1点）开始处理
        public static final String INFO_030 = "030";//您的订单已经拣货完成
        public static final String INFO_040 = "040";//您的订单已经打印并且打包装箱完毕                  Print/Package
        public static final String INFO_050 = "050";//您的订单已交由航空货运开始国际段运送        直邮 GZ_PACKAGE
        public static final String INFO_051 = "051";//您的订单已交由【%s】开始国际段运送 运单号：【%s】        上海转运 TM_PACKAGE
        public static final String INFO_052 = "052";//您的订单已离开美国中部爱荷华州前往洛杉矶        美国内部转运LA_PACKAGE
        public static final String INFO_060 = "060";//您的订单已到达海关，等待机构清关                  Received
        public static final String INFO_070 = "070";//您的订单已交由【s1%】进行国内段运送 运单号：【s2%】        GZ_PACKAGE
        public static final String INFO_071 = "071";//您的订单已清关，等待国内转运    TM_PACKAGE
        public static final String INFO_080 = "080";//您的订单已交由【s1%】进行国内段运送 运单号：【s2%】        TM_PACKAGE
        public static final String INFO_061 = "061";//您的订单已到达香港，将由香港邮政收寄                  HK_PACKAGE
        public static final String INFO_062 = "062";//您的订单将由香港邮政收寄                  HK_PACKAGE
        public static final String INFO_072 = "072";//您的订单已交由【%s】开始配送，运单号：【%s】                  HK_PACKAGE
    }

    // tt_shipment_info.proccess_type
    final class PROCCESS_TYPE {
        public static final String BOT = "0";
        public static final String MANUAL = "1";
    }

    // 快递100订阅
    final class KD100_POLL {
        public static final String NO = "0";
        // 快递100订阅
        public static final String YES = "1";
        // 快递100延迟订阅
        public static final String DELAY = "2";
    }

	
}
