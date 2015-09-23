package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-8-26.
 */
public class MasterWord extends RuleWord{
    private String value;

    public MasterWord() {
        setWordType(WordType.MASTER);
    }

    public MasterWord(String value) {
        this();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
