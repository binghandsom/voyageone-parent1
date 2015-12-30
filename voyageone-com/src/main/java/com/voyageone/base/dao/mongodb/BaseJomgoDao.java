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
 * BaseJomgoDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseJomgoDao {

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

    public CommandResult executeCommand(String jsonCommand) {
        return mongoTemplate.executeCommand(jsonCommand);
    }

}