package com.voyageone.base.dao.mongodb.support;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.QueryDslMongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;


public class BaseMongoRepositoryFactory extends MongoRepositoryFactory {
    public BaseMongoRepositoryFactory(MongoOperations mongoOperations) {
        super(mongoOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        boolean isQueryDslRepository = QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(metadata.getRepositoryInterface());
        Class<?> result = null;
        if (isQueryDslRepository) {
            result = QueryDslMongoRepository.class;
        } else {
//            if (ChannelPartitionID.class.isAssignableFrom(metadata.getIdType())) {
//                result = BasePartitionMongoDao.class;
//            } else {
                result = BaseMongoDao.class;
//            }
        }
        return result;
    }
}
