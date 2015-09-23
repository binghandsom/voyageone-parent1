package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.CustomValueSystemParam;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.*;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Leo on 15-9-18.
 */
@Repository
public class CustomWordModuleConditionNeq extends CustomWordModule{
    public final static String moduleName = CustomWordValueConditionNeq.moduleName;

    public CustomWordModuleConditionNeq () {
        super(moduleName);
    }

    public CustomWordModuleConditionNeq(String moduleName) {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam)    {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) {
        //user param
        CustomModuleUserParamConditionNeq customModuleUserParamConditionNeq = ((CustomWordValueConditionNeq) customWord.getValue()).getUserParam();

        RuleExpression firstParamExpression = customModuleUserParamConditionNeq.getFirstParam();
        RuleExpression secondParamExpression = customModuleUserParamConditionNeq.getSecondParam();

        String firsetParam = expressionParser.parse(firstParamExpression, null);
        String secondParam = expressionParser.parse(secondParamExpression, null);

        if (firsetParam == null && secondParam == null) {
            return String.valueOf(false);
        } else if (firsetParam == null || secondParam == null) {
            return String.valueOf(true);
        } else {
            if (firsetParam.trim().equals(secondParam.trim())) {
                return String.valueOf(false);
            } else {
                return String.valueOf(true);
            }
        }
    }
}
