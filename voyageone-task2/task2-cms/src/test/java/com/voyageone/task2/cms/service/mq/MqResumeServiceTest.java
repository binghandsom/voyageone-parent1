package com.voyageone.task2.cms.service.mq;

import com.voyageone.task2.cms.service.MqResumeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2016/12/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class MqResumeServiceTest {

    @Autowired
    MqResumeService service;

    @Test
    public  void  test() throws Exception {
          service.onStartup(null);
    }
}
