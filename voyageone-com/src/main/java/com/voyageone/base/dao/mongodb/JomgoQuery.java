package com.voyageone.base.dao.mongodb;

import com.voyageone.base.dao.mongodb.support.VOBsonQueryFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.jongo.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private List<String> queryStrList = null;
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
        if (query != null) {
            return query;
        }
        if (queryStrList != null && queryStrList.size() > 0) {
            return "{" + StringUtils.join(queryStrList, ',') + "}";
        }
        return "";
    }

    public Map getQueryMap() {
        //DBObject dbObject = (DBObject) JSON.parse(getQuery());
        //return dbObject.toMap();
        VOBsonQueryFactory queryFactory = new VOBsonQueryFactory();
        Object[] params = getParameters();
        if (params == null) {
            params = new Object[0];
        }
        Query query = queryFactory.createQuery(getQuery(), params);
        return query.toDBObject().toMap();
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

    /**
     * 添加查询条件（使用此方法时不应再使用setQuery()）
     * 使用方法示例：
     *     如最终的查询语句为: ("{'prodId':{$in:#},'platforms.P23':{$exists:true},'platforms.P23.pAttributeStatus':#}"
     *     则调用时应为   jqObj.addQuery("'prodId':{$in:#}");
     *                   jqObj.addParameters(prodIdList);    // 这里的prodIdList可以是数组或List
     *                   jqObj.addQuery("'platforms.P23':{$exists:true}");
     *                   jqObj.addQuery("'platforms.P23.pAttributeStatus':#");
     *                   jqObj.addParameters(attrSts);
     * @param queryStr
     */
    public void addQuery(String queryStr) {
        if (queryStrList == null) {
            queryStrList = new ArrayList<>();
        }
        queryStrList.add(queryStr);
    }

    /**
     * 添加查询参数，应与addQuery()配对使用
     * @param parameters
     */
    public void addParameters(Object... parameters) {
        if (this.parameters == null) {
            this.parameters = parameters;
        } else {
            this.parameters = ArrayUtils.addAll(this.parameters, parameters);
        }
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

    @Override
    public String toString() {
        StringBuilder rs = new StringBuilder();
        rs.append("JomgoQuery =: { query:=");
        rs.append(getQuery());
        rs.append("; parameters:=");
        rs.append(Arrays.toString(parameters));
        rs.append("; projection:=");
        rs.append(projection);
        rs.append("; sort:=");
        rs.append(sort);
        rs.append("; limit:=");
        rs.append(limit);
        rs.append("; skip:=");
        rs.append(skip);
        if (objectId != null) {
            rs.append("; objectId:=");
            rs.append(objectId.toString());
        }
        rs.append("; }");
        return rs.toString();
    }
}
