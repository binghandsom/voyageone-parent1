package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.CustomModuleUserParamIf;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueIf;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleIf extends CustomWordModule {
    public final static String moduleName = CustomWordValueIf.moduleName;

    public CustomWordModuleIf() {
        super(moduleName);
    }

    public CustomWordModuleIf(String moduleName) {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user)    {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamIf customModuleUserParamIf = ((CustomWordValueIf) customWord.getValue()).getUserParam();

        RuleExpression conditionExpression = customModuleUserParamIf.getCondition();
        RuleExpression propValueExpression = customModuleUserParamIf.getPropValue();

        String condition = expressionParser.parse(conditionExpression, shopBean, user, extParameter);
        String propValue = expressionParser.parse(propValueExpression, shopBean, user, extParameter);

        Boolean conditionResult = Boolean.valueOf(condition);
        if (conditionResult == null) {
            return null;
        } else if (conditionResult) {
            return propValue;
        } else
            return null;
    }
}
