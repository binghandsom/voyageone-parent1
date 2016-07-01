package com.voyageone.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dell on 2016/6/30.
 */
public class BeiJingDateUtil {
    //获取当前北京日期
    public static Date getCurrentBeiJingDate() {
      return toBeiJingDate(new Date());
    }
    //当前时区时间To北京时间
    public static Date toBeiJingDate(Date localDate) {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();//当前时区
        long time = localDate.getTime() - timeZone.getRawOffset() + 8 * 3600 * 1000;//北京时区时间戳
        return new Date(time);
    }
    //北京时间To本地时区时间
    public static Date toLocalDate(Date beiJingDate) {
        return new Date(toLocalTime(beiJingDate));
    }
    //北京时间To本地时区时间戳
    public static long toLocalTime(Date beiJingDate) {
        long utcTime = beiJingDate.getTime() - 8 * 3600 * 1000;//UTC时间戳
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();//当前时区
        long localTime = utcTime + timeZone.getRawOffset();//本地时区时间戳
        return localTime;
    }
}
