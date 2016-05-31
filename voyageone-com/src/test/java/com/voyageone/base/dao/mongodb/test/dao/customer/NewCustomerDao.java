package com.voyageone.base.dao.mongodb.test.dao.customer;

import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by DELL on 2015/10/23.
 */

@Repository
public class NewCustomerDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveWithDBObject(DBObject entity) {
        mongoTemplate.save(entity);
    }

}
