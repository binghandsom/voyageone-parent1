package com.voyageone.service.dao.cms;

import com.voyageone.service.daoext.cms.CmsMtChannelConfigDaoExt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by shiqin on 2016-04-12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtChannelConfigDaoExtTest {

    @Autowired
    private CmsMtChannelConfigDaoExt cmsMtChannelConfigDaoExt;

    @Test
    public void testGetActionModelList() throws Exception {
        cmsMtChannelConfigDaoExt.selectByConfigKey("111", "111");

    }
}