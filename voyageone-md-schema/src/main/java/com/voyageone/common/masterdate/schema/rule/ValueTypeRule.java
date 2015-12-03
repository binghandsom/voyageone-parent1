package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class ValueTypeRule extends Rule {
    public ValueTypeRule(String value) {
        super(RuleTypeEnum.VALUE_TYPE_RULE.value(), value);
    }

    public ValueTypeRule() {
        super.name = RuleTypeEnum.VALUE_TYPE_RULE.value();
    }
}
