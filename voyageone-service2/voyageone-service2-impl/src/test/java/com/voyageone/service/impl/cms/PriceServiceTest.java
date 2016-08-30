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
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PriceServiceTest {

    @Autowired
    private PriceService priceService;

    @Autowired
    private ProductService productService;

    @Test
    public void testSystemPriceSetter() throws Exception {

        CmsBtProductModel product = productService.getProductById("017", 47955);

        List<BaseMongoMap<String, Object>> skus = product.getPlatform(27).getSkus();

        Map<String, CmsBtProductModel_Sku> commonSkuMap = product.getCommon().getSkus().stream().collect(toMap(CmsBtProductModel_Sku::getSkuCode, sku -> sku));

        // 记录老价格

        Map<String, List<String>> lastPriceListMap = skus.stream().collect(toMap(sku -> sku.getStringAttribute(skuCode.name()), sku -> new ArrayList<String>() {
            {
                CmsBtProductModel_Sku sku1 = commonSkuMap.get(sku.getStringAttribute(skuCode.name()));

                add(sku1.getClientNetPrice().toString() + " -> " + sku.getStringAttribute(priceRetail.name()));
                add(sku1.getClientMsrpPrice().toString() + " -> " + sku.getStringAttribute(originalPriceMsrp.name()));
                add("     -> " + sku.getStringAttribute(priceMsrp.name()));
                add("     -> " + sku.getStringAttribute(priceMsrpFlg.name()));
                add("     -> " + sku.getStringAttribute(priceSale.name()));

            }
        }));

        // 测试计算

        priceService.setPrice(product, 27, false);

        // 输出结果

        System.out.println("\n\n");

        for (BaseMongoMap<String, Object> sku : skus) {

            String skuCodeValue = sku.getStringAttribute(skuCode.name());

            List<String> list = lastPriceListMap.get(skuCodeValue);

            System.out.println(String.format("%s, \t\t%s -> %s", "priceRetail", list.get(0), sku.getDoubleAttribute(priceRetail.name())));
            System.out.println(String.format("%s, \t%s -> %s", "originalPriceMsrp", list.get(1), sku.getDoubleAttribute(originalPriceMsrp.name())));
            System.out.println(String.format("%s, \t\t\t%s -> %s", "priceMsrp", list.get(2), sku.getDoubleAttribute(priceMsrp.name())));
            System.out.println(String.format("%s, \t%s -> %s", "priceMsrpFlg", list.get(3), sku.getStringAttribute(priceMsrpFlg.name())));
            System.out.println(String.format("%s, \t\t\t%s -> %s", "priceSale", list.get(4), sku.getDoubleAttribute(priceSale.name())));
        }

        System.out.println("\n\n");
    }

    @Test
    public void testFormulaPriceSetter() throws Exception {

        CmsBtProductModel product = productService.getProductById("010", 9303);

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
            System.out.println(String.format("%s, \t\t\t%s -> %s", "priceMsrpFlg", doubleList.get(3), sku.getStringAttribute(priceMsrpFlg.name())));
            System.out.println(String.format("%s, \t\t\t%s -> %s", "priceSale", doubleList.get(4), sku.getDoubleAttribute(priceSale.name())));
        }

        System.out.println("\n\n");
    }
}