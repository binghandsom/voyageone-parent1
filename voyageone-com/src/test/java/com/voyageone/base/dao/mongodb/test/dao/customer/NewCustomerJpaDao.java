package com.voyageone.base.dao.mongodb.test.dao.customer;

import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.test.model.customer.NewCustomer;
import org.springframework.aop.framework.Advised;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCustomerJpaDao extends MongoRepository<NewCustomer, String> {


    NewCustomer findByFirstname(String firstName);
//    public Iterable<NewCustomer> findByFirstName(String firstName);
//    public Iterable<NewCustomer> findByFirstNameRegex(String firstName);

    default void saveWithDBObject(DBObject entity) {
        MongoOperations mongoOperations = getMongoOperations();
        mongoOperations.save(entity, "newCustomer");
    }

    default MongoOperations getMongoOperations() {
        try {
            BaseMongoDao baseMongoDao = (BaseMongoDao) ((Advised)this).getTargetSource().getTarget();
            return baseMongoDao.getMongoOperations();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
