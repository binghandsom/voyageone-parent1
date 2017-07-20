package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductFreeTagsUpdateMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsProductFreeTagsUpdateMQJobTest {

    @Autowired
    CmsProductFreeTagsUpdateMQJob service;

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
        map.setType("usa");
        map.setIsSelAll(true);
        CmsSearchInfoBean2 cmsSearchInfoBean2 = new CmsSearchInfoBean2();
        cmsSearchInfoBean2.setProductPageNum(1);
        cmsSearchInfoBean2.setGroupPageSize(10);
        map.setSearchValue(cmsSearchInfoBean2);
        service.onStartup(map);

    }
}
