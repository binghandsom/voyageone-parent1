package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016/5/26.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class SellerCatServiceTest {

    @Autowired
    SellerCatService sellerCatService;

    @Test
    public void testGetSellerCatsByChannelCart() throws Exception {

        List<CmsBtSellerCatModel> aa = sellerCatService.findNode("001",1,"Men's>Supra>Supra Bleeker");
        List<CmsBtSellerCatModel> result  = sellerCatService.getSellerCatsByChannelCart("017", 23, false);
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
        sellerCatService.deleteSellerCat("010",23, "1124130579", "1124130582", "ethan");

    }

    @Test
    public void testRefreshSellerCat() throws Exception {


        List<CmsBtSellerCatModel> result =  sellerCatService.refreshSellerCat("017", 23, "ethan");

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

    @Test
    public void testRefeshAllProduct() throws Exception {
        sellerCatService.refeshAllProduct("010", 23, "ethan");
    }

//    @Test
//    public void testIsDuplicateNode() throws Exception {
//        List<CmsBtSellerCatModel>  sellerCats = sellerCatService.getSellerCatsByChannelCart("010", 23, false);
//        CmsBtSellerCatModel node = sellerCats.stream().filter(w-> w.getCatId().equals("201")).findFirst().get();
//        System.out.println(JsonUtil.bean2Json(sellerCats));
//        System.out.println(sellerCatService.isDuplicateNode(sellerCats,"运动鞋","0"));
//        System.out.println(sellerCatService.isDuplicateNode(sellerCats,"足球鞋",node.getParentCatId()));
//
//
//    }

    @Test
    public void testPaixu() throws Exception {
        sellerCatService.doResetPlatformSellerCatIndex("010", 23);
    }

}