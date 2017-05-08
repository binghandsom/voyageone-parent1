package com.voyageone.task2.cms.service;

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
 * Created by Charis on 2017/3/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformAttributeUpdateJdServiceTest {

    @Autowired
    private CmsBuildPlatformAttributeUpdateJdService attributeUpdateJdService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformAttributeUpdateJdJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("001");
        tcb.setTask_comment("京东系允许运行的渠道");
        taskControlList.add(tcb);
        attributeUpdateJdService.onStartup(taskControlList);
    }

    @Test
    public void testDoJdShopCategoryUpdate() throws Exception {

        CmsBtSxWorkloadModel work = new CmsBtSxWorkloadModel();

        work.setChannelId("001");
        work.setCartId(26);
        work.setGroupId(10007685L);
        work.setWorkloadName("jd_skuId");

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey("");
        shopBean.setOrder_channel_id("001");
        shopBean.setCart_id("26");
        attributeUpdateJdService.doJdAttributeUpdate(work, shopBean);
    }
}
