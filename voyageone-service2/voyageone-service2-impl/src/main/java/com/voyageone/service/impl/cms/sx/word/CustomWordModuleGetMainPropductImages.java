package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleGetMainPropductImages extends CustomWordModule {

    public final static String moduleName = "GetMainProductImages";

    public CustomWordModuleGetMainPropductImages() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
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
        // modified by morse.lu 2016/06/02 start
//        List<CmsBtProductModel_Field_Image> productImages = mainProduct.getFields().getImages(imageType);
        // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
        List<CmsBtProductModel_Field_Image> productImages = sxProductService.getProductImages(mainProduct, imageType);
        // modified by morse.lu 2016/06/02 end
        if (extParameter != null && extParameter.length > 0) {
            // 获取指定product的图片(如果没找到, 那么就使用主商品的图片)
            for (CmsBtProductModel product : sxData.getProductList()) {
                if (product.getFields().getCode().equals(extParameter[0])) {
                    // modified by morse.lu 2016/06/02 start
//                    productImages = product.getFields().getImages(imageType);
                    // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
                    productImages = sxProductService.getProductImages(product, imageType);
                    // modified by morse.lu 2016/06/02 end
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
                    // 20160513 tom 图片服务器切换 START
                    String completeImageUrl = String.format(imageTemplate, productImage.getName());

//                    String completeImageUrl = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, productImage.getName());
                    // 20160513 tom 图片服务器切换 END
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
        else if (imageIndex >= productImages.size() || StringUtils.isEmpty(productImages.get(imageIndex).getName())) {
            RuleExpression paddingExpression = customModuleUserParamGetMainPrductImages.getPaddingExpression();
            String paddingImageKey = expressionParser.parse(paddingExpression, shopBean, user, extParameter);
            if (paddingImageKey == null || "".equalsIgnoreCase(paddingImageKey)) {
                return null;
            }
            String paddingImage;
            if(imageTemplate != null){
                // 20160513 tom 图片服务器切换 START
                paddingImage = String.format(imageTemplate, paddingImageKey.trim());

//                paddingImage = expressionParser.getSxProductService().getImageByTemplateId(sxData.getChannelId(), imageTemplate, paddingImageKey.trim());
                // 20160513 tom 图片服务器切换 END
//                paddingImage = sxProductService.encodeImageUrl(paddingImage);
                imageUrlList.add(String.format(imageTemplate, paddingImage)); // TODO: 这里是不是写错了? 疑似应该是add paddingImage tom   // morse：好像是错了，task2下面也要改

            }else {
                return paddingImageKey.trim();
            }
        } else {
            CmsBtProductModel_Field_Image productImage = productImages.get(imageIndex);
            if(imageTemplate != null){
                // 20160513 tom 图片服务器切换 START
                String completeImageUrl = String.format(imageTemplate, productImage.getName());

//                String completeImageUrl = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, productImage.getName());
                // 20160513 tom 图片服务器切换 END
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
