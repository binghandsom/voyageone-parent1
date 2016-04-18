package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageWithParam;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageWithParam;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleImageWithParam extends CustomWordModule {

    public final static String moduleName = "ImageWithParam";

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
            if (imageParam == null) {
                continue;
            }
            try {
                imageParam = URLEncoder.encode(imageParam, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.fillInStackTrace();
                logger.error(e.getMessage(), e);
            }
            imageParams.add(imageParam);
        }

        int deleteItemsCount =0;
        for(Iterator<String> iterator=imageParams.iterator();iterator.hasNext();){
            String param = iterator.next();
            if(param == null || "".equals(param.trim())){

                iterator.remove();

                deleteItemsCount++;
            }

        }

        for (int i=0;i<deleteItemsCount;i++){
            imageParams.add("");
        }

        String parseResult = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, imageParams.toArray()));
        if (imageSet != null) {
            imageSet.add(parseResult);
        }

        return parseResult;
    }
}
