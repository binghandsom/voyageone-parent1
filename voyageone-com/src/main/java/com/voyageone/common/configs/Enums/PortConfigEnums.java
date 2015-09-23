package com.voyageone.common.configs.Enums;

/**
 * Created by Jonas on 4/14/2015.
 */
public class PortConfigEnums {
    /**
     * 对应 tm_order_channel_config 表中存在的所有配置名称
     * @author Jonas
     */
    public enum Name {

        /**
         * 扫描类型，1为物品级别，2为订单级别
         */
        scan_type,
        /**
         * 扫描项目
         */
        scan_project,
        /**
         * 状态重置
         */
        status_reset,
        /**
         * 允许发货的港口
         */
        allow_port,
        /**
         * 港口的默认快递公司
         */
        default_express,
        /**
         * 港口的可选择快递公司
         */
        select_express,
    }

    /**
     * 对应 所有港口
     * @author Jack
     */
    public enum Port {
        /**
         * 发往上海
         */
        SH("10"),
        /**
         /**
         * 发往广州
         */
        GZ("13"),
        /**
         * 中国发货
         */
        CN("14"),
        /**
         * 发往洛杉矶
         */
        LA("15");

        private String id;

        Port(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }


    /**
     * 扫描类型
     * @author Jack
     */
    public enum Scan {
        /**
         * 物品
         */
        RES("1"),
        /**
         * 订单
         */
        ORDER("2");

        private String type;

        Scan(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
