package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.task2.cms.mqjob.CmsJmPromotionExportMQJob;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JmBtPromotionExportJobServiceTest {


    @Autowired
    CmsJmPromotionExportMQJob service;
    @Test
    public void testOnStartup() throws Exception {
        //service.onStartup(map2);
        MQConfigInitTestUtil.startMQ(service);
    }
}
