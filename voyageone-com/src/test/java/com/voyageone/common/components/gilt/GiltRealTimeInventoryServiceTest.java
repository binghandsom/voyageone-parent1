package com.voyageone.common.components.gilt;

import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GiltRealTimeInventoryServiceTest {

    @Autowired
    private GiltRealTimeInventoryService giltRealTimeInventoryService;


    @Test
    public void testGetRealTimeInventoryBySkuId() throws Exception {
        ShopBean shopBean=new ShopBean();
       // shopBean.setAppKey("a197f33c5faafdcd67bfb68132ba834f");
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        //shopBean.setSessionKey();
        //httpPost.setHeader("Authorization", "basic "
        //        + "dGNsb3VkYWRtaW46dGNsb3VkMTIz");

        giltRealTimeInventoryService.getRealTimeInventoryBySkuId(shopBean,"4099260");
    }
}