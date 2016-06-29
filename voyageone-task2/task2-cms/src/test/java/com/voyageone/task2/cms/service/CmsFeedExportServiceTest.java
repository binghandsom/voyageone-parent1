package com.voyageone.task2.cms.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/6/28.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsFeedExportServiceTest {

    @Autowired
    CmsFeedExportService cmsFeedExportService;
    @Autowired
    CmsBtExportTaskService cmsBtExportTaskService;
    @Test
    public void testOnStartup() throws Exception {
        List<CmsBtExportTaskModel> cmsBtExportTaskModels = cmsBtExportTaskService.getExportTaskByUser("010", CmsBtExportTaskService.FEED, "james");

        cmsFeedExportService.onStartup(JacksonUtil.jsonToMap(JacksonUtil.bean2Json(cmsBtExportTaskModels.get(0))));
    }
}