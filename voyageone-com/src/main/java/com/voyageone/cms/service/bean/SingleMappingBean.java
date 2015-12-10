package com.voyageone.cms.service.bean;

import com.voyageone.ims.rule_expression.RuleExpression;

/**
 * Created by Leo on 15-12-9.
 */
public class SingleMappingBean extends MappingBean {
    private RuleExpression expression;

    public SingleMappingBean() {
        mappingType = MAPPING_SINGLE;
    }

    public SingleMappingBean(String platformPropId, RuleExpression expression) {
        this.expression = expression;
        this.platformPropId = platformPropId;
    }

    public RuleExpression getExpression() {
        return expression;
    }

    public void setExpression(RuleExpression expression) {
        this.expression = expression;
    }
}
