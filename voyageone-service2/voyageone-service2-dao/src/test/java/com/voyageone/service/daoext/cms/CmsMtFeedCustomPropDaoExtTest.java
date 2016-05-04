package com.voyageone.service.daoext.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016/4/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtFeedCustomPropDaoExtTest {

    @Autowired
    CmsMtFeedCustomPropDaoExt cmsMtFeedCustomPropDaoExt;

    @Test
    public void testSelectAllAttr() throws Exception {

        Map<String, Object> sqlPara = new HashMap<>();
        sqlPara.put("feedCatPath", "1");
        sqlPara.put("propName", "2");
        sqlPara.put("propValue", "3");
        sqlPara.put("tSts", "4");
        sqlPara.put("channelId", "5");
        cmsMtFeedCustomPropDaoExt.selectPropValue(sqlPara);

    }
}