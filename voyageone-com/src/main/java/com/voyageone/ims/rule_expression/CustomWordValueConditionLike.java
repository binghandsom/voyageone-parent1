package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomWordValueConditionLike extends CustomWordValue{
    private CustomModuleUserParamConditionLike userParam;
    @JsonIgnore
    public final static String moduleName = "ConditionLike";

    public CustomWordValueConditionLike() {
    }

    public CustomWordValueConditionLike(RuleExpression firstParam, RuleExpression secondParam) {
        this.userParam = new CustomModuleUserParamConditionLike(firstParam, secondParam);
    }

    public CustomModuleUserParamConditionLike getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamConditionLike userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
