package com.voyageone.batch.cms.bean;

/**
 * Created by Leo on 15-12-9.
 */
public class SingleMappingBean extends MappingBean {
    private String expression;

    public SingleMappingBean() {
        mappingType = MAPPING_SINGLE;
    }

    public SingleMappingBean(String platformPropId, String expression) {
        this.expression = expression;
        this.platformPropId = platformPropId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
