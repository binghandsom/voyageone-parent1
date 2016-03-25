package com.voyageone.base.dao.mongodb;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

import java.util.*;

/**
 * BaseMongoDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMongoDao<T> extends BaseJomgoDao<T> {

    public DBCollection getDBCollection() {
        return mongoTemplate.getDBCollection(collectionName);
    }

    public T selectOne() {
        return mongoTemplate.findOne(entityClass, collectionName);
    }

    public T selectOneWithQuery(String strQuery) {
        return mongoTemplate.findOne(strQuery, entityClass, collectionName);
    }

    public T selectOneWithQuery(JomgoQuery queryObject) {
        return mongoTemplate.findOne(queryObject, entityClass, collectionName);
    }

    public List<T> selectAll() {
        return mongoTemplate.findAll(entityClass, collectionName);
    }

    public Iterator<T> selectCursorAll() {
        return mongoTemplate.findCursorAll(entityClass, collectionName);
    }

    public List<T> select(final String strQuery) {
        return mongoTemplate.find(strQuery, null, entityClass, collectionName);
    }

    public List<T> selectWithProjection(final String strQuery, String projection) {
        return mongoTemplate.find(strQuery, projection, entityClass, collectionName);
    }

    public List<T> select(JomgoQuery queryObject) {
        return mongoTemplate.find(queryObject, entityClass, collectionName);
    }

    public Iterator<T> selectCursor(final String strQuery) {
        return mongoTemplate.findCursor(strQuery, null, entityClass, collectionName);
    }

    public Iterator<T> selectCursor(JomgoQuery queryObject) {
        return mongoTemplate.findCursor(queryObject, entityClass, collectionName);
    }

    public T selectById(String id) {
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public T findAndModify(JomgoUpdate updateObject) {
        return mongoTemplate.findAndModify(updateObject, entityClass, collectionName);
    }

    public long count() {
        return mongoTemplate.count(collectionName);
    }

    public long countByQuery(final String strQuery) {
        return mongoTemplate.count(strQuery, collectionName);
    }

    public WriteResult deleteById(String id) {
        return mongoTemplate.removeById(id, collectionName);
    }

    public CommandResult deleteAll() {
        String commandStr = String.format("{ delete:\"%s\", deletes:[ { q: { }, limit: 0 } ] }", collectionName);
        return mongoTemplate.executeCommand(commandStr);
    }

    public WriteResult deleteWithQuery(String strQuery) {
        return mongoTemplate.remove(strQuery, collectionName);
    }

    public WriteResult updateFirst(String strQuery, String strUpdate) {
        return mongoTemplate.updateFirst(strQuery, strUpdate, collectionName);
    }

    public WriteResult upsertFirst(String strQuery, String strUpdate) {
        return mongoTemplate.upsertFirst(strQuery, strUpdate, collectionName);
    }
}