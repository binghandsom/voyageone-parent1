package com.voyageone.batch.cms.service.putaway.rule_parser;

import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;
import com.voyageone.ims.rule_expression.WordType;

/**
 * Created by Leo on 15-6-18.
 */
public class TextWordParser {
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.TEXT.equals(ruleWord.getWordType()))
            return null;
        else
        {
            TextWord textWord = (TextWord) ruleWord;
            String textValue = textWord.getValue();
            return textValue;
        }
    }
}
