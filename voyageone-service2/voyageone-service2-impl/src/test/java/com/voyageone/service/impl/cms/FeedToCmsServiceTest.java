package com.voyageone.service.impl.cms;

import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-12.
 * @version 2.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class FeedToCmsServiceTest {

    @Autowired
    private FeedCategoryTreeService feedCategoryTreeService;

    @Autowired
    private  FeedToCmsService  feedToCmsService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Test
    public void testGetFeedCategory() throws Exception {

        CmsBtFeedInfoModel cmsBtFeedInfoModel1 = feedInfoService.getProductByCode("001", "68220-gem");
        CmsBtFeedInfoModel cmsBtFeedInfoModel2 = feedInfoService.getProductByCode("001", "68220-lag");
        CmsBtFeedInfoModel cmsBtFeedInfoModel3 = feedInfoService.getProductByCode("001", "68243-bsa");

        cmsBtFeedInfoModel1.getSkus().addAll(cmsBtFeedInfoModel2.getSkus());
        cmsBtFeedInfoModel1.getSkus().addAll(cmsBtFeedInfoModel3.getSkus());
        cmsBtFeedInfoModel1.getSkus().get(0).setBarcode("11111");
        cmsBtFeedInfoModel1.getSkus().get(0).setPriceNet(100000.0);
        cmsBtFeedInfoModel1.getSkus().get(cmsBtFeedInfoModel1.getSkus().size()-1).setSku("james-sku-test1");
        cmsBtFeedInfoModel1.setCode("james-test");
        List<CmsBtFeedInfoModel> temp = feedToCmsService.getRelatedProducts("001",cmsBtFeedInfoModel1);
        System.out.println(temp.size());

//        CmsMtFeedCategoryTreeModel ret = feedToCmsService.getFeedCategory("013");
//
//        assert ret.getCategoryTree().size() > 0;
//
//        assert ret.getCategoryTree().get(0).getChild().size() > 0;
    }

//    @Test
//    public void testGetFinallyCategories() throws Exception {
//
//        List<Map<String,Object>> finallyCategories = feedToCmsService.getFinallyCategories("013");
//
//        assert finallyCategories.size() > 1;
//
//        List<Integer> isChildList = finallyCategories.stream().map(Map<String,Object>::getIsChild).distinct().collect(toList());
//
//        assert isChildList.size() == 1;
//
//        assert isChildList.get(0).equals(1);
//    }

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
////        p1.set(att);
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
////        p2.set(att);
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
//        feedToCmsService.updateProduct("010", products);
    }

    @Test
    public void testAddCategory() throws Exception {
        feedCategoryTreeService.addCategory("111","b-b-e","james");
        feedCategoryTreeService.addCategory("111","f-g-h","james");
        feedCategoryTreeService.addCategory("111","f-g-i","james");
    }

    @Test
    public void testCheckProduct() throws Exception {
        CmsBtFeedInfoModel cmsBtFeedInfoModel = new CmsBtFeedInfoModel();
        List<String> imgs = new ArrayList<>();
        imgs.add("aa");
        cmsBtFeedInfoModel.setImage(imgs);
        feedToCmsService.checkProduct(cmsBtFeedInfoModel);
    }
}