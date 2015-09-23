package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-6-18.
 */
public class TextWord extends RuleWord {
    private String value;

    public TextWord() {
        setWordType(WordType.TEXT);
    }

    public TextWord(String value)
    {
        this();
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TextWord))
        {
            return false;
        }
        TextWord textWord = (TextWord) obj;
        return textWord.getValue().equals(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
