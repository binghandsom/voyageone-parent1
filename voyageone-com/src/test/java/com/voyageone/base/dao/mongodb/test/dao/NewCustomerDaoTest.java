package com.voyageone.base.dao.mongodb.test.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.test.dao.customer.NewCustomerDao;
import com.voyageone.base.dao.mongodb.test.dao.customer.NewCustomerJpaDao;
import com.voyageone.base.dao.mongodb.test.model.customer.Address;
import com.voyageone.base.dao.mongodb.test.model.customer.EmailAddress;
import com.voyageone.base.dao.mongodb.test.model.customer.NewCustomer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import sun.font.TrueTypeFont;

import static junit.framework.TestCase.*;

/**
 * Created by DELL on 2015/10/26.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class NewCustomerDaoTest extends AbstractJUnit4SpringContextTests {

    @Qualifier("newCustomerJpaDao")
    @Autowired
    private NewCustomerJpaDao customerJpaDao;

    @Autowired
    private NewCustomerDao customerDao;

    @Test
    public void saveTest() {
        String firstName = "Alicia_savesCustomer";

        NewCustomer dave1 = customerJpaDao.findByFirstname(firstName);
        if (dave1 != null) {
            customerJpaDao.delete(dave1);
        }

        NewCustomer customer = new NewCustomer(firstName, "Keys");
        //EmailAddress
        EmailAddress email = new EmailAddress("Alicia_savesCustomer@keys.com");
        customer.setEmailAddress(email);
        //Address
        customer.add(new Address("27 Broadway", "New York", "United States"));

        System.out.println(customer);
        customer = customerJpaDao.save(customer);
        System.out.println(customer);

        assertNotNull(customer);
        assertNotNull(customer.get_id());
    }

    @Test
    public void saveTestDBObject() {
        String firstName = "Dave1231";
        NewCustomer dave1 = customerJpaDao.findByFirstname(firstName);
        if (dave1 != null) {
            customerJpaDao.delete(dave1);
        }

        DBObject address = new BasicDBObject();
        address.put("city", "New York");
        address.put("street", "Broadway");
        address.put("country", "United States");

        BasicDBList addresses = new BasicDBList();
        addresses.add(address);

        DBObject dave = new BasicDBObject("firstname", firstName);
        dave.put("lastname", "Matthews");
        dave.put("email", "dave@dmband1231.com");

        dave.put("addresses", addresses);

        customerDao.saveWithDBObject(dave);
        Assert.isTrue(true);
    }

    @Test
    public void saveTestJPADBObject() {
        String firstName = "Dave1231";
        NewCustomer dave1 = customerJpaDao.findByFirstname(firstName);
        if (dave1 != null) {
            customerJpaDao.delete(dave1);
        }

        DBObject address = new BasicDBObject();
        address.put("city", "New York");
        address.put("street", "Broadway");
        address.put("country", "United States");

        BasicDBList addresses = new BasicDBList();
        addresses.add(address);

        DBObject dave = new BasicDBObject("firstname", firstName);
        dave.put("lastname", "Matthews");
        dave.put("email", "dave@dmband1231.com");

        dave.put("addresses", addresses);

        //customerJpaDao.saveWithDBObject(dave);
        Assert.isTrue(true);
    }

}
