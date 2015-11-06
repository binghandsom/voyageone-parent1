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

    protected final static String table_feed = "voyageone_cms.cms_zz_worktable_bcbg_superfeed";

    protected final static String table_feed_full = "voyageone_cms.cms_zz_worktable_bcbg_superfeed_full";

    protected final static String table_style_full = "voyageone_cms.cms_zz_worktable_bcbg_styles_full";

    protected final static String on_product = "styleID = CONCAT(SATNR,'-',COLOR)";

    protected final static String grouping_product = "CONCAT(MATKL,'-',SATNR,COLOR),MATKL,CONCAT(MATKL,'-',SATNR),CONCAT(SATNR,'-',COLOR),COLOR_ATWTB,WHERL,productDesc,A304_KBETR,A073_KBETR,A304_KBETR,A073_KBETR,A073_KBETR";

    protected final static String grouping_model = "CONCAT(MATKL,'-',SATNR),MATKL,WLADG,BRAND_ID,SATNR,ATBEZ,SIZE1_ATINN";

    protected static Pattern special_symbol;

    protected static ChannelConfigEnums.Channel channel;

    protected static BigDecimal fixed_exchange_rate;

    protected static BigDecimal apparels_duty;

    protected static BigDecimal other_duty;

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
        special_symbol = Pattern.compile(Feed.getVal1(channel, FeedEnums.Name.url_special_symbol));

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
