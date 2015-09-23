package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueImageFormat extends CustomWordValue{
    private CustomModuleUserParamImageFormat userParam;
    @JsonIgnore
    public final static String moduleName = "ImageFormat";

    public CustomWordValueImageFormat() {
    }

    public CustomWordValueImageFormat(RuleExpression htmlTemplate, RuleExpression imageUrl) {
        this.userParam = new CustomModuleUserParamImageFormat(htmlTemplate, imageUrl);
    }

    public CustomModuleUserParamImageFormat getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamImageFormat userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
