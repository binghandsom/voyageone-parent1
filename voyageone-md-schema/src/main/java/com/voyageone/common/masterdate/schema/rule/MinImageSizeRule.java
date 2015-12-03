package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MinImageSizeRule extends Rule implements IntervalRuleInterface {
    public MinImageSizeRule(String value) {
        super(RuleTypeEnum.MIN_IMAGE_SIZE_RULE.value(), value, "include");
    }

    public MinImageSizeRule() {
        super.name = RuleTypeEnum.MIN_IMAGE_SIZE_RULE.value();
        super.exProperty = "include";
    }

    public MinImageSizeRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MIN_IMAGE_SIZE_RULE.value(), value);
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
