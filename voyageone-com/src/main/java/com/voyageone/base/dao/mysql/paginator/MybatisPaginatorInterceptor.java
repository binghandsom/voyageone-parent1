package com.voyageone.base.dao.mysql.paginator;

import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.github.miemiedev.mybatis.paginator.dialect.Dialect;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.voyageone.base.dao.mysql.paginator.dialect.VoMySQLDialect;
import com.voyageone.common.util.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.*;

/**
 * MybatisSchemaInterceptor
 * https://github.com/miemiedev/mybatis-paginator/blob/master/src/main/java/com/github/miemiedev/mybatis/paginator/OffsetLimitInterceptor.java
 *
 * @author chuanyu.liang
 * @version 2.0.0, 5/5/16
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MybatisPaginatorInterceptor implements Interceptor, Serializable {

    //private static Logger logger = LoggerFactory.getLogger(MybatisPaginatorInterceptor.class);

    private static final String ID_FILTER = "id_filter";
    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROWBOUNDS_INDEX = 2;

    private Properties properties;
    private List<String> idFilterList;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //final Executor executor = (Executor) invocation.getTarget();
        final Object[] queryArgs = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        if (!checkFilterId(ms.getId())) {
            return invocation.proceed();
        }
        final Object parameter = queryArgs[PARAMETER_INDEX];
        if (parameter instanceof Map) {
            Map parameterMap = (Map) parameter;
            for (Object value : parameterMap.values()) {
                if (value instanceof PageBounds) {
                    PageBounds pageBounds = (PageBounds) value;

                    final Dialect dialect = new VoMySQLDialect(ms, parameter, pageBounds);
                    final BoundSql boundSql = ms.getBoundSql(parameter);

                    queryArgs[MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms, boundSql, dialect.getPageSQL(), dialect.getParameterMappings(), dialect.getParameterObject());
                    queryArgs[PARAMETER_INDEX] = dialect.getParameterObject();
                    queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);

                    return invocation.proceed();
                }
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean checkFilterId(String id) {
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        if (idFilterList == null) {
            if (properties != null) {
                String idFilter = (String) properties.get(ID_FILTER);
                if (!StringUtils.isEmpty(idFilter)) {
                    idFilterList = Arrays.asList(idFilter.split(","));
                }
            }
            if (idFilterList == null) {
                idFilterList = new ArrayList<>();
            }
        }
        if (!idFilterList.isEmpty()) {
            for (String idFilter : idFilterList) {
                if (id.endsWith(idFilter)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql,
                                           String sql, List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql, parameterMappings, parameter);
        return copyFromMappedStatement(ms, new OffsetLimitInterceptor.BoundSqlSqlSource(newBoundSql));
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql,
                                      String sql, List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, parameterMappings, parameter);
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    //see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        //setStatementTimeout()
        builder.timeout(ms.getTimeout());

        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
}
