package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class RegexRule extends Rule {
    public RegexRule(String value) {
        super(RuleTypeEnum.REGEX_RULE.value(), value);
    }

    public RegexRule() {
        super.name = RuleTypeEnum.REGEX_RULE.value();
    }
}