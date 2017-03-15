package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.*;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;

import java.util.List;

/**
 * 获取SxData里的product列表里的商品信息
 * @isMain 1或0：主商品， 非主商品
 * @codeIdx 主商品的场合忽略， 非主商品的场合， 是指第几个商品（主商品不算）
 * @dataType prop或image：common里的项目， 或者图片
 * @propName 当数据类型是text的场合， 这个项目就是common里的属性名称
 * @imageType 当数据类型是image的场合， 这个项目就是指图片类型（商品图之类的）
 * @imageIdx 当数据类型是image的场合， 第几张图片
 */
public class CustomWordModuleGetProductFieldInfo extends CustomWordModule {

    public final static String moduleName = "GetProductFieldInfo";

    public CustomWordModuleGetProductFieldInfo() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamGetProductFieldInfo customModuleUserParamGetProductFieldInfo = ((CustomWordValueGetProductFieldInfo) customWord.getValue()).getUserParam();

        RuleExpression isMainExpression = customModuleUserParamGetProductFieldInfo.getIsMain();
        RuleExpression codeIdxExpression = customModuleUserParamGetProductFieldInfo.getCodeIdx();
        RuleExpression dataTypeExpression = customModuleUserParamGetProductFieldInfo.getDataType();
        RuleExpression propNameExpression = customModuleUserParamGetProductFieldInfo.getPropName();
        RuleExpression imageTypeExpression = customModuleUserParamGetProductFieldInfo.getImageType();
        RuleExpression imageIdxExpression = customModuleUserParamGetProductFieldInfo.getImageIdx();

        String isMain = expressionParser.parse(isMainExpression, shopBean, user, extParameter);
        String codeIdx = expressionParser.parse(codeIdxExpression, shopBean, user, extParameter);
        String dataType = expressionParser.parse(dataTypeExpression, shopBean, user, extParameter);
        String propName = expressionParser.parse(propNameExpression, shopBean, user, extParameter);
        String imageTypeStr = expressionParser.parse(imageTypeExpression, shopBean, user, extParameter);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);
        String imageIdx = expressionParser.parse(imageIdxExpression, shopBean, user, extParameter);

        // 正式逻辑开始， 先确定一下用的是Group里的哪个商品
        CmsBtProductModel cmsBtProductModel = null;
        String mainProductCode = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getCode();
        if (!StringUtils.isEmpty(isMain) && "1".equals(isMain)) {
            // 主商品
            cmsBtProductModel = sxData.getMainProduct();
        } else {
            // 非主商品, 看看是第几个
            if (StringUtils.isEmpty(codeIdx)) {
                // 不指定第几个商品， 这是不行的
                return "";
            } else {
                // 找一下
                int iNow = 0;
                if (sxData.getProductList() != null) {
                    for (CmsBtProductModel p : sxData.getProductList()) {
                        // 不是主商品的场合才计数
                        if (!mainProductCode.equals(p.getCommonNotNull().getFieldsNotNull().getCode())) {
                            if (String.valueOf(iNow).equals(codeIdx)) {
                                // 找到了
                                cmsBtProductModel = p;
                                break;
                            }
                            iNow++;
                        }
                    }
                }
            }
        }

        if (cmsBtProductModel == null) {
            // 没找到指定idx的product
            return "";
        }

        // 看看数据类型
        if (StringUtils.isEmpty(dataType)) {
            // 没有指定类型是不行的
            return "";
        }
        if ("prop".equals(dataType)) {
            // 看看有没有指定的那个属性
            if (StringUtils.isEmpty(propName)) {
                return "";
            } else {
                String v = cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getStringAttribute(propName);
                if (StringUtils.isEmpty(v)) {
                    return "";
                } else {
                    return v;
                }
            }
        } else if ("image".equals(dataType)) {
            if (StringUtils.isEmpty(imageTypeStr) || StringUtils.isEmpty(imageIdx)) {
                return "";
            } else {
                List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProductService.getProductImages(cmsBtProductModel, imageType, sxData.getCartId());
                if (Integer.parseInt(imageIdx) >= cmsBtProductModelFieldImages.size()) {
                    return "";
                } else {
                    return cmsBtProductModelFieldImages.get(Integer.parseInt(imageIdx)).getName();
                }
            }

        } else {
            // 指定了错误的类型也是不行的
            return "";
        }

    }
}
