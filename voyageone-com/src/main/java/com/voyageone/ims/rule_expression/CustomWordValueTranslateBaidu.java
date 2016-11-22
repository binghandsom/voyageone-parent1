package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 16-06-02.
 */
public class CustomWordValueTranslateBaidu extends CustomWordValue {
    private CustomModuleUserParamTranslateBaidu userParam;
    @JsonIgnore
    public final static String moduleName = "TranslateBaidu";

    public CustomWordValueTranslateBaidu() {
    }

    public CustomWordValueTranslateBaidu(RuleExpression transTarget, RuleExpression transType, RuleExpression separator, RuleExpression paddingExpression) {
        this.userParam = new CustomModuleUserParamTranslateBaidu(transTarget, transType, separator, paddingExpression);
    }

    public CustomModuleUserParamTranslateBaidu getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamTranslateBaidu userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
