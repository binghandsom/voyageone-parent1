package com.voyageone.web2.openapi.bi.constants;

/**
 * ExcelHeaderEnum Define
 */
public class ExcelHeaderEnum {
    // upload file path
    public static final String BI_TB_PRODUCT_IMPORT_PATH ="openapi.bi.tb.product.import.path";

    /**
     * Device
     */
    public enum Device {
        pc("pc端"),
        mobile("无线端"),
        all("所有终端");

        // 成员变量
        private String cnName;

        // 构造方法
        Device(String cnName) {
            this.cnName = cnName;
        }

        public String getCnName() {
            return cnName;
        }

        public static Device valueOfCnName(String cnName) {
            for (Device def : Device.values()) {
                if (def.getCnName().equals(cnName)) {
                    return def;
                }
            }
            return null;
        }
    }

    /**
     * 对应 TB 表中存在Column ID
     */
    public enum TBProductColumnDef {
        num_iid("商品id", "num_iid", "num_iid", "num_iid"),
        pv("浏览量", "pv", "pv_pc", "pv_mobile"),
        uv("访客数", "uv", "uv_pc", "uv_mobile"),
        visit_time_avg("平均停留时长", "visit_time_avg", "visit_time_avg_pc", "visit_time_avg_mobile"),
        bounce_rate("详情页跳出率", "bounce_rate", "bounce_rate_pc", "bounce_rate_mobile"),
        order_cvr("下单转化率", "order_cvr", null, null),
        order_amt("下单金额", "order_amt", null, null),
        order_product_number("下单商品件数", "order_product_number", null, null),
        order_buyer_number("下单买家数", "order_buyer_number", null, null),
        pay_cvr("支付转化率", "pay_cvr", "pay_cvr_pc", "pay_cvr_mobile"),
        pay_amt("支付金额", "pay_amt", "pay_amt_pc", "pay_amt_mobile"),
        pay_product_qty("支付商品件数", "pay_product_qty", "pay_product_qty_pc", "pay_product_qty_mobile"),
        pay_buyer_number("支付买家数", "pay_buyer_number", "pay_buyer_number_pc", "pay_buyer_number_mobile"),
        product_cart_increment("加购件数", "product_cart_increment", "product_cart_increment_pc", "product_cart_increment_mobile"),
        product_collector_number("收藏人数", "product_collector_number", null, null);

        // 成员变量
        private String cnName;
        private String column;
        private String columnPc;
        private String columnMobile;

        // 构造方法
        TBProductColumnDef(String cnName, String column, String columnPc, String columnMobile) {
            this.cnName = cnName;
            this.column = column;
            this.columnPc = columnPc;
            this.columnMobile = columnMobile;
        }

        public String getCnName() {
            return cnName;
        }

        public String getColumn() {
            return column;
        }

        public String getColumnPc() {
            return columnPc;
        }

        public String getColumnMobile() {
            return columnMobile;
        }

        public static TBProductColumnDef valueOfCnName(String cnName) {
            for (TBProductColumnDef def : TBProductColumnDef.values()) {
                if (def.getCnName().equals(cnName)) {
                    return def;
                }
            }
            return null;
        }
    }

    /**
     * 对应 JD 表中存在Column ID
     */
    public enum JDProductColumnDef {
        num_iid("商品ID", "num_iid", "num_iid", "num_iid", false),
        pv("浏览量", "pv", "pv_pc", "pv_mobile", false),
        uv("访客数", "uv", "uv_pc", "uv_mobile", false),
        visit_time_avg("商品页停留时间", null, "visit_time_avg_pc", null, false),
        bounce_rate("商品页跳失率", null, "bounce_rate_pc", null, true),
        order_cvr("商品页成交转化率", "order_cvr", null, null, false),
        //        order_amt("下单金额", "order_amt", null, null, false),
//        order_product_number("下单商品件数", "order_product_number", null, null, false),
//        order_buyer_number("下单客户数", "order_buyer_number", null, null, false),
        pay_cvr("商品页成交转化率", "pay_cvr", "pay_cvr_pc", "pay_cvr_mobile", true),
        pay_amt("下单金额", "pay_amt", "pay_amt_pc", "pay_amt_mobile", false),
        pay_product_qty("下单商品件数", "pay_product_qty", "pay_product_qty_pc", "pay_product_qty_mobile", false),
        pay_buyer_number("下单客户数", "pay_buyer_number", "pay_buyer_number_pc", "pay_buyer_number_mobile", false),
        product_cart_increment("加购件数", null, null, null, false),
        product_collector_number("商品关注量", "product_collector_number", null, null, false);

        // 成员变量
        private String cnName;
        private String column;
        private String columnPc;
        private String columnMobile;
        private boolean isRate;

        // 构造方法
        JDProductColumnDef(String cnName, String column, String columnPc, String columnMobile, boolean isRate) {
            this.cnName = cnName;
            this.column = column;
            this.columnPc = columnPc;
            this.columnMobile = columnMobile;
            this.isRate = isRate;
        }

        public String getCnName() {
            return cnName;
        }

        public String getColumn() {
            return column;
        }

        public String getColumnPc() {
            return columnPc;
        }

        public String getColumnMobile() {
            return columnMobile;
        }

        public boolean isRate() {
            return isRate;
        }

        public static JDProductColumnDef valueOfCnName(String cnName) {
            for (JDProductColumnDef def : JDProductColumnDef.values()) {
                if (def.getCnName().equals(cnName)) {
                    return def;
                }
            }
            return null;
        }
    }

}
