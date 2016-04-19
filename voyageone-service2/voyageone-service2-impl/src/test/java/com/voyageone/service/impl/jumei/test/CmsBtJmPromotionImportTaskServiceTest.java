package com.voyageone.service.impl.jumei.test;

import com.voyageone.service.impl.jumei.CmsBtJmPromotionImportTaskService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

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
