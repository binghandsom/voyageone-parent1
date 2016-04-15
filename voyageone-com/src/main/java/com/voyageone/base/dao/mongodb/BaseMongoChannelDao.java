package com.voyageone.base.dao.mongodb;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.Iterator;
import java.util.List;

/**
 * BaseMongoChannelDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMongoChannelDao<T> extends BaseJomgoDao<T> {

    public final static String SPLIT_PART = "c";

    protected String getCollectionName(String channelId) {
        return mongoTemplate.getCollectionName(this.collectionName, channelId, SPLIT_PART);
    }

    public DBCollection getDBCollection(String channelId) {
        return mongoTemplate.getDBCollection(getCollectionName(channelId));
    }

    public T selectOne(String channelId) {
        return mongoTemplate.findOne(entityClass, getCollectionName(channelId));
    }

    public T selectOneWithQuery(String strQuery, String channelId) {
        return mongoTemplate.findOne(strQuery, entityClass, getCollectionName(channelId));
    }

    public T selectOneWithQuery(JomgoQuery queryObject, String channelId) {
        return mongoTemplate.findOne(queryObject, entityClass, getCollectionName(channelId));
    }

    public List<T> selectAll(String channelId) {
        return mongoTemplate.findAll(entityClass, getCollectionName(channelId));
    }

    public Iterator<T> selectCursorAll(String channelId) {
        return mongoTemplate.findCursorAll(entityClass, getCollectionName(channelId));
    }

    public List<T> select(final String strQuery, String channelId) {
        return mongoTemplate.find(strQuery, null, entityClass, getCollectionName(channelId));
    }

    public List<T> selectWithProjection(final String strQuery, String projection, String channelId) {
        return mongoTemplate.find(strQuery, projection, entityClass, getCollectionName(channelId));
    }

    public List<T> select(JomgoQuery queryObject, String channelId) {
        return mongoTemplate.find(queryObject, entityClass, getCollectionName(channelId));
    }

    public Iterator<T> selectCursor(final String strQuery, String channelId) {
        return mongoTemplate.findCursor(strQuery, null, entityClass, getCollectionName(channelId));
    }

    public Iterator<T> selectCursor(JomgoQuery queryObject, String channelId) {
        return mongoTemplate.findCursor(queryObject, entityClass, getCollectionName(channelId));
    }

    public T selectById(String id, String channelId) {
        return mongoTemplate.findById(id, entityClass, getCollectionName(channelId));
    }

    public T findAndModify(JomgoUpdate updateObject, String channelId) {
        return mongoTemplate.findAndModify(updateObject, entityClass, getCollectionName(channelId));
    }

    public long count(String channelId) {
        return mongoTemplate.count(getCollectionName(channelId));
    }

    public long countByQuery(final String strQuery, String channelId) {
        return mongoTemplate.count(strQuery, getCollectionName(channelId));
    }

    public WriteResult deleteById(String id, String channelId) {
        return mongoTemplate.removeById(id, getCollectionName(channelId));
    }

    public CommandResult deleteAll(String channelId) {
        String commandStr = String.format("{ delete:\"%s\", deletes:[ { q: { }, limit: 0 } ] }", getCollectionName(channelId));
        return mongoTemplate.executeCommand(commandStr);
    }

    public WriteResult deleteWithQuery(String strQuery, String channelId) {
        return mongoTemplate.remove(strQuery, getCollectionName(channelId));
    }

    public WriteResult updateFirst(String strQuery, String strUpdate, String channelId) {
        return mongoTemplate.updateFirst(strQuery, strUpdate, getCollectionName(channelId));
    }

    public WriteResult upsertFirst(String strQuery, String strUpdate, String channelId) {
        return mongoTemplate.upsertFirst(strQuery, strUpdate, getCollectionName(channelId));
    }
}