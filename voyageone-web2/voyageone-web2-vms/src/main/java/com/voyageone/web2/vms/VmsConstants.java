package com.voyageone.web2.vms;

public class VmsConstants {

    // 逗号
    public final static char COMMA = ',';
    // utf-8
    public final static String UTF_8 = "utf-8";

    // xlsx后缀
    public final static String XLSX = ".xlsx";
    public final static String PICKING_LIST = "PickingList";

    public final static int DEFAULT_PAGE_SIZE = 10;

    public final static String ORDER_TIME = "order_time";
    public final static String CONSOLIDATION_ORDER_TIME = "consolidation_order_time";

    /**
     * Feed文件导入状态
     */
    public final static class FeedFileStatus {
        public final static String WAITING_IMPORT = "1";
        public final static String IMPORTING = "2";
        public final static String IMPORT_COMPLETED = "3";
        public final static String IMPORT_ERROR = "4";
        public final static String IMPORT_SYSTEM_ERROR = "5";
    }

    /**
     * Feed文件上传类型
     */
    public final static class FeedFileUploadType {
        public final static String ONLINE = "1";
        public final static String FTP = "2";
    }

    /**
     * 财务报表状态
     */
    public final static class FinancialReportStatus {
        public final static String UNCONFIRMED = "0";
        public final static String CONFIRMED = "1";
    }

    /**
     * 数据统计名称
     */
    public final static class DataAmount {
        public final static String NEW_ORDER_COUNT = "NEW_ORDER_COUNT";
        public final static String NEW_SKU_COUNT = "NEW_SKU_COUNT";
        public final static String RECEIVE_ERROR_SHIPMENT_COUNT = "RECEIVE_ERROR_SHIPMENT_COUNT";
    }

    /**
     * com_mt_type中对应配置的ID
     */
    public interface TYPE_ID {

        // 快递公司（vms系统用）
        int EXPRESS_COMPANY = 82;

        // 导入Feed文件状态（vms系统用）
        int IMPORT_FEED_FILE_STATUS = 83;

        // 物品状态（vms系统用）
        int PRODUCT_STATUS = 84;

        // 货运状态（vms系统用）
        int SHIPMENT_STATUS = 85;

    }

    public interface STATUS_VALUE {

        // 物品状态（vms系统用）
        interface PRODUCT_STATUS {
            String OPEN = "1";
            String PACKAGE = "2";
            String SHIPPED = "3";
            String RECEIVED = "5";
            String RECEIVE_ERROR = "6";
            String CANCEL = "7";
        }

        // shipment状态
        interface SHIPMENT_STATUS {
            String OPEN = "1";
            String SHIPPED = "3";
            String ARRIVED = "4";
            String RECEIVED = "5";
            String RECEIVE_ERROR = "6";
        }

        // 操作级别
        interface VENDOR_OPERATE_TYPE {

            String SKU = "SKU";
            String ORDER = "ORDER";
        }

        // 是否显示中国售价
        interface SALE_PRICE_SHOW {
            String SHOW = "1";
            String HIDE = "0";
        }
    }

    public interface PICKING_LIST_ORDER_TYPE {
        String CLIENT_SKU = "client_sku";
        String ORDER = "order_id";
    }

    /**
     * ChannelConfig
     */
    public interface ChannelConfig {

        // 通常ConfigCode
        String COMMON_CONFIG_CODE = "0";

        // 是订单级别的操作还是SKU级别的操作
        String VENDOR_OPERATE_TYPE = "VENDOR_OPERATE_TYPE";
        // 是否显示RetailPrice
        String SALE_PRICE_SHOW = "SALE_PRICE_SHOW";
        // CSV分隔符
        String FEED_CSV_SPLIT_SYMBOL = "FEED_CSV_SPLIT_SYMBOL";
        // CSV文件编码
        String FEED_CSV_ENCODE = "FEED_CSV_ENCODE";

    }

}
