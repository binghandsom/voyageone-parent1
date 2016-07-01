package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.ims.rule_expression.CustomModuleUserParamGetCommonImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetCommonImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.bean.tcb.TaskSignalType;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * cms_bt_image_group(mongoDB)取得url
 * Created by morse.lu on 16-6-2.
 */
@Repository
public class CustomWordModuleGetCommonImages extends CustomWordModule {
    public final static String moduleName = "GetCommonImages";

    @Autowired
    private SxProductService sxProductService;

    public CustomWordModuleGetCommonImages() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
        //user param
        CustomModuleUserParamGetCommonImages customModuleUserParamGetCommonImages = ((CustomWordValueGetCommonImages) customWord.getValue()).getUserParam();

        RuleExpression htmlTemplateExpression = customModuleUserParamGetCommonImages.getHtmlTemplate();
        RuleExpression imageTypeExpression = customModuleUserParamGetCommonImages.getImageType();
        RuleExpression viewTypeExpression = customModuleUserParamGetCommonImages.getViewType();
        RuleExpression useOriUrlExpression = customModuleUserParamGetCommonImages.getUseOriUrl();

        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, null);
        String imageType = expressionParser.parse(imageTypeExpression, null);
        String viewType = expressionParser.parse(viewTypeExpression, null);
        String useOriUrlStr = expressionParser.parse(useOriUrlExpression, null);
        boolean useOriUrl = false;
        if ("1".equals(useOriUrlStr)) {
            useOriUrl = true;
        }

        String parseResult = "";

        List<String> urls;
        try {
            urls = sxProductService.getImageUrls(systemParam.getOrderChannelId(),
                    systemParam.getCartId(),
                    Integer.valueOf(imageType),
                    Integer.valueOf(viewType),
                    systemParam.getMainSxProduct().getCmsBtProductModel().getCommon().getFields().getBrand(),
                    systemParam.getMainSxProduct().getCmsBtProductModel().getCommon().getFields().getProductType(),
                    systemParam.getMainSxProduct().getCmsBtProductModel().getCommon().getFields().getSizeType(),
                    useOriUrl);
        } catch (Exception e) {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(
                            "channelId:" + systemParam.getOrderChannelId() +
                            ". cartId:" + systemParam.getCartId() +
                            ". groupId:" + systemParam.getMainSxProduct().getCmsBtProductModelGroupPlatform().getGroupId() +
                            ". 图片取得失败! (GetCommonImages)"));
        }

        for (String url : urls) {
            if (htmlTemplate != null) {
                parseResult += String.format(htmlTemplate, url);
            } else {
                parseResult += url;
            }
            if (imageSet != null && useOriUrl) {
                imageSet.add(url);
            }
        }

        return parseResult;
    }
}
