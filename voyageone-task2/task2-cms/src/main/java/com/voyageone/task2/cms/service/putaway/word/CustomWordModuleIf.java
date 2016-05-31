package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamIf;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueIf;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Leo on 15-9-18.
 */
@Repository
public class CustomWordModuleIf extends CustomWordModule{
    public final static String moduleName = CustomWordValueIf.moduleName;

    public CustomWordModuleIf() {
        super(moduleName);
    }

    public CustomWordModuleIf(String moduleName) {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
        //user param
        CustomModuleUserParamIf customModuleUserParamIf = ((CustomWordValueIf) customWord.getValue()).getUserParam();

        RuleExpression conditionExpression = customModuleUserParamIf.getCondition();
        RuleExpression propValueExpression = customModuleUserParamIf.getPropValue();

        String condition = expressionParser.parse(conditionExpression, null);
        String propValue = expressionParser.parse(propValueExpression, null);

        Boolean conditionResult = Boolean.valueOf(condition);
        if (conditionResult == null) {
            return null;
        } else if (conditionResult) {
            return propValue;
        } else
            return null;
    }
}
