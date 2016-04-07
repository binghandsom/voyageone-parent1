package com.voyageone.components.gilt.service;

import com.voyageone.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.components.gilt.bean.GiltSku;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class GiltSkuServiceTest {

    @Autowired
    private GiltSkuService giltSkuService;

    @Test
    public void testPageGetSkus() throws Exception {

        GiltPageGetSkusRequest request = new GiltPageGetSkusRequest();
        request.setLimit(2);
        request.setOffset(1);
        //request.setSku_ids("4099260,4099262,2997763");

        List<GiltSku> skus = giltSkuService.pageGetSkus(request);
        System.out.println("Retrun:" + JsonUtil.getJsonString(skus));
    }

    @Test
    public void testGetSkuById() throws Exception {
        GiltSku skus = giltSkuService.getSkuById("4099260");
        System.out.println("Retrun:" + JsonUtil.getJsonString(skus));
    }

    @Test
    public void testGetAllSalesSkus() throws Exception {
        List<GiltSku> skus = giltSkuService.getAllSalesSkus();
        System.out.println("Retrun:" + JsonUtil.getJsonString(skus));
    }


    @Test
    public void testGetSalesSkuIds() throws Exception {

        Set<Long> skus = giltSkuService.getSalesSkuIds();
        System.out.println("Retrun:" + JsonUtil.getJsonString(skus));
    }

    @Test
    public void testGetSkus2() throws Exception {

        Set<Long> skuIds = giltSkuService.getSalesSkuIds();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Long skuId : skuIds) {
            sb.append(skuId);
            if (++i % 100 == 0) {
                List<GiltSku> skus = giltSkuService.getSkus(sb.toString());
                //Todo 业务处理
                System.out.println("Retrun:" + JsonUtil.getJsonString(skus));
                sb = new StringBuilder();
            } else
                sb.append(",");
        }
    }

    @Test
    public void testGetSkus() throws Exception {
        String sku_ids = "4194370,4194371,4194372";
        List<GiltSku> skus = giltSkuService.getSkus(sku_ids);
        //Todo 业务处理
        System.out.println("Retrun:" + JsonUtil.getJsonString(skus));
        System.out.println("size:"+skus.size());
        assertTrue(skus.size() > 0);
    }
}