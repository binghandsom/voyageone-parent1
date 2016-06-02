package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.components.imagecreate.bean.ImageCreateGetRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateGetResponse;
import com.voyageone.components.imagecreate.service.ImageCreateService;
import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.bean.tcb.TaskSignalType;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageWithParam;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageWithParam;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleImageWithParam extends CustomWordModule {

    public final static String moduleName = "ImageWithParam";

    @Autowired
    private ImageCreateService imageCreateService;

    public CustomWordModuleImageWithParam() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    //public Str
    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
        //user param
        CustomModuleUserParamImageWithParam customModuleUserParamImageWithParam= ((CustomWordValueImageWithParam) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamImageWithParam.getImageTemplate();
        List<RuleExpression> imageParamExpressions = customModuleUserParamImageWithParam.getImageParams();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, null);
        List<String> imageParams = new ArrayList<>();
        for (RuleExpression imageParamExpression : imageParamExpressions) {
            String imageParam = expressionParser.parse(imageParamExpression, null);
            if (imageParam == null) {
                continue;
            }
            try {
                imageParam = URLEncoder.encode(imageParam, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.fillInStackTrace();
                logger.error(e.getMessage(), e);
            }
            imageParams.add(imageParam);
        }

        int deleteItemsCount =0;
        for(Iterator<String> iterator=imageParams.iterator();iterator.hasNext();){
            String param = iterator.next();
            if(param == null || "".equals(param.trim())){

                iterator.remove();

                deleteItemsCount++;
            }

        }

        for (int i=0;i<deleteItemsCount;i++){
            imageParams.add("");
        }

        // 20160513 tom 图片服务器切换 START
        String parseResult = UploadImageHandler.encodeImageUrl(String.format(imageTemplate, imageParams.toArray()));

//        ImageCreateGetRequest request = new ImageCreateGetRequest();
//        request.setChannelId(expressionParser.getMasterWordCmsBtProduct().getChannelId());
//        request.setTemplateId(Integer.parseInt(imageTemplate));
//        request.setFile(imageTemplate + "_" + imageParams.get(0)); // 模板id + "_" + 第一个参数(一般是图片名)
//        request.setVParam(imageParams.toArray(new String[(imageParams.size())]));
//        ImageCreateGetResponse response = null;
//        String parseResult;
//        try {
//            response = imageCreateService.getImage(request);
//            parseResult = UploadImageHandler.encodeImageUrl(imageCreateService.getOssHttpURL(response.getResultData().getFilePath()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(
//                            "channelId:" + systemParam.getOrderChannelId() +
//                            ". cartId:" + systemParam.getCartId() +
//                            ". groupId:" + systemParam.getMainSxProduct().getCmsBtProductModelGroupPlatform().getGroupId() +
//                            ". 图片取得失败! 模板id:" + imageTemplate + ", 图片名:" + imageParams.get(0)));
//        }

        // 20160513 tom 图片服务器切换 END
        if (imageSet != null) {
            imageSet.add(parseResult);
        }

        return parseResult;
    }
}
