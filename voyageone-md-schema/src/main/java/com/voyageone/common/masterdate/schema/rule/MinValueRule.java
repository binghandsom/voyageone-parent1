package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MinValueRule extends Rule implements IntervalRuleInterface {
    public MinValueRule(String value) {
        super(RuleTypeEnum.MIN_VALUE_RULE.value(), value, "include");
    }

    public MinValueRule() {
        super.name = RuleTypeEnum.MIN_VALUE_RULE.value();
        super.exProperty = "include";
    }

    public MinValueRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MIN_VALUE_RULE.value(), value);
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
