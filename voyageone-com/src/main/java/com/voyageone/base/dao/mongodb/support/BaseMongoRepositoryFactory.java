package com.voyageone.base.dao.mongodb.support;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.QueryDslMongoRepository;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

/**
 * Created by DELL on 2015/10/21.
 */
public class BaseMongoRepositoryFactory extends MongoRepositoryFactory {
    /**
     * Creates a new {@link MongoRepositoryFactory} with the given {@link MongoOperations}.
     *
     * @param mongoOperations must not be {@literal null}.
     */
    public BaseMongoRepositoryFactory(MongoOperations mongoOperations) {
        super(mongoOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

        boolean isQueryDslRepository = QUERY_DSL_PRESENT
                && QueryDslPredicateExecutor.class.isAssignableFrom(metadata.getRepositoryInterface());

        return isQueryDslRepository ? QueryDslMongoRepository.class : BaseMongoDao.class;
    }
}
