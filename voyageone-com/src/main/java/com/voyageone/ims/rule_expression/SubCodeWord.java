package com.voyageone.ims.rule_expression;

/**
 * Created by tom on 16-12-8.
 */
public class SubCodeWord extends RuleWord {
    private int codeIdx;
    private String value;

    public SubCodeWord() {
        setWordType(WordType.SUBCODE);
    }

    public SubCodeWord(int codeIdx, String value)
    {
        this();
        this.codeIdx = codeIdx;
        this.value = value;
    }

    public int getCodeIdx() {
        return codeIdx;
    }

    public void setCodeIdx(int codeIdx) {
        this.codeIdx = codeIdx;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
