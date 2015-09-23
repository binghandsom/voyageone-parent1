package com.voyageone.batch;

import java.util.HashMap;

public interface SynshipConstants {

    /**
     * 数据有效性
     */
    final class ACTIVE {
        //可用
        public final static String  USABLE = "1";
        //不可用
        public final static String  Disabled = "0";
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
        public static final String SIGN_NAME  = "【%s】";
        // 短信账户余额情报
        public static final Double ACCOUNT_BALANCE = 800d;
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

}
