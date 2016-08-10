package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-9-18.
 */
public class CustomModuleUserParamIf extends CustomModuleUserParam {
    private RuleExpression condition;
    private RuleExpression propValue;

    public CustomModuleUserParamIf() {
    }

    public CustomModuleUserParamIf(RuleExpression condition, RuleExpression propValue, RuleExpression propValue2, RuleExpression propValue3, RuleExpression propValue4) {
        this.condition = condition;
        this.propValue = propValue;
    }

    public RuleExpression getCondition() {
        return condition;
    }

    public void setCondition(RuleExpression condition) {
        this.condition = condition;
    }

    public RuleExpression getPropValue() {
        return propValue;
    }

    public void setPropValue(RuleExpression propValue) {
        this.propValue = propValue;
    }
}
