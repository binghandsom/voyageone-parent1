package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-6-18.
 */
public class TextWord extends RuleWord {
    private String value;
    private boolean isUrl;

    public TextWord() {
        setWordType(WordType.TEXT);
        isUrl = false;
    }

    public TextWord(String value)
    {
        this();
        this.value = value;
    }

    public boolean isUrl() {
        return isUrl;
    }

    public void setUrl(boolean url) {
        isUrl = url;
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
