package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Charis on 2017/3/23.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformAttributeUpdateTmServcieTest {

    @Autowired
    private CmsBuildPlatformAttributeUpdateTmServcie cmsBuildPlatformAttributeUpdateTmServcie;

    @Test
    public void testDoTmAttributeUpdate() throws Exception{
        // "seller_cids", "wireless_desc", "title", "white_bg_image", "description", "sell_points"
        // "item_images"
        CmsBtSxWorkloadModel workloadModel = new CmsBtSxWorkloadModel();
        workloadModel.setWorkloadName("title");
        workloadModel.setChannelId("001");
        workloadModel.setCartId(23);
        workloadModel.setGroupId(10408058L);

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey("");
        shopBean.setOrder_channel_id("001");
        shopBean.setCart_id("26");
        cmsBuildPlatformAttributeUpdateTmServcie.doTmAttibuteUpdate(workloadModel, shopBean);

    }
}
