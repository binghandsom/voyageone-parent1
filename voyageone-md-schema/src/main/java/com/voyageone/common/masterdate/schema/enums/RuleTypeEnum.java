package com.voyageone.common.masterdate.schema.enums;

import com.voyageone.common.masterdate.schema.rule.DevTipRule;
import com.voyageone.common.masterdate.schema.rule.DisableRule;
import com.voyageone.common.masterdate.schema.rule.MaxDecimalDigitsRule;
import com.voyageone.common.masterdate.schema.rule.MaxImageSizeRule;
import com.voyageone.common.masterdate.schema.rule.MaxInputNumRule;
import com.voyageone.common.masterdate.schema.rule.MaxLengthRule;
import com.voyageone.common.masterdate.schema.rule.MaxTargetSizeRule;
import com.voyageone.common.masterdate.schema.rule.MaxValueRule;
import com.voyageone.common.masterdate.schema.rule.MinDecimalDigitsRule;
import com.voyageone.common.masterdate.schema.rule.MinImageSizeRule;
import com.voyageone.common.masterdate.schema.rule.MinInputNumRule;
import com.voyageone.common.masterdate.schema.rule.MinLengthRule;
import com.voyageone.common.masterdate.schema.rule.MinTargetSizeRule;
import com.voyageone.common.masterdate.schema.rule.MinValueRule;
import com.voyageone.common.masterdate.schema.rule.ReadOnlyRule;
import com.voyageone.common.masterdate.schema.rule.RegexRule;
import com.voyageone.common.masterdate.schema.rule.RequiredRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.masterdate.schema.rule.SetRule;
import com.voyageone.common.masterdate.schema.rule.TipRule;
import com.voyageone.common.masterdate.schema.rule.ValueTypeRule;

public enum RuleTypeEnum {
    MAX_LENGTH_RULE("maxLengthRule"),
    MIN_LENGTH_RULE("minLengthRule"),
    MAX_VALUE_RULE("maxValueRule"),
    MIN_VALUE_RULE("minValueRule"),
    MAX_INPUT_NUM_RULE("maxInputNumRule"),
    MIN_INPUT_NUM_RULE("minInputNumRule"),
    VALUE_TYPE_RULE("valueTypeRule"),
    REQUIRED_RULE("requiredRule"),
    DISABLE_RULE("disableRule"),
    MAX_DECIMAL_DIGITS_RULE("maxDecimalDigitsRule"),
    MIN_DECIMAL_DIGITS_RULE("minDecimalDigitsRule"),
    REGEX_RULE("regexRule"),
    SET_RULE("setRule"),
    TIP_RULE("tipRule"),
    DEV_TIP_RULE("devTipRule"),
    READ_ONLY_RULE("readOnlyRule"),
    MAX_TARGET_SIZE_RULE("maxTargetSizeRule"),
    MIN_TARGET_SIZE_RULE("minTargetSizeRule"),
    MAX_IMAGE_SIZE_RULE("maxImageSizeRule"),
    MIN_IMAGE_SIZE_RULE("minImageSizeRule");

    private final String type;

    public static Rule createRule(RuleTypeEnum ruleType) {
        Object rule = null;
        switch(ruleType) {
            case MAX_LENGTH_RULE:
                rule = new MaxLengthRule();
                break;
            case MIN_LENGTH_RULE:
                rule = new MinLengthRule();
                break;
            case MAX_VALUE_RULE:
                rule = new MaxValueRule();
                break;
            case MIN_VALUE_RULE:
                rule = new MinValueRule();
                break;
            case MAX_INPUT_NUM_RULE:
                rule = new MaxInputNumRule();
                break;
            case MIN_INPUT_NUM_RULE:
                rule = new MinInputNumRule();
                break;
            case VALUE_TYPE_RULE:
                rule = new ValueTypeRule();
                break;
            case REQUIRED_RULE:
                rule = new RequiredRule();
                break;
            case DISABLE_RULE:
                rule = new DisableRule();
                break;
            case MAX_DECIMAL_DIGITS_RULE:
                rule = new MaxDecimalDigitsRule();
                break;
            case MIN_DECIMAL_DIGITS_RULE:
                rule = new MinDecimalDigitsRule();
                break;
            case REGEX_RULE:
                rule = new RegexRule();
                break;
            case SET_RULE:
                rule = new SetRule();
                break;
            case TIP_RULE:
                rule = new TipRule();
                break;
            case DEV_TIP_RULE:
                rule = new DevTipRule();
                break;
            case READ_ONLY_RULE:
                rule = new ReadOnlyRule();
                break;
            case MAX_TARGET_SIZE_RULE:
                rule = new MaxTargetSizeRule();
                break;
            case MIN_TARGET_SIZE_RULE:
                rule = new MinTargetSizeRule();
                break;
            case MAX_IMAGE_SIZE_RULE:
                rule = new MaxImageSizeRule();
                break;
            case MIN_IMAGE_SIZE_RULE:
                rule = new MinImageSizeRule();
        }

        return (Rule)rule;
    }

    private RuleTypeEnum(String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }

    public String value() {
        return this.type;
    }

    public static RuleTypeEnum getEnum(String name) {
        RuleTypeEnum[] values = values();
        RuleTypeEnum[] arr$ = values;
        int len$ = values.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            RuleTypeEnum value = arr$[i$];
            if(value.value().equals(name)) {
                return value;
            }
        }

        return null;
    }
}
