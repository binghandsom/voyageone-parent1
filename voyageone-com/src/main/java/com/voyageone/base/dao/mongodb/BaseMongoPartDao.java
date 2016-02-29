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
public abstract class BaseMongoPartDao extends BaseJomgoDao {

    public DBCollection getDBCollection(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.getDBCollection(collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> T selectOne(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne((Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> T selectOneWithQuery(String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(strQuery, (Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> T selectOneWithQuery(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(queryObject, (Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> selectAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findAll((Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> Iterator<T> selectCursorAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursorAll((Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> select(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> selectWithProjection(final String strQuery, String projection, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, projection, (Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> select(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(queryObject, (Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> Iterator<T> selectCursor(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursor(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> Iterator<T> selectCursor(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursor(queryObject, (Class<T>) entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> T selectById(String id, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findById(id, (Class<T>) entityClass, collectionName);
    }

    public <T> T findAndModify(JomgoUpdate updateObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findAndModify(updateObject, (Class<T>) entityClass, collectionName);
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