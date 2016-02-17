package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class DisableRule extends Rule {
    public DisableRule(String value) {
        super(RuleTypeEnum.DISABLE_RULE.value(), value);
    }

    public DisableRule() {
        super.name = RuleTypeEnum.DISABLE_RULE.value();
        super.value = "false";
    }
}
