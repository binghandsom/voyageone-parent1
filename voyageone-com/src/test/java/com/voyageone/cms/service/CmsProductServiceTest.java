package com.voyageone.cms.service;


import com.mongodb.WriteResult;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsProductServiceTest {
    @Autowired
    CmsProductService cmsProductService;

    @Test
    public void testInsertCmsBtProduct() throws Exception {
        CmsBtProductModel productModel = create("001", 1, new Random());
        WriteResult result = cmsProductService.insert(productModel);
        System.out.println(result);
    }

    private CmsBtProductModel create(String channelId, int index, Random random) {
        CmsBtProductModel product = new CmsBtProductModel(channelId);
        product.setProdId(index);
        String catId = String.valueOf(random.nextInt(1000));
        product.setCatId(catId);
        product.setCatIdPath("-100-10000-" + catId + "-");
        String code = String.valueOf(100000 + index);
        CmsBtProductModel_Field fields = product.getFields();
        fields.setCode(code);
        fields.setAttribute("name", "Stud Earrings with Cubic Zirconia in Sterling Silver " + code);
        fields.setColor("Color" + random.nextInt(100));
        fields.setOrigin("china" + random.nextInt(10));
        fields.setYear((2000 + random.nextInt(15)));
        fields.setSeason("summer" + random.nextInt(10));
        fields.setMaterial1("silver" + random.nextInt(10));
        fields.setMaterial2("silver" + random.nextInt(10));
        fields.setMaterial3("silver" + random.nextInt(10));
        fields.setMsrpStart(100.00 + random.nextInt(100));
        fields.setMsrpEnd(200.00 + random.nextInt(100));
        fields.setRetailPriceStart(300.00 + random.nextInt(100));
        fields.setRetailPriceEnd(400.00 + random.nextInt(100));
        fields.setSalePriceStart(500.00 + random.nextInt(100));
        fields.setSalePriceEnd(600.00 + random.nextInt(100));
        fields.setCurrentPriceStart(700.00 + random.nextInt(100));
        fields.setCurrentPriceEnd(800.00 + random.nextInt(100));
        fields.setStatus("pedding");
        fields.setBrand("Jewelry" + random.nextInt(10));
        fields.setSizeType("Men" + random.nextInt(5));
        fields.setInventory(100 + random.nextInt(10));
        fields.setLock(index % 2 == 0);
        fields.setPriceChange(random.nextInt(1));
        List<CmsBtProductModel_Field_Image> images = fields.getImages1();
        images.add(new CmsBtProductModel_Field_Image("xxxxx-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("xxxxx-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("xxxxx-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("xxxxx-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("xxxxx-" + random.nextInt(10) + ".jpg"));

        images = fields.getImages2();
        images.add(new CmsBtProductModel_Field_Image("yyyyy-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("yyyyy-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("yyyyy-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("yyyyy-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("yyyyy-" + random.nextInt(10) + ".jpg"));

        images = fields.getImages3();
        images.add(new CmsBtProductModel_Field_Image("zzzzz-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("zzzzz-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("zzzzz-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("zzzzz-" + random.nextInt(10) + ".jpg"));
        images.add(new CmsBtProductModel_Field_Image("zzzzz-" + random.nextInt(10) + ".jpg"));

        CmsBtProductModel_Group groups = product.getGroups();
        groups.setMsrpStart(100.00 + random.nextInt(100));
        groups.setMsrpEnd(200.00 + random.nextInt(100));
        groups.setRetailPriceStart(300.00 + random.nextInt(100));
        groups.setRetailPriceEnd(400.00 + random.nextInt(100));
        groups.setSalePriceStart(500.00 + random.nextInt(100));
        groups.setSalePriceEnd(600.00 + random.nextInt(100));
        groups.setCurrentPriceStart(700.00 + random.nextInt(100));
        groups.setCurrentPriceEnd(800.00 + random.nextInt(100));

        List<CmsBtProductModel_Group_Platform> platforms = groups.getPlatforms();
        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();
        platform.setGroupId(random.nextInt(1000));
        platform.setCartId(21);
        platform.setNumIId(String.valueOf(2000000 + random.nextInt(1000)));
        platform.setIsMain(false);
        platform.setDisplayOrder(random.nextInt(100));
        platform.setPublishTime("2015-11-12 16:19:00");
        platform.setInstockTime("2015-11-18 16:19:00");
        platform.setStatus("InStock");
        platform.setPublishStatus("等待上新");
        platform.setComment("");
        platform.setInventory(random.nextInt(100));
        platforms.add(platform);

        platform = new CmsBtProductModel_Group_Platform(platform);
        platform.setGroupId(random.nextInt(1000));
        platform.setCartId(23);
        platforms.add(platform);

        List<CmsBtProductModel_Sku> skus = product.getSkus();
        for (int i=1; i<3+random.nextInt(5); i++) {
            CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
            sku.setSku(code + "-" + i);
            sku.setUpc("1234567890"+(100+random.nextInt(100)));
            sku.setPriceMsrp(100.00 + random.nextInt(100));
            sku.setPriceRetail(300.00 + random.nextInt(100));
            sku.setPriceSale(800.00 + random.nextInt(100));
            sku.setPlatformArrCode("1100");
            skus.add(sku);
        }

        CmsBtProductModel_Feed feed = product.getFeedAtts();
        feed.setAttribute("washingtype", "dry cleaning");
        feed.setAttribute("collar", "mandarin collar");
        feed.setAttribute("style", "campus");
        feed.setAttribute("waspe", "dleaning");

        return product;
    }

    @Test
    public void testSelectCmsBtProductById() throws Exception {
        CmsBtProductModel ret = cmsProductService.getProductById("001", 1);
        System.out.println(ret.toString());
        System.out.println(ret.getGroups().getCurrentPriceEnd());
    }

    @Test
    public void testSelectCmsBtProductByCode() throws Exception {
        CmsBtProductModel ret = cmsProductService.getProductByCode("001", "00341");
        System.out.println(ret.toString());
        System.out.println(ret.getGroups().getCurrentPriceEnd());
    }

    @Test
    public void testSelectCmsBtProductByGroupId() throws Exception {
        List<CmsBtProductModel> listRet = cmsProductService.getProductByGroupId("001", 470);
        for (CmsBtProductModel ret : listRet) {
            System.out.println(ret.toString());
            System.out.println(ret.getGroups().getPlatformByGroupId(470));
        }
    }

//    @Test
//    public void testSelectSKUById() throws Exception {
//        List<CmsBtProductModel_Sku> listRet = cmsProductService.selectSKUById("001", 1);
//        for (CmsBtProductModel_Sku ret : listRet) {
//            System.out.println(ret.toString());
//        }
//    }

    @Test
    public void testInsert10W() {
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> lst = new ArrayList<>();
        int index = 0;
        for(int i=1; i<=100000; i++) {
            CmsBtProductModel productModel = create("200", i, new Random());
            lst.add(productModel);
            index++;
            if (i%1000 == 0) {
                System.out.println("current count:=" + index);
                cmsProductService.insert(lst);
                lst.clear();
            }
        }
        if (lst.size()>0) {
            cmsProductService.insert(lst);
        }
        long total = System.currentTimeMillis()-start;
        System.out.println("total count:=" + index + "; totalTime:="+total);
    }

    @Test
    public void remove10W(){
        long start = System.currentTimeMillis();
        cmsProductService.removeAll("101");
        long total = System.currentTimeMillis()-start;
        System.out.println("total totalTime:=" + total);
    }

    @Test
    public void testSelectCmsBtProductByIdOnlyProdId() throws Exception {
        JSONObject ret = cmsProductService.getProductByIdWithJson("001", 1);
        System.out.println(ret.toString());
    }
}