package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.task2.cms.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by dell on 2016/7/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JmBtPromotionImportJobServiceTest extends BaseTest {

    @Autowired
    CmsBtJmPromotionImportTask3Service service;
    @Autowired
    JuMeiProductUpdateJobService serviceJob;

    @Test
    public void testOnStartup() throws Exception {
        service.importFile(25, "/usr/web/contents/cms/jumei_sx/import");
    }

    @Test
    public void testJuMeiProductUpdateJobServiceStart() throws InterruptedException {
        MQConfigInitTestUtil.startMQ(service);

    }
}