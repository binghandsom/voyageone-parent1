package com.voyageone.task2.cms.service;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/12/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBatchSetMainCategoryMqServiceTest {
    @Test
    public void getSizeType() throws Exception {

        cmsBatchSetMainCategoryMqService.getSizeType("girls");
    }


    @Test
    public void onStartup1() throws Exception {

    }

    @Autowired
    CmsBatchSetMainCategoryMqService cmsBatchSetMainCategoryMqService;
    @Test
    public void onStartup() throws Exception {
        String json = "{\"prodIds\":[\"138043\"],\"catId\":\"afb1219772e93efad29b1b3b0b022f8c\",\"catPath\":\"鞋靴>男鞋>运动鞋>篮球鞋>高帮鞋\",\"catPathEn\":\"Shoes>Men's>Athletic>Basketball>High-Tops\",\"pCatList\":[],\"productType\":\"shoes\",\"sizeType\":\"men\",\"productTypeCn\":\"鞋子\",\"sizeTypeCn\":\"男士\",\"hscodeName8\":\"06029900,高帮鞋,双\",\"hscodeName10\":\"\",\"isSelAll\":0,\"userName\":\"james\",\"channelId\":\"928\"}";
        Map<String, Object> param = JacksonUtil.jsonToMap(json);
        cmsBatchSetMainCategoryMqService.onStartup(param);

    }

}