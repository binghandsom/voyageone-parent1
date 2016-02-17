package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class DevTipRule extends Rule {
    private String url;

    public DevTipRule(String value) {
        super(RuleTypeEnum.DEV_TIP_RULE.value(), value);
    }

    public DevTipRule() {
        super.name = RuleTypeEnum.DEV_TIP_RULE.value();
    }

    public DevTipRule(String value, String url) {
        super(RuleTypeEnum.DEV_TIP_RULE.value(), value);
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
