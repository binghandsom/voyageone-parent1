package com.voyageone.web2.cms.views.jm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsJmPromotionExportServiceTest {
    @Autowired
    CmsJmPromotionExportService cmsJmPromotionExportService;
    @Test
    public void doExportJmPromotionFile() throws Exception {
        cmsJmPromotionExportService.doExportJmPromotionFile(111);
    }

}