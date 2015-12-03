package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class RequiredRule extends Rule {
    public RequiredRule(String value) {
        super(RuleTypeEnum.REQUIRED_RULE.value(), value);
    }

    public RequiredRule() {
        super.name = RuleTypeEnum.REQUIRED_RULE.value();
    }
}
