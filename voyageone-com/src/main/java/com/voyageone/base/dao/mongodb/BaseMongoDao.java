package com.voyageone.base.dao.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * BaseMongoDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMongoDao<T> extends BaseJongoDao<T> {

    public DBCollection getDBCollection() {
        return mongoTemplate.getDBCollection(collectionName);
    }

    public T selectOne() {
        return mongoTemplate.findOne(entityClass, collectionName);
    }

    public T selectOneWithQuery(String strQuery) {
        return mongoTemplate.findOne(strQuery, entityClass, collectionName);
    }

    public T selectOneWithQuery(JongoQuery queryObject) {
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

    public List<T> select(JongoQuery queryObject) {
        return mongoTemplate.find(queryObject, entityClass, collectionName);
    }

    public Iterator<T> selectCursor(final String strQuery) {
        return mongoTemplate.findCursor(strQuery, null, entityClass, collectionName);
    }

    public Iterator<T> selectCursor(JongoQuery queryObject) {
        return mongoTemplate.findCursor(queryObject, entityClass, collectionName);
    }

    public T selectById(String id) {
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public T findAndModify(JongoUpdate updateObject) {
        return mongoTemplate.findAndModify(updateObject, entityClass, collectionName);
    }

    public long count() {
        return mongoTemplate.count(collectionName);
    }

    public long countByQuery(final String strQuery) {
        return mongoTemplate.count(strQuery, collectionName);
    }

    public long countByQuery(final String strQuery, Object[] parameters) {
        return mongoTemplate.count(strQuery, parameters, collectionName);
    }

    public long countByQuery(JongoQuery queryObject) {
        return mongoTemplate.count(queryObject.getQuery(), collectionName);
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

    /**
     * 聚合查询
     * @return List<Map> 返回的Map数据结构和aggregate语句对应
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> aggregateToMap(List<JongoAggregate> aggregateList) {
        JongoAggregate[] aggregates = aggregateList.toArray(new JongoAggregate[aggregateList.size()]);
        return aggregateToMap(aggregates);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> aggregateToMap(JongoAggregate... aggregates) {
        return (List<Map<String, Object>>) aggregateToObj(Map.class, collectionName, aggregates);
    }

    /**
     * 聚合查询<br>
     * 必须注意：这里的Model不能简单使用表定义对应的Model，而是要和aggregate语句对应(要定义新的Model/Dao)，否则查询无正确数据
     */
    public List<T> aggregateToObj(List<JongoAggregate> aggregateList) {
        JongoAggregate[] aggregates = aggregateList.toArray(new JongoAggregate[aggregateList.size()]);
        return aggregateToObj(aggregates);
    }
    public List<T> aggregateToObj(JongoAggregate... aggregates) {
        return aggregateToObj(entityClass, collectionName, aggregates);
    }

    public <I>  AggregationResults<I> aggregateToObj(Aggregation aggregates, Class<I> cls) {
        return mongoTemplate.getMongoTemplate().aggregate(aggregates,collectionName,cls);
    }
}