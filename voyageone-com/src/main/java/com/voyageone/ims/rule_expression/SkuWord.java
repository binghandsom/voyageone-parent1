package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 16-1-7.
 */
public class SkuWord extends RuleWord {
    private String value;


    public SkuWord() {
        setWordType(WordType.SKU);
    }

    public SkuWord(String value) {
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
