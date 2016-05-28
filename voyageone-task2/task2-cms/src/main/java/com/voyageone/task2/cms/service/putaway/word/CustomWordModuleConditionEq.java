package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamConditionEq;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueConditionEq;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Leo on 15-9-18.
 */
@Repository
public class CustomWordModuleConditionEq extends CustomWordModule{
    public final static String moduleName = CustomWordValueConditionEq.moduleName;

    public CustomWordModuleConditionEq () {
        super(moduleName);
    }

    public CustomWordModuleConditionEq(String moduleName) {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
        //user param
        CustomModuleUserParamConditionEq customModuleUserParamConditionEq = ((CustomWordValueConditionEq) customWord.getValue()).getUserParam();

        RuleExpression firstParamExpression = customModuleUserParamConditionEq.getFirstParam();
        RuleExpression secondParamExpression = customModuleUserParamConditionEq.getSecondParam();

        String firsetParam = expressionParser.parse(firstParamExpression, null);
        String secondParam = expressionParser.parse(secondParamExpression, null);

        if (firsetParam == null && secondParam == null) {
            return String.valueOf(true);
        } else if (firsetParam == null || secondParam == null) {
            return String.valueOf(false);
        } else {
            if (firsetParam.trim().equals(secondParam.trim())) {
                return String.valueOf(true);
            } else {
                return String.valueOf(false);
            }
        }
    }
}
