package com.voyageone.task2.cms.service.jumei;

import com.sun.javafx.tk.Toolkit;
import com.voyageone.common.spring.SpringStartFinish;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JmBtPromotionExportJobServiceTest {
    @Autowired
    JmBtPromotionExportJobService service;
    @Test
    public void testOnStartup() throws Exception {
        //service.onStartup()
        service.startMQ();
        Thread.sleep(999999999999999999L);
    }
}
