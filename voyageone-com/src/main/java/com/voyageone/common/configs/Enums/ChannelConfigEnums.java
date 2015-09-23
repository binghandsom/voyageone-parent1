package com.voyageone.common.configs.Enums;

import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;

/**
 * Created by Jonas on 4/14/2015.
 */
public class ChannelConfigEnums {
    /**
     * 对应 tm_order_channel_config 表中存在的所有配置名称
     *
     * @author Jonas
     */
    public enum Name {
        /**
         * 电商类型，1为跨境，2为国内
         */
        sale_type,
        /**
         * 指定渠道的仓库
         */
        warehouse,
        /**
         * 发货状态，根据状态决定是否能够发货
         */
        ship_status,
        /**
         * 发货流程，根据港口取得状态变化
         */
        ship_process,
        /**
         * 指定渠道的发货仓库
         */
        ship_warehouse,
        /**
         * 可检索仓库
         */
        store,
        /**
         * 是否打印美国面单，0：不打印，1：打印
         */
        print_us_label,
        /**
         * 初期显示状态
         */
        display_status,
        /**
         * 渠道所在地
         */
        location,
        /**
         * 是否需要大单检查
         */
        large_orders_check,
        /**
         * 建立小票时的检索状态
         */
        receipt_status,
        /**
         * 建立小票时的使用的金额及单位
         */
        receipt_amount,
        /**
         * 超卖检查
         */
        over_sold_check,
        /**
         * 强制直邮广州hs_code_pu
         */
        hs_code_pu_gz,
        /**
         * 发送短信时、直邮时的件数检查
         */
        product_num,
        /**
         * 身份证短信最大发送次数
         */
        sms_idcard_max,
        /**
         * 身份证短信发送间隔（小时单位）
         */
        sms_idcard_interval,
        /**
         * 发货渠道 收件人城市特殊判断
         */
        ship_state_channel,

        // 2015年4月21日 19:14:05 追加 by Jonas

        /**
         * 是否允许自动approved
         */
        auto_approved,

        /**
         * 最大允许Approve件数
         */
        approved_max_amount,

        /**
         * 最大允许Approve金额（人民币）
         */
        approved_max_grand_total,

        /**
         * 最大允许Approve件数（手动）
         */
        manual_approved_max_amount,

        /**
         * 最大允许Approve金额（人民币）（手动）
         */
        manual_approved_max_grand_total,

        /**
         * 追加订单明细许可
         */
        add_order_detail_permit,

        /**
         * 取消订单明细许可
         */
        cancel_order_detail_permit,

        /**
         * 删除订单明细许可
         */
        delete_order_detail_permit,

        /**
         * return订单明细许可
         */
        return_order_detail_permit,

        /**
         * 追加订单许可
         */
        add_order_permit,

        /**
         * 取消订单许可
         */
        cancel_order_permit,

        /**
         * 锁定订单许可
         */
        lock_order_permit,

        /**
         * 订单明细追加许可状态
         */
        add_order_detail_permit_status,

        /**
         * 订单明细取消许可状态
         */
        cancel_order_detail_permit_status,

        /**
         * 订单取消，第三方状态检查
         */
        cancel_order_chk_partner_status,

        /**
         * 订单明细return许可状态
         */
        return_order_detail_permit_status,

        /**
         * 订单明细return许可状态(根据Port 与 Channel的配置互斥)
         */
        return_order_detail_permit_status_port,

        /**
         * surcharge 调整标志
         */
        need_surcharge_adjustment,

        /**
         * 发货方式
         */
        shipping_method,

        /**
         * 默认发货渠道
         */
        default_ship_channel,

        /**
         *
         * need by jiming
         */
        change_ship_address_permit,

        /**
         *
         * need by jiming(根据Channel)
         */
        change_ship_address_permit_status,

        /**
         *
         * need by jiming(根据Port 与 Channel的配置互斥)
         */
        change_ship_address_permit_status_port,

        /**
         * 捡货类型
         * need by Jack
         */
        pickup_type,

        /**
         * 捡货状态
         * need by Jack
         */
        pickup_status,

        /**
         * 重打捡货单类型
         * need by Jack
         */
        relabel_type,

        /**
         * 重打捡货单状态
         * need by Jack
         */
        relabel_status,

        /**
         * 卖家收货地址编号
         * need by Jack
         */
        seller_address_id,

        /**
         * 买家留言订单是否不允许自动approved
         */
        approved_except_buyer_message,

        /**
         * 是否允许插入扩展订单表
         */
        ext_order_insert,

        /**
         * 渠道对应时区
         */
        channel_time_zone,

        /**
         * 没有自动approved订单是否需要发邮件
         */
        not_approved_mail,

        /**
         * ChannelAdvisor 订单是否取消
         */
        need_cancel_CA,

        /**
         * 报关时的金额单位
         */
        declare_unit,
        /**
         * 报关时的金额下限（当小于Val1时，用Val2报关；0时不需要下限控制）
         */
        declare_lower_limit,
        /**
         * 报关时的金额上限（当大于Val1时，用Val2报关）
         */
        declare_upper_limit,
        /**
         * 报关时的件数检查（0时不需要件数检查）
         */
        declare_num_check,
        /**
         * 最大允许sku种类数
         */
        approved_max_sku_kind_number,
        /**
         * 最大允许sku种类数（手动）
         */
        manual_approved_max_sku_kind_number

    }

    /**
     * 对应 tm_order_channel 表中存在的所有渠道ID
     *
     * @author Jack
     */
    public enum Channel {

        /**
         * Sneakerhead
         */
        SN("001"),
        /**
         * PortAmerican
         */
        PA("002"),
        /**
         * Essuntial
         */
        GL("003"),
        /**
         * Juicy Couture
         */
        JC("004"),
        /**
         * Spalding
         */
        SP("005"),
        /**
         * BHFO
         */
        BHFO("006"),

        /**
         * Champion
         */
        CHAMPION("007"),

        /**
         * Real Madrid
         */
        REAL_MADRID("008"),

        /**
         * Jewelry
         */
        JEWELRY("010");


        private String id;

        Channel(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public OrderChannelBean getBean() {
            return ChannelConfigs.getChannel(getId());
        }

        public String getFullName() {
            return getBean().getFull_name();
        }

        public static Channel valueOfId(String id) {
            if (id == null)
                return null;

            switch (id) {
                case "001": return SN;
                case "002": return PA;
                case "003": return GL;
                case "004": return JC;
                case "005": return SP;
                case "006": return BHFO;
                case "007": return CHAMPION;
                case "008": return REAL_MADRID;
                case "010": return JEWELRY;
                default: return null;
            }
        }
    }

    /**
     * 电商类型
     *
     * @author Jack
     */
    public enum Sale {
        /**
         * 跨境(cross-border)
         */
        CB("1"),
        /**
         * 国内
         */
        CN("2");

        private String type;

        Sale(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 是否打印面单
     *
     * @author Jack
     */
    public enum Print {
        /**
         * 不打印
         */
        NO("0"),
        /**
         * 打印
         */
        YES("1");

        private String is;

        Print(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }

    /**
     * 是否检查
     *
     * @author Jack
     */
    public enum Check {
        /**
         * 不检查
         */
        NO("0"),
        /**
         * 检查
         */
        YES("1");

        private String is;

        Check(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }

    /**
     * 金额单位
     *
     * @author Jack
     */
    public enum Amount {
        /**
         * USD
         */
        USD("USD"),
        /**
         * RMB
         */
        RMB("RMB");

        private String unit;

        Amount(String unit) {
            this.unit = unit;
        }

        public String getUnit() {
            return unit;
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
