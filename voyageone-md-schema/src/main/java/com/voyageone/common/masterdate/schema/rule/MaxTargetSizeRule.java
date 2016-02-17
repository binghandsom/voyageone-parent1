package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import org.dom4j.Element;

public class MaxTargetSizeRule extends Rule implements IntervalRuleInterface {
    protected String unit;

    public MaxTargetSizeRule(String value) {
        super(RuleTypeEnum.MAX_TARGET_SIZE_RULE.value(), value, "include");
    }

    public MaxTargetSizeRule() {
        super.name = RuleTypeEnum.MAX_TARGET_SIZE_RULE.value();
        super.exProperty = "include";
        this.unit = "kb";
    }

    public MaxTargetSizeRule(String value, boolean isInclude, String unit) {
        super(RuleTypeEnum.MAX_TARGET_SIZE_RULE.value(), value);
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

    public void specialAttribute(Element rule) {
        if(!StringUtil.isEmpty(this.getUnit())) {
            rule.addAttribute("unit", this.getUnit());
        }

    }
}
