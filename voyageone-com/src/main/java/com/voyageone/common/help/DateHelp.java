package com.voyageone.common.help;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by admin on 2014/10/30.
 */
public class DateHelp {
    static Date DefaultDate;

    public static Date getDefaultDate() {
        if (DefaultDate == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                DefaultDate = sdf.parse("2000-01-01 00:00:00");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return DefaultDate;
    }


    /*
    *
    * */
    public static String DateToString(Date time) {
        // Date d = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }
    public static String DateToString(Date time,String pattern) {
        // Date d = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(time);
    }
    public static Date ToDate(String time) throws ParseException {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date ctime=null;
        if(time.length()<19){ time+="0000-00-00 00:00:00".substring(time.length());}
            ctime = formatter.parse(time);
        return ctime;
    }
    public static Date ToDate(int Year,int month,int day){
        String time= String.format("%04d-%02d-%02d 00:00:00",Year,month,day);
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date ctime=null;
        try {
            ctime = formatter.parse(time);
        }catch (Exception ex)
        {

        }
        return ctime;
    }
    public static int getDayCountByYearMonth(String yyyyMM) throws ParseException {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat oSdf = new SimpleDateFormat("", Locale.ENGLISH);
        oSdf.applyPattern("yyyyMM");
        cal.setTime(oSdf.parse(yyyyMM));
        int num2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return num2;
    }
    public  static  int getWeekOfYearByDate(Date dt) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(dt);
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    public  static  int getDayWeekByDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);//

        return dayofweek-1;
//        switch (dayofweek) {
//            case 1:
//                return 7; //"星期日";
//            case 2:
//                return 1;//"星期一";
//            case 3:
//                return 2;//"星期二";
//            case 4:
//                return 3;//"星期三";
//            case 5:
//                return 4;//"星期四";
//            case 6:
//                return 5;//"星期五";
//            case 7:
//                return 6;//"星期六";
//            default:
//                return 0;
//        }
    }
}


