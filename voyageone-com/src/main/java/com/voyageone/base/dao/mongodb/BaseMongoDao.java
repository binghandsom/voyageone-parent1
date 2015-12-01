package com.voyageone.base.dao.mongodb;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

public class BaseMongoDao {

    protected BaseJomgoTemplate mongoTemplate;

    protected String collectionName;

    protected Class<?> entityClass;

    @Autowired
    public void setMongoTemplate(BaseJomgoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        if (this.collectionName == null) {
            this.collectionName = mongoTemplate.getCollectionName(entityClass);
        }
    }

    public <T> T selectOne() {
        return mongoTemplate.findOne((Class<T>) entityClass, collectionName);
    }

    public <T> T selectOneWithQuery(String strQuery) {
        return mongoTemplate.findOne(strQuery, (Class < T >)entityClass, collectionName);
    }

    public <T> List<T> selectAll() {
        return mongoTemplate.findAll((Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(final String strQuery) {
        return mongoTemplate.find(strQuery, (Class<T>) entityClass, collectionName);
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
        return mongoTemplate.findOne(strQuery, (Class < T >)entityClass, collectionName);
    }

    public <T> List<T> selectAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findAll((Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, (Class<T>) entityClass, collectionName);
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

    public WriteResult deleteAll() {
        return mongoTemplate.removeAll(collectionName);
    }

    public WriteResult deleteWithQuery(String strQuery) {
        return mongoTemplate.remove(strQuery, collectionName);
    }

    public WriteResult deleteById(String id, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.removeById(id, collectionName);
    }

    public WriteResult deleteAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.removeAll(collectionName);
    }

    public WriteResult deleteWithQuery(String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.remove(strQuery, collectionName);
    }

}