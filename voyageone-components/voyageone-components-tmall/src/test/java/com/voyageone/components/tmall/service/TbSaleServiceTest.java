package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 商品上下架，以及查询商品的在售/在库状态 接口测试
 *
 * @author desmond, 17/1/17.
 * @version 2.11.0
 * @since 2.11.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class TbSaleServiceTest {

    @Autowired
    private TbSaleService tbSaleService;

    @Test
    public void testDoWareUpdateListing() throws TopSchemaException, ApiException, GetUpdateSchemaFailException {
        // 测试天猫一口价商品上架功能

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
//        shopBean.setSessionKey(""); // 018 Target
        shopBean.setSessionKey(""); // 天猫官网同购测试店铺


//        String numIId = "532856371018";   // 无SKU  https://detail.tmall.hk/hk/item.htm?id=532856371018
//        String numIId = "535914991001";   // 有SKU  https://detail.tmall.hk/hk/item.htm?id=535914991001
        String numIId = "542998554105";   // 天猫官网同购  https://detail.tmall.hk/hk/item.htm?id=542998554105

        ItemUpdateListingResponse listingResponse = tbSaleService.doWareUpdateListing(shopBean, numIId);
//        assert listingResponse != null;
        System.out.println(listingResponse.getBody());
    }

    @Test
    public void testDoWareUpdateDelisting() throws TopSchemaException, ApiException, GetUpdateSchemaFailException {
        // 测试天猫一口价商品下架功能

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
//        shopBean.setSessionKey(""); // 018 Target
//        shopBean.setSessionKey(""); // 天猫官网同购测试店铺

//        String numIId = "532856371018";   // 无SKU  https://detail.tmall.hk/hk/item.htm?id=532856371018
//        String numIId = "535914991001";   // 有SKU  https://detail.tmall.hk/hk/item.htm?id=535914991001
        String numIId = "542998554105";   // 天猫官网同购  https://detail.tmall.hk/hk/item.htm?id=542998554105

        ItemUpdateDelistingResponse delistingResponse = tbSaleService.doWareUpdateDelisting(shopBean, numIId);
//        assert listingResponse != null;
        System.out.println(delistingResponse.getBody());
    }
}