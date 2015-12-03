package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MaxLengthRule extends Rule implements IntervalRuleInterface {
    protected String unit;
    public static String UNIT_BYTE = "byte";
    public static String UNIT_CHARACTER = "character";

    public MaxLengthRule(String value) {
        super(RuleTypeEnum.MAX_LENGTH_RULE.value(), value, "include");
        this.unit = UNIT_BYTE;
    }

    public MaxLengthRule() {
        super.name = RuleTypeEnum.MAX_LENGTH_RULE.value();
        super.exProperty = "include";
    }

    public MaxLengthRule(String value, boolean isInclude, String unit) {
        super(RuleTypeEnum.MAX_LENGTH_RULE.value(), value);
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

    public void setUnitByte() {
        this.unit = UNIT_BYTE;
    }

    public void setUnitCharacter() {
        this.unit = UNIT_CHARACTER;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
