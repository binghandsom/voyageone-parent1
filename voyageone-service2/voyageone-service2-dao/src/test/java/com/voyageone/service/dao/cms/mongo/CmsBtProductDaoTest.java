package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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

//    @Test
//    public void testGetModelCode() throws Exception {
//
//        Long[] productIds = {22139L};
//
//        List<CmsBtProductModel> modelCode = cmsBtProductDao.getModelCode("010", productIds);
//        Assert.assertNotNull(modelCode);
//    }

    @Test
    public void testCheckProductDataIsReady() throws Exception {

        boolean isReady = cmsBtProductDao.checkProductDataIsReady("013",162l);
        Assert.assertFalse(isReady);
    }

//    @Test
//    public void testGetOnSaleProducts() throws Exception {
//
//        List<CmsBtProductModel> products = cmsBtProductDao.getOnSaleProducts("013","05-58255");
//
//        System.out.println(products.size());
//    }

    @Test
    public void testUpdateFirst() throws Exception {
        String queryStr = "{\"$or\":[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[\"aa\"]},\"fields.translator\":{\"$in\":[null,\"\"]}},{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$in\":[\"123\"]},\"fields.translateTime\":{\"$lt\":\"2016-01-07 16:26:34\"}}]}";
        List lst = cmsBtProductDao.select(queryStr, "013");
        String strUpdate = "{\"$set\":{\"fields.translator\":\"liangchuanyu1\", \"fields.translateTime\":\"2016-01-07 16:26:36\"}}";
        WriteResult isresult = cmsBtProductDao.updateFirst("013", queryStr, strUpdate);
        System.out.println(isresult);
    }
}