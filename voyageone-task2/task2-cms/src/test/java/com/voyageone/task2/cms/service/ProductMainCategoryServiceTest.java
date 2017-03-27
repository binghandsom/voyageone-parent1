package com.voyageone.task2.cms.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.product.ProductMainCategoryService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchSetMainCategoryMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by james on 2016/12/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ProductMainCategoryServiceTest {
    @Autowired
    ProductMainCategoryService productMainCategoryService;

    @Test
    public void getSizeType() throws Exception {

    }

    @Test
    public void onStartup1() throws Exception {

    }

    @Test
    public void onStartup() throws Exception {
        String json = "{\"prodoctCodes\":[\"138043\"],\"catId\":\"afb1219772e93efad29b1b3b0b022f8c\",\"catPath\":\"鞋靴>男鞋>运动鞋>篮球鞋>高帮鞋\",\"catPathEn\":\"Shoes>Men's>Athletic>Basketball>High-Tops\",\"pCatList\":[],\"productType\":\"shoes\",\"sizeType\":\"men\",\"productTypeCn\":\"鞋子\",\"sizeTypeCn\":\"男士\",\"hscodeName8\":\"06029900,高帮鞋,双\",\"hscodeName10\":\"\",\"isSelAll\":0,\"sender\":\"james\",\"channelId\":\"928\"}";

        CmsBatchSetMainCategoryMQMessageBody messageBody = JacksonUtil.json2Bean(json, CmsBatchSetMainCategoryMQMessageBody.class);
        productMainCategoryService.setMainCategory(messageBody);

    }

}