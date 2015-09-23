package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Leo on 15-6-16.
 */
public class DictWord extends RuleWord {
    @JsonProperty("value")
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RuleExpression expression;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isUrl;

    public DictWord() {
        setWordType(WordType.DICT);
        isUrl = null;
    }

    public DictWord(String name) {
        this();
        this.name = name;
    }

    public DictWord(String name, RuleExpression expression, boolean isUrl) {
        this();
        this.name = name;
        this.expression = expression;
        this.isUrl = isUrl;
    }

    @Override
    public String toString() {
        return "Dict:{name:" + name + ", value:" + expression + "}";
    }

    public RuleExpression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DictWord)) {
            return false;
        }
        DictWord dictWord = (DictWord) obj;
        return dictWord.getName().equals(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpression(RuleExpression expression) {
        this.expression = expression;
    }

    public Boolean getIsUrl() {
        return isUrl;
    }

    public void setIsUrl(Boolean isUrl) {
        this.isUrl = isUrl;
    }
}
