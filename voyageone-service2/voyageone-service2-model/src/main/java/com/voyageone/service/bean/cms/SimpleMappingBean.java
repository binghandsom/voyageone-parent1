package com.voyageone.service.bean.cms;

import com.voyageone.ims.rule_expression.RuleExpression;

/**
 * Created by Leo on 15-12-9.
 */
public class SimpleMappingBean extends MappingBean {
    private RuleExpression expression;

    public SimpleMappingBean() {
        mappingType = MAPPING_SIMPLE;
    }

    public SimpleMappingBean(String platformPropId, RuleExpression expression) {
        this();
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
