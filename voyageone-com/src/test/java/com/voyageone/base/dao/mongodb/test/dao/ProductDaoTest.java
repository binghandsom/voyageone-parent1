package com.voyageone.base.dao.mongodb.test.dao;

import com.voyageone.base.dao.mongodb.test.dao.product.ProductDao;
import com.voyageone.base.dao.mongodb.test.model.product.Product;
import com.voyageone.base.dao.mongodb.test.dao.support.ProductJongo;
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

    @Test
    public void testCreate100WanProduct() {
        productDao.saveProduct100Wan();
    }

    @Test
    public void testCreate10WanProduct() {
        productDao.saveProduct10Wan();
    }

    @Test
    public void testQueryWithJongo() {
        productDao.queryWithJongo();
    }

    @Test
    public void testQueryWithJongoObj() {
        productDao.queryWithJongoObj();
    }

    @Test
    public void saveTestJongo() {
        ProductJongo product = new ProductJongo("001", 1, 2);
        productDao.saveWithProduct(product);
        System.out.println(product);
    }

    @Test
    public void testExecuteCommand() {
        productDao.testExecuteCommand();
    }

    @Test
    public void testExecuteCommand1() {
        productDao.testExecuteCommand1();
    }

    @Test
    public void testExecuteCommand2() {
        productDao.testExecuteCommand2();
    }

    @Test
    public void testExecuteQuery() {
        productDao.executeQuery();
    }

    @Test
    public void testExecuteDbCallback() {
        productDao.executeDbCallback();
    }
}
