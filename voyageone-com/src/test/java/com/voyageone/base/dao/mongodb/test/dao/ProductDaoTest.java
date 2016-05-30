package com.voyageone.base.dao.mongodb.test.dao;

import com.voyageone.base.dao.mongodb.test.dao.product.ProductDao;
import com.voyageone.base.dao.mongodb.test.model.product.Product;
import com.voyageone.common.util.DateTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductDaoTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ProductDao productDao;

    @Test
    public void testExecuteCommand() {
        productDao.testExecuteCommand();
    }

    @Test
    public void testCollectionExists() {
        System.out.println(productDao.testCollectionExists());
    }

    @Test
    public void testCreateCollection() {
        productDao.createCollection();
        productDao.dropCollection();
    }

    @Test
    public void testFindOne() {
        System.out.println(productDao.findOne());
    }

    @Test
    public void testFindOneQuery() {
        String query = "{\"product_id\":21}";
        System.out.println(productDao.findOne(query));
    }

    @Test
    public void testExistQuery() {
        String query = "{\"product_id\":21}";
        System.out.println(productDao.exists(query));
    }

    @Test
    public void testFindAll() {
        productDao.findAll();
    }

    @Test
    public void testCountProduct() {
        productDao.countWithProduct();
    }

    @Test
    public void testCountQuery() {
        productDao.countQuery();
    }

    @Test
    public void testInsert1() {
        Product product = new Product("001", 1, 3);
        productDao.insertWithProduct(product);
        System.out.println(product);
        productDao.deleteWithProduct(product);
    }
    @Test
    public void testSave1() {
        Product product = new Product("001", 1, 1);
        productDao.saveWithProduct(product);
        System.out.println(product);
        productDao.deleteWithProduct(product);
    }

    @Test
    public void testSave2() {
        Product product = new Product("001", 1, 2);
        product.setAttribute("prop_1", 1);
        product.setAttribute("prop_2", "2");
        product.setAttribute("prop_3", DateTimeUtil.getDate());
        product.setAttribute("prop_4", DateTimeUtil.getNow());
        productDao.saveWithProduct(product);
        System.out.println(product);
        productDao.deleteWithProduct(product);
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
    public void testQueryWith_() {
        productDao.queryWith_();
    }

    @Test
    public void testUpdateFirst() {
        productDao.updateFirst();
    }

    @Test
    public void testUpdateFirst1() {
        productDao.updateFirst1();
    }

//
//    @Test
//    public void testQueryWithJongoObj() {
//        productDao.queryWithJongoObj();
//    }
//
//    @Test
//    public void saveTestJongo() {
//        ProductJongo product = new ProductJongo("001", 1, 2);
//        productDao.saveWithProduct(product);
//        System.out.println(product);
//    }
//
//    @Test
//    public void testExecuteCommand() {
//        productDao.testExecuteCommand();
//    }
//
//    @Test
//    public void testExecuteCommand1() {
//        productDao.testExecuteCommand1();
//    }
//
//    @Test
//    public void testExecuteCommand2() {
//        productDao.testExecuteCommand2();
//    }
//
//    @Test
//    public void testExecuteQuery() {
//        productDao.executeQuery();
//    }
//
//    @Test
//    public void testExecuteDbCallback() {
//        productDao.executeDbCallback();
//    }
}
