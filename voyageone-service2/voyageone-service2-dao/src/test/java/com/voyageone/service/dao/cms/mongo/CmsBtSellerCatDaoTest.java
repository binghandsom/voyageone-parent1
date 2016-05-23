package com.voyageone.service.dao.cms.mongo;

import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/5/20.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsBtSellerCatDaoTest {

    @Autowired
    CmsBtSellerCatDao cmsBtSellerCatDao;

    @Test
    public void testSelectByChannelCart() throws Exception {
        List<CmsBtSellerCatModel> cmsBtSellerCatModel = cmsBtSellerCatDao.selectByChannelCart("1",1);
        System.out.println(cmsBtSellerCatModel);
    }

    @Test
    public void testInsert() throws Exception {
        CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel("1",1, "111", "test", "1>1>1", "0", "dd", 0, null);
        cmsBtSellerCatDao.insert(cmsBtSellerCatModel);

    }

    @Test
    public void testDelete() throws Exception {
        CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel();
        cmsBtSellerCatModel.set_id("573edbbbaacaa23118390d8e");
        cmsBtSellerCatDao.delete(cmsBtSellerCatModel);
    }

    @Test
    public void testUpdate() throws Exception {

    }
}