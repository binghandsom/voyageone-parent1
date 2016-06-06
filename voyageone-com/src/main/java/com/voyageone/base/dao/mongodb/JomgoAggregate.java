package com.voyageone.base.dao.mongodb;

/**
 * Created by dell on 2016/5/25.
 */
public class JomgoAggregate {
    private String pipelineOperator;
    private Object[] parameters = new Object[0];

    public JomgoAggregate(String pipelineOperator) {
        this.pipelineOperator = pipelineOperator;
    }

    public JomgoAggregate(String pipelineOperator, Object[] parameters) {
        this.pipelineOperator = pipelineOperator;
        if (parameters != null) {
            this.parameters = parameters;
        }
    }

    public String getPipelineOperator() {
        return pipelineOperator;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
