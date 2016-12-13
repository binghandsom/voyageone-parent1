package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.BaseJongoPartTemplate;
import com.voyageone.base.dao.mongodb.BaseJongoTemplate;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtCAdProductDao;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/6/28.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsFeedExportServiceTest {

    @Autowired
    CmsFeedExportService cmsFeedExportService;
    @Autowired
    CmsBtExportTaskService cmsBtExportTaskService;
//    @Autowired
//    temp temp;

    @Test
    public void testOnStartup() throws Exception {

        List<String> s = new ArrayList<>(Arrays.asList("aa","bb","cc"));
        s.remove("bb");
        List<CmsBtExportTaskModel> cmsBtExportTaskModels = cmsBtExportTaskService.getExportTaskByUser("010", CmsBtExportTaskService.FEED, "james", 0, 10);

        cmsFeedExportService.onStartup(JacksonUtil.jsonToMap(JacksonUtil.bean2Json(cmsBtExportTaskModels.get(0))));
    }
    @Test
    public void test2() throws Exception {

//        temp.test2();

    }

//    @Service
//    public class temp {
//
//        @Autowired
//        MongoTemplate mongoTemplate;
//
//        public void test2() throws Exception {
//
//            FileInputStream inputStream = new FileInputStream(new File("D:\\Users\\Administrator\\Downloads\\products.json"));
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            StringBuffer stringBuffer = new StringBuffer();
//            BufferedReader br = new BufferedReader(inputStreamReader);//
//
//            String aa = br.readLine();
//
//            List<Map<String,Object>> jsonMap = JacksonUtil.jsonToMapList(aa);
//
//            jsonMap.forEach(string->mongoTemplate.insert(string, "product_c032"));
//            System.out.println(jsonMap.size());
//
//        }
//    }
}