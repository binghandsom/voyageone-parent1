package com.voyageone.service.impl.cms.jumei.test;

import com.voyageone.common.util.excel.ExcelException;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtJmPromotionExportTaskServiceTest {
    @Autowired
    CmsBtJmPromotionExportTask3Service service;
    @Test
    public  void  test() throws IOException, ExcelException {
        service.export(180,"/usr/web/contents/cms/jumei_sx/export");
    }
}
