package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.bean.GiltHealthcheck;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GiltHealthcheckServiceTest {

    @Autowired
    private GiltHealthcheckService giltHealthcheckService;

    @Test
    public void testPing() throws Exception {
        ShopBean shopBean=new ShopBean();
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        shopBean.setAppKey("YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6");
        GiltHealthcheck  giltHealthcheck=giltHealthcheckService.ping(shopBean);
        System.out.println(JsonUtil.getJsonString(giltHealthcheck));
    }

    @Test
    public void testStatus() throws Exception {
        ShopBean shopBean=new ShopBean();
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        shopBean.setAppKey("YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6");
        GiltHealthcheck  giltHealthcheck=giltHealthcheckService.status(shopBean);
        System.out.println(JsonUtil.getJsonString(giltHealthcheck));
    }
}