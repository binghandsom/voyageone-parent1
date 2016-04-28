package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.ims.rule_expression.ConditionWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by morse.lu on 16-4-26.(copy from task2)
 */
public class ConditionWordParser {
    private ExpressionParser expressionParser;

    public ConditionWordParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public String parse(RuleWord ruleWord)
    {
        if (!WordType.CONDITION.equals(ruleWord.getWordType()))
        {
            return null;
        }
        else
        {
            ConditionWord conditionWord = (ConditionWord) ruleWord;
            String fullPropName = conditionWord.getPropName();
            Map.Entry<WordType, String> decodedPropName = decodePropName(fullPropName);
            switch (decodedPropName.getKey()) {
                case MASTER:
                    //masterWordParser.parse()
                    break;
                case CMS:
                    break;
            }
        }
        return null;
    }

    public Map.Entry<WordType, String> decodePropName(String fullPropName) {
        String separator = ".";
        if (fullPropName == null)
            return null;
        String[] propNameArrays = fullPropName.split(separator);
        if (propNameArrays.length !=2 ) {
            return null;
        }
        Map.Entry<WordType, String> decodedPropName = new AbstractMap.SimpleEntry<WordType, String>(
                WordType.valueOf(propNameArrays[0]),
                propNameArrays[1]
        );
        return decodedPropName;
    }

}
