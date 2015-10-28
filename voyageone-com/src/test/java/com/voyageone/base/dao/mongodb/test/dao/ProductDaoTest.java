package com.voyageone.base.dao.mongodb.test.dao;

import com.voyageone.base.dao.mongodb.test.dao.product.ProductDao;
import com.voyageone.base.dao.mongodb.test.model.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2015/10/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductDaoTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    ProductDao productDao;

    @Test
    public void saveTest() {
        Product product = new Product("001", 1, 2);
        productDao.saveWithProduct(product);
        System.out.println(product);
    }

    @Test
    public void testCreate1000WanProduct() {
       productDao.saveProduct1000Wan();
    }
}
