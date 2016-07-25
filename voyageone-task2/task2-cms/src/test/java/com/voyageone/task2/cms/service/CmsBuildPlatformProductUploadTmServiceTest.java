package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desmond on 2016/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadTmServiceTest {

    @Autowired
    CmsBuildPlatformProductUploadTmService uploadTmService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformProductUploadTmJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("017");
        tcb.setTask_comment("天猫国际PortAmerican海外专营店上新允许运行的渠道");
        taskControlList.add(tcb);
        uploadTmService.onStartup(taskControlList);
    }

    @Test
    public void testUploadProduct() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(247);
        workload.setChannelId("017");
        workload.setCartId(23);
        workload.setGroupId(Long.parseLong("389857"));
        workload.setPublishStatus(0);

        ShopBean shopProp = Shops.getShop("017", "23");

        uploadTmService.uploadProduct(workload, shopProp);
    }
}
