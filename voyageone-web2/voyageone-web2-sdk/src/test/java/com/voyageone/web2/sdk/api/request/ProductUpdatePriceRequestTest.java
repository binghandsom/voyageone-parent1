package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.domain.ProductSkuPriceModel;
import com.voyageone.web2.sdk.api.response.ProductUpdatePriceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2015/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductUpdatePriceRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testProductUpdatePrice() {
        ProductUpdatePriceRequest requestModel = new ProductUpdatePriceRequest("300");
        ProductPriceModel model = new ProductPriceModel();
        model.setProductId(1L);
        model.setMsrpStart(100d);
        model.setMsrpEnd(200d);
        model.setRetailPriceStart(101d);
        model.setRetailPriceEnd(201d);
        model.setSalePriceStart(102d);
        model.setSalePriceEnd(102d);
        model.setCurrentPriceStart(103d);
        model.setCurrentPriceEnd(203d);

        ProductSkuPriceModel skuPriceModel = new ProductSkuPriceModel();
        skuPriceModel.setSkuCode("100001-1");
        skuPriceModel.setPriceMsrp(101d);
        skuPriceModel.setPriceRetail(102d);
        skuPriceModel.setPriceSale(103d);
        model.addSkuPrice(skuPriceModel);

        skuPriceModel = new ProductSkuPriceModel();
        skuPriceModel.setSkuCode("100001-2");
        skuPriceModel.setPriceMsrp(201d);
        skuPriceModel.setPriceRetail(202d);
        skuPriceModel.setPriceSale(203d);
        model.addSkuPrice(skuPriceModel);

        requestModel.addProductPrices(model);

        //SDK取得Product 数据
        ProductUpdatePriceResponse response = voApiClient.execute(requestModel);
        System.out.println(response);
    }
}
