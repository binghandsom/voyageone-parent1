package com.voyageone.base.dao.mongodb;

import com.mongodb.*;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.DateTimeUtil;
import org.jongo.query.Query;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * BaseMongoChannelDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMongoChannelDao<T> extends BaseJomgoDao<T> {

    public static final String SPLIT_PART = "c";

    public String getCollectionName(String channelId) {
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

    /**
     * 使用此方法时必须注意，此处只会更新符合条件的第一条数据
     */
    public T findAndModify(JomgoUpdate updateObject, String channelId) {
        return mongoTemplate.findAndModify(updateObject, entityClass, getCollectionName(channelId));
    }

    public long count(String channelId) {
        return mongoTemplate.count(getCollectionName(channelId));
    }

    public long countByQuery(final String strQuery, String channelId) {
        return mongoTemplate.count(strQuery, getCollectionName(channelId));
    }

    public long countByQuery(final String strQuery, Object[] parameters, String channelId) {
        return mongoTemplate.count(strQuery, parameters, getCollectionName(channelId));
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

    public WriteResult updateFirst(JomgoUpdate updObj, String channelId) {
        return mongoTemplate.updateFirst(updObj, getCollectionName(channelId));
    }

    public WriteResult updateMulti(JomgoUpdate updObj, String channelId) {
        return mongoTemplate.updateMulti(updObj, getCollectionName(channelId));
    }

    /**
     * 根据条件更新指定值 （推荐使用updateFirst()/updateMulti()）
     * @param channelId String
     * @param paraMap 更新条件
     * @param rsMap 更新操作，参数中必须明确指定操作类型如 $set, $addToSet等等，例如：{'$set':{'creater':'LAOWANG'}}
     * @return WriteResult
     */
    @Deprecated // 推荐使用 updateFirst(JomgoUpdate updObj, String channelId) 或 updateMulti(JomgoUpdate updObj, String channelId)
    public WriteResult update(String channelId, Map paraMap, Map rsMap) {
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BasicDBObject params = new BasicDBObject();
        params.putAll(paraMap);
        BasicDBObject result = new BasicDBObject();
        result.putAll(rsMap);
        return coll.update(params, result, false, true);
    }

    /**
     * 批量更新记录
     * @param channelId 渠道ID
     * @param bulkList  更新条件
     * @param modifier  更新者
     * @param key       MongoKey $pop,$set,$push,$addToSet
     * @param isUpsert  是否插入新纪录
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateWithMap(String channelId, List<BulkUpdateModel> bulkList, String modifier, String key, boolean isUpsert) {
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BulkWriteOperation bwo = coll.initializeOrderedBulkOperation();

        //设置更新者和更新时间
        BasicDBObject modifierObj = new BasicDBObject();
        if (modifier != null) {
            modifierObj.append("modifier", modifier);
        }
        //modifierObj.append("modified", DateTimeUtil.getNowTimeStamp());

        for (BulkUpdateModel model: bulkList){
            if (model.getQueryMap() == null) {
                // 如果没有查询条件，则认为是插入数据
                BasicDBObject updateContent = setDBObjectWithMap(model.getUpdateMap());
                updateContent.put("modifier", modifier);
                updateContent.put("modified", DateTimeUtil.getNowTimeStamp());
                updateContent.put("creater", modifier);
                updateContent.put("created", DateTimeUtil.getNowTimeStamp());
                bwo.insert(updateContent);
            } else {
                //生成更新对象
                BasicDBObject updateObj = new BasicDBObject();
                BasicDBObject updateContent = setDBObjectWithMap(model.getUpdateMap());

                //设置更新者和更新时间
                if ("$set".equals(key)) {
                    updateContent.putAll(modifierObj.toMap());
                } else {
                    if (!modifierObj.isEmpty()) {
                        updateObj.append("$set", modifierObj);
                    }
                }
                updateObj.append(key, updateContent);

                //生成查询对象
                BasicDBObject queryObj = setDBObjectWithMap(model.getQueryMap());

                if (isUpsert) {
                    bwo.find(queryObj).upsert().update(updateObj);
                } else {
                    bwo.find(queryObj).update(updateObj);
                }
            }
        }
        //最终批量运行
        return bwo.execute();
    }

    /**
     * 批量更新记录 (此方法只支持更新操作，不支持upsert)
     * @param channelId 渠道ID
     * @param bulkList  更新条件
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateWithModel(String channelId, List<BulkUpdateModel> bulkList) {
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BulkWriteOperation bwo = coll.initializeOrderedBulkOperation();

        for (BulkUpdateModel model: bulkList) {
            //生成更新对象
            BasicDBObject updateObj = new BasicDBObject();
            BasicDBObject updateContent = setDBObjectWithMap(model.getUpdateMap());
            updateObj.append("$set", updateContent);

            //生成查询对象
            BasicDBObject queryObj = setDBObjectWithMap(model.getQueryMap());

            bwo.find(queryObj).update(updateObj);
        }
        //最终批量运行
        return bwo.execute();
    }

    /**
     * 批量更新记录 (此方法只支持更新操作，不支持upsert)
     * @param channelId 渠道ID
     * @param bulkList  更新条件
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateWithJomgo(String channelId, List<JomgoUpdate> bulkList) {
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BulkWriteOperation bwo = coll.initializeOrderedBulkOperation();

        for (JomgoUpdate model: bulkList) {
            //生成更新对象
            Query updObj = mongoTemplate.jongo.createQuery(model.getUpdate(), model.getUpdateParameters());

            //生成查询对象
            Query queryObj = mongoTemplate.jongo.createQuery(model.getQuery(), model.getQueryParameters());

            bwo.find(queryObj.toDBObject()).update(updObj.toDBObject());
        }
        //最终批量运行
        return bwo.execute();
    }

    /**
     * 根据 传入Map批量设置BasicDBObject
     * @param map 条件或者值得MAP
     * @return 处理好的结果
     */
    public BasicDBObject setDBObjectWithMap(Map<String, Object> map) {
        BasicDBObject result = new BasicDBObject();
        result.putAll(map);
        return result;
    }

    /**
     * 聚合查询
     * @return List<Map> 返回的Map数据结构和aggregate语句对应
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> aggregateToMap(String channelId, List<JomgoAggregate> aggregateList) {
        JomgoAggregate[] aggregates = aggregateList.toArray(new JomgoAggregate[aggregateList.size()]);
        return aggregateToMap(channelId, aggregates);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> aggregateToMap(String channelId, JomgoAggregate... aggregates) {
        return (List<Map<String, Object>>) aggregateToObj(Map.class, getCollectionName(channelId), aggregates);
    }

    /**
     * 聚合查询<br>
     * 必须注意：这里的Model不能简单使用表定义对应的Model，而是要和aggregate语句对应(要定义新的Model/Dao)，否则查询无正确数据
     */
    @SuppressWarnings("unchecked")
    public List<T> aggregateToObj(String channelId, List<JomgoAggregate> aggregateList) {
        JomgoAggregate[] aggregates = aggregateList.toArray(new JomgoAggregate[aggregateList.size()]);
        return aggregateToObj(channelId, aggregates);
    }
    public List<T> aggregateToObj(String channelId, JomgoAggregate... aggregates) {
        return aggregateToObj(entityClass, getCollectionName(channelId), aggregates);
    }
}