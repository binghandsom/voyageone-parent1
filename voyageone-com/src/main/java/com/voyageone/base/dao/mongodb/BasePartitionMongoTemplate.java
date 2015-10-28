package com.voyageone.base.dao.mongodb;


import com.mongodb.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public class BasePartitionMongoTemplate implements ApplicationContextAware {
    @Autowired
    MongoTemplate mongoTemplate;

    public <T> CloseableIterator<T> stream(final Query query, final Class<T> entityType) {
        throw new RuntimeException("not support!");
    }

    public CommandResult executeCommand(String jsonCommand) {
        return mongoTemplate.executeCommand(jsonCommand);
    }

    public CommandResult executeCommand(final DBObject command) {
        return mongoTemplate.executeCommand(command);
    }

    @Deprecated
    public CommandResult executeCommand(final DBObject command, final int options) {
        return mongoTemplate.executeCommand(command, options);
    }

    public CommandResult executeCommand(final DBObject command, final ReadPreference readPreference) {
        return mongoTemplate.executeCommand(command, readPreference);
    }

    public void executeQuery(Query query, String collectionName, DocumentCallbackHandler dch) {
        mongoTemplate.executeQuery(query, collectionName, dch);
    }

    public <T> T execute(DbCallback<T> action) {
        return mongoTemplate.execute(action);
    }

    @Deprecated
    public <T> T executeInSession(final DbCallback<T> action) {
        return mongoTemplate.executeInSession(action);
    }

    public <T> T execute(String collectionName, CollectionCallback<T> callback) {
        return mongoTemplate.execute(collectionName, callback);
    }

    public DBCollection createCollection(final String collectionName) {
        return mongoTemplate.createCollection(collectionName);
    }

    public DBCollection createCollection(final String collectionName, final CollectionOptions collectionOptions) {
        return mongoTemplate.createCollection(collectionName, collectionOptions);
    }

    public DBCollection getCollection(final String collectionName) {
        return mongoTemplate.getCollection(collectionName);
    }

    public boolean collectionExists(final String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }

    public void dropCollection(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }

    public IndexOperations indexOps(String collectionName) {
        return mongoTemplate.indexOps(collectionName);
    }

    public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
        return mongoTemplate.findOne(query, entityClass, collectionName);
    }

    public boolean exists(Query query, String collectionName) {
        return mongoTemplate.exists(query, collectionName);
    }

    public boolean exists(Query query, Class<?> entityClass, String collectionName) {
        return mongoTemplate.exists(query, entityClass, collectionName);
    }

    public <T> List<T> find(final Query query, Class<T> entityClass, String collectionName) {
        return mongoTemplate.find(query, entityClass, collectionName);
    }

    public <T> T findById(Object id, Class<T> entityClass, String collectionName) {
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass, String collectionName) {
        return mongoTemplate.geoNear(near, entityClass, collectionName);
    }

    public <T> T findAndModify(Query query, Update update, Class<T> entityClass, String collectionName) {
        return mongoTemplate.findAndModify(query, update, entityClass, collectionName);
    }

    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass, String collectionName) {
        return mongoTemplate.findAndModify(query, update, options, entityClass, collectionName);
    }

    public <T> T findAndRemove(Query query, Class<T> entityClass, String collectionName) {
        return mongoTemplate.findAndRemove(query, entityClass, collectionName);
    }

    public long count(final Query query, String collectionName) {
        return mongoTemplate.count(query, collectionName);
    }

    public long count(Query query, Class<?> entityClass, String collectionName) {
        return mongoTemplate.count(query, entityClass, collectionName);
    }

    public void insert(Object objectToSave, String collectionName) {
        mongoTemplate.insert(objectToSave, collectionName);
    }

    public void insert(Collection<? extends Object> batchToSave, String collectionName) {
        mongoTemplate.insert(batchToSave, collectionName);
    }

    public void save(Object objectToSave, String collectionName) {
        mongoTemplate.save(objectToSave, collectionName);
    }

    public WriteResult upsert(Query query, Update update, String collectionName) {
        return mongoTemplate.upsert(query, update, collectionName);
    }

    public WriteResult upsert(Query query, Update update, Class<?> entityClass, String collectionName) {
        return mongoTemplate.upsert(query, update, entityClass, collectionName);
    }

    public WriteResult updateFirst(final Query query, final Update update, final String collectionName) {
        return mongoTemplate.updateFirst(query, update, collectionName);
    }

    public WriteResult updateFirst(Query query, Update update, Class<?> entityClass, String collectionName) {
        return mongoTemplate.updateFirst(query, update, entityClass, collectionName);
    }

    public WriteResult updateMulti(final Query query, final Update update, String collectionName) {
        return mongoTemplate.updateMulti(query, update, collectionName);
    }

    public WriteResult updateMulti(final Query query, final Update update, Class<?> entityClass, String collectionName) {
        return mongoTemplate.updateMulti(query, update, entityClass, collectionName);
    }

    public WriteResult remove(Object object, String collection) {
        return mongoTemplate.remove(object, collection);
    }

    public WriteResult remove(Query query, String collectionName) {
        return mongoTemplate.remove(query, collectionName);
    }

    public WriteResult remove(Query query, Class<?> entityClass, String collectionName) {
        return mongoTemplate.remove(query, entityClass, collectionName);
    }

    public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
        return mongoTemplate.findAll(entityClass, collectionName);
    }

    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction,
                                             Class<T> entityClass) {
        return mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, entityClass);
    }

    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction,
                                             MapReduceOptions mapReduceOptions, Class<T> entityClass) {
        return mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
    }

    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction,
                                             String reduceFunction, Class<T> entityClass) {
        return mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, entityClass);
    }

    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction,
                                             String reduceFunction, MapReduceOptions mapReduceOptions, Class<T> entityClass) {
        return mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
    }

    public <T> GroupByResults<T> group(String inputCollectionName, GroupBy groupBy, Class<T> entityClass) {
        return mongoTemplate.group(inputCollectionName, groupBy, entityClass);
    }

    public <T> GroupByResults<T> group(Criteria criteria, String inputCollectionName, GroupBy groupBy,
                                       Class<T> entityClass) {
        return mongoTemplate.group(criteria, inputCollectionName, groupBy, entityClass);
    }

    public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, String inputCollectionName, Class<O> outputType) {
        return mongoTemplate.aggregate(aggregation, inputCollectionName, outputType);
    }

    public <O> AggregationResults<O> aggregate(Aggregation aggregation, String collectionName, Class<O> outputType) {
        return mongoTemplate.aggregate(aggregation, collectionName, outputType);
    }

    public <T> List<T> findAllAndRemove(Query query, String collectionName) {
        return mongoTemplate.findAllAndRemove(query, collectionName);
    }

    public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass, String collectionName) {
        return mongoTemplate.findAllAndRemove(query, entityClass, collectionName);
    }

//    public Set<String> getCollectionNames() {
//        return mongoTemplate.getCollectionNames();
//    }

    public String getCollectionName(Object entity, String extend) {
        return getCollectionName(entity.getClass(), extend);
    }

    public String getCollectionName(Class<?> entityClass, String extend) {
        return mongoTemplate.getCollectionName(entityClass) + extend;
    }

    public DB getDb() {
        return mongoTemplate.getDb();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        mongoTemplate.setApplicationContext(applicationContext);
    }
}
