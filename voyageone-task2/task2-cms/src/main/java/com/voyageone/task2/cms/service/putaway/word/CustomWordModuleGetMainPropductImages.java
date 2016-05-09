package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetMainPrductImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetMainProductImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleGetMainPropductImages extends CustomWordModule {
    public final static String moduleName = "GetMainProductImages";

    public CustomWordModuleGetMainPropductImages() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) {
        //user param
        CustomModuleUserParamGetMainPrductImages customModuleUserParamGetMainPrductImages = ((CustomWordValueGetMainProductImages) customWord.getValue()).getUserParam();

        int imageIndex = -1;
        RuleExpression imageIndexExpression = customModuleUserParamGetMainPrductImages.getImageIndex();
        if (imageIndexExpression != null) {
            String imageIndexStr = expressionParser.parse(imageIndexExpression, null);
            imageIndex = Integer.parseInt(imageIndexStr);
        }

        RuleExpression htmlTemplateExpression = customModuleUserParamGetMainPrductImages.getHtmlTemplate();
        String htmlTemplate = null;

        if (htmlTemplateExpression != null)
            htmlTemplate = expressionParser.parse(htmlTemplateExpression, null);

        RuleExpression imageTemplateExpression = customModuleUserParamGetMainPrductImages.getImageTemplate();

        String imageTemplate = null;

        if(imageTemplateExpression!=null)
            imageTemplate = expressionParser.parse(imageTemplateExpression, null);

        RuleExpression imageTypeExpression = customModuleUserParamGetMainPrductImages.getImageType();


        String imageTypeStr = expressionParser.parse(imageTypeExpression, null);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);

        //system param
        SxProductBean mainSxProduct = systemParam.getMainSxProduct();

        List<CmsBtProductModel_Field_Image> productImages = mainSxProduct.getCmsBtProductModel().getFields().getImages(imageType);

        String parseResult = "";

        List<String> imageUrlList = new ArrayList<>();
        //获取所有图片
        if (imageIndex == -1) {
            if (imageTemplate != null) {
                for (CmsBtProductModel_Field_Image productImage : productImages) {
                    String completeImageUrl = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, productImage.getName()));
                    imageUrlList.add(completeImageUrl);
                }
            } else {
                for (CmsBtProductModel_Field_Image productImage : productImages) {
                    parseResult += productImage.getName();
                }
                return parseResult;
            }
        } //padding图片
        else if (imageIndex >= productImages.size() || StringUtils.isEmpty(productImages.get(imageIndex).getName())) {
            RuleExpression paddingExpression = customModuleUserParamGetMainPrductImages.getPaddingExpression();
            String paddingImageKey = expressionParser.parse(paddingExpression, null);
            if (paddingImageKey == null || "".equalsIgnoreCase(paddingImageKey)) {
                return null;
            }
            String paddingImage;
            if(imageTemplate != null){
                paddingImage = String.format(imageTemplate, paddingImageKey.trim());
                paddingImage = UploadImageHandler.encodeImageUrl(paddingImage);
                imageUrlList.add(String.format(imageTemplate, paddingImage));

            }else {
                return paddingImageKey.trim();
            }
        } else {
            CmsBtProductModel_Field_Image productImage = productImages.get(imageIndex);
            if(imageTemplate != null){
                String completeImageUrl = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, productImage.getName()));
                imageUrlList.add(completeImageUrl);
            }else {
                return productImage.getName();
            }
        }
        for (String imageUrl : imageUrlList) {
            if (htmlTemplate != null) {
                parseResult += String.format(htmlTemplate, imageUrl);
            } else {
                parseResult += imageUrl;
            }

            if (imageSet != null) {
                imageSet.add(UploadImageHandler.encodeImageUrl(imageUrl));
            }
        }
        return parseResult;
    }
}
