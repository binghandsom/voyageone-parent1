package com.voyageone.web2.sdk.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2015/12/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductsGetClientTest {

    @Autowired
    private ProductsGetClient productsGetClient;

//    @Test
//    public void testGetProductCodesByCart() {
//        long start = System.currentTimeMillis();
//        List<String> productCodeList = productsGetClient.getProductCodesByCart("300", 21);
//        long end = System.currentTimeMillis();
//
//        int index = 1;
//        for (String productCode : productCodeList) {
//            System.out.println(productCode);
//            index++;
//            if (index > 10) {
//                break;
//            }
//        }
//        System.out.println(productCodeList.size());
//        System.out.println("total time:=" + (end-start));
//    }
}
