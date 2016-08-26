package com.voyageone.base.dao.mysql;

import com.voyageone.common.spring.SpringContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MybatisSqlHelper {

    private static final String replaceValue = "\\{#%%#value#%%#\\}";
    private static final Pattern enterPattern = Pattern.compile("[\\r\n\t]+");
    private static final Pattern blankPattern = Pattern.compile("[\\s]+");

    /**
     * 通过Mapper方法名获取sql
     */
    public static String getMapperSql(String fullMapperMethodName, Object params) {
        return getSql(fullMapperMethodName, params);
    }

    /**
     * 通过Mapper接口和方法名
     */
    public static String getMapperSql(Class mapperInterface, String methodName, Object params) {
        String fullMapperMethodName = mapperInterface.getCanonicalName() + "." + methodName;
        return getSql(fullMapperMethodName, params);
    }


    /**
     * 通过命名空间方式获取sql
     */
    public static String getMapperSql(String namespace, String methodName, Object params) {
        String fullMapperMethodName = namespace + "." + methodName;
        return getSql(fullMapperMethodName, params);
    }

    /**
     * 通过命名空间方式获取sql
     */
    private static String getSql(String mappedStatementId, Object params) {
        Map<String, SqlSessionFactory> sqlSessionFactoryBeanMap = SpringContext.getBeansMap(SqlSessionFactory.class);

        MappedStatement mappedStatement = null;
        Configuration configuration = null;
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryBeanMap.values()) {
            Configuration configurationTemp = sqlSessionFactory.getConfiguration();
            if (configurationTemp.hasStatement(mappedStatementId)) {
                configuration = configurationTemp;
                mappedStatement = configurationTemp.getMappedStatement(mappedStatementId);
                break;
            }
        }

        if (mappedStatement == null) {
            throw new RuntimeException("mappedStatementId not found:" + mappedStatementId);
        }


        TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        BoundSql boundSql = mappedStatement.getBoundSql(params);

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String tmpSql = boundSql.getSql();
        tmpSql = tmpSql.replaceAll("\\?", replaceValue);
        if (parameterMappings != null) {
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (params == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(params.getClass())) {
                        value = params;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(params);
                        value = metaObject.getValue(propertyName);
                    }
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) jdbcType = configuration.getJdbcTypeForNull();
                    tmpSql = replaceParameter(tmpSql, value, jdbcType, parameterMapping.getJavaType());
                }
            }
        }

        // format sql
        String sql = enterPattern.matcher(tmpSql).replaceAll(" ");
        sql = blankPattern.matcher(sql).replaceAll(" ");
        return sql;
    }

    /**
     * 根据类型替换参数
     * <p>
     * 仅作为数字和字符串两种类型进行处理，需要特殊处理的可以继续完善这里
     */
    private static String replaceParameter(String sql, Object value, JdbcType jdbcType, Class javaType) {
        String strValue = String.valueOf(value);
        if (jdbcType != null) {
            switch (jdbcType) {
                //数字
                case BIT:
                case TINYINT:
                case SMALLINT:
                case INTEGER:
                case BIGINT:
                case FLOAT:
                case REAL:
                case DOUBLE:
                case NUMERIC:
                case DECIMAL:
                    break;
                //日期
                case DATE:
                    if (Date.class.isAssignableFrom(javaType)) {
                        strValue = String.valueOf(new java.sql.Date(((Date)value).getTime()));
                    }
                case TIME:
                case TIMESTAMP:
                    if (Date.class.isAssignableFrom(javaType)) {
                        strValue = String.valueOf(new Timestamp(((Date)value).getTime()));
                    }
                    //其他，包含字符串和其他特殊类型
                default:
                    strValue = "'" + strValue + "'";
            }
        } else if (Number.class.isAssignableFrom(javaType)) {
            //不加单引号
            strValue = strValue;
        } else {
            strValue = "'" + strValue + "'";
        }
        return sql.replaceFirst(replaceValue, strValue);
    }
}
