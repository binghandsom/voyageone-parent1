package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetAllImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetAllImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleGetAllImages extends CustomWordModule {

    @Autowired
    private SxProductService sxProductService;

    public final static String moduleName = "GetAllImages";

    public CustomWordModuleGetAllImages() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamGetAllImages customModuleUserParamGetAllImages = ((CustomWordValueGetAllImages) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamGetAllImages.getImageTemplate();
        RuleExpression htmlTemplateExpression = customModuleUserParamGetAllImages.getHtmlTemplate();
        RuleExpression imageTypeExpression = customModuleUserParamGetAllImages.getImageType();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, shopBean, user, extParameter);
        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, shopBean, user, extParameter);
        String imageTypeStr = expressionParser.parse(imageTypeExpression, shopBean, user, extParameter);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);

        //system param
        List<CmsBtProductModel> sxProducts = sxData.getProductList();

        String parseResult = "";
        Set<String> imageSet = new HashSet<>();

        for (CmsBtProductModel sxProduct : sxProducts) {
            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProduct.getFields().getImages(imageType);

            for (CmsBtProductModel_Field_Image cmsBtProductModelFieldImage : cmsBtProductModelFieldImages) {
                String completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());
//                completeImageUrl = sxProductService.encodeImageUrl(completeImageUrl);
                if (htmlTemplate != null) {
                    parseResult += String.format(htmlTemplate, completeImageUrl);
                }
                else {
                    parseResult += completeImageUrl;
                }
                imageSet.add(completeImageUrl);
            }
        }

        if (imageSet.size() > 0 && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM)) {
            sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
        }

        return parseResult;
    }
}
