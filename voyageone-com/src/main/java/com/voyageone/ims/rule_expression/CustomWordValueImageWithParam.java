package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueImageWithParam extends CustomWordValue{
    private CustomModuleUserParamImageWithParam userParam;
    @JsonIgnore
    public final static String moduleName = "ImageWithParam";

    public CustomWordValueImageWithParam() {
    }

    public CustomWordValueImageWithParam(RuleExpression imageTemplate, List<RuleExpression> imageParams, RuleExpression useCmsBtImageTemplate) {
        this.userParam = new CustomModuleUserParamImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate);
    }

    public CustomModuleUserParamImageWithParam getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamImageWithParam userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
