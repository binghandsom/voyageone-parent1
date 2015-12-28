package com.voyageone.batch.wms.service;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.modelbean.InventoryForCmsBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2015/12/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-wms-test.xml")
public class WmsSynInventoryToCmsServiceTest {
    @Autowired
    private WmsSynInventoryToCmsService wmsSynInventoryToCmsService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> tasks = new ArrayList<>();
        TaskControlBean task = new TaskControlBean();

        task.setCfg_name(TaskControlEnums.Name.order_channel_id.toString());
        task.setCfg_val1("001");
        tasks.add(task);
        wmsSynInventoryToCmsService.onStartup(tasks);
    }

    @Test
    public void testBulkUpdateCodeQty() throws Exception {
        List<InventoryForCmsBean> inventoryForCmsBeans = new ArrayList<>();
        InventoryForCmsBean inventory = new InventoryForCmsBean();
        inventory.setCode("100001");
        inventory.setQty(41);
        inventoryForCmsBeans.add(inventory);

        inventory = new InventoryForCmsBean();
        inventory.setCode("100002");
        inventory.setQty(50);
        inventoryForCmsBeans.add(inventory);
        wmsSynInventoryToCmsService.bulkUpdateCodeQty("013",inventoryForCmsBeans,"james");
    }
}