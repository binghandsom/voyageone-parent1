package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lewis on 15-12-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GetPlatformCategoryTreesServiceTest {

    @Autowired
    private GetPlatformCategoryTreesService getPlatformCategoryTreesService;

    @Test
    public void testOnStartup() throws Exception {

        getPlatformCategoryTreesService.startup();

    }

    @Test
    public void testDoSetPlatformCategoryTm() throws Exception {

        ShopBean shopProp = new ShopBean();
        shopProp.setOrder_channel_id("010");
        shopProp.setCart_id(String.valueOf(30));
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("9999");
        shopProp.setAppSecret("9999");
        shopProp.setSessionKey("99999");
        // platformid默认为天猫（1），expressionParser.parse里面会上传照片到天猫空间
        shopProp.setPlatform_id("1");

        getPlatformCategoryTreesService.doSetPlatformCategoryTm(shopProp);

    }

}