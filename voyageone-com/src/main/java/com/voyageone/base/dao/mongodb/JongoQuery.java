package com.voyageone.base.dao.mongodb;

import com.voyageone.base.dao.mongodb.support.VOBsonQueryFactory;
import com.voyageone.common.util.JacksonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.jongo.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BaseJongoPartTemplate Query Object
 *
 * @author chuanyu.liang, 12/11/15.
 * @author Jonas, 2015-12-18.
 * @version 2.0.1
 * @since 2.0.0
 */
public class JongoQuery extends BaseCondition {

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

    public JongoQuery() {
    }

    public JongoQuery(Criteria criteria) {
        setQuery(criteria);
    }

    public JongoQuery(String projection, String query, String sort, Integer limit, Integer skip) {
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
    public JongoQuery setProjectionExt(String... projection) {
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
     *                   注意：如果是单个输出项，该参数也必须设为如："{'key1':1}"
     */
    public JongoQuery setProjection(String projection) {
        this.projection = projection;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public JongoQuery setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public JongoQuery setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getSkip() {
        return skip;
    }

    public JongoQuery setSkip(Integer skip) {
        this.skip = skip;
        return this;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public JongoQuery setQuery(String query) {
        this.query = query;
        return this;
    }

    public JongoQuery setQuery(Criteria criteria) {
        setQuery(JacksonUtil.bean2Json(criteria.getCriteriaObject()));
        return this;
    }

    public String getQuery() {
        if (query != null) {
            return query;
        }
        if (queryStrList != null && queryStrList.size() > 0) {
            return "{$and:[" + StringUtils.join(queryStrList, ',') + "]}";
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

    public Object[] getParameters() {
        return parameters;
    }

    /**
     * 设置查询参数, 顺序必须同步 query 字符串内, 参数占位符 (#) 的位置
     * <p>
     * 使用该方法时，查询语句的写法必须参照 <a href="http://jongo.org/#query-templating">Jongo Query 文档的 Query templating 节</a>
     * <p>
     * 有输出项目过滤时，必须调用setProjection/setProjectionExt来设置，不能写在查询语句中
     *
     * @param parameters 按 query 顺序排列的查询参数值
     * @return 返回当前实例
     */
    public JongoQuery setParameters(Object... parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * 添加查询条件。注意, 使用此方法时不应再使用 {@link JongoQuery#setQuery(String)}, 否则查询条件会被覆盖
     * <p>
     * 使用方法示例：如最终的查询语句为: ("{'prodId':{$in:#},'platforms.P23':{$exists:true},'platforms.P23.pAttributeStatus':#}"
     * 则调用时应为
     * <pre class="code">
     * jqObj.addQuery("{'prodId':{$in:#}}");
     * jqObj.addParameters(prodIdList); // 这里的prodIdList可以是数组或List
     * jqObj.addQuery("{'platforms.P23':{$exists:true}}");
     * jqObj.addQuery("{'platforms.P23.pAttributeStatus':#}");
     * jqObj.addParameters(attrSts);
     * </pre>
     *
     * @param queryStr 查询属性定义语句
     * @return 当前实例
     */
    public JongoQuery addQuery(String queryStr) {
        if (queryStrList == null) {
            queryStrList = new ArrayList<>();
        }
        queryStrList.add(queryStr);
        return this;
    }

    /**
     * 添加查询参数，建议与 {@link JongoQuery#addQuery(String)} 配对使用
     * <p>
     * 使用此方法添加参数时必须注意，如果某参数刚好是一个数组，例如 {@code String[] codeArr = ... }，
     * 则不能简单的使用 addParameters(codeArr) 添加参数，必须使用如下形式：
     * <pre class="code">
     * Object param = codeArr;
     * addParameters(param);
     * </pre>
     *
     * @param parameters 按 query 顺序排列的查询参数值
     * @return 当前实例
     */
    public JongoQuery addParameters(Object... parameters) {
        if (this.parameters == null) {
            this.parameters = parameters;
        } else {
            this.parameters = ArrayUtils.addAll(this.parameters, parameters);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder rs = new StringBuilder();
        String queryStr = getQuery();
        if (queryStr != null && queryStr.length() > 0) {
            VOBsonQueryFactory queryFactory = new VOBsonQueryFactory();
            Object[] params = getParameters();
            if (params == null) {
                params = new Object[0];
            }
            Query query = queryFactory.createQuery(queryStr, params);
            queryStr = query.toDBObject().toString();
        }
        rs.append("JongoQuery =: { query:=");
        rs.append(queryStr);
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
