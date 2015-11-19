package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.CustomValueSystemParam;
import com.voyageone.batch.ims.modelbean.CmsCodePropBean;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetAllImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetAllImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleGetAllImages extends CustomWordModule {
    public final static String moduleName = "GetAllImages";

    public CustomWordModuleGetAllImages() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) {
        //user param
        CustomModuleUserParamGetAllImages customModuleUserParamGetAllImages = ((CustomWordValueGetAllImages) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamGetAllImages.getImageTemplate();
        RuleExpression htmlTemplateExpression = customModuleUserParamGetAllImages.getHtmlTemplate();
        RuleExpression imageTypeExpression = customModuleUserParamGetAllImages.getImageType();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, null);
        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, null);
        String imageType = expressionParser.parse(imageTypeExpression, null);

        //system param
        CmsModelPropBean cmsModelProp = systemParam.getCmsModelProp();

        String parseResult = "";

        for (CmsCodePropBean iterCmsCodeProp : cmsModelProp.getCmsCodePropBeanList()) {
            String productImageOrigin = iterCmsCodeProp.getProp((CmsFieldEnum.CmsCodeEnum) CmsFieldEnum.valueOf(imageType));
            String[] productImages = productImageOrigin.split(",");

            for (String productImage : productImages) {
                String completeImageUrl = String.format(imageTemplate, productImage);
                completeImageUrl = UploadImageHandler.encodeImageUrl(completeImageUrl);
                if (htmlTemplate != null) {
                    parseResult += String.format(htmlTemplate, completeImageUrl);
                }
                else {
                    parseResult += completeImageUrl;
                }
                if (imageSet != null) {
                    imageSet.add(completeImageUrl);
                }
            }

        }
        return parseResult;
    }
}
