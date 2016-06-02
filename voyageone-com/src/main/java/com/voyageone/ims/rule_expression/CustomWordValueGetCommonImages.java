package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 16-06-02.
 */
public class CustomWordValueGetCommonImages extends CustomWordValue {
    private CustomModuleUserParamGetCommonImages userParam;
    @JsonIgnore
    public final static String moduleName = "GetCommonImages";

    public CustomWordValueGetCommonImages() {
    }

    public CustomWordValueGetCommonImages(RuleExpression htmlTemplate, RuleExpression imageTemplate, RuleExpression imageType, RuleExpression useOriUrl) {
        this.userParam = new CustomModuleUserParamGetCommonImages(htmlTemplate, imageTemplate, imageType, useOriUrl);
    }

    public CustomModuleUserParamGetCommonImages getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamGetCommonImages userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
