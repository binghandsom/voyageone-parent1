package com.voyageone.base.dao.mongodb.test.dao;

import com.voyageone.base.dao.mongodb.test.dao.customer.NewCustomerJpaDaoTest;
import com.voyageone.base.dao.mongodb.test.dao.product.ProductDaoTest;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component("mainTestInsertTask")
public class MainTestInsertTask {

    @Autowired
    NewCustomerJpaDaoTest customerDaoTest;

    @Autowired
    ProductDaoTest productDaoTest;

    public void testSave() {
        customerDaoTest.savesCustomer();
        customerDaoTest.saveCustomerByDBObject();
    }

    public void testProductSave1() {
        productDaoTest.saveProduct1();
    }

    public void testProductSave2() {
        Random random = new Random();
        String channel_id = "10";
        System.out.println("start:" + DateTimeUtil.getNow());
        for (int i=1; i<10000001; i++) {
            String cat_id = String.valueOf(random.nextInt(1000)+1);
            String product_id = String.valueOf(i);
            productDaoTest.saveProduct2(channel_id,cat_id,product_id) ;
        }
        System.out.println("end:" + DateTimeUtil.getNow());

    }

    public void testProductSave3() {
        System.out.println("start:" + DateTimeUtil.getNow());
        Random random = new Random();

        for (int i=0; i<10; i++) {
            productDaoTest.saveProduct3(random, i, 1000) ;
        }
        System.out.println("end:" + DateTimeUtil.getNow());

    }
}
