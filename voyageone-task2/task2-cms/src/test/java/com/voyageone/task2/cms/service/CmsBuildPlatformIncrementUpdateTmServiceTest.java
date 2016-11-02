package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 天猫根据规则增量更新商品服务测试
 *
 * Created by desmond on 2016/10/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformIncrementUpdateTmServiceTest {

    @Autowired
    CmsBuildPlatformIncrementUpdateTmService incrementUpdateTmService;

    @Test
    public void testDoIncrementUpdateProduct() throws Exception {

        String channelId = "010";
        int cartId = 23;

        incrementUpdateTmService.doIncrementUpdateProduct("51A0HC13E1-00LCNB0", channelId, cartId);
    }

    @Test
    public void testIncrementUpdateProduct() throws Exception {

        String channelId = "010";
        int cartId = 23;

        ShopBean shopProp = new ShopBean();
        shopProp.setOrder_channel_id(channelId);
        shopProp.setCart_id(String.valueOf(cartId));
        shopProp.setApp_url("");
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        // platformid一定要设成京东，否则默认为天猫（1）的话，expressionParser.parse里面会上传照片到天猫空间，出现异常
        shopProp.setPlatform_id("1");

        incrementUpdateTmService.getIncrementUpdateSchema(shopProp, "525897623218");
    }
}
