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

    public JomgoQuery setProjection(String... projection) {
        String projectionTmp = buildProjection(projection);
        if (projectionTmp != null) {
            this.projection = projectionTmp;
        }
        return this;
    }



    public String getQuery() {
        return query;
    }

    public JomgoQuery setQuery(String query) {
        this.query = query;
        return this;
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
