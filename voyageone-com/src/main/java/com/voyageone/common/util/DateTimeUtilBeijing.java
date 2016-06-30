package com.voyageone.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtilBeijing {

    private static final TimeZone beijingZone = TimeZone.getTimeZone("Asia/Shanghai");

    //获取当前北京日期
    public static Date getCurrentBeiJingDate() {
        return toBeiJingDate(new Date());
    }

    //当前时区时间To北京时间
    public static Date toBeiJingDate(Date localDate) {
        return DateTimeUtil.changeTimeZone(localDate, Calendar.getInstance().getTimeZone(), beijingZone);
    }

    //北京时间To本地时区时间
    public static Date toLocalDate(Date beiJingDate) {
        return DateTimeUtil.changeTimeZone(beiJingDate, beijingZone, Calendar.getInstance().getTimeZone());
    }

    //北京时间To本地时区时间戳
    public static long toLocalTime(Date beiJingDate) {
        return DateTimeUtil.changeTimeZone(beiJingDate, beijingZone, Calendar.getInstance().getTimeZone()).getTime();
    }

}
