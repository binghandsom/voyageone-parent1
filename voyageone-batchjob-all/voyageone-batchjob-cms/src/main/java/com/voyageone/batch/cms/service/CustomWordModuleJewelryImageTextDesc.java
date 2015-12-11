package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.bean.CustomValueSystemParam;
import com.voyageone.batch.cms.model.CmsCodePropBean;
import com.voyageone.batch.cms.service.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamJewelryImageTextDesc;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueJewelryImageTextDesc;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleJewelryImageTextDesc extends CustomWordModule {
    public final static String moduleName = "JewelryImageTextDesc";

    public CustomWordModuleJewelryImageTextDesc() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) {
        //user param
        CustomModuleUserParamJewelryImageTextDesc customModuleUserParamJewelryImageTextDesc= ((CustomWordValueJewelryImageTextDesc) customWord.getValue()).getUserParam();

        CmsCodePropBean mainProductProp = systemParam.getMainProductProp();

        /*
        //http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%5F20150819%5Fx380%5F251x?$380%2D251$&$text05=studs&$text04=women&$text03=none&$text02=red&$text01=sterling
        String imageTextTemplate = customModuleUserParamJewelryImageTextDesc.getImageTextTemplate();
        String handMadeImageTemplate = customModuleUserParamJewelryImageTextDesc.getHandMadeImageTemplate();
        String tipImageUrl = customModuleUserParamJewelryImageTextDesc.getTipImageUrl();
        String htmlTemplate = customModuleUserParamJewelryImageTextDesc.getHtmlTemplate();

        String productMaterial = "H2O";
        String productColor = "transparent";
        String productStone = "stone";
        String suitableCrowd = "anyone";
        String suitableStyle = "socool";
        String imageTextUrl = String.format(imageTextTemplate, productMaterial,
                productColor, productStone, suitableCrowd, suitableStyle);
        imageSet.add(imageTextUrl);

        String handMadeUrlKeysOrigin = mainProductProp.getProp(CmsFieldEnum.CmsCodeEnum.handmade_image);
        String[] handMadeUrlKeys = null;
        if (handMadeUrlKeysOrigin != null && handMadeUrlKeysOrigin.length()!= 0) {
            handMadeUrlKeysOrigin.split(",");
        }

        String handMadeImage1 = null, handMadeImage2 = null;
        //临时
        String defaultHandImageUrlKey = "03156aa10_main";

        if (handMadeUrlKeys == null || handMadeUrlKeys.length == 0) {
            if (defaultHandImageUrlKey != null && !"".equals(defaultHandImageUrlKey)) {
                handMadeImage1 = handMadeImage2 = handMadeImageTemplate + defaultHandImageUrlKey;
                imageSet.add(handMadeImage1);
            }
        } else if (handMadeUrlKeys.length == 1) {
            handMadeImage1 = handMadeImage2 = handMadeImageTemplate + handMadeUrlKeys[0];
            imageSet.add(handMadeImage1);
        } else {
            handMadeImage1 = handMadeImageTemplate + handMadeUrlKeys[0];
            handMadeImage2 = handMadeImageTemplate + handMadeUrlKeys[1];
            imageSet.add(handMadeImage1);
            imageSet.add(handMadeImage2);
        }

        String parseResult = wrapImageTextToHtml(htmlTemplate, imageTextUrl, handMadeImage1, handMadeImage2, tipImageUrl);

        System.out.println("**********************");
        System.out.println(parseResult);
        System.out.println("**********************");
        return parseResult;
        */
        return null;
    }

    private String wrapImageTextToHtml(String htmlTemplate, String imageTextUrl, String handMadeImage1, String handMadeImage2, String tipImageUrl) {
        String htmlDoc = String.format(htmlTemplate, imageTextUrl, handMadeImage1, handMadeImage2, tipImageUrl);
        return htmlDoc;
    }
}
