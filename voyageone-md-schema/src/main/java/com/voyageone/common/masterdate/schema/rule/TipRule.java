package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class TipRule extends Rule {
    private String url;

    public TipRule(String value) {
        super(RuleTypeEnum.TIP_RULE.value(), value);
    }

    public TipRule() {
        super.name = RuleTypeEnum.TIP_RULE.value();
    }

    public TipRule(String value, String url) {
        super(RuleTypeEnum.TIP_RULE.value(), value);
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
