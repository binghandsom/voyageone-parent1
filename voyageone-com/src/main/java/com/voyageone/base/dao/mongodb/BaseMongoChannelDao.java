package com.voyageone.base.dao.mongodb;

import com.mongodb.*;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;

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

    /**
     * 根据条件更新指定值
     * @param channelId String
     * @param paraMap 更新条件
     * @param rsMap 更新操作，参数中必须明确指定操作类型如 $set, $addToSet等等，例如：{'$set':{'creater':'LAOWANG'}}
     * @return WriteResult
     */
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

            //生成更新对象
            BasicDBObject updateObj = new BasicDBObject();
            BasicDBObject updateContent = setDBObjectWithMap(model.getUpdateMap());

            //设置更新者和更新时间
            if ("$set".equals(key)) {
                updateContent.putAll(modifierObj.toMap());
            } else {
                updateObj.append("$set", modifierObj);
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
}