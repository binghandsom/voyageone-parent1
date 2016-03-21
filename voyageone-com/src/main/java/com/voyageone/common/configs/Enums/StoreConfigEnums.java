package com.voyageone.common.configs.Enums;

/**
 * Created by Jack on 6/4/2017.
 */

public class StoreConfigEnums {

    /**
     * 对应 ct_store_config 表中存在的所有配置名称
     *
     * @author Jack
     */
    public enum Name {

        /**
         * 客户库存管理
         */
        client_inventory_manager,

        /**
         * 客户库存保留量
         */
        client_inventory_hold,

        /**
         * 客户库存同步
         */
        client_inventory_syn_type,

        /**
         * 公司ID
         */
        site,

        /**
         * 库存位置编码
         */
        storage_location,

        /**
         * 库存扫描集计
         */
        transfer_display
    }

    /**
     * 库存管理
     */
    public enum Manager {

        /**
         * 不管理
         */
        NO("0"),
        /**
         * 管理
         */
        YES("1");

        private String id;

        Manager(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * 销售管理
     */
    public enum Sale {

        /**
         * 不管理
         */
        NO("0"),
        /**
         * 管理
         */
        YES("1");

        private String id;

        Sale(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * 库存同步类型
     */
    public enum SynType {

        /**
         * 刷新所有商品的库存
         */
        FULL("0"),
        /**
         * 只更新库存变更（增量方式）
         */
        INCREMENT("1"),
        /**
         * 只更新库存变更（刷新方式）
         */
        REFLUSH("2");

        private String id;

        SynType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * 仓库类型
     */
    public enum Type {
        /**
         * 自营
         */
        OWN("0"),
        /**
         * 第三方
         */
        THIRD("1");

        private String id;

        Type(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * 仓库场所
     */
    public enum Location {
        /**
         * 国外
         */
        CB("0"),
        /**
         * 国内
         */
        CN("1");

        private String id;

        Location(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * 仓库类型
     */
    public enum Kind {
        /**
         * 真实
         */
        REAL("0"),
        /**
         * 虚拟
         */
        VIRTUAL("1");

        private String id;

        Kind(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * 捡货单类型
     */
    public enum LabelType {
        /**
         * 详细拣货单
         */
        DETAIL("0"),
        /**
         * 简单拣货单大
         */
        SIMPLE_B("1"),
        /**
         * 简单拣货单小
         */
        SIMPLE_S("2");

        private String id;

        LabelType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
