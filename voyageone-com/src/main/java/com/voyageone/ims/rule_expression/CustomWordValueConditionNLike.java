package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomWordValueConditionNLike extends CustomWordValue{
    private CustomModuleUserParamConditionNLike userParam;
    @JsonIgnore
    public final static String moduleName = "ConditionNLike";

    public CustomWordValueConditionNLike() {
    }

    public CustomWordValueConditionNLike(RuleExpression firstParam, RuleExpression secondParam) {
        this.userParam = new CustomModuleUserParamConditionNLike(firstParam, secondParam);
    }

    public CustomModuleUserParamConditionNLike getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamConditionNLike userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
