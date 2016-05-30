package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 15-9-18.
 */
@Repository
public class CustomWordModuleConditionAnd extends CustomWordModule{
    public final static String moduleName = CustomWordValueConditionAnd.moduleName;

    public CustomWordModuleConditionAnd () {
        super(moduleName);
    }

    public CustomWordModuleConditionAnd(String moduleName) {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
        //user param
        CustomModuleUserParamConditionAnd customModuleUserParamConditionAnd = ((CustomWordValueConditionAnd) customWord.getValue()).getUserParam();

        RuleExpression conditionListExpression = customModuleUserParamConditionAnd.getConditionListExpression();


        List<RuleWord> ruleWordList = conditionListExpression.getRuleWordList();
        for (RuleWord ruleWord : ruleWordList) {
            if (!(ruleWord instanceof  CustomWord)) {
                return String.valueOf(false);
            }
            CustomWord conditionWord = (CustomWord) ruleWord;
            RuleExpression conditionExpression = new RuleExpression();
            conditionExpression.addRuleWord(conditionWord);
            String booleanRs = expressionParser.parse(conditionExpression, null);
            if (!Boolean.valueOf(booleanRs))
                return String.valueOf(false);
        }

        return String.valueOf(true);
    }
}
