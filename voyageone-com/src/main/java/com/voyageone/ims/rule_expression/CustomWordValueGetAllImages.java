package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueGetAllImages extends CustomWordValue {
    private CustomModuleUserParamGetAllImages userParam;
    @JsonIgnore
    public final static String moduleName = "GetAllImages";

    public CustomWordValueGetAllImages() {
    }

    public CustomWordValueGetAllImages(RuleExpression htmlTemplate, RuleExpression imageTemplate, RuleExpression imageType, RuleExpression useOriUrl, RuleExpression useCmsBtImageTemplate, RuleExpression viewType) {
        this.userParam = new CustomModuleUserParamGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, useCmsBtImageTemplate, viewType);
    }

    public CustomModuleUserParamGetAllImages getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamGetAllImages userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
