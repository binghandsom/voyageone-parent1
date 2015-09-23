/*
 * Created on 2005-8-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.voyageone.bi.commonutils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 日期时间工具类 <br>
 * 提供一些常用的日期时间操作方法，所有方法都为静态，不用实例化该类即可使用。 <br>
 * <br>
 * 下为日期格式的简单描述详情请参看java API中java.text.SimpleDateFormat <br>
 * The following pattern letters are defined (all other characters from
 * <code>'A'</code> to <code>'Z'</code> and from <code>'a'</code> to
 * <code>'z'</code> are reserved): <blockquote><table border=0 cellspacing=3
 * cellpadding=0>
 * <tr bgcolor="#ccccff">
 * <th align=left>Letter
 * <th align=left>Date or Time Component
 * <th align=left>Presentation
 * <th align=left>Examples
 * <tr>
 * <td><code>G</code>
 * <td>Era designator
 * <td><a href="#text">Text </a>
 * <td><code>AD</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>y</code>
 * <td>Year
 * <td><a href="#year">Year </a>
 * <td><code>1996</code>;<code>96</code>
 * <tr>
 * <td><code>M</code>
 * <td>Month in year
 * <td><a href="#month">Month </a>
 * <td><code>July</code>;<code>Jul</code>;<code>07</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>w</code>
 * <td>Week in year
 * <td><a href="#number">Number </a>
 * <td><code>27</code>
 * <tr>
 * <td><code>W</code>
 * <td>Week in month
 * <td><a href="#number">Number </a>
 * <td><code>2</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>D</code>
 * <td>Day in year
 * <td><a href="#number">Number </a>
 * <td><code>189</code>
 * <tr>
 * <td><code>d</code>
 * <td>Day in month
 * <td><a href="#number">Number </a>
 * <td><code>10</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>F</code>
 * <td>Day of week in month
 * <td><a href="#number">Number </a>
 * <td><code>2</code>
 * <tr>
 * <td><code>E</code>
 * <td>Day in week
 * <td><a href="#text">Text </a>
 * <td><code>Tuesday</code>;<code>Tue</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>a</code>
 * <td>Am/pm marker
 * <td><a href="#text">Text </a>
 * <td><code>PM</code>
 * <tr>
 * <td><code>H</code>
 * <td>Hour in day (0-23)
 * <td><a href="#number">Number </a>
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>k</code>
 * <td>Hour in day (1-24)
 * <td><a href="#number">Number </a>
 * <td><code>24</code>
 * <tr>
 * <td><code>K</code>
 * <td>Hour in am/pm (0-11)
 * <td><a href="#number">Number </a>
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>h</code>
 * <td>Hour in am/pm (1-12)
 * <td><a href="#number">Number </a>
 * <td><code>12</code>
 * <tr>
 * <td><code>m</code>
 * <td>Minute in hour
 * <td><a href="#number">Number </a>
 * <td><code>30</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>s</code>
 * <td>Second in minute
 * <td><a href="#number">Number </a>
 * <td><code>55</code>
 * <tr>
 * <td><code>S</code>
 * <td>Millisecond
 * <td><a href="#number">Number </a>
 * <td><code>978</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>z</code>
 * <td>Time zone
 * <td><a href="#timezone">General time zone </a>
 * <td><code>Pacific Standard Time</code>;<code>PST</code>;
 * <code>GMT-08:00</code>
 * <tr>
 * <td><code>Z</code>
 * <td>Time zone
 * <td><a href="#rfc822timezone">RFC 822 time zone </a>
 * <td><code>-0800</code> </table> </blockquote>
 * <h4>Examples</h4>
 *
 * The following examples show how date and time patterns are interpreted in the
 * U.S. locale. The given date and time are 2001-07-04 12:08:56 local time in
 * the U.S. Pacific Time time zone. <blockquote><table border=0 cellspacing=3
 * cellpadding=0>
 * <tr bgcolor="#ccccff">
 * <th align=left>Date and Time Pattern
 * <th align=left>Result
 * <tr>
 * <td><code>"yyyy.MM.dd G 'at' HH:mm:ss z"</code>
 * <td><code>2001.07.04 AD at 12:08:56 PDT</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"EEE, MMM d, ''yy"</code>
 * <td><code>Wed, Jul 4, '01</code>
 * <tr>
 * <td><code>"h:mm a"</code>
 * <td><code>12:08 PM</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"hh 'o''clock' a, zzzz"</code>
 * <td><code>12 o'clock PM, Pacific Daylight Time</code>
 * <tr>
 * <td><code>"K:mm a, z"</code>
 * <td><code>0:08 PM, PDT</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"yyyyy.MMMMM.dd GGG hh:mm aaa"</code>
 * <td><code>02001.July.04 AD 12:08 PM</code>
 * <tr>
 * <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code>
 * <td><code>Wed, 4 Jul 2001 12:08:56 -0700</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"yyMMddHHmmssZ"</code>
 * <td><code>010704120856-0700</code> </table> </blockquote>
 *
 * @author 创建日期： 2003.8.28
 */

