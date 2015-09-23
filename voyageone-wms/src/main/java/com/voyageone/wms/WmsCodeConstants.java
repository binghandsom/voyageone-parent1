package com.voyageone.wms;


public interface WmsCodeConstants {

    /**
     * Reservation状态
     */
    final class Reservation_Status {
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
     * Transfer状态
     */
    final class TransferStatus {
        public final static String Open = "0";
        public final static String Close = "1";
    }

    /**
     * 物流信息
     */
    final class Tracking_Info {
        // 您的订单已经拣货完成
        public final static String Reserved = "030";
    }

    /**
     * Transfer 类型
     */
    final class TransferType {
        public final static String IN = "1";
        public final static String OUT = "2";
        public final static String PO = "3";
        public final static String RE = "4";
    }

}
