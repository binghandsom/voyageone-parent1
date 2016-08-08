package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-9-18.
 */
public class CustomModuleUserParamIf extends CustomModuleUserParam {
    private RuleExpression condition;
    private RuleExpression propValue;
    private RuleExpression propValue2;
    private RuleExpression propValue3;
    private RuleExpression propValue4;

    public CustomModuleUserParamIf() {
    }

    public CustomModuleUserParamIf(RuleExpression condition, RuleExpression propValue, RuleExpression propValue2, RuleExpression propValue3, RuleExpression propValue4) {
        this.condition = condition;
        this.propValue = propValue;
        this.propValue2 = propValue2;
        this.propValue3 = propValue3;
        this.propValue4 = propValue4;
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

    public RuleExpression getPropValue2() {
        return propValue2;
    }

    public void setPropValue2(RuleExpression propValue2) {
        this.propValue2 = propValue2;
    }

    public RuleExpression getPropValue3() {
        return propValue3;
    }

    public void setPropValue3(RuleExpression propValue3) {
        this.propValue3 = propValue3;
    }

    public RuleExpression getPropValue4() {
        return propValue4;
    }

    public void setPropValue4(RuleExpression propValue4) {
        this.propValue4 = propValue4;
    }
}
