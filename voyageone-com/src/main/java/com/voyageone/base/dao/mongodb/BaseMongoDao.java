package com.voyageone.base.dao.mongodb;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * BaseMongoDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMongoDao {

    protected BaseJomgoTemplate mongoTemplate;

    protected String collectionName;

    protected Class entityClass;

    static {
        // 在 BaseMongoDao 静态初始化时, 初始化 JsonPath 的 Provider 配置
        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    @Autowired
    public void setMongoTemplate(BaseJomgoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        if (this.collectionName == null) {
            this.entityClass = getEntityClass();
            this.collectionName = mongoTemplate.getCollectionName(entityClass);
        }
    }

    public abstract Class getEntityClass();

    public DBCollection getDBCollection() {
        return mongoTemplate.getDBCollection(collectionName);
    }

    public DBCollection getDBCollection(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.getDBCollection(collectionName);
    }

    public <T> T selectOne() {
        return mongoTemplate.findOne((Class<T>) entityClass, collectionName);
    }

    public <T> T selectOneWithQuery(String strQuery) {
        return mongoTemplate.findOne(strQuery, (Class<T>)entityClass, collectionName);
    }

    public <T> T selectOneWithQuery(JomgoQuery queryObject) {
        return mongoTemplate.findOne(queryObject, (Class<T>)entityClass, collectionName);
    }

    public <T> List<T> selectAll() {
        return mongoTemplate.findAll((Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursorAll() {
        return mongoTemplate.findCursorAll((Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(final String strQuery) {
        return mongoTemplate.find(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> List<T> selectWithProjection(final String strQuery, String projection) {
        return mongoTemplate.find(strQuery, projection, (Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(JomgoQuery queryObject) {
        return mongoTemplate.find(queryObject, (Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursor(final String strQuery) {
        return mongoTemplate.findCursor(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursor(JomgoQuery queryObject) {
        return mongoTemplate.findCursor(queryObject, (Class<T>) entityClass, collectionName);
    }

    public <T> T selectById(String id) {
        return mongoTemplate.findById(id, (Class<T>) entityClass, collectionName);
    }

    public <T> T selectOne(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne((Class<T>) entityClass, collectionName);
    }

    public <T> T selectOneWithQuery(String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(strQuery, (Class<T>)entityClass, collectionName);
    }

    public <T> T selectOneWithQuery(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(queryObject, (Class<T>)entityClass, collectionName);
    }

    public <T> List<T> selectAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findAll((Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursorAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursorAll((Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> List<T> selectWithProjection(final String strQuery, String projection, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, projection, (Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(queryObject, (Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursor(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursor(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursor(JomgoQuery queryObject, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursor(queryObject, (Class<T>) entityClass, collectionName);
    }

    public <T> T selectById(String id, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findById(id, (Class<T>) entityClass, collectionName);
    }

    public long count() {
        return mongoTemplate.count(collectionName);
    }

    public long countByQuery(final String strQuery) {
        return mongoTemplate.count(strQuery, collectionName);
    }

    public long count(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.count(collectionName);
    }

    public long countByQuery(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.count(strQuery, collectionName);
    }

    public WriteResult insert(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        return mongoTemplate.insert(model, collectionName);
    }

    public WriteResult insertWithList(Collection<? extends Object> models) {
        if (models != null && models.size() > 0) {
            BaseMongoModel model = (BaseMongoModel)models.iterator().next();
            String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
            return mongoTemplate.insert(models, collectionName);
        }
        return null;
    }

    public WriteResult update(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        model.setModified(DateTimeUtil.getNowTimeStamp());
        return mongoTemplate.save(model, collectionName);
    }

    public WriteResult delete(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        return mongoTemplate.removeById(model.get_id(), collectionName);
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

    public CommandResult executeCommand(String jsonCommand) {
        return mongoTemplate.executeCommand(jsonCommand);
    }

}