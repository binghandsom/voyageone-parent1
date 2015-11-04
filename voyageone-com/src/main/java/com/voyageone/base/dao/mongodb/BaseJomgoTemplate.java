package com.voyageone.base.dao.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.apache.commons.collections.IteratorUtils;
import org.bson.types.ObjectId;
import org.jongo.*;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.List;

public class BaseJomgoTemplate {
    protected MongoTemplate mongoTemplate;

    protected Jongo jongo;

    public BaseJomgoTemplate(MongoTemplate mongoTemplate) {
        jongo = new Jongo(mongoTemplate.getDb());
    }

    public Command executeCommand(String jsonCommand) {
        return jongo.runCommand(jsonCommand);
    }

    public void executeQuery(String strQuery, String collectionName) {
        //jongo..executeQuery(query, collectionName, dch);
    }

    public void createCollection(final String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    public void createCollection(final String collectionName, final CollectionOptions collectionOptions) {
        mongoTemplate.createCollection(collectionName, collectionOptions);
    }

    public MongoCollection getCollection(String collectionName) {
        return jongo.getCollection(collectionName);
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

    public <T> T findOne(Class<T> entityClass) {
        String collectionName = null;
        return findOne(entityClass, collectionName);
    }

    public <T> T findOne(Class<T> entityClass, String collectionName) {
        return getCollection(collectionName).findOne().as(entityClass);
    }

    public <T> T findOne(String strQuery, Class<T> entityClass) {
        String collectionName = null;
        return findOne(strQuery, entityClass, collectionName);
    }

    public <T> T findOne(String strQuery, Class<T> entityClass, String collectionName) {
        return getCollection(collectionName).findOne(strQuery).as(entityClass);
    }

    public boolean exists(String strQuery, Class<?> entityClass) {
        String collectionName = null;
        return exists(strQuery, collectionName);
    }

    public boolean exists(String strQuery, String collectionName) {
        return getCollection(collectionName).count(strQuery)>0 ? true:false;
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        String collectionName = null;
        return findAll(entityClass, collectionName);
    }

    public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
        return find("", entityClass, collectionName);
    }

    public <T> List<T> find(final String strQuery, Class<T> entityClass) {
        String collectionName = null;
        return find(strQuery, entityClass, collectionName);
    }

    public <T> List<T> find(final String strQuery, Class<T> entityClass, String collectionName) {
        return IteratorUtils.toList(getCollection(collectionName).find(strQuery).as(entityClass));
    }

    public <T> T findById(Object id, Class<T> entityClass) {
        String collectionName = null;
        return findById(id, entityClass, collectionName);
    }

    public <T> T findById(Object id, Class<T> entityClass, String collectionName) {
        String query = "{\"_id\":ObjectId(\"" + id.toString() + "\")}";
        return findOne(query, entityClass, collectionName);
    }

    public void findAndModify(String strQuery, String strUpdate) {
        String collectionName = null;
        findAndModify(strQuery, strUpdate, collectionName);
    }

    public void findAndModify(String strQuery, String strUpdate, String collectionName) {
        // ???
        getCollection(collectionName).update(strQuery).upsert().multi().with(strUpdate);
    }

    public void findAndRemove(String strQuery) {
        String collectionName = null;
        findAndRemove(strQuery, collectionName);
    }

    public void findAndRemove(String strQuery, String collectionName) {
        getCollection(collectionName).remove(strQuery);
    }

    public long count(final String strQuery) {
        String collectionName = null;
        return count(strQuery, collectionName);
    }

    public long count(final String strQuery, String collectionName) {
        return getCollection(collectionName).count(strQuery);
    }

    public void insert(Object objectToSave) {
        String collectionName = null;
        insert(objectToSave, collectionName);
    }

    public void insert(Object objectToSave, String collectionName) {
        getCollection(collectionName).insert(objectToSave);
    }

    public void insert(Collection<? extends Object> batchToSaves) {
        String collectionName = null;
        insert(batchToSaves, collectionName);
    }

    public void insert(Collection<? extends Object> batchToSaves, String collectionName) {
        getCollection(collectionName).insert(batchToSaves);
    }

    public void save(Object objectToSave) {
        String collectionName = null;
        save(objectToSave, collectionName);
    }

    public void save(Object objectToSave, String collectionName) {
        getCollection(collectionName).save(objectToSave);
    }

    public WriteResult upsert(String strQuery, String strUpdate, Class<?> entityClass) {
        String collectionName = null;
        return upsert(strQuery, strUpdate, collectionName);
    }

    public WriteResult upsert(String strQuery, String strUpdate, String collectionName) {
        //multi???
        return getCollection(collectionName).update(strQuery).upsert().multi().with(strUpdate);
    }

    public WriteResult updateFirst(final String strQuery, final String strUpdate, Class<?> entityClass) {
        String collectionName = null;
        return updateFirst(strQuery, strUpdate, collectionName);
    }

    public WriteResult updateFirst(final String strQuery, final String strUpdate, final String collectionName) {
        //???
        return null;
    }

    public WriteResult updateMulti(final String strQuery, final String strUpdate, Class<?> entityClass) {
        String collectionName = null;
        return updateMulti(strQuery, strUpdate, collectionName);
    }

    public WriteResult updateMulti(final String strQuery, final String strUpdate, final String collectionName) {
        //???
        return null;
    }

    public void remove(ObjectId id) {
        String collectionName = null;
        remove(id, collectionName);
    }

    public void remove(ObjectId id, String collectionName) {
        String query = "{\"_id\":ObjectId(\"" + id.toString() + "\")}";
        remove(query, collectionName);
    }

    public void removeAll(Class<?> entityClass) {
        String collectionName = null;
        removeAll(collectionName);
    }

    public void removeAll(String collectionName) {
        getCollection(collectionName).remove();
    }

    public void remove(String strQuery, Class<?> entityClass) {
        String collectionName = null;
        remove(strQuery, collectionName);
    }

    public void remove(String strQuery, String collectionName) {
        getCollection(collectionName).remove(strQuery);
    }

    public Distinct distinct(String key, Class<?> entityClass) {
        String collectionName = null;
        return distinct(key, collectionName);
    }

    public Distinct distinct(String key, String collectionName) {
        return getCollection(collectionName).distinct(key);
    }

    public Aggregate aggregate(String pipelineOperator, Class<?> entityClass) {
        String collectionName = null;
        return aggregate(pipelineOperator, collectionName);
    }

    public Aggregate aggregate(String pipelineOperator, String collectionName) {
        return getCollection(collectionName).aggregate(pipelineOperator);
    }

    public Aggregate aggregate(String pipelineOperator, Class<?> entityClass, Object... parameters) {
        String collectionName = null;
        return aggregate(pipelineOperator, collectionName, parameters);
    }

    public Aggregate aggregate(String pipelineOperator, String collectionName, Object... parameters) {
        return getCollection(collectionName).aggregate(pipelineOperator, parameters);
    }

    public void drop(Class<?> entityClass) {
        String collectionName = null;
        drop(collectionName);
    }

    public void drop(String collectionName) {
        getCollection(collectionName).drop();
    }

    public void dropIndex(String keys, Class<?> entityClass) {
        String collectionName = null;
        dropIndex(keys, collectionName);
    }

    public void dropIndex(String keys, String collectionName) {
        getCollection(collectionName).dropIndex(keys);
    }

    public void ensureIndex(String keys) {
        //???
        //getCollection(collectionName).createIndex(createQuery(keys).toDBObject());
    }
    public void ensureIndex(String keys, String options) {
        //???
    }

    public String getName() {
        //???
        return "";
        //return collection.getName();
    }

    public DBCollection getDBCollection(Class<?> entityClass) {
        String collectionName = null;
        return getDBCollection(collectionName);
    }

    public DBCollection getDBCollection(String collectionName) {
        return mongoTemplate.getCollection(collectionName);
    }
}
