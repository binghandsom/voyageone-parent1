package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-9-18.
 */
public class CustomModuleUserParamConditionEq extends CustomModuleUserParam {
    private RuleExpression firstParam;
    private RuleExpression secondParam;

    public CustomModuleUserParamConditionEq() {
    }

    public CustomModuleUserParamConditionEq(RuleExpression firstParam, RuleExpression secondParam) {
        this.firstParam = firstParam;
        this.secondParam = secondParam;
    }

    public RuleExpression getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(RuleExpression firstParam) {
        this.firstParam = firstParam;
    }

    public RuleExpression getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(RuleExpression secondParam) {
        this.secondParam = secondParam;
    }
}
