package com.voyageone.batch.cms.service.feed;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 鉴于特殊情况,使用统一的类进行静态变量的管理和共用.但 BCBG 的任务启动前必须初始化
 * Created by Jonas on 11/4/15.
 */
class BcbgWsdlConstants {

    public static Pattern special_symbol;

    public static ChannelConfigEnums.Channel channel;

    public static String table;

    public static String imageTable;

    public static String imageJoin;

    public static String productTable;

    public static String productJoin;

    public static String modelTable;

    public static String modelJoin;

    public static BigDecimal fixed_exchange_rate;

    public static BigDecimal apparels_duty;

    public static BigDecimal other_duty;

    /**
     * 初始化所有静态变量
     */
    protected static void init() {
        if (channel != null) {
            // 已初始化,就不再进行了
            return;
        }

        channel = ChannelConfigEnums.Channel.BCBG;
        // 主表
        table = Feed.getVal1(channel, FeedEnums.Name.table_id);
        // 图片表
        imageTable = Feed.getVal1(channel, FeedEnums.Name.image_table_id);
        // 特殊字符 (正则)
        special_symbol = Pattern.compile(Feed.getVal1(channel, FeedEnums.Name.url_special_symbol));
        // 商品表
        productTable = Feed.getVal1(channel, FeedEnums.Name.product_table_id);
        // 图片表的 Join 部分
        imageJoin = Feed.getVal1(channel, FeedEnums.Name.image_table_join);
        // 商品表的 Join 部分
        productJoin = Feed.getVal1(channel, FeedEnums.Name.product_table_join);
        // Model 表
        modelTable = Feed.getVal1(channel, FeedEnums.Name.model_table_id);
        // Model 的 Join 部分
        modelJoin = Feed.getVal1(channel, FeedEnums.Name.model_table_join);

        fixed_exchange_rate = getBigDecimalFeedVal1(FeedEnums.Name.fixed_exchange_rate);
        apparels_duty = getBigDecimalFeedVal1(FeedEnums.Name.apparels_duty);
        other_duty = getBigDecimalFeedVal1(FeedEnums.Name.other_duty);

        BigDecimal one = new BigDecimal(1);
        apparels_duty = one.subtract(apparels_duty);
        other_duty = one.subtract(other_duty);
    }

    private static BigDecimal getBigDecimalFeedVal1(FeedEnums.Name name) {
        String val1 = Feed.getVal1(channel, name);
        return new BigDecimal(val1);
    }
}
