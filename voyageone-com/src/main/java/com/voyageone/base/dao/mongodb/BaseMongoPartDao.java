package com.voyageone.base.dao.mongodb;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

import java.util.Iterator;
import java.util.List;

/**
 * BaseMongoPartDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMongoPartDao<T> extends BaseJomgoDao<T> {

    public DBCollection getDBCollection(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.getDBCollection(collectionName);
    }

    public T selectOne(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(entityClass, collectionName);
    }

    public T selectOneWithQuery(String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(strQuery, entityClass, collectionName);
    }

    public T selectOneWithQuery(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(queryObject, entityClass, collectionName);
    }

    public List<T> selectAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findAll(entityClass, collectionName);
    }

    public Iterator<T> selectCursorAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursorAll(entityClass, collectionName);
    }

    public List<T> select(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, null, entityClass, collectionName);
    }

    public List<T> selectWithProjection(final String strQuery, String projection, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, projection, entityClass, collectionName);
    }

    public List<T> select(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(queryObject, entityClass, collectionName);
    }

    public Iterator<T> selectCursor(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursor(strQuery, null, entityClass, collectionName);
    }

    public Iterator<T> selectCursor(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursor(queryObject, entityClass, collectionName);
    }

    public T selectById(String id, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public T findAndModify(JomgoUpdate updateObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findAndModify(updateObject, entityClass, collectionName);
    }

    public long count(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.count(collectionName);
    }

    public long countByQuery(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.count(strQuery, collectionName);
    }

    public WriteResult deleteById(String id, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.removeById(id, collectionName);
    }

    public CommandResult deleteAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        String commandStr = String.format("{ delete:\"%s\", deletes:[ { q: { }, limit: 0 } ] }", collectionName);
        return mongoTemplate.executeCommand(commandStr);
    }

    public CommandResult renameCollection(String channelId, String newCollectionName) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        String newCollectionNameT = mongoTemplate.getCollectionName(newCollectionName, channelId);
        String commandStr = String.format("{ renameCollection : '%s', to: '%s', dropTarget: true}", collectionName, newCollectionNameT);
        return mongoTemplate.executeCommand(commandStr);
    }

    public WriteResult deleteWithQuery(String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.remove(strQuery, collectionName);
    }

    public WriteResult updateFirst(String channelId, String strQuery, String strUpdate) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.updateFirst(strQuery, strUpdate, collectionName);
    }

    public WriteResult upsertFirst(String channelId, String strQuery, String strUpdate) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.upsertFirst(strQuery, strUpdate, collectionName);
    }
}