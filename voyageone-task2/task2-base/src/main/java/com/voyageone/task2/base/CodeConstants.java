package com.voyageone.task2.base;


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
        public static final String ITSYNSHIP = "ITSYNSHIP";
        public static final String NEED_SOLVE ="NEED_SOLVE";
        public static final String SP_THIRD_REPORT = "SP_THIRD_REPORT";
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
        public final static String Shipped = "14";
        public final static String Arrived = "15";
        public final static String Clearance = "16";
        public final static String ShippedCN = "17";
        public final static String ShippedTP = "24";
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
        public static final String INFO_010 = "010";//订单已揽收
        public static final String INFO_020 = "020";//订单已经进入【%s仓库】准备出库，预定于%s时间早晨10点左右开始处理
        public static final String INFO_030 = "030";//订单开始发往集散中心
        public static final String INFO_040 = "040";//订单已抵达集散中心
        public static final String INFO_050 = "050";//订单开始国际运输
        public static final String INFO_051 = "051";//订单已交由【%s】开始国际段运送 运单号：【%s】
        public static final String INFO_052 = "052";//订单已离开美国%s前往洛杉矶
        public static final String INFO_053 = "053";//订单开始配送
        public static final String INFO_060 = "060";//订单已到达海关，等待机构清关
        public static final String INFO_061 = "061";//订单已抵达香港口岸，将由香港邮政收寄
        public static final String INFO_062 = "062";//您的订单将由香港邮政收寄
        public static final String INFO_070 = "070";//订单已交由【%s】进行国内段运送 运单号：【%s】
        public static final String INFO_071 = "071";//订单已通关完成
        public static final String INFO_072 = "072";//订单已交由【%s】开始配送，运单号：【%s】
        public static final String INFO_077 = "077";//订单国际段物流已结束，等待上海转发。
        public static final String INFO_080 = "080";//订单已交由【%s】进行国内段运送 运单号：【%s】
        public static final String INFO_990 = "990";//订单正在被海关查验，查验时间预计1至2天，请耐心等待。
        public static final String INFO_991 = "991";//因为快递承运商无法递送，取消之前的运单，等待变更新的快递承运商
        public static final String INFO_992 = "992";//货物确认丢件，仓库重新安排发货

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
