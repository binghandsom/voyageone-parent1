package com.voyageone.base.dao.mongodb;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * BaseJomgoPartTemplate Query Object
 *
 * @author chuanyu.liang, 12/11/15.
 * @author Jonas, 2015-12-18.
 * @version 2.0.1
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

        // 当没有参数时,不需要设定任何内容
        if (projection.length == 0)
            return this;

        // 先过滤掉所有空
        List<String> fields = Arrays.stream(projection)
                .filter(i -> !StringUtils.isEmpty(i))
                .map(String::trim)
                .collect(toList());

        // 没有任何参数..
        if (fields.size() == 0)
            return this;

        // 当有参数时,检查第一个是否是 json 格式,如果是,则忽略后续所有参数
        // 如果不是,则默认后续所有参数都是列名,而非 json
        String first = fields.get(0);
        if (first.startsWith("{") && first.endsWith("}")) {
            this.projection = first;
            return this;
        }

        // 格式为 {"a":1,"b":1,"c":1},则重复的间隔为 ":1," 则 {"joining(?:1,?:1,?)":1} => {"a":1,"b":1,"c":1}
        this.projection = String.format("{\"%s\":1}", fields.stream().collect(joining("\":1,\"")));
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
