package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueJewelryImageTextDesc extends CustomWordValue{
    private CustomModuleUserParamJewelryImageTextDesc userParam;
    @JsonIgnore
    public final static String moduleName = "JewelryImageTextDesc";

    public CustomWordValueJewelryImageTextDesc() {}

    public CustomWordValueJewelryImageTextDesc(RuleExpression htmlTemplate, RuleExpression imageTextTemplate, RuleExpression handMadeImageTemplate, RuleExpression tipImageUrl) {
        this.userParam = new CustomModuleUserParamJewelryImageTextDesc(htmlTemplate, imageTextTemplate, handMadeImageTemplate, tipImageUrl);
    }

    public CustomModuleUserParamJewelryImageTextDesc getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamJewelryImageTextDesc userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
