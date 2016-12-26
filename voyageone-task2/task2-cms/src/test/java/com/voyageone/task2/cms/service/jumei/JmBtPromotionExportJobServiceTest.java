package com.voyageone.task2.cms.service.jumei;

import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JmBtPromotionExportJobServiceTest {


    @Autowired
    JmBtPromotionExportJobService service;
    @Test
    public void testOnStartup() throws Exception {
        Map<String,Object> map2 = new HashMap();
        map2.put("id",308);
        service.onStartup(map2);
    }
}
