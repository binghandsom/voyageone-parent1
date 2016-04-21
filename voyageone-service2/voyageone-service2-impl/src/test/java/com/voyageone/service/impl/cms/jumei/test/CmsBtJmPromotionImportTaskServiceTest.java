package com.voyageone.service.impl.cms.jumei.test;

import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionImportTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtJmPromotionImportTaskServiceTest {
    @Autowired
    CmsBtJmPromotionImportTaskService service;

    @Test
    public  void  test() throws Exception {
        service.importFile(18,"/usr/JMImport");
    }
}
