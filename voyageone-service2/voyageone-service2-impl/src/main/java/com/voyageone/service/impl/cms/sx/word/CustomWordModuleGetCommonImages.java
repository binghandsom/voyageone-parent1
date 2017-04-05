package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetCommonImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetCommonImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;

import java.util.*;
import java.util.stream.Collectors;

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

        String image = "";
        String parseResult = "";
        List<String> urls = new ArrayList<>();
        List<String> imageList = new ArrayList<>();
        // charis 对一张尺码表对应多张尺码图的进行绑定处理
        if ("2".equals(imageType) && sxData.getSizeChartId() != null && sxData.getSizeChartId() != 0) {
            List<Map<String, Object>> imageGroupList = sxProductService.getListImageGroupBySizeChartId(sxData.getChannelId(), sxData.getSizeChartId(), viewType);
            Map<String, Object> imageMap = imageGroupList.stream()
                    .filter(map -> sxData.getCartId().equals(map.get("cartId")))
                    .findAny()
                    .orElse(new HashMap<>());
            if (imageMap.get("image") != null) {
                imageList = ((List<CmsBtImageGroupModel_Image>) imageMap.get("image")).stream()
                        .map(i -> i.getPlatformUrl())
                        .collect(Collectors.toList());
            }
        }
        if (ListUtils.notNull(imageList)) {
            urls.addAll(imageList);
        }else {
            urls = sxProductService.getImageUrls(
                    sxData.getChannelId(),
//                                    sxData.getMainProduct().getOrgChannelId(),
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
        }

        if (imageIndex == null) {
            for (String url : urls) {
                if (htmlTemplate != null) {
                    parseResult += String.format(htmlTemplate, url);
                } else {
                    parseResult += url;
                }
            }
        } else {
            // 取得指定图片index(从0开始)对应的图片
            int intImageIndex = Integer.parseInt(imageIndex);
            if (intImageIndex >= urls.size()) {
                parseResult = "";
            } else {
                String url = urls.get(intImageIndex);
                if (htmlTemplate != null) {
                    parseResult = String.format(htmlTemplate, url);
                } else {
                    parseResult = url;
                }
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
