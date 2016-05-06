package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamConditionLike;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueConditionLike;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import org.springframework.stereotype.Repository;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleConditionLike extends CustomWordModule {
    public final static String moduleName = CustomWordValueConditionLike.moduleName;

    public CustomWordModuleConditionLike() {
        super(moduleName);
    }

    public CustomWordModuleConditionLike(String moduleName) {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user)    {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamConditionLike customModuleUserParamConditionLike = ((CustomWordValueConditionLike) customWord.getValue()).getUserParam();

        RuleExpression firstParamExpression = customModuleUserParamConditionLike.getFirstParam();
        RuleExpression secondParamExpression = customModuleUserParamConditionLike.getSecondParam();

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
            return String.valueOf(true);
        } else {
            return String.valueOf(false);
        }

    }
}
