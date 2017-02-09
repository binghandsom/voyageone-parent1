package com.voyageone.task2.cms.service.tools;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsRefreshProductsMQMessageBody;
import com.voyageone.task2.cms.mqjob.advanced.search.CmsRefreshProductsMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * MQ 任务 CmsRefreshProductsJobService 的单元测试
 * Created by jonas on 2016/11/3.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsRefreshProductsMQJobTest {

    @Autowired
    private CmsRefreshProductsMQJob cmsRefreshProductsMQJob;

    @Test
    public void onStartup() throws Exception {
        CmsRefreshProductsMQMessageBody map = new CmsRefreshProductsMQMessageBody();
        map.setTaskId(1);
        cmsRefreshProductsMQJob.onStartup(map);
    }
}