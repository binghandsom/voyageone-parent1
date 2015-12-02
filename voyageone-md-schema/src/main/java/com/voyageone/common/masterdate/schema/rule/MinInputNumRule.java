package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MinInputNumRule extends Rule implements IntervalRuleInterface {
    public MinInputNumRule(String value) {
        super(RuleTypeEnum.MIN_INPUT_NUM_RULE.value(), value, "include");
    }

    public MinInputNumRule() {
        super.name = RuleTypeEnum.MIN_INPUT_NUM_RULE.value();
        super.exProperty = "include";
    }

    public MinInputNumRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MIN_INPUT_NUM_RULE.value(), value);
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
