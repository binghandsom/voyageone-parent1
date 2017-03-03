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
import java.util.Map;

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

        RuleExpression useOriUrlExpression = customModuleUserParamGetMainPrductImages.getUseOriUrl();
        // 使用原图flg(1:使用原图)
        String useOriUrlStr = expressionParser.parse(useOriUrlExpression, shopBean, user, extParameter);

        //system param
        CmsBtProductModel mainProduct = sxData.getMainProduct();

        // 判断想要获取哪个product的图片
        // 如果没有指定特别的extParameter, 那么就认为是主商品的图片
        // modified by morse.lu 2016/06/02 start
//        List<CmsBtProductModel_Field_Image> productImages = mainProduct.getFields().getImages(imageType);
        // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
        List<CmsBtProductModel_Field_Image> productImages = sxProductService.getProductImages(mainProduct, imageType, sxData.getCartId());
        // modified by morse.lu 2016/06/02 end
        if (extParameter != null && extParameter.length > 0) {
            // 获取指定product的图片(如果没找到, 那么就使用主商品的图片)
            for (CmsBtProductModel product : sxData.getProductList()) {
                // modified by morse.lu 2016/06/27 start
                // 表结构变化，改从common下的fields里去取
//                if (product.getFields().getCode().equals(extParameter[0])) {
                if (product.getCommon().getFields().getCode().equals(extParameter[0])) {
                    // modified by morse.lu 2016/06/27 end
                    // modified by morse.lu 2016/06/02 start
//                    productImages = product.getFields().getImages(imageType);
                    // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
                    productImages = sxProductService.getProductImages(product, imageType, sxData.getCartId());
                    // modified by morse.lu 2016/06/02 end
                    break;
                }
            }
        }

        String parseResult = "";

        List<String> imageUrlList = new ArrayList<>();
        //获取所有图片
        if (imageIndex == -1) {
            // add by desmond 2016/07/15 start
            String completeImageUrl = "";
            // 看是否使用原图
            if (!StringUtils.isEmpty(useOriUrlStr) && "1".equals(useOriUrlStr)) {
                for (CmsBtProductModel_Field_Image productImage : productImages) {
                    // 使用原图(不使用image模板，不用判断是否有image模板)
                    completeImageUrl = getPicOriUrl(productImage.getName(), moduleName);
                    imageUrlList.add(completeImageUrl);
                }
                // add by desmond 2016/07/15 end
            } else {
                // 不使用原图时
                if (imageTemplate != null) {
                    for (CmsBtProductModel_Field_Image productImage : productImages) {
                        // 20160513 tom 图片服务器切换 START
                        completeImageUrl = String.format(imageTemplate, productImage.getName());

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
            }
        } //padding图片
        else if (imageIndex >= productImages.size() || StringUtils.isEmpty(productImages.get(imageIndex).getName())) {
            RuleExpression paddingExpression = customModuleUserParamGetMainPrductImages.getPaddingExpression();
            String paddingImageKey = expressionParser.parse(paddingExpression, shopBean, user, extParameter);
            if (paddingImageKey == null || "".equalsIgnoreCase(paddingImageKey)) {
                // 20160618 tom 如果padding没有内容, 不返回空, 因为返回空的话, 整个wordList都为空了 START
//                return null;
                return "";
                // 20160618 tom 如果padding没有内容, 不返回空, 因为返回空的话, 整个wordList都为空了 END
            }
            String paddingImage;
            // add by desmond 2016/07/15 start
            // 看是否使用原图
            if (!StringUtils.isEmpty(useOriUrlStr) && "1".equals(useOriUrlStr)) {
                // 使用原图(不使用image模板，不用判断是否有image模板)
                paddingImage = getPicOriUrl(paddingImageKey.trim(), moduleName);
                imageUrlList.add(paddingImage);
                // add by desmond 2016/07/15 end
            } else {
                // modified by morse.lu 2016/07/18 start
                // 吊牌图和耐久性标签padding直接写死空白图片的url
                RuleExpression paddingIsUrlExpression = customModuleUserParamGetMainPrductImages.getPaddingIsUrl();
                String paddingIsUrl = expressionParser.parse(paddingIsUrlExpression, shopBean, user, null);
//            if(imageTemplate != null){
                if (!Boolean.parseBoolean(paddingIsUrl) && imageTemplate != null) {
                    // modified by morse.lu 2016/07/18 end
                    // 20160513 tom 图片服务器切换 START
                    paddingImage = String.format(imageTemplate, paddingImageKey.trim());

//                paddingImage = expressionParser.getSxProductService().getImageByTemplateId(sxData.getChannelId(), imageTemplate, paddingImageKey.trim());
                    // 20160513 tom 图片服务器切换 END
//                paddingImage = sxProductService.encodeImageUrl(paddingImage);
                    // modified 2016/07/15 start
                    // 这里是不是写错了? 疑似应该是add paddingImage tom   // morse：好像是错了，task2下面也要改
//                imageUrlList.add(String.format(imageTemplate, paddingImage));
                    imageUrlList.add(paddingImage);
                    // modified 016/07/15 end
                } else {
                    return paddingImageKey.trim();
                }
            }
        } else {
            CmsBtProductModel_Field_Image productImage = productImages.get(imageIndex);
            // add by desmond 2016/07/15 start
            String completeImageUrl = "";
            // 看是否使用原图
            if (!StringUtils.isEmpty(useOriUrlStr) && "1".equals(useOriUrlStr)) {
                // 使用原图(不使用image模板，不用判断是否有image模板)
                completeImageUrl = getPicOriUrl(productImage.getName(), moduleName);
                imageUrlList.add(completeImageUrl);
                // add by desmond 2016/07/15 end
            } else {
                if (imageTemplate != null) {
                    // 20160513 tom 图片服务器切换 START
                    completeImageUrl = String.format(imageTemplate, productImage.getName());

//                String completeImageUrl = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, productImage.getName());
                    // 20160513 tom 图片服务器切换 END
//                completeImageUrl = sxProductService.encodeImageUrl(completeImageUrl);
                    imageUrlList.add(completeImageUrl);
                } else {
                    return productImage.getName();
                }
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

        Map<String, String> map = null;
        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
            map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, new HashSet<>(imageUrlList), user);
        } else if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId())) {
            map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, new HashSet<>(imageUrlList), user);
        }
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!StringUtils.isEmpty(entry.getValue())) {
                    parseResult = parseResult.replace(entry.getKey(), entry.getValue());
                } else { // add by desmond 2016/07/13 start
                    // 如果未能取得平台url(源图片去的失败或者上传到平台失败)时，删除原图片url
                    if (htmlTemplate != null) {
                        parseResult = parseResult.replace(String.format(htmlTemplate, entry.getKey()), "");
                    } else {
                        parseResult = parseResult.replace(entry.getKey(), "");
                    }
                }
                // add by desmond 2016/07/13 end
            }
        }

        return parseResult;
    }

}
