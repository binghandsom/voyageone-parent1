package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchLockProductsMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 123 on 2017/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchLockProductsMQJobTest {

    @Autowired
    private CmsAdvSearchLockProductsMQJob cmsAdvSearchLockProductsMQJob;

    @Test
    public void onStartup() throws Exception {
        String json = "";

        AdvSearchLockProductsMQMessageBody messageBody = JacksonUtil.json2Bean(json, AdvSearchLockProductsMQMessageBody.class);
        cmsAdvSearchLockProductsMQJob.onStartup(messageBody);

    }
}
