package com.voyageone.synship;

import java.util.HashMap;
import java.util.Map;

public class SynshipConstants {

    /**
     * Tracking 需要的操作地址
     */
    public static final class TrackingUrls {
        // 聚美订单物流信息查询
        public static final String GET_TRACKING_JM_INFO = "/tracking/getJMInfo";
        // 官网订单物流信息查询
        public static final String GET_TRACKING_CN_INFO = "/tracking/getCNInfo";
        // 订单物流信息查询
        public static final String GET_TRACKING_INFO = "/tracking/getInfo";
    }

    /**
     * ItemDetail 需要的操作地址
     */
    public static final class ItemDetailUrls {
        public static final String IMPORT = "/wms/itemDetail/import";
    }

    /**
     * Product 需要的操作地址
     */
    public static final class ProductUrls {
        public static final String IMPORT = "/ims/product/import";
    }

    /**
     * 时区
     */
    public static final class TimeZone {
        public static final int CN = 8;
    }

    /**
     * 物流信息
     */
    public static final class TrackingInfo {
        public static final String DISPLAY ="1";
        public static final String SPREAD ="1";
    }

    /**
     * 物流类型
     */
    public static final class TrackingType {
        public static final String WEBID ="1";
        public static final String PHONE ="2";
        public static final String SYNSHIPNO ="3";
        public static final String TRACKINGNO ="4";
        public static final String ORDERNUM ="5";
    }


}
