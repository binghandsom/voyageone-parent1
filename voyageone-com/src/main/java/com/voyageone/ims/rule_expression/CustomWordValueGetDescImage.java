package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tom on 16-8-26.
 */
public class CustomWordValueGetDescImage extends CustomWordValue{
    private CustomModuleUserParamGetDescImage userParam;
    @JsonIgnore
    public final static String moduleName = "GetDescImage";

    public CustomWordValueGetDescImage() {}

    public CustomWordValueGetDescImage(RuleExpression field, RuleExpression width, RuleExpression startX, RuleExpression startY, RuleExpression sectionSize, RuleExpression fontSize, RuleExpression oneLineBit) {
        this.userParam = new CustomModuleUserParamGetDescImage(field, width, startX, startY, sectionSize, fontSize, oneLineBit);
    }

    public CustomModuleUserParamGetDescImage getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamGetDescImage userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
