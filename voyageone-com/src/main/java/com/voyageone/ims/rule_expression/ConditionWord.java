package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-9-18.
 */
public class ConditionWord extends RuleWord{
    private ConditionSymbolEnum symbol;
    private String propName;
    private String propValue;

    public ConditionWord() {
        setWordType(WordType.CONDITION);
    }

    public ConditionWord(ConditionSymbolEnum symbol, String propName, String propValue) {
        this();
        this.symbol = symbol;
        this.propName = propName;
        this.propValue = propValue;
    }

    public ConditionSymbolEnum getSymbol() {
        return symbol;
    }

    public void setSymbol(ConditionSymbolEnum symbol) {
        this.symbol = symbol;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }
}
