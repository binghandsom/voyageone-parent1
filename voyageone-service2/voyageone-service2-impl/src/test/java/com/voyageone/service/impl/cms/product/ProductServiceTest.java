package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.model.cms.mongo.product.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/6/7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    public void testUpdateProductPlatform() throws Exception {

        CmsBtProductModel product = productService.getProductById("010", 2522623L);
        CmsBtProductModel_Platform_Cart platformCart =  new CmsBtProductModel_Platform_Cart();
        platformCart.setCartId(27);
        platformCart.setpCatId("50009522");
        platformCart.setpCatPath("尿片/洗护/喂哺/推车床>奶瓶/奶瓶相关>奶瓶");
        platformCart.setpCatStatus("1");
        platformCart.setStatus("Pending");
        platformCart.setpIsMain(1);
        platformCart.setpNumIId("1234567890");
        platformCart.setpPublishError("PublishError");
        platformCart.setpBrandId("1111");
        platformCart.setpBrandName("Munchkin/麦奇肯");
        platformCart.setpPublishTime("");
        platformCart.setpAttributeStatus("1");
        platformCart.setpAttributeSetter("ethan");
        platformCart.setpAttributeSetTime("");
        BaseMongoMap<String, Object> fieldsMap = new BaseMongoMap<>();
        fieldsMap.put("productNameCn", "麦奇肯奶瓶");
        fieldsMap.put("productNameEn", "Munchkin LATCH 1pk 8oz BPA Free Baby Bottle");
        fieldsMap.put("productLongName", "麦奇肯奶瓶-标题要长");
        fieldsMap.put("productMediumName", "麦奇肯奶瓶-标题要中");
        fieldsMap.put("productShortName", "麦奇肯奶瓶-标题要短");
        fieldsMap.put("beforeDate", "不明");
        fieldsMap.put("suitPeople", "12个月");
        fieldsMap.put("specialExplain", "无");
        fieldsMap.put("searchMetaTextCustom", "奶瓶");
        platformCart.setFields(fieldsMap);
        List<BaseMongoMap<String, Object>>  skus= new ArrayList<>();
        BaseMongoMap<String, Object> skuMap = new BaseMongoMap<>();
        skuMap.put("skuCode", "15183274");
        skuMap.put("priceSale", 10.0);
        skuMap.put("priceChgFlg", "U");
        skuMap.put("jmSpuNo", "");
        skuMap.put("jmSkuNo", "");
        skuMap.put("property", "OTHER 其他");
        skuMap.put("attribute", "蓝色");
        skuMap.put("size", "236毫升");


        BaseMongoMap<String, Object> skuMap1 = new BaseMongoMap<>();
        skuMap1.put("skuCode", "15183275");
        skuMap1.put("priceSale", 10.0);
        skuMap1.put("priceChgFlg", "U");
        skuMap1.put("jmSpuNo", "");
        skuMap1.put("jmSkuNo", "");
        skuMap1.put("property", "OTHER 其他");
        skuMap1.put("attribute", "红色");
        skuMap1.put("size", "236毫升");

        skus.add(skuMap);
        skus.add(skuMap1);

        platformCart.setSkus(skus);


        CmsBtProductModel_SellerCat sellerCat = new CmsBtProductModel_SellerCat();
        sellerCat.setcId("101");
        List<String> cIds = new ArrayList<>();
        cIds.add("1");
        cIds.add("101");
        sellerCat.setcIds(cIds);
        sellerCat.setcName("奶瓶");
        List<String> cNames = new ArrayList<>();
        cNames.add("喂养用品");
        cNames.add("喂养用品>奶瓶");
        sellerCat.setcNames(cNames);

        List<CmsBtProductModel_SellerCat> sellerCats = new ArrayList<>();
        sellerCats.add(sellerCat);

        platformCart.setSellerCats(sellerCats);

        productService.updateProductPlatform("010", 2522623L, platformCart,"test");






    }
}