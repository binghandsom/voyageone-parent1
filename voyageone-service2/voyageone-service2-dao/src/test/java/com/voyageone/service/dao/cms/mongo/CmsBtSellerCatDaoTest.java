package com.voyageone.service.dao.cms.mongo;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
        List<CmsBtSellerCatModel> cmsBtSellerCatModel = cmsBtSellerCatDao.selectByChannelCart("1", 1);
        System.out.println(cmsBtSellerCatModel);
    }

    @Test
    public void testInsert() throws Exception {

        CmsBtSellerCatModel football = new CmsBtSellerCatModel("010", 20, "201", "足球鞋", "运动鞋->足球鞋", "101", "101-201", 0, new ArrayList<CmsBtSellerCatModel>());
        CmsBtSellerCatModel basketball = new CmsBtSellerCatModel("010", 20, "202", "篮球鞋", "运动鞋->篮球鞋", "101", "101-202", 0, new ArrayList<CmsBtSellerCatModel>());
        List<CmsBtSellerCatModel> list = new ArrayList<CmsBtSellerCatModel>();
        list.add(football);
        list.add(basketball);
        CmsBtSellerCatModel root = new CmsBtSellerCatModel("010", 20, "101", "运动鞋", "运动鞋", "0", "101", 1, list);
        cmsBtSellerCatDao.insert(root);

        CmsBtSellerCatModel ring = new CmsBtSellerCatModel("010", 20, "211", "戒指", "首饰->戒指", "102", "102-211", 0, new ArrayList<CmsBtSellerCatModel>());
        CmsBtSellerCatModel bracelet = new CmsBtSellerCatModel("010", 20, "212", "手镯", "首饰->手镯", "102", "102-212", 0, new ArrayList<CmsBtSellerCatModel>());
        List<CmsBtSellerCatModel> list1 = new ArrayList<CmsBtSellerCatModel>();
        list1.add(ring);
        list1.add(bracelet);
        CmsBtSellerCatModel root1 = new CmsBtSellerCatModel("010", 20, "102", "首饰", "首饰", "0", "102", 1, list1);
        cmsBtSellerCatDao.insert(root1);

    }

    @Test
    public void testDelete() throws Exception {
        CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel();
        cmsBtSellerCatModel.set_id("573edbbbaacaa23118390d8e");
        cmsBtSellerCatDao.delete(cmsBtSellerCatModel);
    }


    @Test
    public void testUpdate() throws Exception {

       cmsBtSellerCatDao.update("010", 20, "耳钉", "212");
    }
}