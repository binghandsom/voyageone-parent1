package com.voyageone.web2.cms.views.jm;

import com.voyageone.service.impl.cms.jumei.CmsBtJmImageTemplateService;
import com.voyageone.service.model.cms.mongo.CmsBtJmImageTemplateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by james on 2016/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsJmPromotionExportServiceTest {

    @Test
    public void doExportJmPromotionFile1() throws Exception {
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = new CmsBtJmImageTemplateModel();
        //cmsBtJmImageTemplateModel.setImageId(1);
        cmsBtJmImageTemplateModel.setName("2-移动端入口图-和app首页5号图一致-2048x1024");
        cmsBtJmImageTemplateService.insert(cmsBtJmImageTemplateModel);
    }

    @Autowired
    CmsJmPromotionExportService cmsJmPromotionExportService;

    @Autowired
    CmsBtJmImageTemplateService cmsBtJmImageTemplateService;
    @Test
    public void doExportJmPromotionFile() throws Exception {
        cmsJmPromotionExportService.doExportJmPromotionFile(111,CmsJmPromotionExportService.HASHID);
    }

}