package com.voyageone.base.dao.mongodb;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import net.minidev.json.JSONObject;
import org.apache.commons.collections.IteratorUtils;
import org.bson.types.ObjectId;
import org.jongo.*;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.List;


public class BaseJomgoPartTemplate {

    protected MongoTemplate mongoTemplate;

    protected Jongo jongo;

    public BaseJomgoPartTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.jongo = new Jongo(mongoTemplate.getDb());
    }

    public CommandResult executeCommand(String jsonCommand) {
        //JSON.serialize()
        return jongo.runCommand(jsonCommand).map(new RawResultHandler<CommandResult>());
    }

    public void createCollection(final String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    public void createCollection(final String collectionName, final CollectionOptions collectionOptions) {
        mongoTemplate.createCollection(collectionName, collectionOptions);
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
        return find("", entityClass, collectionName);
    }

    public List<JSONObject> find(final String strQuery, String collectionName) {
        return find(strQuery, JSONObject.class, collectionName);
    }

    public <T> List<T> find(final String strQuery, Class<T> entityClass, String collectionName) {
        List<T> result;
        if (strQuery == null) {
            result = IteratorUtils.toList(getCollection(collectionName).find().as(entityClass));
        } else {
            result = IteratorUtils.toList(getCollection(collectionName).find(strQuery).as(entityClass));
        }
        return result;
    }

    public JSONObject findById(String id, String collectionName) {
        return findById(id, JSONObject.class, collectionName);
    }

    public <T> T findById(String id, Class<T> entityClass, String collectionName) {
        String query = "{\"_id\":ObjectId(\"" + id + "\")}";
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

    public void insert(Object objectToSave, String collectionName) {
        getCollection(collectionName).insert(objectToSave);
    }

    public WriteResult insert(Collection<? extends Object> batchToSaves, String collectionName) {
        return getCollection(collectionName).insert(batchToSaves.toArray());
    }

    public void save(Object objectToSave, String collectionName) {
        getCollection(collectionName).save(objectToSave);
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

    public void removeById(String id, String collectionName) {
        getCollection(collectionName).remove(new ObjectId(id));
    }

    public void removeAll(String collectionName) {
        getCollection(collectionName).remove();
    }

    public void remove(String strQuery, String collectionName) {
        getCollection(collectionName).remove(strQuery);
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

    public void drop(String collectionName) {
        getCollection(collectionName).drop();
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
