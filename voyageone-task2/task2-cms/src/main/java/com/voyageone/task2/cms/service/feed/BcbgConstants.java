package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 鉴于特殊情况,使用统一的类进行静态变量的管理和共用.但 BCBG 的任务启动前必须初始化
 * Created by Jonas on 11/4/15.
 */
class BcbgConstants {

    final static String MATNR = "MATNR";
    final static String EAN11 = "EAN11";
    final static String BRAND_ID = "BRAND_ID";
    final static String MATKL = "MATKL";
    final static String MATKL_ATT1 = "MATKL_ATT1";
    final static String ZZCODE1 = "ZZCODE1";
    final static String ZZCODE2 = "ZZCODE2";
    final static String ZZCODE3 = "ZZCODE3";
    final static String MEINS = "MEINS";
    final static String BSTME = "BSTME";
    final static String COLOR = "COLOR";
    final static String COLOR_ATWTB = "COLOR_ATWTB";
    final static String SIZE1 = "SIZE1";
    final static String SIZE1_ATWTB = "SIZE1_ATWTB";
    final static String SIZE1_ATINN = "SIZE1_ATINN";
    final static String ATBEZ = "ATBEZ";
    final static String SAISO = "SAISO";
    final static String SAISJ = "SAISJ";
    final static String SAITY = "SAITY";
    final static String SATNR = "SATNR";
    final static String MAKTX = "MAKTX";
    final static String WLADG = "WLADG";
    final static String WHERL = "WHERL";
    final static String MEAN_EAN11 = "MEAN_EAN11";
    final static String A304_DATAB = "A304_DATAB";
    final static String A304_DATBI = "A304_DATBI";
    final static String A304_KBETR = "A304_KBETR";
    final static String A304_KONWA = "A304_KONWA";
    final static String A073_DATAB = "A073_DATAB";
    final static String A073_DATBI = "A073_DATBI";
    final static String A073_KBETR = "A073_KBETR";
    final static String A073_KONWA = "A073_KONWA";

    static Pattern special_symbol;

    protected static ChannelConfigEnums.Channel channel;

    static BigDecimal fixed_exchange_rate;

    static BigDecimal apparels_duty;

    static BigDecimal other_duty;

    /**
     * 初始化所有静态变量
     */
    protected static void init() {
        if (channel != null) {
            // 已初始化,就不再进行了
            return;
        }

        channel = ChannelConfigEnums.Channel.BCBG;
        // 特殊字符 (正则)
        special_symbol = Pattern.compile(Feeds.getVal1(channel, FeedEnums.Name.url_special_symbol));

        fixed_exchange_rate = getBigDecimalFeedVal1(FeedEnums.Name.fixed_exchange_rate);
        apparels_duty = getBigDecimalFeedVal1(FeedEnums.Name.apparels_duty);
        other_duty = getBigDecimalFeedVal1(FeedEnums.Name.other_duty);

        BigDecimal one = new BigDecimal(1);
        apparels_duty = one.subtract(apparels_duty);
        other_duty = one.subtract(other_duty);
    }

    private static BigDecimal getBigDecimalFeedVal1(FeedEnums.Name name) {
        String val1 = Feeds.getVal1(channel, name);
        return new BigDecimal(val1);
    }
}
