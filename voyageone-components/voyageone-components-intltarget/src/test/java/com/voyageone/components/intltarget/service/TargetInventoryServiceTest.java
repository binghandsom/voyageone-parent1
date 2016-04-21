package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.bean.inventory.TargetInventory;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryOrderRequestProduct;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryRequestProduct;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

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
        TargetInventoryRequest request=new TargetInventoryRequest();
        TargetInventoryOrderRequestProduct orderProduct=new TargetInventoryOrderRequestProduct();
        TargetInventoryRequestProduct product=new TargetInventoryRequestProduct();
        product.setChannel_id("ESTORE");
        product.setProduct_id("11296555");
        product.setLocation_ids("10,234");
        product.setIs_parent_id(false);
        product.setMultichannel_option("ship");
        product.setInventory_type("all");
        product.setSort("distance");
        product.setField_groups("full");
        product.setLimit(0);
        orderProduct.setProduct(product);
        orderProduct.setRequest_line_id(1);
        request.setProducts(Arrays.asList(orderProduct));
        TargetInventory targetInventory=targetInventoryService.getTargetInventory(request);
        System.out.println(JacksonUtil.bean2Json(targetInventory));
    }


}