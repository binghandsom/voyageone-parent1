package com.voyageone.web2.cms.views.bi_report;

import com.voyageone.service.daoext.report.BiReportSalesShop010DaoExt;
import com.voyageone.web2.cms.views.biReport.consult.BiRepConsultService;
import com.voyageone.web2.cms.views.biReport.consult.BiRepSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created by dell on 2017/1/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class BiRepTest {
    @Autowired
    BiRepSupport biRepSupport;
    @Autowired
    private BiRepConsultService biRepConsultService;
    @Autowired
    private BiReportSalesShop010DaoExt biReportSalesShop010DaoExt;

    /*
        @Test
        public void biReportTest()
        {
            biRepConsultService.createXLSFile();
        }*/
    @Test
    public void getData() {
       /* NumberFormat percent = NumberFormat.getPercentInstance();  //建立百分比格式化引用
        percent.setMaximumFractionDigits(3); //百分比小数点最多3位
        BigDecimal interestRate = new BigDecimal("0.008"); //利率
        BigDecimal bg=new BigDecimal(percent.format(interestRate));
        System.out.println(bg.toString());*/

        BigDecimal bg1=new BigDecimal(2.4535);
        BigDecimal nbg=bg1.setScale(2,BigDecimal.ROUND_HALF_UP);
        System.out.println(nbg.multiply(new BigDecimal(1000)).doubleValue());
        System.out.println((double)1/200);
        BigDecimal rate=new BigDecimal((double)1/200);
        System.out.println(rate.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
       /* BigDecimal a=new BigDecimal(0);
        BigDecimal b=new BigDecimal(1);
        System.out.println(a==null+" "+a.equals());
        System.out.println(b.divide(a));*/

     /*   Map<String, Object> map = new HashMap<>();
        map.put("shopId", 19);
        map.put("staDate", "2016-1-1");
        map.put("endDate", "2017-1-1");
      *//*  BigDecimal bd=*//*
        System.out.println(biReportSalesShop010DaoExt.selectAmtDateToDate(map));*/
      /*  bd.setScale(2,BigDecimal.ROUND_CEILING);

        System.out.println(bd.doubleValue());*/
       /* Map mapForWTD= new HashMap<String,Object>();
        mapForWTD.put("shopId",id);
        mapForWTD.put("staDate",thisWeekFirstDay);
        mapForWTD.put("endDate",date);
        BigDecimal WTD=biReportSalesShop010DaoExt.selectAmtDateToDate(mapForWTD);*/
       /* Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        Date now = new Date();
        Date date = new Date(117, 1, 17);
        System.out.println();
        calendar.setTime(now);
        System.out.println(calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.DAY_OF_WEEK));*/
//            biRepConsultService.getData();

//        biRepConsultService.createXLSFile();
    }

    public <T> void testcall(Collection<T> dataset) {

    }
}
