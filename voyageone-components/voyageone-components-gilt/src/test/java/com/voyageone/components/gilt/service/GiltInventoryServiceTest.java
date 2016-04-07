package com.voyageone.components.gilt.service;

import com.voyageone.components.gilt.bean.GiltInventory;
import com.voyageone.components.gilt.bean.GiltPageInventoryRequest;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class GiltInventoryServiceTest {

    @Autowired
    private GiltInventoryService giltInventoryService;


    @Test
    public void testInventoryBySkuId() throws Exception {
        GiltInventory giltInventory= giltInventoryService.getInventoryBySkuId("197647");
        System.out.println(JsonUtil.getJsonString(giltInventory));
    }

    @Test
    public void testPageGetInventories() throws Exception {
        GiltPageInventoryRequest request=new GiltPageInventoryRequest();
        String sku_ids="197653,356500";
//        request.setLimit(2);
//        request.setOffset(2);
        request.setSku_ids(sku_ids);
        List<GiltInventory> list= giltInventoryService.pageGetInventories(request);
        System.out.println(JsonUtil.getJsonString(list));
    }

}