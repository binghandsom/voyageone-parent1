package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamConditionNLike;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueConditionNLike;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleConditionNLike extends CustomWordModule {
    public final static String moduleName = CustomWordValueConditionNLike.moduleName;

    public CustomWordModuleConditionNLike() {
        super(moduleName);
    }

    public CustomWordModuleConditionNLike(String moduleName) {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user)    {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamConditionNLike customModuleUserParamConditionNLike = ((CustomWordValueConditionNLike) customWord.getValue()).getUserParam();

        RuleExpression firstParamExpression = customModuleUserParamConditionNLike.getFirstParam();
        RuleExpression secondParamExpression = customModuleUserParamConditionNLike.getSecondParam();

        String firsetParam = expressionParser.parse(firstParamExpression, shopBean, user, extParameter);
        String secondParam = expressionParser.parse(secondParamExpression, shopBean, user, extParameter);

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
            return String.valueOf(false);
        } else {
            return String.valueOf(true);
        }

    }
}
