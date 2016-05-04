package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetMainPrductImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetMainProductImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleGetMainPropductImages extends CustomWordModule {

    @Autowired
    private SxProductService sxProductService;

    public final static String moduleName = "GetMainProductImages";

    public CustomWordModuleGetMainPropductImages() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamGetMainPrductImages customModuleUserParamGetMainPrductImages = ((CustomWordValueGetMainProductImages) customWord.getValue()).getUserParam();

        int imageIndex = -1;
        RuleExpression imageIndexExpression = customModuleUserParamGetMainPrductImages.getImageIndex();
        if (imageIndexExpression != null) {
            String imageIndexStr = expressionParser.parse(imageIndexExpression, shopBean, user, extParameter);
            imageIndex = Integer.parseInt(imageIndexStr);
        }

        RuleExpression htmlTemplateExpression = customModuleUserParamGetMainPrductImages.getHtmlTemplate();
        String htmlTemplate = null;

        if (htmlTemplateExpression != null)
            htmlTemplate = expressionParser.parse(htmlTemplateExpression, shopBean, user, extParameter);

        RuleExpression imageTemplateExpression = customModuleUserParamGetMainPrductImages.getImageTemplate();

        String imageTemplate = null;

        if(imageTemplateExpression!=null)
            imageTemplate = expressionParser.parse(imageTemplateExpression, shopBean, user, extParameter);

        RuleExpression imageTypeExpression = customModuleUserParamGetMainPrductImages.getImageType();


        String imageTypeStr = expressionParser.parse(imageTypeExpression, shopBean, user, extParameter);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);

        //system param
        CmsBtProductModel mainProduct = sxData.getMainProduct();

        // 判断想要获取哪个product的图片
        // 如果没有指定特别的extParameter, 那么就认为是主商品的图片
        List<CmsBtProductModel_Field_Image> productImages = mainProduct.getFields().getImages(imageType);
        if (extParameter != null && extParameter.length > 0) {
            // 获取指定product的图片(如果没找到, 那么就使用主商品的图片)
            for (CmsBtProductModel product : sxData.getProductList()) {
                if (product.getFields().getCode().equals(extParameter[0])) {
                    productImages = product.getFields().getImages(imageType);
                    break;
                }
            }
        }

        String parseResult = "";

        List<String> imageUrlList = new ArrayList<>();
        //获取所有图片
        if (imageIndex == -1) {
            if (imageTemplate != null) {
                for (CmsBtProductModel_Field_Image productImage : productImages) {
                    String completeImageUrl = String.format(imageTemplate, productImage.getName());
//                    completeImageUrl = sxProductService.encodeImageUrl(completeImageUrl);
                    imageUrlList.add(completeImageUrl);
                }
            } else {
                for (CmsBtProductModel_Field_Image productImage : productImages) {
                    parseResult += productImage.getName();
                }
                return parseResult;
            }
        } //padding图片
        else if (imageIndex >= productImages.size()) {
            RuleExpression paddingExpression = customModuleUserParamGetMainPrductImages.getPaddingExpression();
            String paddingImageKey = expressionParser.parse(paddingExpression, shopBean, user, extParameter);
            if (paddingImageKey == null || "".equalsIgnoreCase(paddingImageKey)) {
                return null;
            }
            String paddingImage;
            if(imageTemplate != null){
                paddingImage = String.format(imageTemplate, paddingImageKey.trim());
//                paddingImage = sxProductService.encodeImageUrl(paddingImage);
                imageUrlList.add(String.format(imageTemplate, paddingImage));

            }else {
                return paddingImageKey.trim();
            }
        } else {
            CmsBtProductModel_Field_Image productImage = productImages.get(imageIndex);
            if(imageTemplate != null){
                String completeImageUrl = String.format(imageTemplate, productImage.getName());
//                completeImageUrl = sxProductService.encodeImageUrl(completeImageUrl);
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

//            if (imageSet != null) {
//                imageSet.add(UploadImageHandler.encodeImageUrl(imageUrl));
//            }
        }

        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
            sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, new HashSet<>(imageUrlList), user);
        }

        return parseResult;
    }
}
