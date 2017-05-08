package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Charis on 2017/4/7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformAttributeUpdateJmServiceTest {

    @Autowired
    private CmsBuildPlatformAttributeUpdateJmService cmsBuildPlatformAttributeUpdateJmService;


    @Test
    public void testDoJmTitleUpdate() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setChannelId("012");
        workload.setCartId(27);
        workload.setGroupId(Long.parseLong("978142"));   //bcbg
        workload.setPublishStatus(0);
        workload.setWorkloadName("title");

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey("");
        shopBean.setOrder_channel_id("001");
        shopBean.setCart_id("26");
//        workload.setModified();
        cmsBuildPlatformAttributeUpdateJmService.doJmAttributeUpdate(workload, shopBean);
    }




}
