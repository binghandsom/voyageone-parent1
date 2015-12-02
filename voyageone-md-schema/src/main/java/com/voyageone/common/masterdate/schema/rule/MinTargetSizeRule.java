package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MinTargetSizeRule extends Rule implements IntervalRuleInterface {
    protected String unit;

    public MinTargetSizeRule(String value) {
        super(RuleTypeEnum.MIN_TARGET_SIZE_RULE.value(), value, "include");
    }

    public MinTargetSizeRule() {
        super.name = RuleTypeEnum.MIN_TARGET_SIZE_RULE.value();
        super.exProperty = "include";
        this.unit = "kb";
    }

    public MinTargetSizeRule(String value, boolean isInclude, String unit) {
        super(RuleTypeEnum.MIN_TARGET_SIZE_RULE.value(), value);
        if(isInclude) {
            super.exProperty = "include";
        } else {
            super.exProperty = "not include";
        }

        this.unit = unit;
    }

    public void setValueIntervalInclude() {
        super.exProperty = "include";
    }

    public void setValueIntervalNotInclude() {
        super.exProperty = "not include";
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
