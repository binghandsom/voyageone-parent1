package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
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
public class CmsMtChannelValuesDaoTest {

    @Autowired
    private CmsMtChannelValuesDao cmsMtChannelValuesDao;

    @Test
    public void testSelectList() throws Exception {

    }

    @Test
    public void testInsert() throws Exception {
        CmsMtChannelValuesModel cmsMtChannelValuesModel = new CmsMtChannelValuesModel();

        cmsMtChannelValuesModel.setChannelId("1");
        cmsMtChannelValuesModel.setKey("2");
        cmsMtChannelValuesModel.setType(3);
        cmsMtChannelValuesModel.setValue("4");

        cmsMtChannelValuesDao.insert(cmsMtChannelValuesModel);


    }

    @Test
    public void testUpdate() throws Exception {

    }
}