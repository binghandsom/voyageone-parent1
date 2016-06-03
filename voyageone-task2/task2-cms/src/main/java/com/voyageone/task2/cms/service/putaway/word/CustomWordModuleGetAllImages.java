package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetAllImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetAllImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.bean.tcb.TaskSignalType;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleGetAllImages extends CustomWordModule {
    public final static String moduleName = "GetAllImages";

//    @Autowired
//    private ImageCreateService imageCreateService;
    @Autowired
    private SxProductService sxProductService;

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
        RuleExpression useOriUrlExpression = customModuleUserParamGetAllImages.getUseOriUrl();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, null);
        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, null);
        String imageTypeStr = expressionParser.parse(imageTypeExpression, null);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);
        String useOriUrlStr = expressionParser.parse(useOriUrlExpression, null);

        //system param
        List<SxProductBean> sxProductBeans = systemParam.getSxProductBeans();

        String parseResult = "";

        for (SxProductBean sxProductBean : sxProductBeans) {
            // modified by morse.lu 2016/06/02 start
//            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProductBean.getCmsBtProductModel().getFields().getImages(imageType);
            // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProductService.getProductImages(sxProductBean.getCmsBtProductModel(), imageType);
            // modified by morse.lu 2016/06/02 end

            for (CmsBtProductModel_Field_Image cmsBtProductModelFieldImage : cmsBtProductModelFieldImages) {
                // 20160512 tom 有可能为空 add START
                if (StringUtils.isEmpty(cmsBtProductModelFieldImage.getName())) {
                    continue;
                }
                // 20160512 tom 有可能为空 add END
                // 20160513 tom 图片服务器切换 START
//                String completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());

                String completeImageUrl;
                if ("1".equals(useOriUrlStr)) {
                    // 使用原图
                    // start
                    try {
                        String url = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?req=imageprops", cmsBtProductModelFieldImage.getName());
                        logger.info("[CustomWordModuleGetAllImages]取得图片大小url:" + url);
                        String result = HttpUtils.get(url, null);
                        result = result.substring(result.indexOf("image"));
                        String[] args = result.split("image\\.");
                        Map<String, String> responseMap = new HashMap<>();
                        for (String param : args) {
                            if (param.indexOf("=") > 0) {
                                String[] keyVal = param.split("=");
                                if (keyVal.length > 1) {
                                    responseMap.put(keyVal[0], keyVal[1]);
                                } else {
                                    responseMap.put(keyVal[0], "");
                                }
                            }
                        }
                        completeImageUrl = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&rgn=0,0,%s,%s", cmsBtProductModelFieldImage.getName(), responseMap.get("width"), responseMap.get("height"));
                        logger.info("[CustomWordModuleGetAllImages]取得原始图片url:" + completeImageUrl);
                    } catch (Exception e) {
//                        throw new BusinessException("[CustomWordModuleGetAllImages]取得原始图片url失败!");
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(
                                "[CustomWordModuleGetAllImages]取得原始图片url失败! " +
                                        "channelId:" + systemParam.getOrderChannelId() +
                                        ". groupId:" + systemParam.getMainSxProduct().getCmsBtProductModelGroupPlatform().getGroupId() +
                                        ". productImageName:" + cmsBtProductModelFieldImage.getName()));
                    }
                    // end
                } else {
//                    ImageCreateGetRequest request = new ImageCreateGetRequest();
//                    request.setChannelId(expressionParser.getMasterWordCmsBtProduct().getChannelId());
//                    request.setTemplateId(Integer.parseInt(imageTemplate));
//                    request.setFile(imageTemplate + "_" + cmsBtProductModelFieldImage.getName()); // 模板id + "_" + 第一个参数(一般是图片名)
//                    String[] vPara = {cmsBtProductModelFieldImage.getName()};
//                    request.setVParam(vPara);
//                    ImageCreateGetResponse response = null;
//                    try {
//                        response = imageCreateService.getImage(request);
//                        completeImageUrl = imageCreateService.getOssHttpURL(response.getResultData().getFilePath());
//                    } catch (Exception e) {
//                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(
//                                "channelId:" + systemParam.getOrderChannelId() +
//                                        ". cartId:" + systemParam.getCartId() +
//                                        ". groupId:" + systemParam.getMainSxProduct().getCmsBtProductModelGroupPlatform().getGroupId() +
//                                        ". 图片取得失败! 模板id:" + imageTemplate + ", 图片名:" + cmsBtProductModelFieldImage.getName()));
//                    }
                    completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());
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
