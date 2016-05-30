package com.voyageone.task2.cms.service;

import com.voyageone.task2.cms.service.CmsPlatformProductImportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author james.li on 2016/1/21.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformProductImportServiceTest {

    @Autowired
    private CmsPlatformProductImportService cmsPlatformProductImportService;
//    @Test
//    public void testGetPlatformProduct() throws Exception {
//        ShopBean shopbean = new ShopBean();
//        shopbean.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopbean.setAppKey("21008948");
//        shopbean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
//        shopbean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//
//        cmsPlatformProductImportService.getPlatformProduct(423802042L,shopbean);
//    }
//
//    @Test
//    public void testGetPlatformWareInfoItem() throws Exception {
//        ShopBean shopbean = new ShopBean();
//        shopbean.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopbean.setAppKey("21008948");
//        shopbean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
//        shopbean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//
//        cmsPlatformProductImportService.getPlatformWareInfoItem("524395554281", shopbean);
//    }

    @Test
    public void testSetMainProduct() throws Exception {

        cmsPlatformProductImportService.doMain();

    }

}