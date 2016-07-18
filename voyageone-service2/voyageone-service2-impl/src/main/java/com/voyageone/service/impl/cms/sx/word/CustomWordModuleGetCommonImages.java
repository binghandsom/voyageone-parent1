package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetCommonImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetCommonImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        RuleExpression imageIndexExpression = customModuleUserParamGetCommonImages.getImageIndex();

        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, shopBean, user, extParameter);
        String imageType = expressionParser.parse(imageTypeExpression, shopBean, user, extParameter);
        String viewType = expressionParser.parse(viewTypeExpression, shopBean, user, extParameter);
        String useOriUrlStr = expressionParser.parse(useOriUrlExpression, shopBean, user, extParameter);
        String imageIndex = expressionParser.parse(imageIndexExpression, shopBean, user, extParameter);
        boolean useOriUrl = false;
        if ("1".equals(useOriUrlStr)) {
            useOriUrl = true;
        }

        String parseResult = "";

        List<String> urls = sxProductService.getImageUrls(sxData.getChannelId(),
                                    sxData.getCartId(),
                                    Integer.valueOf(imageType),
                                    Integer.valueOf(viewType),
                                    // modified by morse.lu 2016/06/27 start
                                    // 表结构变化，改从common下的fields里去取
//                                    sxData.getMainProduct().getFields().getBrand(),
//                                    sxData.getMainProduct().getFields().getProductType(),
//                                    sxData.getMainProduct().getFields().getSizeType(),
                                    sxData.getMainProduct().getCommon().getFields().getBrand(),
                                    sxData.getMainProduct().getCommon().getFields().getProductType(),
                                    sxData.getMainProduct().getCommon().getFields().getSizeType(),
                                    // modified by morse.lu 2016/06/27 end
                                    useOriUrl);

        if (imageIndex == null) {
            for (String url : urls) {
                if (htmlTemplate != null) {
                    parseResult += String.format(htmlTemplate, url);
                } else {
                    parseResult += url;
                }
            }
        } else {
            int intImageIndex = Integer.parseInt(imageIndex);
            if (intImageIndex >= urls.size()) {
                parseResult = "";
            } else {
                parseResult = urls.get(intImageIndex);
            }
        }

        Map<String, String> map = null;
        Set<String> imageSet = new HashSet<>(urls);
        if (!imageSet.isEmpty() && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId()) && useOriUrl) {
            map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
        } else if (!imageSet.isEmpty() && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId()) && useOriUrl) {
            map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
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
