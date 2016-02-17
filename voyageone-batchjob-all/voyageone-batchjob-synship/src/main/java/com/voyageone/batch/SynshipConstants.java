package com.voyageone.batch;

import java.util.HashMap;

public interface SynshipConstants {

    /**
     * 数据有效性
     */
    final class ACTIVE {
        //可用
        public final static String USABLE = "1";
        //不可用
        public final static String Disabled = "0";
    }

    /**
     * 库存分配时标志位设置（WmsSetAllotInventoryJob）
     */
    final class SEND_TRACKING_FLG {
        // 6:国内段推送挖完毕（仅对京东国际）
        public static final String JD_INTERNAL_SENDED = "6";
        // 国内段待推送（仅对京东国际）
        public static final String JD_INTERNAL_WAIT = "5";
        // 推送忽略
        public static final String IGNORE = "4";
        // 订单未找到
        public static final String NOT_FOUND = "3";
        // 该物流公司揽收或派送范围不支持
        public static final String B79 = "2";
        // 1:已推送
        public static final String SENDED = "1";
        //  0:未推送
        public static final String UNSEND = "0";
    }

    /**
     * 签名检查
     */
    final class SMS_CHECK {
        // 基础内容
        public static final String SIGN_NAME = "【%s】";
        // 短信账户余额情报(物流)
        public static final Double ACCOUNT_BALANCE_LOGISTICS = 800d;
        // 短信账户余额情报(营销)
        public static final Double ACCOUNT_BALANCE_MARKETING = 200d;
        // 短信限制次数
        public static final Integer SMS_SENT_lIMIT = 1;
    }

    /**
     * 短信发送状态
     */
    final class SMS_STATUS {
        // 未发送
        public static final String NOT_SENT = "0";
        // 发送成功
        public static final String SENT_SUCCESS = "1";
        // 发送成功
        public static final String CHECK_ERROR = "2";
        // 发送失败
        public static final String SENT_FAILED = "3";
        // 忽略
        public static final String SENT_IGNORE = "4";
    }

    /**
     * 短信发送类型
     */
    final class SMS_SNET_TYPE {
        // 发送类型
        public static final String CLIENT = "1";
        public static final String WEB = "2";
    }

    /**
     * 身份证审核状态
     */
    interface IdCardStatus {
        /**
         * 未审核
         */
        String UNAUDITED = "0";
        /**
         * 批准
         */
        String APPROVED = "1";
        /**
         * 拒绝
         */
        String REJECTED = "2";
        /**
         * 删除
         */
        String DELETE = "3";
        /**
         * 等待后台审核
         */
        String WAITING_AUTO = "4";
    }

    /**
     * 对应表 tt_idcard_history 的 reason 字段
     * <ul>
     * <li>01：IdCard中已存在</li>
     * <li>02：跨境易中已存在</li>
     * <li>03：跨境易中验证通过</li>
     * <li>04：身份证格式错误</li>
     * <li>05：跨境易中验证不通过</li>
     * <li>06：调用出现网络错误，不同于 10，06只特指 axis 的错误</li>
     * <li>07：历史记录存在失败记录</li>
     * <li>08：与原单信息不同</li>
     * <li>09：历史记录存在成功记录</li>
     * <li>10：调用出现错误</li>
     * <li>11：验证通过但没有图片</li>
     * <li>12：IDCARD中已存在同一手机号码、姓名的审核通过记录</li>
     * </ul>
     *
     * @author jonas gao
     */
    interface Reason {
        String ID_CARD_EXISTS = "01";
        String EMS_EXISTS = "02";
        String EMS_PASS = "03";
        String ID_CARD_FORMAT_ERROR = "04";
        String EMS_FAIL = "05";
        String CALL_EMS_FAIL = "06";
        String HISTORY_EXISTS_FAIL = "07";
        String DIFF_FROM_ORDER = "08";
        String HISTORY_EXISTS = "09";
        String CALL_ERROR = "10";
        String IMAGE_NOT_EXISTS = "11";
        String PHONE_NAME_EXISTS = "12";
    }

    /**
     * 对应表 tt_idcard_history 的 audit_result 字段
     * 0：审核不通过, 1：审核通过
     * <p>同时也可以用于 isSuccess 字段</p>
     *
     * @author jonas gao
     */
    interface AuditResult {
        String PASS = "1";
        String FAIL = "0";
        String Force = "2";
    }

