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
    /**
     * Feed文件导入状态
     */
    public final static class FeedFileStatus {
        public final static String WAITING_IMPORT = "1";
        public final static String IMPORTING = "2";
        public final static String IMPORT_COMPLETED = "3";
        public final static String IMPORT_WITH_ERROR = "4";
        public final static String IMPORT_WITH_SYSTEM_ERROR = "5";
    }

    /**
     * Feed文件上传类型
     */
    public final static class FeedFileUploadType {
        public final static String ONLINE = "1";
        public final static String FTP = "2";
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
            int OPEN = 1;
            int PACKAGE = 2;
            int SHIPPED = 3;
            int RECEIVED = 5;
            int RECEIVE_WITH_ERROR = 6;
            int CANCEL = 7;
        }

        interface SHIPMENT_STATUS {
            int OPEN = 1;
            int SHIPPED = 3;
            int ARRIVED = 4;
            int RECEIVED = 5;
            int RECEIVE_WITH_ERROR = 6;
        }


        interface VENDOR_OPERATE_TYPE {

            String SKU = "SKU";
            String ORDER = "ORDER";
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
        // CSV分隔符
        String FEED_CSV_SPLIT_SYMBOL = "FEED_CSV_SPLIT_SYMBOL";
        // CSV文件编码
        String FEED_CSV_ENCODE = "FEED_CSV_ENCODE";

    }

}
