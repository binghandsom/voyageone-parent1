package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueIf extends CustomWordValue{
    private CustomModuleUserParamIf userParam;
    @JsonIgnore
    public final static String moduleName = "If";

    public CustomWordValueIf() {
    }

    public CustomWordValueIf(RuleExpression condition, RuleExpression propName) {
        this.userParam = new CustomModuleUserParamIf(condition, propName);
    }

    public CustomModuleUserParamIf getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamIf userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
