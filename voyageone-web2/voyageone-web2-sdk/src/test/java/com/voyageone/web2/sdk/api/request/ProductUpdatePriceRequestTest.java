package com.voyageone.web2.sdk.api.request;

import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.domain.ProductSkuPriceModel;
import com.voyageone.web2.sdk.api.response.ProductUpdatePriceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        ProductPriceBean model = new ProductPriceBean();
        model.setProductId(1L);

        ProductSkuPriceBean skuPriceModel = new ProductSkuPriceBean();
        skuPriceModel.setSkuCode("100001-1");
        skuPriceModel.setPriceMsrp(108d);
        skuPriceModel.setPriceRetail(109d);
        skuPriceModel.setPriceSale(110d);
        model.addSkuPrice(skuPriceModel);

        skuPriceModel = new ProductSkuPriceBean();
        skuPriceModel.setSkuCode("100001-2");
        skuPriceModel.setPriceMsrp(301d);
        skuPriceModel.setPriceRetail(302d);
        skuPriceModel.setPriceSale(303d);
        model.addSkuPrice(skuPriceModel);


        requestModel.addProductPrices(model);
        requestModel.setModifier("testLiang");

        //SDK取得Product 数据
        ProductUpdatePriceResponse response = voApiClient.execute(requestModel);
        System.out.println(response);
    }
}
