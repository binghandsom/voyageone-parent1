package com.voyageone.cms.service;

import com.voyageone.cms.service.dao.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.FeedProductModel;
import com.voyageone.cms.service.model.FeedSkuModel;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by james.li on 2015/11/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsProductServiceTest {
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testSaveCmsBtProduct() throws Exception {
        CmsBtProductModel productModel = create();
        cmsBtProductDao.save(productModel);
    }

    private CmsBtProductModel create() {
        CmsBtProductModel product = new CmsBtProductModel("c001");
        product.setProdId(1);
        product.setCid(111112);
        product.setCidPath("-100001-111111-111112-");
        product.getField().setCode("00341");
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
        product.getField().setAttribute("price_change", 0);
        product.getField().setAttribute("updateTime", "2015-05-21 00:00:00");
        product.getField().setAttribute("updater", "2015-08-21 00:00:00");
        product.getField().setAttribute("createTime", "2015-05-21 00:00:00");
        product.getField().setAttribute("creater", "2015-08-21 00:00:00");

        product.setGroupAtt("msrpStart", 2500.00);
        product.setGroupAtt("msrpEnd", 2099.00);
        product.setGroupAtt("retailPriceStart", 2000.00);
        product.setGroupAtt("retailPriceENd", 2099.00);
        product.setGroupAtt("salePriceStart", 1899.00);
        product.setGroupAtt("salePriceEnd", 2089.00);
        product.setGroupAtt("currentPriceStart", 1099.00);
        product.setGroupAtt("currentPriceEnd", 1289.00);

        List<Map<String, Object>> platforms = new ArrayList<>();
        Map<String, Object> platform = new LinkedHashMap<>();
        platform.put("groupId", 1);
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

        platform = new LinkedHashMap<>(platform);
        platform.put("groupId", 2);
        platform.put("cartId", "23");
        platforms.add(platform);
        product.setGroupAtt("platforms", platforms);

        List<Map<String, Object>> skus = new ArrayList<>();
        Map<String, Object> sku = new LinkedHashMap<>();
        sku.put("sku", "123456-001-7");
        sku.put("upc", "1234567890123");
        sku.put("price_msrp", 1089.00);
        sku.put("price_retail", 1089.00);
        sku.put("price_sale", 899.00);
        sku.put("platform", 899.00);
        skus.add(sku);
        sku = new LinkedHashMap<>(sku);
        sku.put("sku", "123456-001-9");
        skus.add(sku);
        sku = new LinkedHashMap<>(sku);
        sku.put("sku", "123456-001-10");
        skus.add(sku);
        product.setSkus(skus);

        product.setTags(new ArrayList<>());

        product.setFeedAtt("washingtype", "dry cleaning");
        product.setFeedAtt("collar", "mandarin collar");
        product.setFeedAtt("style", "campus");
        product.setFeedAtt("waspe", "dleaning");

        return product;
    }

    @Test
    public void testGetCmsBtProduct() throws Exception {
        CmsBtProductModel ret = cmsBtProductDao.getProductByCode("010", "c001");
        System.out.println(JsonUtil.getJsonString(ret));
    }



}