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
    public void onStartup1() throws Exception {

    }

    @Autowired
    CmsBatchSetMainCategoryMqService cmsBatchSetMainCategoryMqService;
    @Test
    public void onStartup() throws Exception {
        String json = "{\"prodIds\":[\"01411YAA\"],\"catId\":\"849b471c5cb15340eba958625579fbdc\",\"catPath\":\"服饰>少男少女及儿童服饰>女童装（0～24个月）>服饰套装\",\"catPathEn\":\"Clothing>Kids' & Baby>Baby Girls (0-24 Months)>Clothing Sets\",\"pCatList\":[],\"productType\":\"Baby Costume\",\"sizeType\":\"Baby Girls\",\"productTypeCn\":\"婴儿服饰\",\"sizeTypeCn\":\"女童\",\"isSelAll\":0,\"userName\":\"james\",\"channelId\":\"010\"}";
        Map<String, Object> param = JacksonUtil.jsonToMap(json);
        cmsBatchSetMainCategoryMqService.onStartup(param);

    }

}