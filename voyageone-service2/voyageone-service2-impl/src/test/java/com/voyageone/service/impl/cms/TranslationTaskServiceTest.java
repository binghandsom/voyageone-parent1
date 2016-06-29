package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Ethan Shi on 2016/6/28.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TranslationTaskServiceTest {

    @Autowired
    TranslationTaskService translationTaskService;

    @Test
    public void testGetTaskSummary() throws Exception {
        TaskSummaryBean result = translationTaskService.getTaskSummary("010", "salley.yang");
        System.out.println(JsonUtil.bean2Json(result));

    }

    @Test
    public void testGetCurrentTask() throws Exception {
        TranslationTaskBean result  = translationTaskService.getCurrentTask("010", "will");
        System.out.println(JsonUtil.bean2Json(result));

    }
}