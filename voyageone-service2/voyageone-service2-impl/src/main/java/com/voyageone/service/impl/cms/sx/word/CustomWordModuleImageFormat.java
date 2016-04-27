package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageFormat;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageFormat;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import org.springframework.stereotype.Repository;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleImageFormat extends CustomWordModule {
    public final static String moduleName = "ImageFormat";

    public CustomWordModuleImageFormat() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) throws Exception {
        //user param
        CustomModuleUserParamImageFormat customModuleUserParamImageFormat= ((CustomWordValueImageFormat) customWord.getValue()).getUserParam();

        RuleExpression htmlTemplateExpression = customModuleUserParamImageFormat.getHtmlTemplate();
        RuleExpression imageUrlExpression = customModuleUserParamImageFormat.getImageUrl();

        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, shopBean, user);
        String imageUrl = expressionParser.parse(imageUrlExpression, shopBean, user);

       String parseResult = String.format(htmlTemplate, imageUrl);

       return parseResult;
    }
}
