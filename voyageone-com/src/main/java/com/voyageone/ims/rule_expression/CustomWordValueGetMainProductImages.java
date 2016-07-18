package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueGetMainProductImages extends CustomWordValue{
    private CustomModuleUserParamGetMainPrductImages userParam;
    @JsonIgnore
    public final static String moduleName = "GetMainProductImages";

    public CustomWordValueGetMainProductImages() {}

    public CustomWordValueGetMainProductImages(RuleExpression htmlTemplate, RuleExpression imageTemplate, RuleExpression imageIndex, RuleExpression imageType, RuleExpression paddingExpression, RuleExpression useOriUrl) {
        this.userParam = new CustomModuleUserParamGetMainPrductImages(htmlTemplate, imageTemplate, imageIndex, imageType, paddingExpression, useOriUrl);
    }

    public CustomModuleUserParamGetMainPrductImages getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamGetMainPrductImages userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
