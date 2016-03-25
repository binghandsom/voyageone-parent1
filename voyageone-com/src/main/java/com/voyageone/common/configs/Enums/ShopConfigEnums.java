package com.voyageone.common.configs.Enums;

/**
 * Created by Jack on 4/14/2017.
 */

public class ShopConfigEnums {
    /**
     * 对应 tm_channel_shop_config 表中存在的所有配置名称
     */
    public enum Name {
        push_cost_price,
        seller_address_id,
        expected_ship_date,
        /**
         * 是否同步运单到平台（0：不同步、1：同步)
         */
        send_tracking,
        /**
         * 同步运单号类型（0：Synship、1：Express)
         */
        tracking_type,
        /**
         * 重试次数
         */
        api_repeat,
        /**
         * 快递公司名
         */
        company,
        /**
         * 物流公司一览
         */
        logistics,

        /**
         * 库存同步标识（现有：0 同步, 1 完成, 4 忽略）
         */
        sync_inventory_flg,
        /**
         * 根据cartId取得文件名字
         */
        filename,
        /**
         * 上新时，使用那种价格作为上新时Sku的价格
         */
        upload_price_choice,
        /**
         * 模拟发货标志位（1：订单进入系统即同步运单至平台；0：实际发货再同步）
         */
        sim_shipping,

        /**
         * 天猫特价宝的活动ID
         */
        promotion_id,

        /**
         * barcode转sku设置名
         */
        barcode_2_sku,

        /**
         * 主渠道
         */
        main_channel_id,
        /**
         * 同步运单号的判断条件（订单状态）
         */
        res_status_shipping,
        /**
         * 同步运单号的判断条件（时间范围）
         */
        time_limit_shipping,
        /**
         * 同步运单号的判断条件（不在同步范围的仓库）
         */
        not_need_store
    }

    /**
     * 是否同步
     */
    public enum Sent {
        /**
         * 不同步
         */
        NO("0"),
        /**
         * 同步
         */
        YES("1");

        private String is;

        Sent(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }

    /**
     * 运单类型
     */
    public enum Type {
        /**
         * 不检查
         */
        Synship("Synship"),
        /**
         * 检查
         */
        Express("Express");

        private String is;

        Type(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }

    /**
     * 是否模拟
     */
    public enum Sim {
        /**
         * 不模拟
         */
        NO("0"),
        /**
         * 模拟
         */
        YES("1");

        private String is;

        Sim(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }

    /**
     * 同步运单号类型 0代表线下，1代表线上
     */
    public enum Line {
        /**
         * 线上
         */
        ONLINE("1"),
        /**
         * 线下
         */
        OFFLINE("0");

        private String is;

        Line(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }
}
