package com.voyageone.base.dao.mongodb;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.collections.IteratorUtils;
import org.jongo.Aggregate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * BaseJomgoDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseJomgoDao<T> implements ApplicationContextAware {

    protected BaseJomgoTemplate mongoTemplate;

    protected String collectionName;

    protected Class<T> entityClass;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.mongoTemplate = applicationContext.getBean(BaseJomgoTemplate.class);
        if (this.collectionName == null) {
            this.entityClass = getEntityClass();
            this.collectionName = mongoTemplate.getCollectionName(entityClass);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>)getSuperClassGenricType(getClass(), 0);
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return       the index generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    public WriteResult insert(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        return mongoTemplate.insert(model, collectionName);
    }

    public WriteResult insertWithList(Collection<? extends T> models) {
        if (models != null && !models.isEmpty()) {
            BaseMongoModel model = (BaseMongoModel)models.iterator().next();
            String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
            return mongoTemplate.insert(models, collectionName);
        }
        return null;
    }

    public WriteResult delete(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        return mongoTemplate.removeById(model.get_id(), collectionName);
    }

    public WriteResult update(BaseMongoModel model) {
//        if (StringUtils.isEmpty(model.get_id())) {
//            throw new RuntimeException("_id is empty");
//        }
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        model.setModified(DateTimeUtil.getNowTimeStamp());
        return mongoTemplate.save(model, collectionName);
    }

    public CommandResult executeCommand(String jsonCommand) {
        return mongoTemplate.executeCommand(jsonCommand);
    }

    /**
     * 聚合查询<br>
     * 必须注意：这里的Model不能简单使用表定义对应的Model，而是要和aggregate语句对应(要定义新的Model/Dao)，否则查询无正确数据
     */
    @SuppressWarnings("unchecked")
    public List<T> aggregateToObj(Class entityClass, String collectionName, JomgoAggregate... aggregates) {
        Aggregate aggr = mongoTemplate.aggregate(aggregates[0].getPipelineOperator(), collectionName, aggregates[0].getParameters());
        for (int i=1; i<aggregates.length; i++) {
            aggr.and(aggregates[i].getPipelineOperator(), aggregates[i].getParameters());
        }
        return IteratorUtils.toList(aggr.as(entityClass));
    }

    public String getQueryStr(JomgoQuery quyObj) {
        if (quyObj.getQuery() == null || quyObj.getQuery().isEmpty()) {
            return "";
        }
        return mongoTemplate.jongo.createQuery(quyObj.getQuery(), quyObj.getParameters()).toDBObject().toString();
    }
}