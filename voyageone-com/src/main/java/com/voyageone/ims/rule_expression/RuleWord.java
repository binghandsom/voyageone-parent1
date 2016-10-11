package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Leo on 15-6-2.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME
    , include = JsonTypeInfo.As.PROPERTY
    , property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextWord.class, name = "TEXT"),
        @JsonSubTypes.Type(value = CmsWord.class, name = "CMS"),
        @JsonSubTypes.Type(value = DictWord.class, name = "DICT"),
        @JsonSubTypes.Type(value = CustomWord.class, name = "CUSTOM"),
        @JsonSubTypes.Type(value = MasterWord.class, name = "MASTER"),
        @JsonSubTypes.Type(value = MasterHtmlWord.class, name = "MASTER_HTML"),
        @JsonSubTypes.Type(value = MasterClrHtmlWord.class, name = "MASTER_CLR_HTML"),
        @JsonSubTypes.Type(value = ConditionWord.class, name = "CONDITION"),
        @JsonSubTypes.Type(value = FeedCnWord.class, name = "FEED_CN"),
        @JsonSubTypes.Type(value = FeedOrgWord.class, name = "FEED_ORG"),
        @JsonSubTypes.Type(value = SkuWord.class, name = "SKU"),
        @JsonSubTypes.Type(value = CommonWord.class, name = "COMMON"),
        //TODO add other field
})
public abstract class RuleWord {
    @JsonIgnore
    private WordType wordType;

    public RuleExpression toExpression() {
        RuleExpression ruleExpression = new RuleExpression();
        ruleExpression.addRuleWord(this);
        return ruleExpression;
    }
    public WordType getWordType() {
        return wordType;
    }

    public void setWordType(WordType wordType) {
        this.wordType = wordType;
    }
}
