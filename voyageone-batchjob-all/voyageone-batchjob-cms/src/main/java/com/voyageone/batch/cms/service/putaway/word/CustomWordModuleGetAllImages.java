package com.voyageone.batch.cms.service.putaway.word;

import com.voyageone.batch.cms.bean.CustomValueSystemParam;
import com.voyageone.batch.cms.bean.SxProductBean;
import com.voyageone.batch.cms.service.putaway.UploadImageHandler;
import com.voyageone.batch.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.cms.service.model.CmsBtProductConstants;
import com.voyageone.cms.service.model.CmsBtProductModel_Field_Image;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetAllImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetAllImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        String imageTypeStr = expressionParser.parse(imageTypeExpression, null);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);

        //system param
        List<SxProductBean> sxProductBeans = systemParam.getSxProductBeans();

        String parseResult = "";

        for (SxProductBean sxProductBean : sxProductBeans) {
            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProductBean.getCmsBtProductModel().getFields().getImages(imageType);

            for (CmsBtProductModel_Field_Image cmsBtProductModelFieldImage : cmsBtProductModelFieldImages) {
                String completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());
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
