package com.voyageone.ims.rule_expression;

import java.util.Map;

/**
 * Mapping type是Common类型的设值,与MASTER类型比，除了取值地方不同，逻辑完全一致
 *
 * @author morse.lu 2016/06/27
 * @since 2.1.0
 */
public class CommonWord extends RuleWord {
    private String value;
    private Map<String, String> extra;

    public CommonWord() {
        setWordType(WordType.COMMON);
    }

    public CommonWord(String value) {
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
