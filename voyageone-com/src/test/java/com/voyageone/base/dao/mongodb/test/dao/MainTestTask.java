package com.voyageone.base.dao.mongodb.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by DELL on 2015/10/21.
 */
@Component("MainTestTask")
public class MainTestTask {

    @Autowired
    CustomerDao customerDao;


    public void testSelect() {

        // start
        Customer customer = new Customer("aa", "bb");
        customerDao.save(customer);

        System.out.println(customerDao.count());

        Iterable<Customer> list = customerDao.findAll();
        for (Customer row : list) {
            System.out.println("findByLastName:=" + row);
            customerDao.delete(row);
        }

    }
}
