package com.voyageone.web2.vms;

public class VmsConstants {

    // 逗号
    public final static char COMMA = ',';
    // utf-8
    public final static String UTF_8 = "utf-8";


    /**
     * Feed文件导入状态
     */
    public final static class FeedFileStatus {
        public final static String WAITING_IMPORT = "1";
        public final static String IMPORT_COMPLETED = "2";
        public final static String IMPORT_WITH_ERROR = "3";
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

    /**
     * ChannelConfig
     */
    public interface ChannelConfig {

        // 通常ConfigCode
        String COMMON_CONFIG_CODE = "0";

        // CSV分隔符
        String FEED_CSV_SPLIT_SYMBOL = "FEED_CSV_SPLIT_SYMBOL";
        // CSV文件编码
        String FEED_CSV_ENCODE = "FEED_CSV_ENCODE";

    }
}
