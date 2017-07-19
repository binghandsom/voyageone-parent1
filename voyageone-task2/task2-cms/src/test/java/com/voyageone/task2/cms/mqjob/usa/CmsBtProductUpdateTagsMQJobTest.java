package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductFreeTagsUpdateMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateTagsMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBtProductUpdateTagsMQJobTest {

    @Autowired
    CmsBtProductUpdateTagsMQJob cmsBtProductUpdateTagsMQJob;
    @Test
    public void testOnStartup() throws Exception {
        CmsProductFreeTagsUpdateMQMessageBody map = new CmsProductFreeTagsUpdateMQMessageBody();

        map.setChannelId("001");
        map.setProdCodeList(Collections.singletonList("812654-110"));
        ArrayList<String> list = new ArrayList<>();
        list.add("-2222-");
        list.add("-3333-");
        list.add("-5555-1111-");
        list.add("-4444-777-");
        map.setTagPathList(list);

        cmsBtProductUpdateTagsMQJob.onStartup(map);

    }
}