package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MinLengthRule extends Rule implements IntervalRuleInterface {
    protected String unit;
    public static String UNIT_BYTE = "byte";
    public static String UNIT_CHARACTER = "character";

    public MinLengthRule(String value) {
        super(RuleTypeEnum.MIN_LENGTH_RULE.value(), value, "include");
    }

    public MinLengthRule() {
        super.name = RuleTypeEnum.MIN_LENGTH_RULE.value();
        super.exProperty = "include";
        this.unit = UNIT_BYTE;
    }

    public MinLengthRule(String value, boolean isInclude, String unit) {
        super(RuleTypeEnum.MIN_LENGTH_RULE.value(), value);
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
