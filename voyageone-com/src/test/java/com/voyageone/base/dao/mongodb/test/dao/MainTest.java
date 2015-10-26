package com.voyageone.base.dao.mongodb.test.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Created by DELL on 2015/10/21.
 */
public class MainTest {
    public static void main(String[] args) {
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");

//        MainTestReadTask testTask = ctx.getBean(MainTestReadTask.class);
//        testTask.testSelect();

        MainTestInsertTask testTask1 = ctx.getBean(MainTestInsertTask.class);
        testTask1.testSave();

        testTask1.testProductSave3();

    }
}
