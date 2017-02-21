package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueConditionNeq extends CustomWordValue{
    private CustomModuleUserParamConditionNeq userParam;
    @JsonIgnore
    public final static String moduleName = "ConditionNeq";

    public CustomWordValueConditionNeq() {
    }

    public CustomWordValueConditionNeq(RuleExpression firstParam, RuleExpression secondParam, RuleExpression ignoreCaseFlg) {
        this.userParam = new CustomModuleUserParamConditionNeq(firstParam, secondParam, ignoreCaseFlg);
    }

    public CustomModuleUserParamConditionNeq getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamConditionNeq userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
