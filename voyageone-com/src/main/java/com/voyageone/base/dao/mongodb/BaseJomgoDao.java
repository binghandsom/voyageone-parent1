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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * BaseJomgoDao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseJomgoDao<T> {

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

    @Autowired
    public void setMongoTemplate(BaseJomgoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
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
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
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

}