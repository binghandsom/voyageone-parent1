package com.voyageone.common.configs.Enums;

import com.voyageone.common.configs.Channels;
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
         * 图片上传
         */
        ImageUploadService,
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
         * 是否打印sku面单，0：不打印，1：打印
         */
        print_sku_label,
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
         * 公司扣点
         */
        vo_commission,

        /**
         * 阿里扣点
         */
        alipay_fee,

        /**
         * 天猫扣点
         */
        tmall_commission,

        /**
         * 没有自动approved订单是否需要发邮件
         */
        not_approved_mail,

        /**
         * ChannelAdvisor 订单是否取消
         */
        need_cancel_CA,

        shipping_method1,

        /**
         * KitBag 订单是否取消
         */
        need_cancel_KitBag,

        /**
         * 第三方 订单是否取消
         */
        need_cancel_ThirdParty,

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
        manual_approved_max_sku_kind_number,
        /**
         * 非真实姓名的限制
         */
        is_true_name_check,
        /**
         * 订单地址包含菜鸟驿站的限制
         */
        is_cainiaoyizhan_check,
        /**
         * 渠道允许发货数量（0：代表不用检查）
         */
        shipping_num,
        /**
         * 多件物品时，渠道允许发货金额（0：代表不用检查）
         */
        shipping_amount,
        /**
         * scene7上传图片文件夹路径
         */
        scene7_image_folder,
        /**
         * 斯伯丁特殊物品篮板
         */
        special_goods_backboard,
        /**
         * 斯伯丁特殊物品定制球
         */
        special_goods_ball,
        /**
         * 是否允许捡货状态
         */
        pickup_permit,
        /**
         * 是否允许捡货打印拣货单
         */
        pickup_label_print,
        /**
         * 是否允许收货状态
         */
        receive_permit,
        /**
         * 收货状态
         */
        receive_status,
        /**
         * 收货类型
         */
        receive_type,
        /**
         * 收货重打状态
         */
        receive_relabel_status,
        /**
         * 收货重打类型
         */
        receive_relabel_type,
        /**
         * 是否允许收货打印拣货单
         */
        receive_label_print,
        /**
         * 再分配标志判断
         */
        allot_inventory_again,
        /**
         * 订单拆分
         */
        order_split,
        /**
         * SMS用户密码
         */
        sms_password,
        /**
         * SMS用户
         */
        sms_user,
        /**
         * 短信客服号
         */
        sms_add_serial,
        /**
         * 强制发货渠道仓库
         */
        ship_store_channel,
        /**
         * SMS用户密码(营销)
         */
        sms_password_marketing,
        /**
         * SMS用户(营销)
         */
        sms_user_marketing,
        /**
         * 短信客服号(营销)
         */
        sms_add_serial_marketing,
        /**
         * confirmed时间间隔
         */
        confirmed_time_interval,
        /**
         * 分配仓库
         */
        allot_store,
        /**
         * 第三方订单(1：代表订单由商家自行管理)
         */
        third_order,
        /**
         * 产品库存再设定
         */
        searchsku_reset_inventory,
        /**
         * 产品信息检索路径
         */
        searchsku_path

    }

    /**
     * 对应 tm_order_channel 表中存在的所有渠道ID
     *
     * @author Jack
     */
    public enum Channel {

        /**
         * 共通channel属性设置用, 000
         */
        NONE("000"),

        /**
         * Sneakerhead, 001
         */
        SN("001"),

        /**
         * PortAmerican, 002
         */
        PA("002"),

        /**
         * Essuntial, 003
         */
        GL("003"),

        /**
         * Juicy Couture, 004
         */
        JC("004"),

        /**
         * Spalding, 005
         */
        SP("005"),

        /**
         * BHFO, 006
         */
        BHFO("006"),

        /**
         * Champion, 007
         */
        CHAMPION("007"),

        /**
         * Real Madrid, 008
         */
        REAL_MADRID("008"),

        /**
         * SwissWatch, 009
         */
        SWISSWATCH("009"),

        /**
         * Jewelry, 010
         */
        JEWELRY("010"),

        /**
         * BCBG, 012
         */
        BCBG("012"),

        /**
         * SEARS, 013
         */
        SEARS("013"),

        /**
         * WMF
         */
        WMF("014"),

        /**
         * GILT
         */
        GILT("015"),

        /**
         * SHOE_CITY
         */
        SHOE_CITY("016"),

        /**
         * LUCKY_VITAMIN
         */
        LUCKY_VITAMIN("017"),

        /**
         * TARGET
         */
        TARGET("018"),

        /**
         * SummerGuru
         */
        SUMMERGURU("019"),

        /**
         * EdcSkincare
         */
        EDCSKINCARE("020"),

        /**
         * BHFO_MINIMALL
         */
        BHFO_MINIMALL("021"),

        /**
         * DFO
         */
        DFO("022"),

        /**
         * ShoeZoo
         */
        ShoeZoo("023"),

        /**
         * OverStock
         */
        OverStock("024"),

        /**
         * OverStock
         */
        FragranceNet("025"),

        /**
         * LightHouse
         */
        LightHouse("026"),

        /**
         * Yogademocracy
         */
        Yogademocracy("027"),

        /**
         * ShoeMetro
         */
        ShoeMetro("028"),

        /**
         * Modotex
         */
        Modotex("029"),

        /**
         * Wella
         */
        Wella("030"),

        /**
         * Woodland
         */
        Woodland("031"),

        /**
         * Frye
         */
        Frye("032"),

        /**
         * Kitbag
         */
        KitBag("033"),

        /**
         * Coty
         */
        Coty("034"),

        /**
         * LikingBuyer
         */
        LikingBuyer("035"),

        /**
         * LikingBuyer
         */
        Cinxus("036"),

        /**
         * LikingBuyer
         */
        SharonShoe("037"),

        /**
         * FAMbrand
         */
        FAMbrand("038"),

        /**
         * TestChannel088
         */
        TestChannel088("088"),

        /**
         * TestChannel089
         */
        TestChannel089("089"),

        /**
         * TestChannel090
         */
        TestChannel090("090"),

        /**
         * TestChannel091
         */
        TestChannel091("091"),
        /**
         * TestChannel092
         */
        TestChannel092("092"),

        /**
         * TestChannel093
         */
        TestChannel093("093"),

        /**
         * US匠心界
         */
        USJGJ("928"),

        /**
         * US悦境
         */
        USJGY("929"),
        /**
         * TEST
         */
        TEST("996"),

        /**
         * USJGT
         */
        USJGT("998"),

        /**
         * VOYAGEONE
         */
        VOYAGEONE("997");

        private String id;

        Channel(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public OrderChannelBean getBean() {
            return Channels.getChannel(getId());
        }

        public String getFullName() {
            return getBean().getFull_name();
        }

        public static Channel valueOfId(String id) {
            if (id == null)
                return null;

            switch (id) {
                case "000": return NONE;
                case "001": return SN;
                case "002": return PA;
                case "003": return GL;
                case "004": return JC;
                case "005": return SP;
                case "006": return BHFO;
                case "007": return CHAMPION;
                case "008": return REAL_MADRID;
                case "009": return SWISSWATCH;
                case "010": return JEWELRY;
                case "012": return BCBG;
                case "013": return SEARS;
                case "014": return WMF;
                case "015": return GILT;
                case "016": return SHOE_CITY;
                case "017": return LUCKY_VITAMIN;
                case "018": return TARGET;
                case "019": return SUMMERGURU;
                case "020": return EDCSKINCARE;
                case "021": return BHFO_MINIMALL;
                case "022": return DFO;
                case "023": return ShoeZoo;
                case "024": return OverStock;
                case "025": return FragranceNet;
                case "026": return LightHouse;
                case "027": return Yogademocracy;
                case "028": return ShoeMetro;
                case "029": return Modotex;
                case "030": return Wella;
                case "031": return Woodland;
                case "032": return Frye;
                case "033": return KitBag;
                case "034": return Coty;
                case "035": return LikingBuyer;
                case "036": return Cinxus;
                case "037": return SharonShoe;
                case "038": return FAMbrand;
                case "088": return TestChannel088;
                case "089": return TestChannel089;
                case "090": return TestChannel090;
                case "091": return TestChannel091;
                case "092": return TestChannel092;
                case "093": return TestChannel093;
                case "928": return USJGJ;
                case "929": return USJGY;
                case "997": return VOYAGEONE;
                case "996": return TEST;
                case "998": return USJGT;
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
        ORDER("2"),
        /**
         * 订单物品(物品运单号)
         */
        ITEM("3"),
        /**
         * 订单物品(物品Barcode)
         */
        UPC("4");

        private String type;

        Scan(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 是否允许捡货或收货
     *
     * @author Jack
     */
    public enum Reserve {
        /**
         * 不允许
         */
        NO("0"),
        /**
         * 允许
         */
        YES("1");

        private String is;

        Reserve(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }

}
