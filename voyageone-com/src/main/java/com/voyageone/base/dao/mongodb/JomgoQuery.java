package com.voyageone.base.dao.mongodb;

import org.bson.types.ObjectId;

/**
 * BaseJomgoPartTemplate Query Object
 *
 * @author chuanyu.liang, 12/11/15.
 * @author Jonas, 2015-12-18.
 * @version 2.0.1
 * @since 2.0.0
 */
public class JomgoQuery extends BaseCondition {

    /**
     * column
     */
    private String projection;

    /**
     * query condition
     */
    private String query;

    /**
     * query condition
     */
    private Object[] parameters = null;

    /**
     * query key
     */
    private ObjectId objectId;

    /**
     * sort condition
     */
    private String sort;

    /**
     * limit
     */
    private Integer limit;

    /**
     * skip count
     */
    private Integer skip;


    public JomgoQuery() {
    }

    public JomgoQuery(String projection, String query, String sort, Integer limit, Integer skip) {
        this.projection = projection;
        this.query = query;
        this.sort = sort;
        this.limit = limit;
        this.skip = skip;
    }

    public String getProjection() {
        return projection;
    }

    /**
     * 设置输出项目，兼容setProjection()方法
     *
     * @param projection 参数形式，可以为数组如：['key1','key2']，也可以直接使用字符串的形式如："{'key1':1,'key2':1}"
     *                   注意：如果是单个输出项，该参数可以直接设为如：'key1'
     */
    public JomgoQuery setProjectionExt(String... projection) {
        String projectionTmp = buildProjection(projection);
        if (projectionTmp != null) {
            this.projection = projectionTmp;
        }
        return this;
    }

    /**
     * 直接设置输出项目
     *
     * @param projection 必须是如"{'key1':1,'key2':1}"的形式，必须要有大括号
     *                     注意：如果是单个输出项，该参数也必须设为如："{'key1':1}"
     */
    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getQuery() {
        return query;
    }

    public JomgoQuery setQuery(String query) {
        this.query = query;
        return this;
    }

    public Object[] getParameters() {
        return parameters;
    }

    /**
     * 设置查询参数
     * 使用该方法时，查询语句的写法必须参照 http://jongo.org/#querying 的Query templating一节
     * 有输出项目过滤时，必须调用setProjection/setProjectionExt来设置，不能写在查询语句中
     *
     * @param parameters
     */
    public void setParameters(Object... parameters) {
        this.parameters = parameters;
    }

    public String getSort() {
        return sort;
    }

    public JomgoQuery setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public JomgoQuery setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getSkip() {
        return skip;
    }

    public JomgoQuery setSkip(Integer skip) {
        this.skip = skip;
        return this;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }
}
