package com.voyageone.ims.rule_expression;

public class CustomModuleUserParamConditionLike extends CustomModuleUserParam {
    private RuleExpression firstParam;
    private RuleExpression secondParam;

    public CustomModuleUserParamConditionLike() {
    }

    public CustomModuleUserParamConditionLike(RuleExpression firstParam, RuleExpression secondParam) {
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
