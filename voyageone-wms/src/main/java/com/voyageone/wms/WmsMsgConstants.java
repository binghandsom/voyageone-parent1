package com.voyageone.wms;

/**
 * Created by Tester on 5/5/2015.
 *
 * @author Jonas
 */
public final class WmsMsgConstants {

    /**
     * 入出库页面MSG定义
     */
    public final static class TransferMsg {
        public static String PACKAGE_NAME_EXISTS = "3000002";
        public static String EXISTS_TRANSFER_NAME = "3000003";
        public static String TRANSFER_ALREADY_CLOSE = "3000004";
        public static String TRANSFER_NOT_EXISTS = "3000005";
        public static String NO_TRANSFER_OUT_MAPPING = "3000006";
        public static String INVALID_TRANSFER_TYPE = "3000007";
        public static String NO_TRANSFER_ITEM_EXISTS = "3000008";
        public static String NO_PACKAGE_EXISTS = "3000009";
        public static String INVALID_SKU = "3000010";
        public static String PACKAGE_ALREADY_CLOSED = "3000011";
        public static String NO_PACKAGE_ITEM_EXISTS = "3000012";
        public static String IN_OUT_NOT_MATCH = "3000013";
        public static String PACKAGE_NOT_CLOSE = "3000014";
        public static final String DOWNLOAD_FAILED = "3000027";
    }

    /**
     * 退货页面使用MSG定义
     */
    public final static class ReturnMsg {
    }

    /**
     * 货架管理页面使用MSG定义
     */
    public final static class ItemLocationMsg {
        public static final String NOT_FOUND_CHANNEL = "3000015";
        /**
         * 1: 货架名称
         */
        public static final String LOCATION_NAME_EXISTS = "3000016";
        /**
         * 1: 货架名称
         */
        public static final String LOCATION_NOT_EMPTY = "3000017";
        /**
         * 1: 商品 Code
         */
        public static final String CANT_MATCH_UNIQUE_ITEM = "3000018";
        /**
         * 1: 商品的 Code
         */
        public static final String NOT_FOUND_CODE_IN_CHANNEL = "3000019";
        /**
         * 1: 商品的 Code，2: 货架名称
         */
        public static final String ITEM_ALREADY_IN_THERE = "3000020";
        /**
         * 1: 货架名称
         */
        public static final String NOT_FOUND_LOCATION = "3000021";
    }

    /**
     * Reservation管理页面使用MSG定义
     */
    public final static class RsvListMsg {
        /**
         * 记录已被其它人编辑过
         */
        public static final String UPDATE_ERROR = "3000025";

    }

    /**
     * 捡货管理页面使用MSG定义
     */
    public final static class PickUpMsg {
        /**
         * 1: 根据扫描内容无法找到相关商品
         */
        public static final String NOT_FOUND_SCANNO = "3000022";
        /**
         * 2: 相关商品中存在超卖
         */
        public static final String OVER_SOLD = "3000023";
        /**
         * 3: 根据发货渠道无法找到相关的折扣信息
         */
        public static final String NOT_FOUND_DISCOUNT_RATE = "3000024";
        /**
         * 3: 可捡货清单下载失败
         */
        public static final String DOWNLOAD_FAILED = "3000026";
        /**
         * 4: 取消物品的提示信息
         */
        public static final String CANCELLED = "3000063";

    }

    /**
     * 新商品（UPC）管理页面使用MSG定义
     */
    public final static class NewItemMsg {

        public static final String PRODUCT_INVALID = "3000028";

        public static final String NOT_FOUND_PRODUCT_TYPE = "3000029";

        public static final String ITEM_DETAIL_INVALID = "3000030";

        /**
         * 1: Product 搜索的参数
         */
        public static final String NOT_FOUND_TARGET_PRODUCT = "3000031";

        /**
         * 1: 搜索的目标 Barcode
         */
        public static final String BARCODE_IN_USE = "3000032";
    }

    /**
     * 库存盘点页面使用MSG定义
     */
    public final static class TakeStockMsg {
        public static final String NONEXISTENT_BARCODE = "3000001";
        public static final String CHANGE_TO_STOCK_FAILED = "3000035";
        public static final String CANNOT_FIXED_OLD_SESSION = "3000057";
        public static final String UPCSKU_CANNOT_BOTH_NULL ="3000058";
    }

    /**
     * 超卖页面使用MSG定义
     */
    public final static class BackOrderListMsg {
        public static final String NONEXISTENT_SKU = "3000033";
        public static final String EXIST_SKU = "3000034";
    }

    /**
     * 收货（Receive）页面使用MSG定义
     */
    public final static class ReceiveListMsg {

    }

    /**
     * 报告管理（Report）页面使用MSG定义
     */
    public final static class ReportMsg {
        public static final String INVDELRPT_DOWNLOAD_FAILED = "3000050";
    }
}
