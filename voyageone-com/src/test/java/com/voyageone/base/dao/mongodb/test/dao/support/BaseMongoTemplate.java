package com.voyageone.base.dao.mongodb.test.dao.support;

import com.mongodb.Mongo;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.lang.reflect.Field;

/**
 * Created by DELL on 2015/10/29.
 */
public class BaseMongoTemplate extends MongoTemplate {

    public BaseMongoTemplate(Mongo mongo, String databaseName) {
        super(mongo, databaseName);
    }

    public BaseMongoTemplate(Mongo mongo, String databaseName, UserCredentials userCredentials) {
        super(mongo, databaseName, userCredentials);
    }

    public BaseMongoTemplate(MongoDbFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    public BaseMongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
        try {
            MongoConverter mongoConverter_r = mongoConverter == null ? getDefaultMongoConverter(mongoDbFactory) : mongoConverter;

            Class<?> clazz = MongoTemplate.class;
            Field queryMapperField = clazz.getDeclaredField("queryMapper");
            queryMapperField.setAccessible(true);
            QueryMapper queryMapper_r = new BaseQueryMapper(mongoConverter_r);
            queryMapperField.set(this, queryMapper_r);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final MongoConverter getDefaultMongoConverter(MongoDbFactory factory) {
       DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
       MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
       converter.afterPropertiesSet();
       return converter;
    }

    private static final MongoConverter get_DefaultMongoConverter(MongoDbFactory factory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.afterPropertiesSet();
        return converter;
    }
}
