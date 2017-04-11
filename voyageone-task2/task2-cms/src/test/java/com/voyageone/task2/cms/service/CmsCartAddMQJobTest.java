package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsCartAddMQMessageBody;
import com.voyageone.task2.cms.mqjob.CmsCartAddMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by james on 2016/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsCartAddMQJobTest {

    @Autowired
    CmsCartAddMQJob cmsCartAddMQJob;
    @Test
    public void onStartup(){
        CmsCartAddMQMessageBody map = new CmsCartAddMQMessageBody();
        map.setChannelId("010");
        map.setCartId(928);
        map.setSender("CmsCartAddMQJob");
        try {
            cmsCartAddMQJob.onStartup(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}