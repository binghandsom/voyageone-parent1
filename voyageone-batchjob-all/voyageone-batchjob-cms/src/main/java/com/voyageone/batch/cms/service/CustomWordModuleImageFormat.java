package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.bean.CustomValueSystemParam;
import com.voyageone.batch.cms.service.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageFormat;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageFormat;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleImageFormat extends CustomWordModule {
    public final static String moduleName = "ImageFormat";

    public CustomWordModuleImageFormat() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) {
        return parse(customWord, expressionParser, systemParam, null);
    }

    //public Str
    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) {
        //user param
        CustomModuleUserParamImageFormat customModuleUserParamImageFormat= ((CustomWordValueImageFormat) customWord.getValue()).getUserParam();

        RuleExpression htmlTemplateExpression = customModuleUserParamImageFormat.getHtmlTemplate();
        RuleExpression imageUrlExpression = customModuleUserParamImageFormat.getImageUrl();

        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, null);
        String imageUrl = expressionParser.parse(imageUrlExpression, null);

       String parseResult = String.format(htmlTemplate, imageUrl);

       return parseResult;
    }
}
