package com.voyageone.service.impl.jumei.test;

import com.voyageone.service.impl.jumei.CmsBtJmPromotionExportTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtJmPromotionExportTaskServiceTest {
    @Autowired
    CmsBtJmPromotionExportTaskService service;
    @Test
    public  void  test() throws FileNotFoundException {
        service.export(1);
    }
}
