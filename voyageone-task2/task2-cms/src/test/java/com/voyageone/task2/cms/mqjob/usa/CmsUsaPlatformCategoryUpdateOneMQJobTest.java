package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateOneMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/8/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUsaPlatformCategoryUpdateOneMQJobTest {

    @Autowired
    CmsUsaPlatformCategoryUpdateOneMQJob cmsUsaPlatformCategoryUpdateOneMQJob;
    @Test
    public void testOnStartup() throws Exception {
        CmsUsaPlatformCategoryUpdateOneMQMessageBody body = new CmsUsaPlatformCategoryUpdateOneMQMessageBody();
        body.setChannelId("001");
        body.setCartId(8);
        body.setProductCodes(Collections.singletonList("68220-gem"));
        body.setpCatPath("Men's>XuSong");
        body.setpCatId("7");

        cmsUsaPlatformCategoryUpdateOneMQJob.onStartup(body);

    }
}