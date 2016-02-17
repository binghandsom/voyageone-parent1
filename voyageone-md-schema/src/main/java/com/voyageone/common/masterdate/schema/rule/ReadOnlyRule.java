package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class ReadOnlyRule extends Rule {
    public ReadOnlyRule(String value) {
        super(RuleTypeEnum.READ_ONLY_RULE.value(), value);
    }

    public ReadOnlyRule() {
        super.name = RuleTypeEnum.READ_ONLY_RULE.value();
    }
}
