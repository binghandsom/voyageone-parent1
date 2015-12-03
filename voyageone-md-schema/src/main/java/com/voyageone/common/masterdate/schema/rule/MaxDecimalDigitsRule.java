package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MaxDecimalDigitsRule extends Rule implements IntervalRuleInterface {
    public MaxDecimalDigitsRule(String value) {
        super(RuleTypeEnum.MAX_DECIMAL_DIGITS_RULE.value(), value, "include");
    }

    public MaxDecimalDigitsRule() {
        super.name = RuleTypeEnum.MAX_DECIMAL_DIGITS_RULE.value();
        super.exProperty = "include";
    }

    public MaxDecimalDigitsRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MAX_DECIMAL_DIGITS_RULE.value(), value);
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
