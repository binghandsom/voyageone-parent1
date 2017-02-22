package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamConditionNeq;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueConditionNeq;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleConditionNeq extends CustomWordModule {
    public final static String moduleName = CustomWordValueConditionNeq.moduleName;

    public CustomWordModuleConditionNeq () {
        super(moduleName);
    }

    public CustomWordModuleConditionNeq(String moduleName) {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user)    {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamConditionNeq customModuleUserParamConditionNeq = ((CustomWordValueConditionNeq) customWord.getValue()).getUserParam();

        RuleExpression firstParamExpression = customModuleUserParamConditionNeq.getFirstParam();
        RuleExpression secondParamExpression = customModuleUserParamConditionNeq.getSecondParam();
        RuleExpression ignoreCaseFlgExpression = customModuleUserParamConditionNeq.getIgnoreCaseFlg();

        String firsetParam = expressionParser.parse(firstParamExpression, shopBean, user, extParameter);
        String secondParam = expressionParser.parse(secondParamExpression, shopBean, user, extParameter);
        String ignoreCaseFlg = expressionParser.parse(ignoreCaseFlgExpression, shopBean, user, extParameter);
        boolean blnIgnoreCaseFlg = false;
        if (!StringUtils.isEmpty(ignoreCaseFlg) && ignoreCaseFlg.equals("1")) {
            blnIgnoreCaseFlg = true;
        }

        if (firsetParam == null && secondParam == null) {
            return String.valueOf(false);
        } else if (firsetParam == null || secondParam == null) {
            return String.valueOf(true);
        } else {
            if (blnIgnoreCaseFlg) {
                if (firsetParam.trim().toLowerCase().equals(secondParam.trim().toLowerCase())) {
                    return String.valueOf(false);
                }
            } else {
                if (firsetParam.trim().equals(secondParam.trim())) {
                    return String.valueOf(false);
                }
            }
            return String.valueOf(true);
        }
    }
}
