package com.voyageone.base.dao.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import net.minidev.json.JSONObject;
import org.apache.commons.collections.IteratorUtils;
import org.bson.types.ObjectId;
import org.jongo.*;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;


public class BaseJomgoPartTemplate {

    protected MongoTemplate mongoTemplate;


    protected Jongo jongo;

    @Resource
    MongoCollectionMapping mongoCollectionMapping;

    public BaseJomgoPartTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        try {
            this.jongo = new Jongo(mongoTemplate.getDb());
        } catch (Exception e) {
            throw new RuntimeException("MongoDB connection error:", e);
        }
    }

    public CommandResult executeCommand(String jsonCommand) {
        //JSON.serialize()
        return jongo.runCommand(jsonCommand).map(new RawResultHandler<CommandResult>());
    }

    public void createCollection(final String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    public DBCollection createCollection(final String collectionName, final CollectionOptions collectionOptions) {
        return mongoTemplate.createCollection(collectionName, collectionOptions);
    }

    public String getCollectionName(Class<?> entityClass) {
        return mongoCollectionMapping.getCollectionName(entityClass);
    }

    public String getCollectionName(Class<?> entityClass, String channelId) {
        return mongoCollectionMapping.getCollectionName(entityClass, channelId);
    }

    public String getCollectionName(String collectionName, String channelId) {
        return mongoCollectionMapping.getCollectionName(collectionName, channelId);
    }

    public String getCollectionName(BaseMongoModel model) {
        String collectionName = getCollectionName(model.getClass());
        return getCollectionName(collectionName, model);
    }

    public String getCollectionName(String collectionName, BaseMongoModel model) {
        if (model instanceof ChannelPartitionModel) {
            return getCollectionName(collectionName, ((ChannelPartitionModel)model).getChannelId());
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

    public JSONObject findOne( String collectionName) {
        return findOne(JSONObject.class, collectionName);
    }

    public <T> T findOne(Class<T> entityClass, String collectionName) {
        return getCollection(collectionName).findOne().as(entityClass);
    }

    public JSONObject findOne(String strQuery, String collectionName) {
        return findOne(strQuery, JSONObject.class, collectionName);
    }

    public <T> T findOne(String strQuery, Class<T> entityClass, String collectionName) {
        JSONObject aa = getCollection(collectionName).findOne(strQuery).as(JSONObject.class);
        return getCollection(collectionName).findOne(strQuery).as(entityClass);
    }

    public boolean exists(String strQuery, String collectionName) {
        return getCollection(collectionName).count(strQuery)>0;
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

    public <T> List<T> find(final String strQuery, String projection, Class<T> entityClass, String collectionName) {
        return IteratorUtils.toList(findCursor(strQuery, projection, entityClass, collectionName));
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
        MongoCursor<T> result;
        if (strQuery == null) {
            Find find = getCollection(collectionName).find();
            if (projection != null) {
                find = find.projection(projection);
            }
            result = find.as(entityClass);
        } else {
            Find find = getCollection(collectionName).find(strQuery);
            if (projection != null) {
                find = find.projection(projection);
            }
            result = find.as(entityClass);
        }
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

//    public void findAndModify(String strQuery, String strUpdate, String collectionName) {
//        // ???
//        getCollection(collectionName).update(strQuery).upsert().multi().with(strUpdate);
//    }
//
//    public void findAndRemove(String strQuery, String collectionName) {
//        getCollection(collectionName).remove(strQuery);
//    }

    public long count(final String collectionName) {
        return getCollection(collectionName).count();
    }

    public long count(final String strQuery, String collectionName) {
        return getCollection(collectionName).count(strQuery);
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

    public WriteResult upsert(String strQuery, String strUpdate, String collectionName) {
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

    public Distinct distinct(String key, String collectionName) {
        return getCollection(collectionName).distinct(key);
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
}
