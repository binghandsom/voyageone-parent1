package com.voyageone.service.impl.wms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/16 16:43
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class InventoryCenterLogicServiceTest {


    @Autowired
    InventoryCenterLogicService inventoryCenterLogicService;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Test
    public void getCodeStockDetails() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter();

        WmsCodeStoreInvBean result = inventoryCenterLogicService.getCodeStockDetails("001", "332148-008");
        System.out.println(mapper.writeValueAsString(result));
    }

}