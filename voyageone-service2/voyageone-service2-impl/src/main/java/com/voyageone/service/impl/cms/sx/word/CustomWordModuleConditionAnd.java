package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.*;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleConditionAnd extends CustomWordModule {
    public final static String moduleName = CustomWordValueConditionAnd.moduleName;

    public CustomWordModuleConditionAnd () {
        super(moduleName);
    }

    public CustomWordModuleConditionAnd(String moduleName) {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user)    {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
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
            String booleanRs = expressionParser.parse(conditionExpression, shopBean, user, extParameter);
            if (!Boolean.valueOf(booleanRs))
                return String.valueOf(false);
        }

        return String.valueOf(true);
    }
}
