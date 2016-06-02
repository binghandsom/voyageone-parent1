package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamImageWithParam;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueImageWithParam;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleImageWithParam extends CustomWordModule {

    public final static String moduleName = "ImageWithParam";

    public CustomWordModuleImageWithParam() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamImageWithParam customModuleUserParamImageWithParam= ((CustomWordValueImageWithParam) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamImageWithParam.getImageTemplate();
        List<RuleExpression> imageParamExpressions = customModuleUserParamImageWithParam.getImageParams();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, shopBean, user, extParameter);
        List<String> imageParams = new ArrayList<>();
        boolean isFirstParam = true;
        for (RuleExpression imageParamExpression : imageParamExpressions) {
            String imageParam = expressionParser.parse(imageParamExpression, shopBean, user, extParameter);
            // added by morse.lu 2016/06/02 start
            if (isFirstParam) {
                isFirstParam = false;
                // 第一个参数是商品图名，取不到的话，直接去取fields.images1[0]
                if (StringUtils.isEmpty(imageParam)) {
                    CmsBtProductModel product = sxData.getMainProduct();
                    if (extParameter != null && extParameter.length > 0) {
                        // 获取指定product的图片(如果没找到, 那么就使用主商品的图片)
                        for (CmsBtProductModel productModel : sxData.getProductList()) {
                            if (productModel.getFields().getCode().equals(extParameter[0])) {
                                product = productModel;
                                break;
                            }
                        }
                    }
                    imageParam = product.getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
                }
            }
            // added by morse.lu 2016/06/02 end
            if (imageParam == null) {
                continue;
            }
            try {
                imageParam = URLEncoder.encode(imageParam, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.fillInStackTrace();
                $error(e.getMessage(), e);
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
        String parseResult = String.format(imageTemplate, imageParams.toArray());
//        String parseResult = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, imageParams.get(0));
//        String parseResult = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, imageParams.toArray(new String[imageParams.size()]));
        // 20160513 tom 图片服务器切换 END

//        parseResult = sxProductService.encodeImageUrl(parseResult);
        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
            Set<String> url = new HashSet<>();
            url.add(parseResult);
            sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
        }
//        if (imageSet != null) {
//            imageSet.add(parseResult);
//        }

        return parseResult;
    }
}
