package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.CustomValueSystemParam;
import com.voyageone.batch.ims.modelbean.CmsCodePropBean;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;
import com.voyageone.ims.enums.CmsFieldEnum;
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
        String imageType = expressionParser.parse(imageTypeExpression, null);

        //system param
        String code = systemParam.getMainProductProp().getProp(CmsFieldEnum.CmsCodeEnum.code);
        CmsModelPropBean cmsModelProp = systemParam.getCmsModelProp();

        CmsCodePropBean cmsCodeProp = null;

        for (CmsCodePropBean iterCmsCodeProp: cmsModelProp.getCmsCodePropBeanList())
        {
            String iterCode = iterCmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.code);
            if (iterCode.equals(code))
            {
                cmsCodeProp = iterCmsCodeProp;
                break;
            }
        }

        if (cmsCodeProp == null)
            return null;

        String propImageProp = cmsCodeProp.getProp((CmsFieldEnum.CmsCodeEnum) CmsFieldEnum.valueOf(imageType));

        String[] propImages;
        if (propImageProp == null || "".equals(propImageProp.trim()))
        {
            propImages = new String[] {};
        } else {
            propImages = propImageProp.split(",");
        }


        String parseResult = "";

        List<String> imageUrlList = new ArrayList<>();
        //获取所有图片
        if (imageIndex == -1) {
            if (imageTemplate != null) {
                for (String propImage : propImages) {
                    String completeImageUrl = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, propImage.trim()));
                    imageUrlList.add(completeImageUrl);
                }
            } else {
                for (String propImage : propImages) {
                    parseResult += propImage;
                }
                return parseResult;
            }
        } //padding图片
        else if (imageIndex > propImages.length) {
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
            String propImage = propImages[imageIndex - 1];
            if(imageTemplate != null){
                String completeImageUrl = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, propImage.trim()));
                imageUrlList.add(completeImageUrl);
            }else {
                return propImage.trim();
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