    /**
     * 短信发送返回错误信息
     */
    final class SMS_ERR_INFO {
        //错误信息
        public static final HashMap<Integer, String> CONTENTS = new HashMap<Integer, String>() {
            {
                put(-1, "系统异常");
                put(-2, "客户端异常");
                put(-3, "手机号码不符合要求");
                put(-101, "命令不被支持");
                put(-102, "RegistryTransInfo删除信息失败");
                put(-103, "RegistryInfo更新信息失败");
                put(-104, "请求超过限制");
                put(-110, "号码注册激活失败");
                put(-111, "企业注册失败");
                put(-113, "充值失败");
                put(-117, "发送短信失败");
                put(-118, "接收MO失败");
                put(-119, "接收Report失败");
                put(-120, "修改密码失败");
                put(-122, "号码注销激活失败");
                put(-123, "查询单价失败");
                put(-124, "查询余额失败");
                put(-125, "设置MO转发失败");
                put(-126, "路由信息失败");
                put(-127, "计费失败0余额");
                put(-128, "计费失败余额不足");
                put(-190, "数据操作失败");
                put(-1100, "序列号错误,序列号不存在内存中,或尝试攻击的用户");
                put(-1102, "序列号密码错误");
                put(-1103, "序列号Key错误");
                put(-1104, "路由失败，请联系系统管理员");
                put(-1105, "注册号状态异常, 未用 1");
                put(-1107, "注册号状态异常, 停用 3");
                put(-1108, "注册号状态异常, 停止 5");
                put(-1131, "充值卡无效");
                put(-1132, "充值密码无效");
                put(-1133, "充值卡绑定异常");
                put(-1134, "充值状态无效");
                put(-1135, "充值金额无效");
                put(-1901, "数据库插入操作失败");
                put(-1902, "数据库更新操作失败");
                put(-1903, "数据库删除操作失败");
                put(-9000, "数据格式错误,数据超出数据库允许范围");
                put(-9001, "序列号格式错误");
                put(-9002, "密码格式错误");
                put(-9003, "客户端Key格式错误");
                put(-9004, "设置转发格式错误");
                put(-9005, "公司地址格式错误");
                put(-9006, "企业中文名格式错误");
                put(-9007, "企业中文名简称格式错误");
                put(-9008, "邮件地址格式错误");
                put(-9009, "企业英文名格式错误");
                put(-9010, "企业英文名简称格式错误");
                put(-9011, "传真格式错误");
                put(-9012, "联系人格式错误");
                put(-9013, "联系电话");
                put(-9014, "邮编格式错误");
                put(-9015, "新密码格式错误");
                put(-9016, "发送短信包大小超出范围");
                put(-9017, "发送短信内容格式错误");
                put(-9018, "发送短信扩展号格式错误");
                put(-9019, "发送短信优先级格式错误");
                put(-9020, "发送短信手机号格式错误");
                put(-9021, "发送短信定时时间格式错误");
                put(-9022, "发送短信唯一序列值错误");
                put(-9023, "充值卡号格式错误");
                put(-9024, "充值密码格式错误");
                put(-9025, "客户端请求sdk5超时");
            }
        };
    }

    String SHORTURL_PRE_SALE = "9";

    String SMS_WORDS = "WORDS";

    String SMS_COST = "COST";

    String SMS_INFO = "SMS_INFO";

    String SMS_CONTENT_VALID_NO_MATCH = "02"; //身份证上传短信（不匹配时的重发）

    String SMS_CONTENT_VALID_NO_IMAGE = "03"; //身份证上传短信（无图片、查无此人时的重发）

    String SMS_SNET_TYPE_CLIENT = "1";

    String SMS_STATUS_NOT_SENT = "0";

    String SMS_TYPE_CLOUD_CLIENT = "cloud_client";

    /**
     * 获取品牌方物流信息时超卖警告邮件（SynshipGetClientShipping）
     */
    final class EmailSynshipGetClientShipping {

        // 表格式
        public final static String TABLE = "<div><span>%s</span>"
                + "<table><tr>"
                + "<th></th><th>Shop</th><th>OrderNum</th><th>WebID</th><th>SynShipNo</th><th>ClientOrderId</th>"
                + "<th>SellerOrderId</th><th>Sku</th><th>ResId</th><th>ShipName</th><th>ShipPhone</th><th>OrderDateTime</th>"
                + "</tr>%s</table></div>";
        // 行格式
        public final static String ROW = "<tr>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "</tr>";
        // 邮件名
        public final static String SUBJECT = "%s品牌方取消订单一览";
        // 概要说明
        public final static String HEAD = "<font color='red'>以下订单已经被品牌方取消，请确认</font>";
        // 件数
        public final static String COUNT = "取消总数：%s";

    }
}
