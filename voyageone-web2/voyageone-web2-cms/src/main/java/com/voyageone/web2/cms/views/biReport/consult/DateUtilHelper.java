package com.voyageone.web2.cms.views.biReport.consult;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dell on 2017/1/17.
 */
@Service
public class DateUtilHelper {
    private Date date;
    DateUtilHelper()
    {}
    DateUtilHelper(Date date1)
    {
        this.date=date1;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 获取到本周一的零点开始
     * @return
     */
    public  Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofWeek=cal.get(Calendar.DAY_OF_WEEK);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 獲取星期一
     * @return
     */
    public  Date getNowWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, -1); //解决周日会出现 并到下一周的情况
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public  Date getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public  Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    public  Date getLastMonthStartMorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTime(getTimesMonthmorning());
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public  Date getCurrentQuarterStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                cal.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                cal.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                cal.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                cal.set(Calendar.MONTH, 9);
            cal.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(cal.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public  Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }


    public  Date getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
        return cal.getTime();
    }

    public  Date getCurrentYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    public  Date getLastYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    // 获得当天0点时间
    public  Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     *获取昨天时间
    * */
    public Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);
        return cal.getTime();
    }

    /**
     *获取上年同一天时间
     * */
    public Date getTheSameDayLastYear()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
        return cal.getTime();
    }

    public static  void main(String[] args)
    {
        Date date=new Date(117,10,23);
        DateUtilHelper dateUtilHelper=new DateUtilHelper(date);
        System.out.println("当前时间：" + date.toLocaleString());
        System.out.println("当天0点时间：" + dateUtilHelper.getTimesmorning().toLocaleString());
        System.out.println("昨天0点时间：" + dateUtilHelper.getYesterday().toLocaleString());
        System.out.println("本周周一0点时间：" + dateUtilHelper.getTimesWeekmorning().toLocaleString());
        System.out.println("本周周一0点时间：" + dateUtilHelper.getNowWeekMonday().toLocaleString());
        System.out.println("同比：" + dateUtilHelper.getTheSameDayLastYear().toLocaleString());
    }
}
