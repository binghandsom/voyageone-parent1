package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.cms.service.model.CmsMtComSchemaModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by lewis on 16-1-7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtCommonSchemaDaoTest {

    @Autowired
    CmsMtCommonSchemaDao commonSchemaDao;

    @Test
    public void testGetComSchema() throws Exception {

        CmsMtComSchemaModel comSchemaModel = commonSchemaDao.getComSchema();

        Assert.assertNotNull(comSchemaModel);

    }
}