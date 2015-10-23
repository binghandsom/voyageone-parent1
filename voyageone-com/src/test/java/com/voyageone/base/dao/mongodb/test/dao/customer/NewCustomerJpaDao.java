package com.voyageone.base.dao.mongodb.test.dao.customer;

import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.test.model.customer.NewCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCustomerJpaDao extends MongoRepository<NewCustomer, String> {
    NewCustomer findByFirstname(String firstName);
//    public Iterable<NewCustomer> findByFirstName(String firstName);
//    public Iterable<NewCustomer> findByFirstNameRegex(String firstName);
}
