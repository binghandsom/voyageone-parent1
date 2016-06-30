package com.voyageone.base.dao.mongodb;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * BaseMongoChannelDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMongoCartDao<T> extends BaseJomgoDao<T> {

    public static final String SPLIT_PART = "p";

    protected String getCollectionName(int cartId) {
        return mongoTemplate.getCollectionName(this.collectionName, String.valueOf(cartId), SPLIT_PART);
    }

    public DBCollection getDBCollection(int cartId) {
        return mongoTemplate.getDBCollection(getCollectionName(cartId));
    }

    public T selectOne(int cartId) {
        return mongoTemplate.findOne(entityClass, getCollectionName(cartId));
    }

    public T selectOneWithQuery(String strQuery, int cartId) {
        return mongoTemplate.findOne(strQuery, entityClass, getCollectionName(cartId));
    }

    public T selectOneWithQuery(JomgoQuery queryObject, int cartId) {
        return mongoTemplate.findOne(queryObject, entityClass, getCollectionName(cartId));
    }

    public List<T> selectAll(int cartId) {
        return mongoTemplate.findAll(entityClass, getCollectionName(cartId));
    }

    public Iterator<T> selectCursorAll(int cartId) {
        return mongoTemplate.findCursorAll(entityClass, getCollectionName(cartId));
    }

    public List<T> select(final String strQuery, int cartId) {
        return mongoTemplate.find(strQuery, null, entityClass, getCollectionName(cartId));
    }

    public List<T> selectWithProjection(final String strQuery, String projection, int cartId) {
        return mongoTemplate.find(strQuery, projection, entityClass, getCollectionName(cartId));
    }

    public List<T> select(JomgoQuery queryObject, int cartId) {
        return mongoTemplate.find(queryObject, entityClass, getCollectionName(cartId));
    }

    public Iterator<T> selectCursor(final String strQuery, int cartId) {
        return mongoTemplate.findCursor(strQuery, null, entityClass, getCollectionName(cartId));
    }

    public Iterator<T> selectCursor(JomgoQuery queryObject, int cartId) {
        return mongoTemplate.findCursor(queryObject, entityClass, getCollectionName(cartId));
    }

    public T selectById(String id, int cartId) {
        return mongoTemplate.findById(id, entityClass, getCollectionName(cartId));
    }

    public T findAndModify(JomgoUpdate updateObject, int cartId) {
        return mongoTemplate.findAndModify(updateObject, entityClass, getCollectionName(cartId));
    }

    public long count(int cartId) {
        return mongoTemplate.count(getCollectionName(cartId));
    }

    public long countByQuery(final String strQuery, int cartId) {
        return mongoTemplate.count(strQuery, getCollectionName(cartId));
    }

    public WriteResult deleteById(String id, int cartId) {
        return mongoTemplate.removeById(id, getCollectionName(cartId));
    }

    public CommandResult deleteAll(int cartId) {
        String commandStr = String.format("{ delete:\"%s\", deletes:[ { q: { }, limit: 0 } ] }", getCollectionName(cartId));
        return mongoTemplate.executeCommand(commandStr);
    }

    public WriteResult deleteWithQuery(String strQuery, int cartId) {
        return mongoTemplate.remove(strQuery, getCollectionName(cartId));
    }

    public WriteResult updateFirst(String strQuery, String strUpdate, int cartId) {
        return mongoTemplate.updateFirst(strQuery, strUpdate, getCollectionName(cartId));
    }

    public WriteResult upsertFirst(String strQuery, String strUpdate, int cartId) {
        return mongoTemplate.upsertFirst(strQuery, strUpdate, getCollectionName(cartId));
    }

    /**
     * 聚合查询
     * @return List<Map> 返回的Map数据结构和aggregate语句对应
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> aggregateToMap(int cartId, List<JomgoAggregate> aggregateList) {
        JomgoAggregate[] aggregates = aggregateList.toArray(new JomgoAggregate[aggregateList.size()]);
        return aggregateToMap(cartId, aggregates);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> aggregateToMap(int cartId, JomgoAggregate... aggregates) {
        return (List<Map<String, Object>>) aggregateToObj(Map.class, getCollectionName(cartId), aggregates);
    }

    /**
     * 聚合查询<br>
     * 必须注意：这里的Model不能简单使用表定义对应的Model，而是要和aggregate语句对应(要定义新的Model/Dao)，否则查询无正确数据
     */
    @SuppressWarnings("unchecked")
    public List<T> aggregateToObj(int cartId, List<JomgoAggregate> aggregateList) {
        JomgoAggregate[] aggregates = aggregateList.toArray(new JomgoAggregate[aggregateList.size()]);
        return aggregateToObj(cartId, aggregates);
    }
    public List<T> aggregateToObj(int cartId, JomgoAggregate... aggregates) {
        return aggregateToObj(entityClass, getCollectionName(cartId), aggregates);
    }
}