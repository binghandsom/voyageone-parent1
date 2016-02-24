package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.CustomValueSystemParam;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamConditionLike;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueConditionLike;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class CustomWordModuleConditionLike extends CustomWordModule{
    public final static String moduleName = CustomWordValueConditionLike.moduleName;

    public CustomWordModuleConditionLike() {
        super(moduleName);
    }

    public CustomWordModuleConditionLike(String moduleName) {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam)    {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) {
        //user param
        CustomModuleUserParamConditionLike customModuleUserParamConditionLike = ((CustomWordValueConditionLike) customWord.getValue()).getUserParam();

        RuleExpression firstParamExpression = customModuleUserParamConditionLike.getFirstParam();
        RuleExpression secondParamExpression = customModuleUserParamConditionLike.getSecondParam();

        String firsetParam = expressionParser.parse(firstParamExpression, null);
        String secondParam = expressionParser.parse(secondParamExpression, null);

        // 防止null
        if (StringUtils.isEmpty(firsetParam)) {
            firsetParam = "";
        }
        if (StringUtils.isEmpty(secondParam)) {
            secondParam = "";
        }

        // 都转成小写
        firsetParam = firsetParam.toLowerCase();
        secondParam = secondParam.toLowerCase();

        // 查看是否包含
        if (firsetParam.contains(secondParam)) {
            return String.valueOf(true);
        } else {
            return String.valueOf(false);
        }

    }
}
