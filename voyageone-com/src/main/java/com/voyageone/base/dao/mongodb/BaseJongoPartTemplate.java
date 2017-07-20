package com.voyageone.base.dao.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.CartPartitionModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.base.dao.mongodb.support.VOJacksonMapper;
import com.voyageone.base.exception.VoMongoException;
import com.voyageone.common.util.StringUtils;
import net.minidev.json.JSONObject;
import org.apache.commons.collections.IteratorUtils;
import org.bson.types.ObjectId;
import org.jongo.*;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.List;

/**
 * BaseJongoPartTemplate
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class BaseJongoPartTemplate {

    private static final Object[] NO_PARAMETERS = new Object[0];

    protected MongoTemplate mongoTemplate;

    protected Jongo jongo;

    public BaseJongoPartTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        try {
            Mapper mapper = new VOJacksonMapper.Builder().build();
            this.jongo = new Jongo(mongoTemplate.getDb(), mapper);
        } catch (Exception e) {
            throw new VoMongoException("MongoDB connection error:", e);
        }
    }

    public CommandResult executeCommand(String jsonCommand) {
        //JSON.serialize()
        return jongo.runCommand(jsonCommand).map(new RawResultHandler<>());
    }

    public void createCollection(final String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    public DBCollection createCollection(final String collectionName, final CollectionOptions collectionOptions) {
        return mongoTemplate.createCollection(collectionName, collectionOptions);
    }

    public String getCollectionName(Class<?> entityClass) {
        return MongoCollectionName.getCollectionName(entityClass);
    }

    public String getCollectionName(Class<?> entityClass, String partStr, String split) {
        return MongoCollectionName.getCollectionName(entityClass, partStr, split);
    }

    public String getCollectionName(String collectionName, String partStr, String split) {
        return MongoCollectionName.getCollectionName(collectionName, partStr, split);
    }

    public String getCollectionName(BaseMongoModel model) {
        String collectionName = getCollectionName(model.getClass());
        return getCollectionName(collectionName, model);
    }

    public String getCollectionName(String collectionName, BaseMongoModel model) {
        if (model instanceof CartPartitionModel) {
            return getCollectionName(collectionName, String.valueOf(((CartPartitionModel) model).getCartId()), BaseMongoCartDao.SPLIT_PART);
        } else if (model instanceof ChannelPartitionModel) {
            return getCollectionName(collectionName, ((ChannelPartitionModel) model).getChannelId(), BaseMongoChannelDao.SPLIT_PART);
        }
        return collectionName;
    }

    public MongoCollection getCollection(String collectionName) {
        return jongo.getCollection(collectionName);
    }

    public DBCollection getDBCollection(final String collectionName) {
        return jongo.getCollection(collectionName).getDBCollection();
    }

    public boolean collectionExists(final String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }

    public void dropCollection(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }

    public JSONObject findOne(String collectionName) {
        return findOne(JSONObject.class, collectionName);
    }

    public <T> T findOne(Class<T> entityClass, String collectionName) {
        return findOne("", entityClass, collectionName);
    }

    public JSONObject findOne(String strQuery, String collectionName) {
        return findOne(strQuery, JSONObject.class, collectionName);
    }

    public <T> T findOne(String strQuery, Class<T> entityClass, String collectionName) {
        return findOne(strQuery, null, entityClass, collectionName);
    }

    public <T> T findOne(String strQuery, String projection, Class<T> entityClass, String collectionName) {
        JongoQuery query = new JongoQuery();
        query.setQuery(strQuery);
        query.setProjection(projection);
        return findOne(query, entityClass, collectionName);
    }

    public <T> T findOne(JongoQuery queryObject, Class<T> entityClass, String collectionName) {
        FindOne find;

        //condition
        if (!StringUtils.isEmpty(queryObject.getQuery())) {
            if (queryObject.getParameters() == null || queryObject.getParameters().length == 0) {
                find = getCollection(collectionName).findOne(queryObject.getQuery());
            } else {
                find = getCollection(collectionName).findOne(queryObject.getQuery(), queryObject.getParameters());
            }
        } else if (queryObject.getObjectId() != null) {
            find = getCollection(collectionName).findOne(queryObject.getObjectId());
        } else {
            find = getCollection(collectionName).findOne();
        }

        //column
        if (!StringUtils.isEmpty(queryObject.getProjection())) {
            find = find.projection(queryObject.getProjection());
        }

        //sort
        if (!StringUtils.isEmpty(queryObject.getSort())) {
            find = find.orderBy(queryObject.getSort());
        }

        //execute
        return find.as(entityClass);
    }

    public boolean exists(String strQuery, String collectionName) {
        return getCollection(collectionName).count(strQuery) > 0;
    }

    public List<JSONObject> findAll(String collectionName) {
        return findAll(JSONObject.class, collectionName);
    }

    public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
        return find(null, null, entityClass, collectionName);
    }

    public List<JSONObject> find(final String strQuery, String collectionName) {
        return find(strQuery, null, JSONObject.class, collectionName);
    }

    public List<JSONObject> find(final String strQuery, String projection, String collectionName) {
        return find(strQuery, projection, JSONObject.class, collectionName);
    }

    public <T> List<T> find(final String strQuery, Class<T> entityClass, String collectionName) {
        return find(strQuery, null, entityClass, collectionName);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(final String strQuery, String projection, Class<T> entityClass, String collectionName) {
        return IteratorUtils.toList(findCursor(strQuery, projection, entityClass, collectionName));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(JongoQuery queryObject, Class<T> entityClass, String collectionName) {
        return IteratorUtils.toList(findCursor(queryObject, entityClass, collectionName));
    }

    public <T> MongoCursor<T> findCursorAll(Class<T> entityClass, String collectionName) {
        return findCursor(null, null, entityClass, collectionName);
    }

    public MongoCursor<JSONObject> findCursorAll(final String strQuery, String collectionName) {
        return findCursor(strQuery, null, JSONObject.class, collectionName);
    }

    public MongoCursor<JSONObject> findCursor(final String strQuery, String collectionName) {
        return findCursor(strQuery, null, JSONObject.class, collectionName);
    }

    public MongoCursor<JSONObject> findCursor(final String strQuery, String projection, String collectionName) {
        return findCursor(strQuery, projection, JSONObject.class, collectionName);
    }

    public <T> MongoCursor<T> findCursor(final String strQuery, Class<T> entityClass, String collectionName) {
        return findCursor(strQuery, null, entityClass, collectionName);
    }

    public <T> MongoCursor<T> findCursor(final String strQuery, String projection, Class<T> entityClass, String collectionName) {
        JongoQuery query = new JongoQuery();
        query.setQuery(strQuery);
        query.setProjection(projection);
        return findCursor(query, entityClass, collectionName);
    }

    public <T> MongoCursor<T> findCursor(JongoQuery queryObject, Class<T> entityClass, String collectionName) {
        MongoCursor<T> result;
        Find find;

        //condition
        if (StringUtils.isEmpty(queryObject.getQuery())) {
            find = getCollection(collectionName).find();
        } else {
            if (queryObject.getParameters() == null || queryObject.getParameters().length == 0) {
                find = getCollection(collectionName).find(queryObject.getQuery());
            } else {
                find = getCollection(collectionName).find(queryObject.getQuery(), queryObject.getParameters());
            }
        }

        //column
        if (!StringUtils.isEmpty(queryObject.getProjection())) {
            find = find.projection(queryObject.getProjection());
        }

        //sort
        if (!StringUtils.isEmpty(queryObject.getSort())) {
            find = find.sort(queryObject.getSort());
        }

        //limit
        if (queryObject.getLimit() != null) {
            find = find.limit(queryObject.getLimit());
        }

        //skip
        if (queryObject.getSkip() != null) {
            find = find.skip(queryObject.getSkip());
        }

        //execute
        result = find.as(entityClass);
        return result;
    }

    public JSONObject findById(String id, String collectionName) {
        return findById(id, JSONObject.class, collectionName);
    }

    public <T> T findById(String id, Class<T> entityClass, String collectionName) {
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", new ObjectId(id));
        String query = queryObj.toString();
        return findOne(query, entityClass, collectionName);
    }

    public <T> T findAndModify(JongoUpdate updateObject, Class<T> entityClass, String collectionName) {
        FindAndModify findAndModify;

        //condition
        if (StringUtils.isEmpty(updateObject.getQuery())) {
            findAndModify = getCollection(collectionName).findAndModify();
        } else {
            findAndModify = getCollection(collectionName).findAndModify(updateObject.getQuery(), updateObject.getQueryParameters());
        }

        //column
        if (!StringUtils.isEmpty(updateObject.getProjection())) {
            findAndModify = findAndModify.projection(updateObject.getProjection());
        }

        //sort
        if (!StringUtils.isEmpty(updateObject.getSort())) {
            findAndModify = findAndModify.sort(updateObject.getSort());
        }

        //update
        if (!StringUtils.isEmpty(updateObject.getUpdate())) {
            findAndModify = findAndModify.with(updateObject.getUpdate(), updateObject.getUpdateParameters());
        }

        //remove
        if (updateObject.isRemove()) {
            findAndModify = findAndModify.remove();
        }

        //returnNew
        if (updateObject.isReturnNew()) {
            findAndModify = findAndModify.returnNew();
        }

        //returnNew
        if (updateObject.isUpsert()) {
            findAndModify = findAndModify.upsert();
        }

        //execute
        return findAndModify.as(entityClass);
    }


    public long count(final String collectionName) {
        return getCollection(collectionName).count();
    }

    public long count(final String strQuery, String collectionName) {
        return count(strQuery, NO_PARAMETERS, collectionName);
    }

    public long count(final String strQuery, Object[] parameters, String collectionName) {
        Object[] parametersTemp = parameters;
        if (parametersTemp == null) {
            parametersTemp = NO_PARAMETERS;
        }
        return getCollection(collectionName).count(strQuery, parametersTemp);
    }

    public WriteResult insert(Object objectToSave, String collectionName) {
        return getCollection(collectionName).insert(objectToSave);
    }

    public WriteResult insert(Collection<? extends Object> batchToSaves, String collectionName) {
        return getCollection(collectionName).insert(batchToSaves.toArray());
    }

    public WriteResult save(Object objectToSave, String collectionName) {
        return getCollection(collectionName).save(objectToSave);
    }

    public WriteResult updateFirst(final String strQuery, final String strUpdate, final String collectionName) {
        return getCollection(collectionName).update(strQuery).with(strUpdate);
    }

    public WriteResult updateMulti(final String strQuery, final String strUpdate, final String collectionName) {
        return getCollection(collectionName).update(strQuery).multi().with(strUpdate);
    }

    public WriteResult updateFirst(JongoUpdate updObj, final String collectionName) {
        return getCollection(collectionName).update(updObj.getQuery(), updObj.getQueryParameters()).with(updObj.getUpdate(), updObj.getUpdateParameters());
    }

    public WriteResult updateMulti(JongoUpdate updObj, final String collectionName) {
        return getCollection(collectionName).update(updObj.getQuery(), updObj.getQueryParameters()).multi().with(updObj.getUpdate(), updObj.getUpdateParameters());
    }

    public WriteResult upsertFirst(String strQuery, String strUpdate, String collectionName) {
        return getCollection(collectionName).update(strQuery).upsert().with(strUpdate);
    }

    public WriteResult upsertMulti(String strQuery, String strUpdate, String collectionName) {
        return getCollection(collectionName).update(strQuery).upsert().multi().with(strUpdate);
    }

    public WriteResult removeById(String id, String collectionName) {
        return getCollection(collectionName).remove(new ObjectId(id));
    }

    public WriteResult removeAll(String collectionName) {
        return getCollection(collectionName).remove();
    }

    public WriteResult remove(String strQuery, String collectionName) {
        return getCollection(collectionName).remove(strQuery);
    }

    public <T> List<T> distinct(String key, String collectionName, final Class<T> clazz) {
        return getCollection(collectionName).distinct(key).as(clazz);
    }

    public Aggregate aggregate(String pipelineOperator, String collectionName) {
        return getCollection(collectionName).aggregate(pipelineOperator);
    }

    public Aggregate aggregate(String pipelineOperator, String collectionName, Object... parameters) {
        return getCollection(collectionName).aggregate(pipelineOperator, parameters);
    }

    public void dropIndex(String keys, String collectionName) {
        getCollection(collectionName).dropIndex(keys);
    }

    public void ensureIndex(String keys, String collectionName) {
        getCollection(collectionName).ensureIndex(keys);
    }

    public void ensureIndex(String keys, String options, String collectionName) {
        getCollection(collectionName).ensureIndex(keys, options);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

}
