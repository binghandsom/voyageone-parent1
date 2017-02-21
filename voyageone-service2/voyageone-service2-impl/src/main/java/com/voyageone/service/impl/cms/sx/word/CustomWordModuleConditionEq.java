package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamConditionEq;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueConditionEq;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleConditionEq extends CustomWordModule {
    public final static String moduleName = CustomWordValueConditionEq.moduleName;

    public CustomWordModuleConditionEq () {
        super(moduleName);
    }

    public CustomWordModuleConditionEq(String moduleName) {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user)    {
//        return parse(customWord, expressionParser, sxData, shopBean,user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamConditionEq customModuleUserParamConditionEq = ((CustomWordValueConditionEq) customWord.getValue()).getUserParam();

        RuleExpression firstParamExpression = customModuleUserParamConditionEq.getFirstParam();
        RuleExpression secondParamExpression = customModuleUserParamConditionEq.getSecondParam();
        RuleExpression ignoreCaseFlgExpression = customModuleUserParamConditionEq.getIgnoreCaseFlg();

        String firsetParam = expressionParser.parse(firstParamExpression, shopBean, user, extParameter);
        String secondParam = expressionParser.parse(secondParamExpression, shopBean, user, extParameter);
        String ignoreCaseFlg = expressionParser.parse(ignoreCaseFlgExpression, shopBean, user, extParameter);
        boolean blnIgnoreCaseFlg = false;
        if (!StringUtils.isEmpty(ignoreCaseFlg) && ignoreCaseFlg.equals("1")) {
            blnIgnoreCaseFlg = true;
        }

        if (firsetParam == null && secondParam == null) {
            return String.valueOf(true);
        } else if (firsetParam == null || secondParam == null) {
            return String.valueOf(false);
        } else {
            if (blnIgnoreCaseFlg) {
                if (firsetParam.trim().toLowerCase().equals(secondParam.trim().toLowerCase())) {
                    return String.valueOf(true);
                }
            } else {
                if (firsetParam.trim().equals(secondParam.trim())) {
                    return String.valueOf(true);
                }
            }

            return String.valueOf(false);
        }
    }
}
