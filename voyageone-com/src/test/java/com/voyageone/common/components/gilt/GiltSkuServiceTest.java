package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.common.components.gilt.bean.GiltRealTimeInventory;
import com.voyageone.common.components.gilt.bean.GiltSku;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GiltSkuServiceTest {

    @Autowired
    private GiltSkuService giltSkuService;

    @Test
    public void testPageGetSkus() throws Exception {
        ShopBean shopBean=new ShopBean();
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        shopBean.setAppKey("YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6");

        GiltPageGetSkusRequest request=new GiltPageGetSkusRequest();
        request.setLimit(2);
        request.setOffset(1);
        //request.setSku_ids("4099260,4099262,2997763");

        List<GiltSku> skus= giltSkuService.pageGetSkus(shopBean,request);
        System.out.println("Retrun:"+JsonUtil.getJsonString(skus));
    }

    @Test
    public void testGetSkuById() throws Exception {
        ShopBean shopBean=new ShopBean();
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        shopBean.setAppKey("YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6");


        GiltSku skus= giltSkuService.getSkuById(shopBean,"4099260");
        System.out.println("Retrun:"+JsonUtil.getJsonString(skus));
    }

    @Test
    public void testGetAllSalesSkus() throws Exception {
        ShopBean shopBean=new ShopBean();
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        shopBean.setAppKey("YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6");


        List<GiltSku> skus= giltSkuService.getAllSalesSkus(shopBean);
        System.out.println("Retrun:"+JsonUtil.getJsonString(skus));
    }
}