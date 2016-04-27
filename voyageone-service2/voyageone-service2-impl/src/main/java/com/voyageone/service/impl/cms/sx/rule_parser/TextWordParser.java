package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;
import com.voyageone.ims.rule_expression.WordType;

/**
 * Created by morse.lu on 16-4-26.(copy from task2)
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
