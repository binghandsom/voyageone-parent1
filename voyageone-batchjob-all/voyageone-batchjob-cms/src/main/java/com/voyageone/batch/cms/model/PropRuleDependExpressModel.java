package com.voyageone.batch.cms.model;

public class PropRuleDependExpressModel {

    // 关联属性的id
    private String extraPropId;
    // 关联属性的判断符号
    private String symbol;
    // 关联属性的判断值
    private String extraValue;

    public String getExtraPropId() {
        return extraPropId;
    }

    public void setExtraPropId(String extraPropId) {
        this.extraPropId = extraPropId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }
}
