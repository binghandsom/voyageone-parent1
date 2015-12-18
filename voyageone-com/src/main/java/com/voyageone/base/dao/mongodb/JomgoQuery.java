package com.voyageone.base.dao.mongodb;


/**
 * BaseJomgoPartTemplate Query Object
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class JomgoQuery {

    /**
     * column
     */
    private String projection;

    /**
     * query condition
     */
    private String query;

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

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }
}
