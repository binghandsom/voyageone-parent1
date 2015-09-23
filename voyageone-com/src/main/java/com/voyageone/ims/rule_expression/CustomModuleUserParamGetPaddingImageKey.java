package com.voyageone.ims.rule_expression;


public class CustomModuleUserParamGetPaddingImageKey extends CustomModuleUserParam {
    //user param
    private RuleExpression imageIndex;
    private RuleExpression paddingPropName;

    public CustomModuleUserParamGetPaddingImageKey() {}

    public CustomModuleUserParamGetPaddingImageKey(RuleExpression paddingPropName, RuleExpression imageIndex) {
        this.imageIndex = imageIndex;
        this.paddingPropName = paddingPropName;
    }

    public RuleExpression getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(RuleExpression imageIndex) {
        this.imageIndex = imageIndex;
    }

    public RuleExpression getPaddingPropName() {
        return paddingPropName;
    }

    public void setPaddingPropName(RuleExpression paddingPropName) {
        this.paddingPropName = paddingPropName;
    }
}
