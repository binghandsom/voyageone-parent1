package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueGetPaddingImageKey extends CustomWordValue{
    private CustomModuleUserParamGetPaddingImageKey userParam;
    @JsonIgnore
    public final static String moduleName = "GetPaddingImageKey";

    public CustomWordValueGetPaddingImageKey() {
    }

    public CustomWordValueGetPaddingImageKey(RuleExpression paddingPropName, RuleExpression imageIndex) {
        this.userParam = new CustomModuleUserParamGetPaddingImageKey(paddingPropName, imageIndex);
    }

    public CustomModuleUserParamGetPaddingImageKey getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamGetPaddingImageKey userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
