package com.voyageone.base.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseMongoDao {

    protected BaseJomgoTemplate mongoTemplate;

    protected String collectionName;

    protected Class<?> entityClass;

    @Autowired
    public void setMongoTemplate(BaseJomgoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = mongoTemplate.getCollectionName(entityClass);
    }

}