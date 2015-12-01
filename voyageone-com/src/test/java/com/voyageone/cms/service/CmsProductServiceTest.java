package com.voyageone.cms.service;


import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
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
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testInsertCmsBtProduct() throws Exception {
        CmsBtProductModel productModel = create();
        cmsBtProductDao.insert(productModel);
    }

    private CmsBtProductModel create() {
        CmsBtProductModel product = new CmsBtProductModel("001");
        product.setProdId(1);
        product.setCid(111112);
        product.setCidPath("-100001-111111-111112-");
        product.getField().setCode("00342");
        String aa = product.getField().getCode();
        //product.setFieldAtt("code", "00341");
        product.getField().setAttribute("name", "Stud Earrings with Cubic Zirconia in Sterling Silver");
        product.getField().setColor("red");
        product.getField().setAttribute("origin", "china");
        product.getField().setAttribute("year", "2015");
        product.getField().setAttribute("season", "summer");
        product.getField().setAttribute("material1", "silver");
        product.getField().setAttribute("material2", "silver");
        product.getField().setAttribute("material3", "silver");
        product.getField().setAttribute("msrpStart", "2500.00");
        product.getField().setAttribute("msrpEnd", 2099.00);
        product.getField().setAttribute("retailPriceStart", 2000.00);
        product.getField().setAttribute("retailPriceENd", 2099.00);
        product.getField().setAttribute("salePriceStart", 1899.00);
        product.getField().setAttribute("salePriceEnd", 2089.00);
        product.getField().setAttribute("currentPriceStart", 1099.00);
        product.getField().setAttribute("currentPriceEnd", 1289.00);
        product.getField().setAttribute("status", "pedding");
        product.getField().setAttribute("brand", "Jewelry");
        product.getField().setAttribute("sizeType", "Men's");
        product.getField().setAttribute("inventory", "50");

        List<Map<String, Object>> images = new ArrayList<>();
        Map<String, Object> image = new HashMap<>();
        image.put("name", "xxxxx-1.jpg");
        image.put("type", 1);
        images.add(image);
        image = new HashMap<>(image);
        image.put("name", "xxxxx-2.jpg");
        images.add(image);
        image = new HashMap<>(image);
        image.put("name", "xxxxx-3.jpg");
        image.put("type", 2);
        images.add(image);
        product.getField().setAttribute("images", images);

        product.getField().setAttribute("lock", false);
        product.getField().setAttribute("priceChange", 0);
        product.getField().setAttribute("updateTime", "2015-05-21 00:00:00");
        product.getField().setAttribute("updater", "2015-08-21 00:00:00");
        product.getField().setAttribute("createTime", "2015-05-21 00:00:00");
        product.getField().setAttribute("creater", "2015-08-21 00:00:00");

        product.getGroup().setMsrpStart(2500.00);
        product.getGroup().setAttribute("msrpEnd", 2099.00);
        product.getGroup().setAttribute("retailPriceStart", 2000.00);
        product.getGroup().setAttribute("retailPriceENd", 2099.00);
        product.getGroup().setAttribute("salePriceStart", 1899.00);
        product.getGroup().setAttribute("salePriceEnd", 2089.00);
        product.getGroup().setAttribute("currentPriceStart", 1099.00);
        product.getGroup().setAttribute("currentPriceEnd", 1289.00);

        List<CmsBtProductModel_Group_Platform> platforms = product.getGroup().getPlatforms();
        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();
        platform.setGroupId(1);
        platform.put("cartId", "21");
        platform.put("numIid", "2076132");
        platform.put("isMain", 1);
        platform.put("displayOrder", 1);
        platform.put("publishTime", "2015-11-12 16:19:00");
        platform.put("instockTime", "2015-11-18 16:19:00");
        platform.put("status", "InStock");
        platform.put("publishStatus", "等待上新");
        platform.put("comment", "");
        platform.put("inventory", 50);
        platforms.add(platform);

        platform = new CmsBtProductModel_Group_Platform(platform);
        platform.put("groupId", 2);
        platform.put("cartId", "23");
        platforms.add(platform);

        List<CmsBtProductModel_Sku> skus = product.getSkus();
        CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
        sku.setSku("123456-001-7");
        sku.put("upc", "1234567890123");
        sku.put("priceMsrp", 1089.00);
        sku.put("priceRetail", 1089.00);
        sku.put("priceSale", 899.00);
        sku.put("platform", 899.00);
        skus.add(sku);
        sku = new CmsBtProductModel_Sku(sku);
        sku.put("sku", "123456-001-9");
        skus.add(sku);
        sku = new CmsBtProductModel_Sku(sku);
        sku.put("sku", "123456-001-10");
        skus.add(sku);
        product.setSkus(skus);

        //product.setTags(new ArrayList<>());

        product.getFeedAtts().setAttribute("washingtype", "dry cleaning");
        product.getFeedAtts().setAttribute("collar", "mandarin collar");
        product.getFeedAtts().setAttribute("style", "campus");
        product.getFeedAtts().setAttribute("waspe", "dleaning");

        return product;
    }

    @Test
    public void testSelectCmsBtProductById() throws Exception {
        CmsBtProductModel ret = cmsBtProductDao.selectProductById("001", 1);
        System.out.println(ret.toString());
        System.out.println(ret.getGroup().getCurrentPriceEnd());
    }

    @Test
    public void testSelectCmsBtProductByCode() throws Exception {
        CmsBtProductModel ret = cmsBtProductDao.selectProductByCode("001", "00341");
        System.out.println(ret.toString());
        System.out.println(ret.getGroup().getCurrentPriceEnd());
    }

    @Test
    public void testSelectCmsBtProductByGroupId() throws Exception {
        List<CmsBtProductModel> listRet = cmsBtProductDao.selectProductByGroupId("001", 1);
        for (CmsBtProductModel ret : listRet) {
            System.out.println(ret.toString());
            System.out.println(ret.getGroup().getCurrentPriceEnd());
        }
    }

    @Test
    public void testSelectSKUById() throws Exception {
        List<CmsBtProductModel_Sku> listRet = cmsBtProductDao.selectSKUById("001", 1);
        for (CmsBtProductModel_Sku ret : listRet) {
            System.out.println(ret.toString());
        }
    }
}