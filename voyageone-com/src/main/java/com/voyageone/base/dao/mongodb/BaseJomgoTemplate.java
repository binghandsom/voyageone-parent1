package com.voyageone.base.dao.mongodb;

import com.mongodb.WriteResult;
import org.jongo.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.List;

public class BaseJomgoTemplate extends BaseJomgoPartTemplate {

    public BaseJomgoTemplate(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    protected String getCollectionName(Class<?> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }

    public <T> T findOne(Class<T> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return findOne(entityClass, collectionName);
    }

    public <T> T findOne(String strQuery, Class<T> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return findOne(strQuery, entityClass, collectionName);
    }

    public boolean exists(String strQuery, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return exists(strQuery, collectionName);
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return findAll(entityClass, collectionName);
    }

    public <T> List<T> find(final String strQuery, Class<T> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return find(strQuery, entityClass, collectionName);
    }

    public <T> T findById(String id, Class<T> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return findById(id, entityClass, collectionName);
    }

//    public void findAndModify(String strQuery, String strUpdate) {
//        String collectionName = getCollectionName(entityClass);
//        findAndModify(strQuery, strUpdate, collectionName);
//    }
//
//    public void findAndRemove(String strQuery) {
//        String collectionName = getCollectionName(entityClass);
//        findAndRemove(strQuery, collectionName);
//    }


    public WriteResult insert(Object objectToSave) {
        String collectionName = getCollectionName(objectToSave.getClass());
        return insert(objectToSave, collectionName);
    }

    public WriteResult insert(Collection<? extends Object> batchToSaves) {
        String collectionName = null;
        if (batchToSaves != null && batchToSaves.size()>0) {
            collectionName = getCollectionName(batchToSaves.iterator().next().getClass());
        }
        return insert(batchToSaves, collectionName);
    }

    public WriteResult save(Object objectToSave) {
        String collectionName = getCollectionName(objectToSave.getClass());
        return save(objectToSave, collectionName);
    }

    public WriteResult updateFirst(final String strQuery, final String strUpdate, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return updateFirst(strQuery, strUpdate, collectionName);
    }

    public WriteResult updateMulti(final String strQuery, final String strUpdate, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return updateMulti(strQuery, strUpdate, collectionName);
    }

    public WriteResult upsert(String strQuery, String strUpdate, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return upsert(strQuery, strUpdate, collectionName);
    }

    public WriteResult removeById(String id, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return removeById(id, collectionName);
    }

    public WriteResult removeAll(Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return removeAll(collectionName);
    }

    public WriteResult remove(String strQuery, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return remove(strQuery, collectionName);
    }

    public Distinct distinct(String key, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return distinct(key, collectionName);
    }

    public Aggregate aggregate(String pipelineOperator, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        return aggregate(pipelineOperator, collectionName);
    }

    public Aggregate aggregate(String pipelineOperator, Class<?> entityClass, Object... parameters) {
        String collectionName = getCollectionName(entityClass);
        return aggregate(pipelineOperator, collectionName, parameters);
    }

//    public void drop(Class<?> entityClass) {
//        String collectionName = getCollectionName(entityClass);
//        drop(collectionName);
//    }

    public void dropIndex(String keys, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        dropIndex(keys, collectionName);
    }

    public void ensureIndex(String keys, Class<?> entityClass) {
        String collectionName = getCollectionName(entityClass);
        ensureIndex(keys, collectionName);
    }

}
