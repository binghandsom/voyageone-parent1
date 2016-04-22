package com.voyageone.service.daoext.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ethan Shi on 2016/4/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtFeedCustomOptionDaoExtTest {

    @Autowired
    CmsMtFeedCustomOptionDaoExt cmsMtFeedCustomOptionDaoExt;

    @Test
    public void testSelectPropList() throws Exception {

        cmsMtFeedCustomOptionDaoExt.selectPropList("1");

    }

    @Test
    public void testSelectPropValue() throws Exception {
        cmsMtFeedCustomOptionDaoExt.selectPropValue("1");

    }
}