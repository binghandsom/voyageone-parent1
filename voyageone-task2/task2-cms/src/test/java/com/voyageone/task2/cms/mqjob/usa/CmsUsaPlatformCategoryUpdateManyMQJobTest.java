package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateManyMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUsaPlatformCategoryUpdateManyMQJobTest {

    @Autowired
    CmsUsaPlatformCategoryUpdateManyMQJob cmsUsaPlatformCategoryUpdateManyMQJob;

    @Test
    public void testOnStartup() throws Exception {
        CmsUsaPlatformCategoryUpdateManyMQMessageBody body = new CmsUsaPlatformCategoryUpdateManyMQMessageBody();

        body.setCartId(8);
        body.setChannelId("001");
        body.setProductCodes(Collections.singletonList("68243-blk"));
        body.setStatue(false);
        ArrayList<Map<String, String>> lists = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("pCatPath","Sale>Toddler>50% Off & Up");
        lists.add(map);
//        body.setpCatPathAndPCatIds(lists);
        cmsUsaPlatformCategoryUpdateManyMQJob.onStartup(body);
    }
}