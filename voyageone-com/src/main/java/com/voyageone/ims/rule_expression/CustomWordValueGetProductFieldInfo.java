package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueGetProductFieldInfo extends CustomWordValue{
    private CustomModuleUserParamGetProductFieldInfo userParam;
    @JsonIgnore
    public final static String moduleName = "GetProductFieldInfo";

    public CustomWordValueGetProductFieldInfo() {
    }

    public CustomWordValueGetProductFieldInfo(RuleExpression isMain, RuleExpression codeIdx, RuleExpression dataType, RuleExpression propName, RuleExpression imageType, RuleExpression imageIdx, RuleExpression paddingImageType) {
        this.userParam = new CustomModuleUserParamGetProductFieldInfo(isMain, codeIdx, dataType, propName, imageType, imageIdx, paddingImageType);
    }

    public CustomModuleUserParamGetProductFieldInfo getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamGetProductFieldInfo userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
