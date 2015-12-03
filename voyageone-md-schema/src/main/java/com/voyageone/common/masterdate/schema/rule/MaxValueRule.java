package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MaxValueRule extends Rule implements IntervalRuleInterface {
    public MaxValueRule(String value) {
        super(RuleTypeEnum.MAX_VALUE_RULE.value(), value, "include");
    }

    public MaxValueRule() {
        super.name = RuleTypeEnum.MAX_VALUE_RULE.value();
        super.exProperty = "include";
    }

    public MaxValueRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MAX_VALUE_RULE.value(), value);
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
