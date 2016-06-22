package com.voyageone.task2.cms.service.product;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jason.jiang on 2016/05/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSumProdOrdersServiceTest {

    @Autowired
    ProductService productService;

    @Test
    public void testGetSum() {
        CmsBtProductModel productModel = productService.getProductByCode("006", "P1527764-BLACK");
        System.out.println(productModel);

        System.out.println(productModel.getSales().getCodeSum7(0));
        System.out.println(productModel.getSales().getCodeSum30(0));
        System.out.println(productModel.getSales().getCodeSumAll(0));

        System.out.println(productModel.getSales().getSkuSum(0, "BH1757287"));
        System.out.println(productModel.getSales().getSkuSum(27, "BH1757287"));

        System.out.println(productModel.getSales().getSkuSum(27, "BH1757287", 7));
        System.out.println(productModel.getSales().getSkuSum(27, "BH1757287", 30));
        System.out.println(productModel.getSales().getSkuSum(27, "BH1757287", null));
    }
}