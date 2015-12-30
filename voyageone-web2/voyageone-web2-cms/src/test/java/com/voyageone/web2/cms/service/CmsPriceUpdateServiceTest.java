package com.voyageone.web2.cms.service;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubuchun 15/12/28
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPriceUpdateServiceTest {

    @Autowired
    private CmsPriceUpdateService cmsPriceUpdateService;

    @Test
    public void testUpdatePriceByCode() throws Exception {
        String channelId = "001";
        String code = "100001";
        String modifier = "gu";
        List<CmsBtProductModel_Sku> skuModelList = new ArrayList<>();
        CmsBtProductModel_Sku sku1 = new CmsBtProductModel_Sku();
        sku1.setSkuCode("100001-1");
        sku1.setBarcode("1234567890159");
        sku1.setPriceMsrp(100d);
        sku1.setPriceRetail(200d);
        sku1.setPriceSale(300d);
        List<Integer> skuCarts1 = new ArrayList<>();
        skuCarts1.add(21);
        skuCarts1.add(23);
        sku1.setSkuCarts(skuCarts1);
        skuModelList.add(sku1);

        CmsBtProductModel_Sku sku2 = new CmsBtProductModel_Sku();
        sku2.setSkuCode("100001-2");
        sku2.setBarcode("1234567890159");
        sku2.setPriceMsrp(1000d);
        sku2.setPriceRetail(2000d);
        sku2.setPriceSale(3000d);
        List<Integer> skuCarts2 = new ArrayList<>();
        skuCarts2.add(21);
        skuCarts2.add(23);
        sku2.setSkuCarts(skuCarts2);
        skuModelList.add(sku2);

        CmsBtProductModel_Sku sku3 = new CmsBtProductModel_Sku();
        sku3.setSkuCode("100001-3");
        sku3.setBarcode("1234567890159");
        sku3.setPriceMsrp(500d);
        sku3.setPriceRetail(1500d);
        sku3.setPriceSale(1500d);
        List<Integer> skuCarts3 = new ArrayList<>();
        skuCarts3.add(21);
        skuCarts3.add(23);
        sku3.setSkuCarts(skuCarts3);
        skuModelList.add(sku3);

        CmsBtProductModel_Sku sku4 = new CmsBtProductModel_Sku();
        sku4.setSkuCode("100001-4");
        sku4.setBarcode("1234567890159");
        sku4.setPriceMsrp(10d);
        sku4.setPriceRetail(20d);
        sku4.setPriceSale(30d);
        List<Integer> skuCarts4 = new ArrayList<>();
        skuCarts4.add(21);
        skuCarts4.add(23);
        sku4.setSkuCarts(skuCarts4);
        skuModelList.add(sku4);

        cmsPriceUpdateService.updatePriceByCode(channelId, code, skuModelList, modifier);

        assert true;
    }
}
