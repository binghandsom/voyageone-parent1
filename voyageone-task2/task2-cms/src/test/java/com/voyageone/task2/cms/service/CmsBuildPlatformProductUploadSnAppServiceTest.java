package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by Charis on 2017/8/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadSnAppServiceTest {


    @Autowired
    private CmsBuildPlatformProductUploadSnAppService cmsBuildPlatformProductUploadSnAppService;

    @Test
    public void testUploadProduct() throws Exception {
        CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
        model.setChannelId("001");
        model.setGroupId(Long.parseLong("9954545"));
        model.setCartId(35);
        model.setPublishStatus(0);
        model.setCreater("charis_test");
        model.setModified(new Date());

        ShopBean shopProp = new ShopBean();
        shopProp.setOrder_channel_id("001");
        shopProp.setCart_id("35");
        shopProp.setApp_url("");
        cmsBuildPlatformProductUploadSnAppService.uploadProduct(model, shopProp);

    }



}