public final class DateTimeUtil {

    private static Logger log = Logger.getLogger(DateTimeUtil.class);

    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 缺省时间显示格式：HH:mm:ss
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 简单的日期显示格式： yyyyMMdd
     */
    public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";

    /**
     * 私有构造方法，禁止对该类进行实例化
     */
    private DateTimeUtil() {
    }

    /**
     * 得到系统当前日期时间
     *
     * @return 当前日期时间
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 得到用缺省方式格式化的当前日期
     *
     * @return 当前日期
     */
    public static String getDate() {
        return getDateTime(DEFAULT_DATE_FORMAT);
    }

    /**
     * 得到用缺省方式格式化的当前日期及时间
     *
     * @return 当前日期及时间
     */
    public static String getDateTime() {
        return getDateTime(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 得到系统当前日期及时间，并用指定的方式格式化
     *
     * @param pattern
     *            显示格式
     * @return 当前日期及时间
     */
    public static String getDateTime(String pattern) {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, pattern);
    }

    /**
     * 得到用指定方式格式化的日期
     *
     * @param date
     *            需要进行格式化的日期
     * @param pattern
     *            显示格式
     * @return 日期时间字符串
     */
    public static String getDateTime(Date date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 将一个字符串用给定的格式转换为字符串类型。 <br>
     * 注意：如果返回null，则表示解析失败
     *
     * @param dateSting
     *            需要解析的日期字符串
     * @param pattern
     *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期
     */
    public static String parseStr(String dateSting, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        if (dateSting == null || dateSting.trim().length() == 0) {
            return "";
        }
        if (dateSting.length() == 10) {
            dateSting = dateSting + " 00:00:00";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(parse(dateSting, DEFAULT_DATETIME_FORMAT));
    }

    /**
     * 将一个字符串用给定的格式转换为字符串类型。 <br>
     * 注意：如果返回null，则表示解析失败
     *
     * @param dateSting
     *            需要解析的日期字符串
     * @param pattern
     *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期
     */
    public static String formatDate(Date date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        if (date == null) {
            return "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 得到当前年份
     *
     * @return 当前年份
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     *
     * @return 当前月份
     */
    public static int getCurrentMonth() {
        // 用get得到的月份数比实际的小1，需要加上
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前日
     *
     * @return 当前日
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 得到当前分钟
     *
     * @return 当前分钟
     */
    public static int getCurrentMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 得到当前小时
     *
     * @return 当前小时
     */
    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    }

    /**
     * 得到当前秒
     *
     * @return 当前秒
     */
    public static int getCurrentSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);

    }

    /**
     * 得到指定日期年份
     *
     * @return 年份
     */
    public static int getDateYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 得到指定日期月份
     *
     * @return 月份
     */
    public static int getDateMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 得到指定日期日
     *
     * @return 日
     */
    public static int getDateDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 得到指定分钟
     *
     * @return 指定分钟
     */
    public static int getDateMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 得到指定小时
     *
     * @return 指定小时
     */
    public static int getDateHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);

    }

    /**
     * 得到指定秒
     *
     * @return 指定秒
     */
    public static int getDateSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);

    }

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     *
     * @param days
     *            增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(int days) {
        return add(getNow(), days, Calendar.DATE);
    }

    /**
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date
     *            基准日期
     * @param days
     *            增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }

    /**
     * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param months
     *            增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(int months) {
        return add(getNow(), months, Calendar.MONTH);
    }

    /**
     * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
     *
     * @param date
     *            基准日期
     * @param months
     *            增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    /**
     * 取得当前日期以后某年的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param months
     * @return
     */
    public static Date addYears(int years) {
        return add(getNow(), years, Calendar.YEAR);
    }

    /**
     * 取得指定日期以后某年的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param date
     *            基准日期
     * @param years
     *            增加的月份数
     * @return
     */
    public static Date addYears(Date date, int years) {
        return add(date, years, Calendar.YEAR);
    }

    /**
     * 内部方法。为指定日期增加相应的天数或月数
     *
     * @param date
     *            基准日期
     * @param amount
     *            增加的数量
     * @param field
     *            增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    private static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(field, amount);

        return calendar.getTime();
    }

    /**
     * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one
     *            第一个日期数，作为基准
     * @param two
     *            第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     *
     * @param one
     *            第一个日期数，作为基准
     * @param two
     *            第二个日期数，作为比较
     * @return 两个日期相差月份数
     */
    public static int diffMonths(Date one, Date two) {

        Calendar calendar = Calendar.getInstance();

        // 得到第一个日期的年分和月份数
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);
        int monthOne = calendar.get(Calendar.MONDAY);

        // 得到第二个日期的年份和月份
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);
        int monthTwo = calendar.get(Calendar.MONDAY);

        return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
    }

    /**
     * 将一个字符串用给定的格式转换为日期类型。 <br>
     * 注意：如果返回null，则表示解析失败
     *
     * @param datestr
     *            需要解析的日期字符串
     * @param pattern
     *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期
     */
    public static Date parse(String datestr, String pattern) {
        Date date = null;

        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        if (datestr == null || datestr.trim().length() == 0) {
            return date;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 返回本月的最后一天
     *
     * @return 本月最后一天的日期
     */
    public static Date getMonthLastDay() {
        return getMonthLastDay(getNow());
    }

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @param date
     *            基准日期
     * @return 该月最后一天的日期
     */
    public static Date getMonthLastDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 将日期设置为下一月第一天
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);

        // 减去1天，得到的即本月的最后一天
        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }

    /**
     * 返回本月的第一天
     *
     * @return 本月第一天的日期
     */
    public static Date getMonthFirstDay() {
        return getMonthFirstDay(getNow());
    }

    /**
     * 返回给定日期中的月份中的第一天
     *
     * @param date
     *            基准日期
     * @return 该月第一天的日期
     */
    public static Date getMonthFirstDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 将日期设置为下一月第一天
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);

        return calendar.getTime();
    }

    /**
     * 格式化java.sql.Timestamp 对象成字符串
     *
     * @param obj
     * @return
     */
    public static String parseSqlTimeStamp(Object obj) {
        if (obj instanceof java.sql.Date) {
            String result = obj.toString().trim();
            if (result.length() == 10) {
                result = result + " 00:00:00";
            }
            obj = java.sql.Timestamp.valueOf(result);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeUtil.DEFAULT_DATETIME_FORMAT);
        return dateFormat.format((java.sql.Timestamp) obj);
    }

    /**
     * 格式化java.sql.Time 对象成字符串
     *
     * @param obj
     * @return
     */
    public static String parseSqlTime(Object obj) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeUtil.DEFAULT_TIME_FORMAT);
        return dateFormat.format((java.sql.Time) obj);
    }

    /**
     * 格式化java.sql.Date对象成字符串
     *
     * @param obj
     * @return
     */
    public static String parseSqlDate(Object obj) {
        if (obj instanceof java.sql.Timestamp) {
            String result = obj.toString().trim();
            if (result.length() > 10) {
                result = result.substring(0, 10);
            }
            obj = java.sql.Date.valueOf(result);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeUtil.DEFAULT_DATE_FORMAT);
        return dateFormat.format((java.sql.Date) obj);
    }

    /**
     * 格式化 日期串
     *
     * type ==1 时 “2005年10月1日”
     *
     * type ==2 时 "二〇〇五年十月一日"
     *
     * @return
     */
    public static String formatDate(String dateStr, String type) throws Exception {

        if (dateStr == null || dateStr.trim().length() == 0) {
            return "";
        }

        Date date = DateTimeUtil.parse(dateStr.trim().substring(0, 10), "yyyy-MM-dd");
        if (type.trim().equalsIgnoreCase("1")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            return df.format(date);

        } else if (type.trim().equalsIgnoreCase("2")) {

            String year = String.valueOf(DateTimeUtil.getDateYear(date));
            String month = String.valueOf(DateTimeUtil.getDateMonth(date));
            String day = String.valueOf(DateTimeUtil.getDateDay(date));

            StringBuffer str = new StringBuffer("");
            str.append(DateTimeUtil.getUpStr(Integer.parseInt(year.substring(0, 1))));
            str.append(DateTimeUtil.getUpStr(Integer.parseInt(year.substring(1, 2))));
            str.append(DateTimeUtil.getUpStr(Integer.parseInt(year.substring(2, 3))));
            str.append(DateTimeUtil.getUpStr(Integer.parseInt(year.substring(3, 4))));
            str.append("年");
            if (month.length() > 1) {
                if (Integer.parseInt(month) < 20) {
                    str.append("十");
                } else {
                    str.append(DateTimeUtil.getUpStr(Integer.parseInt(month.substring(0, 1))));
                }
                str.append(DateTimeUtil.getUpStr(Integer.parseInt(month.substring(1, 2))));
            } else {
                str.append(DateTimeUtil.getUpStr(Integer.parseInt(month.substring(0, 1))));
            }

            str.append("月");
            if (day.length() > 1) {
                if (Integer.parseInt(day) < 20) {
                    str.append("十");
                } else {
                    str.append(DateTimeUtil.getUpStr(Integer.parseInt(day.substring(0, 1))));
                }
                str.append(DateTimeUtil.getUpStr(Integer.parseInt(day.substring(1, 2))));
            } else {
                str.append(DateTimeUtil.getUpStr(Integer.parseInt(day.substring(0, 1))));
            }

            str.append("日");
            return str.toString();
        }

        return "";
    }

    /**
     * 获取当前月上月的最后一天
     *
     * @param args
     */
    public static Date getPreMonthLastDay() {

        return DateTimeUtil.getMonthLastDay(DateTimeUtil.addMonths(-1));

    }

    /*public static void main(String[] args) {

        System.out.println("＝＝＝＝＝日期工具接口介绍＝＝＝＝＝");
        String test = "2005-08-21";

        Date date;
        try {
            formatDate(test, "1");
            formatDate(test, "2");

            // date = parse(test, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
            //
            // System.out.println("得到指定日期年份 － getCurrentYear():"
            // + DateTimeUtil.getDateYear(date));
            // System.out.println("得到指定日期月份 － getCurrentMonth():"
            // + DateTimeUtil.getDateMonth(date));
            // System.out.println("得到指定日期日子 － getCurrentDay():"
            // + DateTimeUtil.getDateDay(date));
            // System.out.println("得到指定日期小时 － getDateHour():"
            // + DateTimeUtil.getDateHour(date));
            // System.out.println("得到指定日期分钟 － getDateMinute():"
            // + DateTimeUtil.getDateMinute(date));
            // System.out.println("得到指定日期秒 － getDateSecond():"
            // + DateTimeUtil.getDateSecond(date));
            //
            // System.out.println("得到当前日期 － getDate():" +
            // DateTimeUtil.getDate());
            // System.out.println("得到当前日期时间 － getDateTime():"
            // + DateTimeUtil.getDateTime());
            //
            // System.out.println("得到当前年份 － getCurrentYear():"
            // + DateTimeUtil.getCurrentYear());
            // System.out.println("得到当前月份 － getCurrentMonth():"
            // + DateTimeUtil.getCurrentMonth());
            // System.out.println("得到当前日子 － getCurrentDay():"
            // + DateTimeUtil.getCurrentDay());
            //
            // System.out.println("解析 － parse(" + test + "):"
            // + getDateTime(date, "yyyy-MM-dd"));
            //
            // System.out.println("自增月份 － addMonths(3):"
            // + getDateTime(addMonths(3), "yyyy-MM-dd"));
            // System.out.println("增加月份 － addMonths(" + test + ",3):"
            // + getDateTime(addMonths(date, 3), "yyyy-MM-dd"));
            // System.out.println("增加日期 － addDays(" + test + ",3):"
            // + getDateTime(addDays(date, 3), "yyyy-MM-dd"));
            // System.out.println("自增日期 － addDays(3):"
            // + getDateTime(addDays(3), "yyyy-MM-dd"));
            //
            // System.out.println("比较日期 － diffDays():"
            // + DateTimeUtil.diffDays(DateTimeUtil.getNow(), date));
            // System.out.println("比较月份 － diffMonths():"
            // + DateTimeUtil.diffMonths(DateTimeUtil.getNow(), date));
            //
            // System.out.println("得到" + test + "所在月份的最后一天:"
            // + getDateTime(getMonthLastDay(date), "yyyy-MM-dd"));
            //
            // System.out.println("=="
            // + DateTimeUtil.parseStr("2005-08-09 00:00:00.0",
            // "yyyy-MM-dd"));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

    }*/

    /**
     * 得到当前为星期几,1-星期日,2-星期一....,7-星期六;
     *
     * @return int 得到当前为星期几,1-星期日,2-星期一....,7-星期六;
     */
    public static int getNowWeek() {
        Date objDate = new Date();
        Calendar objCalendarDate = Calendar.getInstance();
        objCalendarDate.setTime(objDate);
        return objCalendarDate.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据日期返回是星期几
     *
     * @param day
     *            1-星期日,2-星期一....,7-星期六;
     * @return 星期日,星期一....,星期六;
     */
    public static String getWeekDay(int day) {
        String week = "";
        switch (day) {
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
            default:
                week = "星期日";
                break;
        }
        return week;
    }

    /**
     * 根据数字返回大写 文本
     *
     * @param
     * @return
     */
    public static String getUpStr(int int_num) {
        String up_str = "";
        switch (int_num) {
            case 0:
                up_str = "〇";
                break;
            case 1:
                up_str = "一";
                break;
            case 2:
                up_str = "二";
                break;
            case 3:
                up_str = "三";
                break;
            case 4:
                up_str = "四";
                break;
            case 5:
                up_str = "五";
                break;
            case 6:
                up_str = "六";
                break;
            case 7:
                up_str = "七";
                break;
            case 8:
                up_str = "八";
                break;
            case 9:
                up_str = "九";
                break;
            default:
                up_str = "〇";
                break;
        }
        return up_str;
    }

    /**
     * 根据日期类型获取字符串类型
     *
     * @param date
     * @return 得到的格式为 "yyyy-MM-dd"
     */
    public static String getDateString(Date date) {
        String str_date = null;
        int year = DateTimeUtil.getDateYear(date);
        int motnh = DateTimeUtil.getDateMonth(date);
        int day = DateTimeUtil.getDateDay(date);
        if (day > 9) {
            str_date = String.valueOf(year) + "-" + String.valueOf(motnh) + "-" + String.valueOf(day);
        } else {
            str_date = String.valueOf(year) + "-" + String.valueOf(motnh) + "-0" + String.valueOf(day);
        }
        return str_date;

    }
    /**
     * <p>Discription: 判断时间date1是否在时间date2之前,时间格式 2005-4-21 16:16:34</p>
     * @param date1
     * @param date2
     * @return
     * @author       : 
     */
    public static boolean isDateBefore(String date1,String date2){
        try{
            DateFormat df = DateFormat.getDateTimeInstance();
            boolean flag= df.parse(date1).before(df.parse(date2));
            return flag;
        }catch(ParseException e){
            System.out.print("[SYS] " + e.getMessage());
            return false;
        }
    }
    /**
     * <p>Discription: 得到当前日期后或前某一天</p>
     * @param pattern
     * @param days
     * @return
     * @author       : 
     */
    public static String getAddDateTime(String pattern,int days) {
        Date datetime = addDays(days);
        return getDateTime(datetime, pattern);
    }

    /**
     * 取得指定日期以后某年的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param date
     *            基准日期
     * @param hours
     *            增加的时间数
     * @return
     */
    public static Date addHours(Date date, int hours) {
        return add(date, hours, Calendar.HOUR_OF_DAY);
    }

    // 日期字段串转化为 Calendar
    public static Calendar parseDateString(String datePara){
        Calendar c = Calendar.getInstance();
        try {
            DateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
            Date date = df.parse(datePara);
            c.setTime(date);

            return c;
        }catch(ParseException e){
//		      System.out.print("DateTimeUtil.parseDateString " + e.getMessage());
            log.error("DateTimeUtil.parseDateString ", e);
            return null;
        }

    }

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @param dateMonth
     *            基准月份
     * @return 该月最后一天
     */
    public static int getMonthLastDay(int dateMonth) {

        try {
            DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            int year = dateMonth / 100;
            int month = dateMonth % 100;
            Date date = df.parse(year + "-" + month + "-" + "01");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch(ParseException e) {
            log.error("DateTimeUtil.getMonthLastDay ", e);
            return 0;
        }
    }

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     *
     * @param date
     *				待计算日期
     * @param days
     *            增加的日期数
     * @return 增加以后的日期
     */
    public static int addDays(int date, int days) {
        int ret = 0;

        Date datepara = DateTimeUtil.parse(String.valueOf(date), SIMPLE_DATE_FORMAT);
        Date retDate = DateTimeUtil.addDays(datepara, days);

        ret = Integer.parseInt(DateTimeUtil.getDateTime(retDate, SIMPLE_DATE_FORMAT));

        return ret;
    }

    public static void main(String[] args) {
        System.out.println(addDays(20140106,1));
        System.out.println(addDays(20140131,1));

    }
}
