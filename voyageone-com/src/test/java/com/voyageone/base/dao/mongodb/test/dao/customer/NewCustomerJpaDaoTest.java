package com.voyageone.base.dao.mongodb.test.dao.customer;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.test.model.customer.Address;
import com.voyageone.base.dao.mongodb.test.model.customer.EmailAddress;
import com.voyageone.base.dao.mongodb.test.model.customer.NewCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NewCustomerJpaDaoTest {

    @Autowired
    NewCustomerJpaDao customerJpaDao;

    @Autowired
    NewCustomerDao customerDao;

    public void savesCustomer() {
        NewCustomer dave1 = customerJpaDao.findByFirstname("Alicia_savesCustomer");
        if (dave1 != null) {
            customerJpaDao.delete(dave1);
        }

        NewCustomer dave = new NewCustomer("Alicia_savesCustomer", "Keys");
        //EmailAddress
        EmailAddress email = new EmailAddress("Alicia_savesCustomer@keys.com");
        dave.setEmailAddress(email);
        //Address
        dave.add(new Address("27 Broadway", "New York", "United States"));

        System.out.println(dave);
        dave = customerJpaDao.save(dave);
        System.out.println(dave);
    }

    public void saveCustomerByDBObject() {
        DBObject address = new BasicDBObject();
        address.put("city", "New York");
        address.put("street", "Broadway");
        address.put("country", "United States");

        BasicDBList addresses = new BasicDBList();
        addresses.add(address);

        DBObject dave = new BasicDBObject("firstname", "Dave");
        dave.put("lastname", "Matthews");
        dave.put("email", "dave@dmband.com");

        dave.put("addresses", addresses);

        customerDao.saveWithDBObject(dave);
    }
}
