package com.voyageone.common.util;

import org.junit.Test;

import java.util.Date;

public class DateTimeUtilBeijingTest {

    @Test
    public void test1() {
        System.out.println(DateTimeUtilBeijing.getCurrentBeiJingDate());
    }

    @Test
    public void test2() {
        Date now = new Date();
        System.out.println(DateTimeUtilBeijing.toBeiJingDate(now));
    }

    @Test
    public void test3() {
        Date now = new Date();
        System.out.println(DateTimeUtilBeijing.toLocalDate(now));
    }

    @Test
    public void test4() {
        Date now = new Date();
        long time = DateTimeUtilBeijing.toLocalTime(now);
        System.out.println("local time : " + new Date(time));
    }
}
