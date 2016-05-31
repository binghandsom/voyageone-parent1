package com.voyageone.service.dao.cms.mongo;

import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lewis on 16-1-7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtCommonSchemaDaoTest {

    @Autowired
    private CmsMtCommonSchemaDao commonSchemaDao;

    @Test
    public void testGetComSchema() throws Exception {

        CmsMtCommonSchemaModel comSchemaModel = commonSchemaDao.getComSchema();

        Assert.assertNotNull(comSchemaModel);

    }
}