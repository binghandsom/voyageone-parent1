package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-9-18.
 */
public class CustomModuleUserParamConditionNeq extends CustomModuleUserParam {
    private RuleExpression firstParam;
    private RuleExpression secondParam;
    private RuleExpression ignoreCaseFlg;

    public CustomModuleUserParamConditionNeq() {
    }

    public CustomModuleUserParamConditionNeq(RuleExpression firstParam, RuleExpression secondParam, RuleExpression ignoreCaseFlg) {
        this.firstParam = firstParam;
        this.secondParam = secondParam;
        this.ignoreCaseFlg = ignoreCaseFlg;
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

    public RuleExpression getIgnoreCaseFlg() {
        return ignoreCaseFlg;
    }

    public void setIgnoreCaseFlg(RuleExpression ignoreCaseFlg) {
        this.ignoreCaseFlg = ignoreCaseFlg;
    }
}
