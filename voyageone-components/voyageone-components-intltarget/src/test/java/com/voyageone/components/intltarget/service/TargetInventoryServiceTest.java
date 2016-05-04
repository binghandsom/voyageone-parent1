package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryRequest;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TargetInventoryServiceTest {

    @Autowired
    private TargetInventoryService targetInventoryService;

    @Test
    public void testGetTargetInventory() throws Exception {
        TargetInventoryRequest request = new TargetInventoryRequest();
        request.setProduct_id("11204568");
        TargetInventoryResponse response = targetInventoryService.getTargetInventory(request);
        System.out.println(JacksonUtil.bean2Json(response));
    }
}