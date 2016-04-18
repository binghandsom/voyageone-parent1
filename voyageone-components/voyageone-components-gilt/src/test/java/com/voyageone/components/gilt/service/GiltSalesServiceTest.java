package com.voyageone.components.gilt.service;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.gilt.bean.*;
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
@ContextConfiguration(locations = "classpath:test-context.xml")
public class GiltSalesServiceTest {


    @Autowired
    private GiltSalesService giltSalesService;

    @Test
    public void testGetSkusBySaleId() throws Exception {
        GiltPageGetSaleAttrRequest request=new GiltPageGetSaleAttrRequest();
        request.setLimit(2);
        request.setOffset(0);
        request.setId("1141689861");
        //request.setSku_ids("4099260,4099262,2997763");

        List<GiltSku> skus= giltSalesService.getSkusBySaleId(request);
        System.out.println("Retrun:"+ JsonUtil.getJsonString(skus));
        assertTrue(skus.size()>0);
    }

    @Test
    public void testGetAllSales() throws Exception {
        List<GiltSale> skus= giltSalesService.getAllSales();
        System.out.println("Retrun:"+ JsonUtil.getJsonString(skus));

    }

    @Test
    public void testGetSaleById() throws Exception {
        GiltSale sale= giltSalesService.getSaleById("1141506556");
        System.out.println("Retrun:"+ JsonUtil.getJsonString(sale));
    }

    @Test
    public void testGetInventorysBySaleId() throws Exception {
        GiltPageGetSaleAttrRequest request=new GiltPageGetSaleAttrRequest();
        request.setLimit(2);
        request.setOffset(0);
        request.setId("1141689861");
        List<GiltInventory> list=giltSalesService.getInventorysBySaleId(request);
        System.out.println("Retrun:"+ JsonUtil.getJsonString(list));
        assertTrue(list.size()>0);
    }

    @Test
    public void testGetRealTimeInventoriesBySaleId() throws Exception {
        GiltPageGetSaleAttrRequest request=new GiltPageGetSaleAttrRequest();
        request.setLimit(2);
        request.setOffset(0);
        request.setId("1141689861");
        List<GiltRealTimeInventory> list= giltSalesService.getRealTimeInventoriesBySaleId(request);
        System.out.println("Retrun:"+ JsonUtil.getJsonString(list));
        assertTrue(list.size()>0);
    }
}