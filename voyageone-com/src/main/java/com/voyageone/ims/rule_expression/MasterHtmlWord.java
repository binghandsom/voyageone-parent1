package com.voyageone.ims.rule_expression;

import java.util.Map;

/**
 * Created by Leo on 15-8-26.
 */
public class MasterHtmlWord extends MasterWord{
    private String value;
    private Map<String, String> extra;

    public MasterHtmlWord() {
        setWordType(WordType.MASTER_HTML);
    }

    public MasterHtmlWord(String value) {
        this();
        this.value = value;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
