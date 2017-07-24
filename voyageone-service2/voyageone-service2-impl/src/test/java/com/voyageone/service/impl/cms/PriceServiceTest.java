package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM.*;
import static java.util.stream.Collectors.toMap;

/**
 * Created by Ethan Shi on 2016/7/14.
 *
 * @author Ethan Shi
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class PriceServiceTest {

    @Autowired
    private PriceService priceService;

    @Autowired
    private ProductService productService;

    @Test
    public void testSystemPriceSetter() throws Exception {

        CmsBtProductModel product = productService.getProductByCode("007", "54413");

        // 测试计算

        priceService.setPrice(product, false);

        // 输出结果

        System.out.println("\n\n");

        System.out.println("\n\n");
    }

    @Test
    public void testFormulaPriceSetter() throws Exception {

        CmsBtProductModel product = productService.getProductById("012", 2524592);

        List<BaseMongoMap<String, Object>> skus = product.getPlatform(23).getSkus();

        // 记录老价格

        Map<String, List<Double>> lastPriceListMap = skus.stream().collect(toMap(sku -> sku.getStringAttribute(skuCode.name()), sku -> new ArrayList<Double>() {
            {
                add(sku.getDoubleAttribute(priceRetail.name()));
                add(sku.getDoubleAttribute(originalPriceMsrp.name()));
                add(sku.getDoubleAttribute(priceMsrp.name()));
                add(sku.getDoubleAttribute(priceMsrpFlg.name()));
                add(sku.getDoubleAttribute(priceSale.name()));
            }
        }));

        // 测试计算

        priceService.setPrice(product, 23, false);

        // 输出结果

        System.out.println("\n\n");

        for (BaseMongoMap<String, Object> sku : skus) {

            String skuCodeValue = sku.getStringAttribute(skuCode.name());

            List<Double> doubleList = lastPriceListMap.get(skuCodeValue);

            System.out.println(String.format("%s, \t\t%s -> %s", "priceRetail", doubleList.get(0), sku.getDoubleAttribute(priceRetail.name())));
            System.out.println(String.format("%s, \t%s -> %s", "originalPriceMsrp", doubleList.get(1), sku.getDoubleAttribute(originalPriceMsrp.name())));
            System.out.println(String.format("%s, \t\t\t%s -> %s", "priceMsrp", doubleList.get(2), sku.getDoubleAttribute(priceMsrp.name())));
            System.out.println(String.format("%s, \t\t%s -> %s", "priceMsrpFlg", doubleList.get(3), sku.getStringAttribute(priceMsrpFlg.name())));
            System.out.println(String.format("%s, \t\t\t%s -> %s", "priceSale", doubleList.get(4), sku.getDoubleAttribute(priceSale.name())));

            System.out.println("\n\n");
        }

        System.out.println("\n\n");
    }
}