package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateCategoryMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBtProductUpdateCategoryMQJobTest {

    @Autowired
    CmsBtProductUpdateCategoryMQJob CmsBtProductUpdateCategoryMQJob;
    @Test
    public void testOnStartup() throws Exception {

        CmsBtProductUpdateCategoryMQJob.onStartup(new CmsBtProductUpdateCategoryMQMessageBody());
    }
}