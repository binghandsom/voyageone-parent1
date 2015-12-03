package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MaxInputNumRule extends Rule implements IntervalRuleInterface {
    public MaxInputNumRule(String value) {
        super(RuleTypeEnum.MAX_INPUT_NUM_RULE.value(), value, "include");
    }

    public MaxInputNumRule() {
        super.name = RuleTypeEnum.MAX_INPUT_NUM_RULE.value();
        super.exProperty = "include";
    }

    public MaxInputNumRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MAX_INPUT_NUM_RULE.value(), value);
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
