package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gump on 2016/07/27.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class YogaDemocracyAnalysisServiceTest {
    @Autowired
    YogaDemocracyAnalysisService yogaDemocracyAnalysisService;

    @Test
    public void testOnStartup() throws Exception {


        String str = "7.1oz".trim();

        Pattern pattern = Pattern.compile("[^0-9.]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            int index = str.indexOf(matcher.group());
            if(index != -1){
                String a= str.substring(0,index);
                String b=str.substring(index,str.length());
                System.out.println(a+"   "+b);
            }
        }

        yogaDemocracyAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }
}