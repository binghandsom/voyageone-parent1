package com.voyageone.base.dao.mongodb;

/**
 * BaseJongoPartTemplate Update Object
 *
 * @author chuanyu.liang, 12/11/15.
 * @author Jonas, 2015-12-18.
 * @version 2.0.1
 * @since 2.0.0
 */
public class JongoUpdate extends BaseCondition {

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
    private Object[] queryParameters = null;

    /**
     * sort
     */
    private String sort;

    /**
     * update
     */
    private String update;
    /**
     * query condition
     */
    private Object[] updateParameters = null;

    /**
     * remove
     */
    private boolean remove = false;

    /**
     * returnNew
     */
    private boolean returnNew = true;

    /**
     * upsert
     */
    private boolean upsert = false;


    public JongoUpdate() {

    }

    public String getProjection() {
        return projection;
    }

    public JongoUpdate setProjection(String... projection) {
        String projectionTmp = buildProjection(projection);
        if (projectionTmp != null) {
            this.projection = projectionTmp;
        }
        return this;
    }

    public String getQuery() {
        return query;
    }

    public JongoUpdate setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public JongoUpdate setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getUpdate() {
        return update;
    }

    public JongoUpdate setUpdate(String update) {
        this.update = update;
        return this;
    }

    public boolean isRemove() {
        return remove;
    }

    public JongoUpdate setRemove(boolean remove) {
        this.remove = remove;
        return this;
    }

    public boolean isReturnNew() {
        return returnNew;
    }

    public JongoUpdate setReturnNew(boolean returnNew) {
        this.returnNew = returnNew;
        return this;
    }

    public boolean isUpsert() {
        return upsert;
    }

    public JongoUpdate setUpsert(boolean upsert) {
        this.upsert = upsert;
        return this;
    }

    public Object[] getQueryParameters() {
        if (queryParameters == null) {
            return new Object[0];
        }
        return queryParameters;
    }

    public void setQueryParameters(Object... queryParameters) {
        this.queryParameters = queryParameters;
    }

    public Object[] getUpdateParameters() {
        if (updateParameters == null) {
            return new Object[0];
        }
        return updateParameters;
    }

    public void setUpdateParameters(Object... updateParameters) {
        this.updateParameters = updateParameters;
    }
}
