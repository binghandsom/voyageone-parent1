package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.model.cms.CmsMtPlatformCategoryExtendInfoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by lewis on 15-11-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GetPlatformCategorySchemaServiceTest {

    @Autowired
    private CmsMtPlatformCategoryDao cmsMtPlatformCategoryDao;

    @Autowired
    private GetPlatformCategorySchemaService getPlatformCategorySchemaService;

    @Test
    public void testOnStartup() throws Exception {
        // 插入类目信息
        getPlatformCategorySchemaService.startup();
    }

    @Test
    public void testOnStartup1() throws Exception {
        getPlatformCategorySchemaService.onStartup(new ArrayList<>());
    }

    @Test
    public void testDo() throws Exception {
        String channelId = "024";
        int cartId = 23;   // 天猫平台
//        int cartId = 30;   // 天猫官网同购

        // 初始化
        Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap = getPlatformCategorySchemaService.doInit();

        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            return;
        }
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("21008948");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");  // 天猫平台 overstock海外旗舰店
//        shopProp.setSessionKey("");  // 天猫官网同购 overstock海外旗舰店
        // platformid一定要设成京东，否则默认为天猫（1）的话，expressionParser.parse里面会上传照片到天猫空间，出现异常
        shopProp.setPlatform_id("1");

        // 调用主逻辑(channel, cart, 特殊处理类目的信息一览)
        String logInfo = String.format("获取天猫类目schema");

        getPlatformCategorySchemaService.doLogic(shopProp, platformCategoryExtendInfoMap, logInfo);
    }

}
