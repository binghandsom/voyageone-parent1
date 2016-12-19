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

    @Autowired
    CmsBatchSetMainCategoryMqService cmsBatchSetMainCategoryMqService;
    @Test
    public void onStartup() throws Exception {
        String json = "{\"prodIds\":[\"SJ9020SZW\",\"01411YAA\"],\"catId\":\"34a6e076d631eee902547f8b2e8f5405\",\"catPath\":\"珠宝饰品>Bracelets & Anklets>Anklets\",\"pCatList\":[],\"isSelAll\":0,\"userName\":\"james\",\"channelId\":\"010\"}";
        Map<String, Object> param = JacksonUtil.jsonToMap(json);
        cmsBatchSetMainCategoryMqService.onStartup(param);

    }

}