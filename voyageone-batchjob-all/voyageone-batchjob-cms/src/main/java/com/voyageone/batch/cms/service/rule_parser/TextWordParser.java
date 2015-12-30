package com.voyageone.batch.cms.service.rule_parser;

import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;
import com.voyageone.ims.rule_expression.WordType;
import java.util.Set;

/**
 * Created by Leo on 15-6-18.
 */
public class TextWordParser {
    public String parse(RuleWord ruleWord, Set<String> imageSet)
    {
        if (!WordType.TEXT.equals(ruleWord.getWordType()))
            return null;
        else
        {
            TextWord textWord = (TextWord) ruleWord;
            String textValue = textWord.getValue();
            if (textWord.isUrl()) {
                imageSet.add(textValue);
            }
            return textValue;
        }
    }
}
