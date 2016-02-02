package com.voyageone.cms.service.dao.mongodb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by lewis on 2016/2/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtProductDaoTest {

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testGetModelCode() throws Exception {

       String modelCode = cmsBtProductDao.getModelCode("013",161l);
        Assert.assertNotNull(modelCode);
    }

    @Test
    public void testCheckProductDataIsReady() throws Exception {

        boolean isReady = cmsBtProductDao.checkProductDataIsReady("013",162l);
        Assert.assertFalse(isReady);
    }
}