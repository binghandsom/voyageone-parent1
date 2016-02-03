package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.bean.GiltPageGetSaleAttrRequest;
import com.voyageone.common.components.gilt.bean.GiltSale;
import com.voyageone.common.components.gilt.bean.GiltSku;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import static org.junit.Assert.*;
/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GiltSalesServiceTest {
    private static final String url="https://api-sandbox.gilt.com/global/";
    private static final String tokenKey="YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6";
    private ShopBean shopBean;

    @Before
    public void before() {
        shopBean=new ShopBean();
        shopBean.setApp_url(url);
        shopBean.setAppKey(tokenKey);
    }
    @Autowired
    private GiltSalesService giltSalesService;

    @Test
    public void testGetSaleSkusById() throws Exception {
        GiltPageGetSaleAttrRequest request=new GiltPageGetSaleAttrRequest();
        request.setLimit(2);
        request.setOffset(0);
        request.setId("1141689861");
        //request.setSku_ids("4099260,4099262,2997763");

        List<GiltSku> skus= giltSalesService.getSaleSkusById(shopBean,request);
        System.out.println("Retrun:"+ JsonUtil.getJsonString(skus));
        assertTrue(skus.size()>0);
    }

    @Test
    public void testGetAllSales() throws Exception {
        List<GiltSale> skus= giltSalesService.getAllSales(shopBean);
        System.out.println("Retrun:"+ JsonUtil.getJsonString(skus));

    }

    @Test
    public void testGetSaleById() throws Exception {
        GiltPageGetSaleAttrRequest request=new GiltPageGetSaleAttrRequest();
        request.setLimit(2);
        request.setOffset(0);
        request.setId("1141689861");
        //request.setSku_ids("4099260,4099262,2997763");

        List<GiltSale> skus= giltSalesService.getAllSales(shopBean);
        System.out.println("Retrun:"+ JsonUtil.getJsonString(skus));
    }

    @Test
    public void testGetSaleInventorysById() throws Exception {

    }

    @Test
    public void testGetSaleRealTimeInventorysById() throws Exception {

    }
}