package com.voyageone.task2.cms.service;

import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Charis on 2017/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformAttributeUpdateTmTongGouServiceTest {



    @Autowired
    private CmsBuildPlatformAttributeUpdateTmTongGouService cmsBuildPlatformAttributeUpdateTmTongGouService;

    @Test
    public void testDoTmAttributeUpdate() throws Exception{
        // "seller_cids", "wireless_desc", "title", "white_bg_image", "description", "sell_points"
        // "main_images"
        CmsBtSxWorkloadModel workloadModel = new CmsBtSxWorkloadModel();
        workloadModel.setWorkloadName("title");
        workloadModel.setChannelId("024");
        workloadModel.setCartId(30);
        workloadModel.setGroupId(1245596L);

        cmsBuildPlatformAttributeUpdateTmTongGouService.doTmTongGouAttibuteUpdate(workloadModel);

    }
}
