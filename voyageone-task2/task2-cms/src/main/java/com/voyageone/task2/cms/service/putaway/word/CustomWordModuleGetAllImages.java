package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.imagecreate.bean.ImageCreateGetRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateGetResponse;
import com.voyageone.components.imagecreate.service.ImageCreateService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.bean.tcb.TaskSignalType;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetAllImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetAllImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleGetAllImages extends CustomWordModule {
    public final static String moduleName = "GetAllImages";

    @Autowired
    private ImageCreateService imageCreateService;

    public CustomWordModuleGetAllImages() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
        //user param
        CustomModuleUserParamGetAllImages customModuleUserParamGetAllImages = ((CustomWordValueGetAllImages) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamGetAllImages.getImageTemplate();
        RuleExpression htmlTemplateExpression = customModuleUserParamGetAllImages.getHtmlTemplate();
        RuleExpression imageTypeExpression = customModuleUserParamGetAllImages.getImageType();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, null);
        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, null);
        String imageTypeStr = expressionParser.parse(imageTypeExpression, null);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);

        //system param
        List<SxProductBean> sxProductBeans = systemParam.getSxProductBeans();

        String parseResult = "";

        for (SxProductBean sxProductBean : sxProductBeans) {
            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProductBean.getCmsBtProductModel().getFields().getImages(imageType);

            for (CmsBtProductModel_Field_Image cmsBtProductModelFieldImage : cmsBtProductModelFieldImages) {
                // 20160512 tom 有可能为空 add START
                if (StringUtils.isEmpty(cmsBtProductModelFieldImage.getName())) {
                    continue;
                }
                // 20160512 tom 有可能为空 add END
                // 20160513 tom 图片服务器切换 START
//                String completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());

                ImageCreateGetRequest request = new ImageCreateGetRequest();
                request.setChannelId(expressionParser.getMasterWordCmsBtProduct().getChannelId());
                request.setTemplateId(Integer.parseInt(imageTemplate));
                request.setFile(imageTemplate + "_" + cmsBtProductModelFieldImage.getName()); // 模板id + "_" + 第一个参数(一般是图片名)
                String[] vPara = {cmsBtProductModelFieldImage.getName()};
                request.setVParam(vPara);
                ImageCreateGetResponse response = null;
                String completeImageUrl;
                try {
                    response = imageCreateService.getImage(request);
                    completeImageUrl = imageCreateService.getOssHttpURL(response.getResultData().getFilePath());
                } catch (Exception e) {
                    throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(
                                    "channelId:" + systemParam.getOrderChannelId() +
                                    ". cartId:" + systemParam.getCartId() +
                                    ". groupId:" + systemParam.getMainSxProduct().getCmsBtProductModelGroupPlatform().getGroupId() +
                                    ". 图片取得失败! 模板id:" + imageTemplate + ", 图片名:" + cmsBtProductModelFieldImage.getName()));
                }

                // 20160513 tom 图片服务器切换 END
                completeImageUrl = UploadImageHandler.encodeImageUrl(completeImageUrl);
                if (htmlTemplate != null) {
                    parseResult += String.format(htmlTemplate, completeImageUrl);
                }
                else {
                    parseResult += completeImageUrl;
                }
                if (imageSet != null) {
                    imageSet.add(completeImageUrl);
                }
            }

        }
        return parseResult;
    }
}
