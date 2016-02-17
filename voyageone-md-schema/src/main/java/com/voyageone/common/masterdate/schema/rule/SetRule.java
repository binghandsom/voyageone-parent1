package com.voyageone.common.masterdate.schema.rule;


import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class SetRule extends Rule {
    public SetRule(String value) {
        super(RuleTypeEnum.SET_RULE.value(), value);
    }

    public SetRule() {
        super.name = RuleTypeEnum.SET_RULE.value();
    }
}
