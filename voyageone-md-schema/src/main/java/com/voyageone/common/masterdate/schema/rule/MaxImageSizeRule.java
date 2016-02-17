package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MaxImageSizeRule extends Rule implements IntervalRuleInterface {
    public MaxImageSizeRule(String value) {
        super(RuleTypeEnum.MAX_IMAGE_SIZE_RULE.value(), value, "include");
    }

    public MaxImageSizeRule() {
        super.name = RuleTypeEnum.MAX_IMAGE_SIZE_RULE.value();
        super.exProperty = "include";
    }

    public MaxImageSizeRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MAX_IMAGE_SIZE_RULE.value(), value);
        if(isInclude) {
            super.exProperty = "include";
        } else {
            super.exProperty = "not include";
        }

    }

    public void setValueIntervalInclude() {
        super.exProperty = "include";
    }

    public void setValueIntervalNotInclude() {
        super.exProperty = "not include";
    }
}
