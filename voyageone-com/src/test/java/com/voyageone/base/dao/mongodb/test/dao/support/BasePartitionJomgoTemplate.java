package com.voyageone.base.dao.mongodb.test.dao.support;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.apache.commons.collections.IteratorUtils;
//import org.jongo.Jongo;
//import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
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

/**
 * Created by DELL on 2015/10/29.
 */
public class BasePartitionJomgoTemplate {
    @Autowired
    MongoTemplate mongoTemplate;

//    Jongo jongo = null;
//    private Jongo getJongo() {
//        if (jongo == null) {
//            jongo = new Jongo(mongoTemplate.getDb());
//        }
//        return jongo;
//    }
//
//    private MongoCollection getMongoCollection(String collectionName) {
//        return getJongo().getCollection(collectionName);
//    }
//
//    public <T> CloseableIterator<T> stream(final Query query, final Class<T> entityType) {
//        throw new RuntimeException("not support!");
//    }
//
//    public void executeCommand(String jsonCommand) {
//        getJongo().runCommand(jsonCommand);
//    }
//
//    public DBCollection createCollection(final String collectionName) {
//        return mongoTemplate.createCollection(collectionName);
//    }
//
//    public DBCollection createCollection(final String collectionName, final CollectionOptions collectionOptions) {
//        return mongoTemplate.createCollection(collectionName, collectionOptions);
//    }
//
//    public DBCollection getCollection(final String collectionName) {
//        return mongoTemplate.getCollection(collectionName);
//    }
//
//    public boolean collectionExists(final String collectionName) {
//        return mongoTemplate.collectionExists(collectionName);
//    }
//
//    public void dropCollection(String collectionName) {
//        mongoTemplate.dropCollection(collectionName);
//    }
//
//    public IndexOperations indexOps(String collectionName) {
//        return mongoTemplate.indexOps(collectionName);
//    }
//
//    public <T> T findOne(Class<T> entityClass, String collectionName) {
//        return getMongoCollection(collectionName).findOne().as(entityClass);
//    }
//
//    public <T> T findOne(final String query, Class<T> entityClass, String collectionName) {
//        return getMongoCollection(collectionName).findOne(query).as(entityClass);
//    }
//
//    public boolean exists(final String query, String collectionName) {
//        long count = getMongoCollection(collectionName).count(query);
//        if (count == 0) {
//            return false;
//        }
//        return true;
//    }
//
//    public <T> List<T> find(final String strQuery, Class<T> entityClass, String collectionName) {
//        return IteratorUtils.toList(getMongoCollection(collectionName).find(strQuery).as(entityClass));
//    }
//
//    public <T> T findById(Object id, Class<T> entityClass, String collectionName) {
//        String query = "{\"_id\":ObjectId(\"" + id.toString() + "\")}";
//        return findOne(query, entityClass, collectionName);
//    }
//
//    public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass, String collectionName) {
//        return mongoTemplate.geoNear(near, entityClass, collectionName);
//    }
//
//    public <T> T findAndModify(Query query, Update update, Class<T> entityClass, String collectionName) {
//        return mongoTemplate.findAndModify(query, update, entityClass, collectionName);
//    }
//
//    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass, String collectionName) {
//        return mongoTemplate.findAndModify(query, update, options, entityClass, collectionName);
//    }
//
//    public <T> T findAndRemove(Query query, Class<T> entityClass, String collectionName) {
//        return mongoTemplate.findAndRemove(query, entityClass, collectionName);
//    }
//
//    public long count(final Query query, String collectionName) {
//        return mongoTemplate.count(query, collectionName);
//    }
//
//    public long count(Query query, Class<?> entityClass, String collectionName) {
//        return mongoTemplate.count(query, entityClass, collectionName);
//    }
//
//    public void insert(Object objectToSave, String collectionName) {
//        mongoTemplate.insert(objectToSave, collectionName);
//    }
//
//    public void insert(Collection<? extends Object> batchToSave, String collectionName) {
//        mongoTemplate.insert(batchToSave, collectionName);
//    }
//
//    public void save(Object objectToSave, String collectionName) {
//        MongoCollection mongoCollection = getJongo().getCollection(collectionName);
//        mongoCollection.save(objectToSave);
//    }
//
//    public WriteResult upsert(Query query, Update update, String collectionName) {
//        return mongoTemplate.upsert(query, update, collectionName);
//    }
//
//    public WriteResult upsert(Query query, Update update, Class<?> entityClass, String collectionName) {
//        return mongoTemplate.upsert(query, update, entityClass, collectionName);
//    }
//
//    public WriteResult updateFirst(final Query query, final Update update, final String collectionName) {
//        return mongoTemplate.updateFirst(query, update, collectionName);
//    }
//
//    public WriteResult updateFirst(Query query, Update update, Class<?> entityClass, String collectionName) {
//        return mongoTemplate.updateFirst(query, update, entityClass, collectionName);
//    }
//
//    public WriteResult updateMulti(final Query query, final Update update, String collectionName) {
//        return mongoTemplate.updateMulti(query, update, collectionName);
//    }
//
//    public WriteResult updateMulti(final Query query, final Update update, Class<?> entityClass, String collectionName) {
//        return mongoTemplate.updateMulti(query, update, entityClass, collectionName);
//    }
//
//    public WriteResult remove(Object object, String collection) {
//        return mongoTemplate.remove(object, collection);
//    }
//
//    public WriteResult remove(Query query, String collectionName) {
//        return mongoTemplate.remove(query, collectionName);
//    }
//
//    public WriteResult remove(Query query, Class<?> entityClass, String collectionName) {
//        return mongoTemplate.remove(query, entityClass, collectionName);
//    }
//
//    public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
//        return mongoTemplate.findAll(entityClass, collectionName);
//    }
//
//    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction,
//                                             Class<T> entityClass) {
//        return mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, entityClass);
//    }
//
//    public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction,
//                                             MapReduceOptions mapReduceOptions, Class<T> entityClass) {
//        return mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
//    }
//
//    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction,
//                                             String reduceFunction, Class<T> entityClass) {
//        return mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, entityClass);
//    }
//
//    public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction,
//                                             String reduceFunction, MapReduceOptions mapReduceOptions, Class<T> entityClass) {
//        return mongoTemplate.mapReduce(query, inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
//    }
//
//    public <T> GroupByResults<T> group(String inputCollectionName, GroupBy groupBy, Class<T> entityClass) {
//        return mongoTemplate.group(inputCollectionName, groupBy, entityClass);
//    }
//
//    public <T> GroupByResults<T> group(Criteria criteria, String inputCollectionName, GroupBy groupBy,
//                                       Class<T> entityClass) {
//        return mongoTemplate.group(criteria, inputCollectionName, groupBy, entityClass);
//    }
//
//    public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, String inputCollectionName, Class<O> outputType) {
//        return mongoTemplate.aggregate(aggregation, inputCollectionName, outputType);
//    }
//
//    public <O> AggregationResults<O> aggregate(Aggregation aggregation, String collectionName, Class<O> outputType) {
//        return mongoTemplate.aggregate(aggregation, collectionName, outputType);
//    }
//
//    public <T> List<T> findAllAndRemove(Query query, String collectionName) {
//        return mongoTemplate.findAllAndRemove(query, collectionName);
//    }
//
//    public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass, String collectionName) {
//        return mongoTemplate.findAllAndRemove(query, entityClass, collectionName);
//    }
//
////    public Set<String> getCollectionNames() {
////        return mongoTemplate.getCollectionNames();
////    }
//
//    public String getCollectionName(Object entity, String extend) {
//        return getCollectionName(entity.getClass(), extend);
//    }
//
//    public String getCollectionName(Class<?> entityClass, String extend) {
//        return mongoTemplate.getCollectionName(entityClass) + extend;
//    }
//
//    public DB getDb() {
//        return mongoTemplate.getDb();
//    }

}
