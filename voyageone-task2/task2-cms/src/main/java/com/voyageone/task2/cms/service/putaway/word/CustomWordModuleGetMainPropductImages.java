package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetMainPrductImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetMainProductImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Autowired
//    private ImageCreateService imageCreateService;
    @Autowired
    private SxProductService sxProductService;

    public CustomWordModuleGetMainPropductImages() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
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

        // modified by morse.lu 2016/06/02 start
//        List<CmsBtProductModel_Field_Image> productImages = mainSxProduct.getCmsBtProductModel().getFields().getImages(imageType);
        // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
        List<CmsBtProductModel_Field_Image> productImages = sxProductService.getProductImages(mainSxProduct.getCmsBtProductModel(), imageType);
        // modified by morse.lu 2016/06/02 end

        String parseResult = "";

        List<String> imageUrlList = new ArrayList<>();
        //获取所有图片
        if (imageIndex == -1) {
            if (imageTemplate != null) {
                for (CmsBtProductModel_Field_Image productImage : productImages) {
                    // 20160513 tom 图片服务器切换 START
                    String completeImageUrl = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, productImage.getName()));

//                    String completeImageUrl = getImageByTemplateId(expressionParser, imageTemplate, productImage.getName(), systemParam);
                    // 20160513 tom 图片服务器切换 END
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
                // 20160513 tom 图片服务器切换 START
                paddingImage = String.format(imageTemplate, paddingImageKey.trim());

//                paddingImage = getImageByTemplateId(expressionParser, imageTemplate, paddingImageKey.trim(), systemParam);
                // 20160513 tom 图片服务器切换 END
                paddingImage = UploadImageHandler.encodeImageUrl(paddingImage);
                imageUrlList.add(String.format(imageTemplate, paddingImage));

            }else {
                return paddingImageKey.trim();
            }
        } else {
            CmsBtProductModel_Field_Image productImage = productImages.get(imageIndex);
            if(imageTemplate != null){
                // 20160513 tom 图片服务器切换 START
                String completeImageUrl = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, productImage.getName()));

//                String completeImageUrl = getImageByTemplateId(expressionParser, imageTemplate, productImage.getName(), systemParam);
                // 20160513 tom 图片服务器切换 END
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

    // 20160513 tom 图片服务器切换 START
//    private String getImageByTemplateId(ExpressionParser expressionParser, String imageTemplate, String imageName, CustomValueSystemParam systemParam) throws TaskSignal {
//
//        ImageCreateGetRequest request = new ImageCreateGetRequest();
//        request.setChannelId(expressionParser.getMasterWordCmsBtProduct().getChannelId());
//        request.setTemplateId(Integer.parseInt(imageTemplate));
//        request.setFile(imageTemplate + "_" + imageName); // 模板id + "_" + 第一个参数(一般是图片名)
//        String[] vPara = {imageName};
//        request.setVParam(vPara);
//        ImageCreateGetResponse response = null;
//        try {
//            response = imageCreateService.getImage(request);
//            return imageCreateService.getOssHttpURL(response.getResultData().getFilePath());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(
//                            "channelId:" + systemParam.getOrderChannelId() +
//                            ". cartId:" + systemParam.getCartId() +
//                            ". groupId:" + systemParam.getMainSxProduct().getCmsBtProductModelGroupPlatform().getGroupId() +
//                            ". 图片取得失败! 模板id:" + imageTemplate + ", 图片名:" + imageName));
//        }
//    }
    // 20160513 tom 图片服务器切换 END

}
