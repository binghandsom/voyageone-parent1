package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-9-18.
 */
public class ConditionExpression {
    private WordType type;
    private ConditionSymbolEnum symbol;
    private String key;
    private String value;


    public WordType getType() {
        return type;
    }

    public void setType(WordType type) {
        this.type = type;
    }

    public ConditionSymbolEnum getSymbol() {
        return symbol;
    }

    public void setSymbol(ConditionSymbolEnum symbol) {
        this.symbol = symbol;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
