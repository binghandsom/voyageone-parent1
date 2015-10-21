package com.voyageone.base.dao.mongodb.support;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * Created by DELL on 2015/10/21.
 */
public class BaseMongoRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends MongoRepositoryFactoryBean<T, S, ID> {

    /**
     * Creates and initializes a {@link RepositoryFactorySupport} instance.
     *
     * @param operations
     * @return
     */
    protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
        return new BaseMongoRepositoryFactory(operations);
    }
}
