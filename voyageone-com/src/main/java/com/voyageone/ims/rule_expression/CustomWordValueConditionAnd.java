package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueConditionAnd extends CustomWordValue{
    private CustomModuleUserParamConditionAnd userParam;
    @JsonIgnore
    public final static String moduleName = "ConditionAnd";

    public CustomWordValueConditionAnd() {
    }

    public CustomWordValueConditionAnd(RuleExpression conditionListExpression) {
        this.userParam = new CustomModuleUserParamConditionAnd(conditionListExpression);
    }

    public CustomModuleUserParamConditionAnd getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamConditionAnd userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
