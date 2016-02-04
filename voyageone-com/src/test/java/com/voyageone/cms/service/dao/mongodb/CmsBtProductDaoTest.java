package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.cms.service.model.CmsBtProductModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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

        Long[] productIds = new Long[]{};
        productIds[0] = 163L;

        List<CmsBtProductModel> modelCode = cmsBtProductDao.getModelCode("013", productIds);
        Assert.assertNotNull(modelCode);
    }

    @Test
    public void testCheckProductDataIsReady() throws Exception {

        boolean isReady = cmsBtProductDao.checkProductDataIsReady("013",162l);
        Assert.assertFalse(isReady);
    }

    @Test
    public void testGetOnSaleProducts() throws Exception {

        List<CmsBtProductModel> products = cmsBtProductDao.getOnSaleProducts("013","05-58255");

        System.out.println(products.size());
    }
}