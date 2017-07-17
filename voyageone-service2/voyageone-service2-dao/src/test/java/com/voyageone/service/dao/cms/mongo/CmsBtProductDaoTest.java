package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lewis on 2016/2/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtProductDaoTest {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

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

    @Test
    public void testGetOnSaleProducts() throws Exception {
        List<CmsBtProductModel> products = cmsBtProductDao.select("{\"fields.code\" : \"51A0HC13E1-00LC#NB0\"}", "010");

        System.out.println(products.size());

        JongoQuery queryObject = new JongoQuery();
        //"_id" : 56e6b8d7eb99b2fdf432ddfe
        ObjectId objectId = new ObjectId("56e6b8d7eb99b2fdf432ddfe");
        queryObject.setObjectId(objectId);
        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryObject, "010");

        System.out.println(product);
    }

    @Test
    public void testUpdateFirst() throws Exception {
        String queryStr = "{\"$or\":[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[\"aa\"]},\"fields.translator\":{\"$in\":[null,\"\"]}},{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$in\":[\"123\"]},\"fields.translateTime\":{\"$lt\":\"2016-01-07 16:26:34\"}}]}";
        List lst = cmsBtProductDao.select(queryStr, "013");
        String strUpdate = "{\"$set\":{\"fields.translator\":\"liangchuanyu1\", \"fields.translateTime\":\"2016-01-07 16:26:36\"}}";
        WriteResult isresult = cmsBtProductDao.updateFirst(queryStr, strUpdate, "013");
        System.out.println(isresult);
    }

    @Test
    public void testDeleteSellerCat() throws Exception {

        CmsBtSellerCatModel football = new CmsBtSellerCatModel("018", 27, "201", "足球鞋1", "运动鞋1>足球鞋1", "101", "101-201", 0, new ArrayList<CmsBtSellerCatModel>());

        cmsBtProductDao.deleteSellerCat("018", football, 27, "will");

    }

    @Test
    public void testUpdateSellerCat() throws Exception {
        CmsBtSellerCatModel football = new CmsBtSellerCatModel("018", 27, "201", "足球鞋1", "运动鞋1>足球鞋1", "101", "101-201", 0, new ArrayList<CmsBtSellerCatModel>());
        CmsBtSellerCatModel shoe = new CmsBtSellerCatModel("018", 27, "101", "运动鞋1", "运动鞋1", "101", "101", 1, new ArrayList<CmsBtSellerCatModel>());

        ArrayList<CmsBtSellerCatModel> list = new ArrayList<CmsBtSellerCatModel>();
        list.add(shoe);
        list.add(football);
        cmsBtProductDao.updateSellerCat("018", list, 27, "will");
    }
}