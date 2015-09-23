package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.CustomValueSystemParam;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageWithParam;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageWithParam;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleImageWithParam extends CustomWordModule {
    public final static String moduleName = "ImageWithParam";

    private Log logger = LogFactory.getLog(CustomWordModule.class);

    public CustomWordModuleImageWithParam() {
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
        CustomModuleUserParamImageWithParam customModuleUserParamImageWithParam= ((CustomWordValueImageWithParam) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamImageWithParam.getImageTemplate();
        List<RuleExpression> imageParamExpressions = customModuleUserParamImageWithParam.getImageParams();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, null);
        List<String> imageParams = new ArrayList<>();
        for (RuleExpression imageParamExpression : imageParamExpressions) {
            String imageParam = expressionParser.parse(imageParamExpression, null);
            try {
                imageParam = URLEncoder.encode(imageParam, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.fillInStackTrace();
                logger.error(e.fillInStackTrace());
            }
            imageParams.add(imageParam);
        }

        String parseResult = String.format(imageTemplate, imageParams.toArray());
        if (imageSet != null) {
            imageSet.add(parseResult);
        }

        return parseResult;
    }
}
