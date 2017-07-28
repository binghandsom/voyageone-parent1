package com.voyageone.task2.cms.mqjob.usa;

import com.google.common.collect.Lists;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsCategoryReceiveMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Charis on 2017/7/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformCategoryTransferUsMqJobTest {

    @Autowired
    CmsBuildPlatformCategoryTransferUsMqJob cmsBuildPlatformCategoryTransferUsMqJob;


    @Test
    public void onStartup() throws Exception {
        List<String> catIds = Lists.newArrayList("6-7");
        CmsCategoryReceiveMQMessageBody body = new CmsCategoryReceiveMQMessageBody();
        body.setChannelId("001");
        body.setCartId("8");
        body.setFullCatIds(catIds);

        cmsBuildPlatformCategoryTransferUsMqJob.onStartup(body);
    }
}
