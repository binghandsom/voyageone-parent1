package com.voyageone.base.dao.mongodb;

import com.voyageone.base.dao.mongodb.support.VOBsonQueryFactory;
import org.jongo.query.Query;

/**
 * JomgoAggregate Aggregate Object
 *
 * @author chuanyu.liang, 6/11/16
 * @version 2.0.1
 * @since 2.0.0
 */
public class JomgoAggregate {
    private String pipelineOperator;
    private Object[] parameters = new Object[0];

    public JomgoAggregate(String pipelineOperator) {
        this.pipelineOperator = pipelineOperator;
    }

    public JomgoAggregate(String pipelineOperator, Object... parameters) {
        this.pipelineOperator = pipelineOperator;
        if (parameters != null && parameters.length > 0) {
            this.parameters = parameters;
        }
    }

    public String getPipelineOperator() {
        return pipelineOperator;
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        if (pipelineOperator != null && pipelineOperator.length() > 0) {
            VOBsonQueryFactory queryFactory = new VOBsonQueryFactory();
            Object[] params = getParameters();
            if (params == null) {
                params = new Object[0];
            }
            Query query = queryFactory.createQuery(pipelineOperator, params);
            return query.toDBObject().toString();
        } else {
            return "";
        }
    }
}
