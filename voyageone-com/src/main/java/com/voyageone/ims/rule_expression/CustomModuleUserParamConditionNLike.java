package com.voyageone.ims.rule_expression;

public class CustomModuleUserParamConditionNLike extends CustomModuleUserParam {
    private RuleExpression firstParam;
    private RuleExpression secondParam;

    public CustomModuleUserParamConditionNLike() {
    }

    public CustomModuleUserParamConditionNLike(RuleExpression firstParam, RuleExpression secondParam) {
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
