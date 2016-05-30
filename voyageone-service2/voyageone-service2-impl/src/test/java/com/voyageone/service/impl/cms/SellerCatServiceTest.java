package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/5/26.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SellerCatServiceTest {

    @Autowired
    SellerCatService sellerCatService;

    @Test
    public void testGetSellerCatsByChannelCart() throws Exception {

        List<CmsBtSellerCatModel> result  = sellerCatService.getSellerCatsByChannelCart("010", 23, false);
        System.out.println(JsonUtil.bean2Json(result));

    }

    @Test
    public void testAddSellerCat() throws Exception {

    }

    @Test
    public void testUpdateSellerCat() throws Exception {
        sellerCatService.updateSellerCat("010", 23 , "新系列测试", "1124130579" , "ethan");

    }

    @Test
    public void testDeleteSellerCat() throws Exception {
        sellerCatService.deleteSellerCat("010",23, "1124130579", "1124130582");

    }

    @Test
    public void testRefreshSellerCat() throws Exception {

        List<CmsBtSellerCatModel> result =  sellerCatService.refreshSellerCat("010", 23, "test");
        System.out.println(JsonUtil.bean2Json(result));

        sellerCatService.save(result);


    }

    @Test
    public void testGetSellerCatConfig() throws Exception {

        Map map = sellerCatService.getSellerCatConfig(23);
        System.out.println(JsonUtil.bean2Json(map));
        Map map2 =sellerCatService.getSellerCatConfig(24);
        System.out.println(JsonUtil.bean2Json(map2));

    }
}