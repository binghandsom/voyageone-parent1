package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Leo on 15-6-18.
 */
public class CustomWord extends RuleWord {
    @JsonProperty("value")
    private CustomWordValue value;

    private CustomWord() {
        setWordType(WordType.CUSTOM);
    }

    public CustomWord(CustomWordValue customWordValue) {
        this();
        this.value = customWordValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CustomWord)) {
            return false;
        }
        CustomWord customWord = (CustomWord) obj;
        return customWord.getValue().equals(value);
    }

    public CustomWordValue getValue() {
        return value;
    }

    public void setValue(CustomWordValue value) {
        this.value = value;
    }
}

