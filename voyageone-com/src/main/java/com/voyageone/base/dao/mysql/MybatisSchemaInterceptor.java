package com.voyageone.base.dao.mysql;

import com.voyageone.common.util.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * MybatisSchemaInterceptor
 *
 * @author chuanyu.liang
 * @version 2.0.0, 5/5/16
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class MybatisSchemaInterceptor implements Interceptor, Serializable {

    private transient Logger logger = LoggerFactory.getLogger(getClass());

    private Properties properties;

    /**
     * mapped statement parameter index.
     */
    private static final int STATEMENT_INDEX = 0;
//    /** parameter index. */
//    private static final int PARAMETER_INDEX = 1;
//    /** parameter index. */
//    private static final int ROWBOUNDS_INDEX = 2;
//    /** ResultHandler index. */
//    private static final int RESULT_HANDLER_INDEX = 3;

    private static final String DB_SCHEMA_SET = "database_schema";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    /**
     * perform change schema intercetion.
     *
     * @param queryArgs Executor.query params.
     */
    private void processIntercept(final Object[] queryArgs) {
//        for(Object o:queryArgs) {
//            System.out.println(o);
//        }
        final Statement ms = (Statement) queryArgs[STATEMENT_INDEX];
        Connection connection = null;
        Statement stmt = null;
        try {
            //get connection
            connection = ms.getConnection();
            String dbSchema = "";
            if (properties != null) {
                dbSchema = (String) properties.get(DB_SCHEMA_SET);
            }
            if (!StringUtils.isEmpty(dbSchema)) {
                stmt = connection.createStatement();
                stmt.execute("use " + dbSchema + ";");
            }
        } catch (SQLException e) {
            logger.error("The total number of access to the database failure.", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                logger.error("Close the database stmt.close error.", e);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
