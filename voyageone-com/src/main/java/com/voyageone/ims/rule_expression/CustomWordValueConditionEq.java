package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueConditionEq extends CustomWordValue{
    private CustomModuleUserParamConditionEq userParam;
    @JsonIgnore
    public final static String moduleName = "ConditionEq";

    public CustomWordValueConditionEq() {
    }

    public CustomWordValueConditionEq(RuleExpression firstParam, RuleExpression secondParam, RuleExpression ignoreCaseFlg) {
        this.userParam = new CustomModuleUserParamConditionEq(firstParam, secondParam, ignoreCaseFlg);
    }

    public CustomModuleUserParamConditionEq getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamConditionEq userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
