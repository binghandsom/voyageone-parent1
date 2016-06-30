package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetCommonImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetCommonImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * cms_bt_image_group(mongoDB)取得url
 * Created by morse.lu on 16-6-2.
 */
public class CustomWordModuleGetCommonImages extends CustomWordModule {

    public final static String moduleName = "GetCommonImages";

    public CustomWordModuleGetCommonImages() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamGetCommonImages customModuleUserParamGetCommonImages = ((CustomWordValueGetCommonImages) customWord.getValue()).getUserParam();

        RuleExpression htmlTemplateExpression = customModuleUserParamGetCommonImages.getHtmlTemplate();
        RuleExpression imageTypeExpression = customModuleUserParamGetCommonImages.getImageType();
        RuleExpression viewTypeExpression = customModuleUserParamGetCommonImages.getViewType();
        RuleExpression useOriUrlExpression = customModuleUserParamGetCommonImages.getUseOriUrl();

        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, shopBean, user, extParameter);
        String imageType = expressionParser.parse(imageTypeExpression, shopBean, user, extParameter);
        String viewType = expressionParser.parse(viewTypeExpression, shopBean, user, extParameter);
        String useOriUrlStr = expressionParser.parse(useOriUrlExpression, shopBean, user, extParameter);
        boolean useOriUrl = false;
        if ("1".equals(useOriUrlStr)) {
            useOriUrl = true;
        }

        String parseResult = "";

        List<String> urls = sxProductService.getImageUrls(sxData.getChannelId(),
                                    sxData.getCartId(),
                                    Integer.valueOf(imageType),
                                    Integer.valueOf(viewType),
                                    sxData.getMainProduct().getCommon().getFields().getBrand(),
                                    sxData.getMainProduct().getCommon().getFields().getProductType(),
                                    sxData.getMainProduct().getCommon().getFields().getSizeType(),
                                    useOriUrl);

        for (String url : urls) {
            if (htmlTemplate != null) {
                parseResult += String.format(htmlTemplate, url);
            } else {
                parseResult += url;
            }
        }

        Set<String> imageSet = new HashSet<>(urls);
        if (imageSet.size() > 0 && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId()) && useOriUrl) {
            sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
        } else if (imageSet.size() > 0 && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId()) && useOriUrl) {
            sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
        }

        return parseResult;
    }
}
