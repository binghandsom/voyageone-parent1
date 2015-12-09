package com.voyageone.cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class FeedToCmsServiceTest {
    @Autowired
    FeedToCmsService feedToCmsService;
    @Test
    public void testGetFeedCategory() throws Exception {
//        CmsMtFeedCategoryTreeModel ret = feedToCmsService.getFeedCategory("010");
//        System.out.println(JsonUtil.getJsonString(ret));
    }

    @Test
    public void testSetFeedCategory() throws Exception {
        List<Map> tree = new ArrayList<>();
        Map o = new HashMap<>();

        List<Map> child = new ArrayList<>();
        Map b = new HashMap();
        b.put("category","dd");
        child.add(b);
        Map c = new HashMap();
        c.put("category", "c");
        child.add(c);
        o.put("ategory","a");
        o.put("child",child);
        tree.add(o);

//        feedToCmsService.setFeedCategory("012",tree);
    }

    @Test
    public void testFindCategory() throws Exception {
//        CmsMtFeedCategoryTreeModel ret = feedToCmsService.getFeedCategory("013");
//        Map child =  feedToCmsService.findCategory(ret, "Home-Kitchen");
//        Map child =  feedToCmsService.findCategory(ret.getCategoryTree(), "Clothing-Kids' Apparel-Boys");
    }

    @Test
    public void testAddCategory() throws Exception {
//        CmsMtFeedCategoryTreeModel tree = feedToCmsService.getFeedCategory("013");
//        feedToCmsService.addCategory(tree.getCategoryTree(),"Clothing-Kids' Apparel-Boys");
//        feedToCmsService.setFeedCategory(tree);
    }

    @Test
    public void testUpdateProduct() throws Exception {
//        List<FeedProductModel> products = new ArrayList<>();
//        FeedProductModel p1= new FeedProductModel("010");
//        p1.setCategory("Home-voyageone-james");
//        p1.setBrand("voyage");
//        p1.setCode("james");
//        p1.setColor("red");
//        p1.setLong_description("long_description");
//        p1.setShort_description("short_description");
//        p1.setModel("james-red");
//        p1.setName("james");
//        List<String> img = new ArrayList<>();
//        img.add("http://ssdsf/1.jpg");
//        img.add("http://ssdsf/2.jpg");
//        p1.setImage(img);
//
//        Map att = new HashMap<>();
//        att.put("a","1");
//        att.put("b", "2");
////        p1.setAttribute(att);
//
//        List<FeedSkuModel> skus = new ArrayList<>();
//        FeedSkuModel sku = new FeedSkuModel();
//        sku.setBarcode("111111");
//        sku.setClientSku("");
//        sku.setPrice_current(13.01);
//        sku.setPrice_msrp(13.01);
//        skus.add(sku);
//
//        p1.setSkus(skus);
//
//        products.add(p1);
//
//        FeedProductModel p2= new FeedProductModel("010");
//        p2.setCategory("Home-voyageone-lijun");
//        //p2.setChannelId("010");
//        p2.setBrand("voyage");
//        p2.setCode("lijun");
//        p2.setColor("blue");
//        p2.setLong_description("long_description");
//        p2.setShort_description("short_description");
//        p2.setModel("james-blue");
//        p2.setName("james");
//        img = new ArrayList<>();
//        img.add("http://ssdsf/3.jpg");
//        img.add("http://ssdsf/4.jpg");
//        p2.setImage(img);
//
//        att = new HashMap<>();
//        att.put("a","1");
//        att.put("b", "2");
////        p2.setAttribute(att);
//
//        skus = new ArrayList<>();
//        sku = new FeedSkuModel();
//        sku.setBarcode("111111");
//        sku.setClientSku("");
//        sku.setPrice_current(14.01);
//        sku.setPrice_msrp(14.01);
//        skus.add(sku);
//
//        p2.setSkus(skus);
//
//        products.add(p2);
//        feedToCmsService.updateProduct("010",products);

    }

//    @Test
//    public void testGetFinallyCategories() throws Exception {
//        List<Map> ret = feedToCmsService.getFinallyCategories("013");
//
//    }
}