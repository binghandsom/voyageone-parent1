package com.voyageone.service.impl.cms.sx.sku_field.tmall;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.MappingBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.sx.sku_field.AbstractSkuFieldBuilder;
import com.voyageone.service.model.cms.CmsMtPlatformPropSkuModel;
import com.voyageone.service.model.cms.constants.SkuTemplateConstants;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by morse.lu 2016/05/09 (copy and modified from task2 / TmallGjSkuFieldBuilderImpl_0)
 * 线程安全: 否
 */
public class TmallGjSkuFieldBuilderImpl1 extends AbstractSkuFieldBuilder {

    private final static int TPL_INDEX = 1;

    private Field skuField;
    private Field colorExtendField;

    private Field sku_colorField;
    private Field sku_priceField;
    private Field sku_quantityField;
    private Field sku_outerIdField;
    private Field sku_barCodeField;
    private Field sku_market_timeField;
    // added by morse.lu 2016/08/17 start
    private Field sku_skuIdField;
    private Field sku_productIdField;
    // added by morse.lu 2016/08/17 end

    private Field colorExtend_aliasnameField;
    private Field colorExtend_colorField;
    private Field colorExtend_basecolorField;
    private Field colorExtend_imageField;

    // added by morse.lu 2016/07/04 start
    private Field hscodeField;
    // added by morse.lu 2016/07/04 end

    private int availableColorIndex = 0;

    private BuildSkuResult buildSkuResult;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsBtProductModel_Sku> colorCmsSkuPropMap;

        //Build prop extend result
//        Map<String, ComplexValue> codeValueComplexValueMap;

        public BuildSkuResult() {
            colorCmsSkuPropMap = new HashMap<>();
//            codeValueComplexValueMap = new HashMap<>();
        }

        public Map<String, CmsBtProductModel_Sku> getColorCmsSkuPropMap() {
            return colorCmsSkuPropMap;
        }

//        public Map<String, ComplexValue> getCodeValueComplexValueMap() {
//            return codeValueComplexValueMap;
//        }
    }

    @Override
    protected boolean init(List<Field> platformProps, int cartId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("cartId", cartId);
        searchMap.put("template", TPL_INDEX); // 模板1
        List<CmsMtPlatformPropSkuModel> listTmallSkuInfos = cmsMtPlatformPropSkuDao.selectList(searchMap);

        // Map<prop_id, List<CmsMtPlatformPropSkuModel>>
        Map<String, List<CmsMtPlatformPropSkuModel>> mapTmallSkuInfos = listTmallSkuInfos.stream().collect(Collectors.groupingBy(model -> model.getPropId()));

        for (Field platformProp : platformProps) {
            if ("hscode".equals(platformProp.getId())) {
                // added by morse.lu 2016/07/04 start
                hscodeField = platformProp;
                // added by morse.lu 2016/07/04 end
                continue;
            }

            if (mapTmallSkuInfos.get(platformProp.getId()) == null) {
                $warn("No sku info find for platformProp: [prop_id=" + platformProp.getId() + ", cartId=" + cartId + "]");
                $warn(this.getClass().getName() + " is not applicable!");
                return false;
            }

            // 取得这个propId所有的type
            List<Integer> listType = mapTmallSkuInfos.get(platformProp.getId()).stream().map(CmsMtPlatformPropSkuModel::getSkuType).collect(Collectors.toList());

            for (Integer type : listType) {
                //SKU
                if (type.intValue() == SkuTemplateConstants.SKU) {
                    skuField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_BARCODE) {
                    sku_barCodeField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_COLOR) {
                    sku_colorField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_OUTERID) {
                    sku_outerIdField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_PRICE) {
                    sku_priceField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_QUANTITY) {
                    sku_quantityField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_MARKET_TIME) {
                    sku_market_timeField = platformProp;
                }
                // added by morse.lu 2016/08/17 start
                if (type.intValue() == SkuTemplateConstants.SKU_ID) {
                    sku_skuIdField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_PRODUCTID) {
                    sku_productIdField = platformProp;
                }
                // added by morse.lu 2016/08/17 end

                //EXTENDCOLOR
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR) {
                    colorExtendField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_ALIASNAME) {
                    colorExtend_aliasnameField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_COLOR) {
                    colorExtend_colorField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_IMAGE) {
                    colorExtend_imageField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_BASECOLOR) {
                    colorExtend_basecolorField = platformProp;
                }

                // 暂时不知道匹配什么
                if (type.intValue() == SkuTemplateConstants.UNKOWN) {
                    addUnkownField(platformProp);
                }
            }
        }

        if (skuField == null) {
            $warn(this.getClass().getName() + " requires sku!");
            return false;
        }
        if (sku_colorField == null) {
            $warn(this.getClass().getName() + " requires sku color!");
            return false;
        }

        return true;
    }

    // modified by morse.lu 2016/07/04 start
    // 不用Mapping了
//    private void buildSkuColor(ComplexValue skuFieldValue, ExpressionParser expressionParser, MappingBean colorMapping, CmsBtProductModel_Sku cmsSkuProp, ShopBean shopBean, String user) throws Exception {
    private void buildSkuColor(ComplexValue skuFieldValue, CmsBtProductModel productModel, CmsBtProductModel_Sku cmsSkuProp) throws Exception {
        // modified by morse.lu 2016/07/04 end
        if (sku_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> colorOptions = ((SingleCheckField)sku_colorField).getOptions();
            // added by morse.lu 2016/07/26 start
            if (availableColorIndex >= colorOptions.size()) {
                throw new BusinessException(String.format("最多只能%d个sku!请拆分group!", colorOptions.size()));
            }
            // added by morse.lu 2016/07/26 end
            String colorValue = colorOptions.get(availableColorIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_colorField.getId(), new Value(colorValue));
            buildSkuResult.getColorCmsSkuPropMap().put(colorValue, cmsSkuProp);
        } else {
            // modified by morse.lu 2016/07/04 start
            // 不用Mapping了
//            RuleExpression skuColorExpression = ((SimpleMappingBean)colorMapping).getExpression();
//            String skuColor = expressionParser.parse(skuColorExpression, shopBean, user, null);
            String skuColor = getSkuValue(productModel, sku_colorField.getId(), cmsSkuProp.getSkuCode());
            if (StringUtils.isEmpty(skuColor)) {
                // 没填的话用code
                skuColor = productModel.getCommon().getFields().getCode();
            }
            // modified by morse.lu 2016/07/04 end
            skuFieldValue.setInputFieldValue(sku_colorField.getId(), skuColor);
            buildSkuResult.getColorCmsSkuPropMap().put(skuColor, cmsSkuProp);
        }
    }

    private Field buildSkuProp(Field skuField, ExpressionParser expressionParser, MappingBean skuMapping, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        List<CmsBtProductModel> sxProducts = expressionParser.getSxData().getProductList();
        buildSkuResult = new BuildSkuResult();

        // deleted by morse.lu 2016/07/04 start
        // 这一版不用Mapping啦,方法入参先留着,以防以后有特殊的需要Mapping的属性
//        ComplexMappingBean skuMappingComplex = (ComplexMappingBean) skuMapping;
//        List<MappingBean> subMappingBeans = skuMappingComplex.getSubMappings();
//        Map<String, Field> fieldMap = ((MultiComplexField)skuField).getFieldMap();
//
//        Map<String, MappingBean> skuSubMappingMap = new HashMap<>();
//        for (MappingBean mappingBean : subMappingBeans) {
//            String propId = mappingBean.getPlatformPropId();
//            skuSubMappingMap.put(propId, mappingBean);
//        }
        // deleted by morse.lu 2016/07/04 end
        // deleted by morse.lu 2016/10/18 start
        // 这段又不需要了- -！ 因为允许改类目了
        // added by morse.lu 2016/08/17 start
//        List<ComplexValue> multiComplexDefaultValues = ((MultiComplexField) skuField).getDefaultComplexValues();
//        // Map<sku_outerId商家编码即skuCode, ComplexValue>
//        Map<String, ComplexValue> mapSkuComplexValue = new HashMap<>();
//        if (ListUtils.notNull(multiComplexDefaultValues) && sku_outerIdField != null) {
//            for (ComplexValue complexValue : multiComplexDefaultValues) {
//                String sku_outerId = "";
//                for (String fieldId : complexValue.getFieldKeySet()) {
//                    if (fieldId.equals(sku_outerIdField.getId())) {
//                        sku_outerId = ((InputField) complexValue.getValueField(fieldId)).getValue();
//                        break;
//                    }
//                }
//                mapSkuComplexValue.put(sku_outerId, complexValue);
//            }
//        }
        // added by morse.lu 2016/08/17 end
        // deleted by morse.lu 2016/10/18 end

        List<ComplexValue> complexValues = new ArrayList<>();
        for (CmsBtProductModel sxProduct : sxProducts) {
            List<CmsBtProductModel_Sku> cmsSkuPropBeans = sxProduct.getCommon().getSkus();
            for (CmsBtProductModel_Sku cmsSkuProp : cmsSkuPropBeans) {
                //CmsBtProductModel_Sku 是Map<String, Object>的子类
                // deleted by morse.lu 2016/07/04 start
                // 不Mapping了,不用了吧,虽然留着也不影响,但是set的对象不对,应该是common下的skus + 各平台下面的skus
//                expressionParser.setSkuPropContext(cmsSkuProp);
                // deleted by morse.lu 2016/07/04 end
                ComplexValue skuFieldValue = new ComplexValue();
                complexValues.add(skuFieldValue);

                // modified by morse.lu 2016/07/04 start
                // 不用Mapping了
//                buildSkuColor(skuFieldValue, expressionParser, skuSubMappingMap.get(sku_colorField.getId()), cmsSkuProp, shopBean, user);
                buildSkuColor(skuFieldValue, sxProduct, cmsSkuProp);
                // modified by morse.lu 2016/07/04 end

                // modified by morse.lu 2016/07/04 start
                // 不用Mapping了
//                for (MappingBean mappingBean : skuMappingComplex.getSubMappings()) {
//                    String propId = mappingBean.getPlatformPropId();
//                    // add by morse.lu 2016/05/15 start
//                    // target店上新临时添加写死用
//                    if ("hscode".equals(propId)) {
//                        RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                        String propValue = expressionParser.parse(ruleExpression, shopBean, user, null); // "0410004300, 戒指 ,对" 或者  "0410004300, 戒指 ,只"
//                        skuFieldValue.setInputFieldValue(propId, propValue.split(",")[0]);
////                        skuFieldValue.setInputFieldValue(propId, "0410004300");
//                        continue;
//                    }
//                    // add by morse.lu 2016/05/15 end
//                    if (propId.equals(sku_colorField.getId())) {
//                        continue;
//                    } else if (propId.equals(sku_quantityField.getId())) {
//                        int skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
//                        skuFieldValue.setInputFieldValue(propId, String.valueOf(skuQuantity));
//                    } else {
//                        RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                        String propValue = expressionParser.parse(ruleExpression, shopBean, user, null);
//                        Field subField = fieldMap.get(propId);
//                        if (subField != null) {
//                            if (subField.getType() == FieldTypeEnum.INPUT) {
//                                skuFieldValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
//                            } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
//                                skuFieldValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
//                            }
//                        }
//                    }
//                }
                for (Field field : ((MultiComplexField)skuField).getFields()) {
                    String fieldId = field.getId();
                    if (fieldId.equals(sku_colorField.getId())) {
                        continue;
                    }
                    // deleted by morse.lu 2016/07/12 start
                    // 暂时不要条形码
//                    if (sku_barCodeField != null && fieldId.equals(sku_barCodeField.getId())) {
//                        skuFieldValue.setInputFieldValue(sku_barCodeField.getId(), sxProduct.getCommon().getSku(cmsSkuProp.getSkuCode()).getBarcode());
//                        continue;
//                    }
                    // deleted by morse.lu 2016/07/12 end
                    if (sku_outerIdField != null && fieldId.equals(sku_outerIdField.getId())) {
                        skuFieldValue.setInputFieldValue(sku_outerIdField.getId(), cmsSkuProp.getSkuCode());
                        continue;
                    }
                    if (sku_priceField != null && fieldId.equals(sku_priceField.getId())) {
                        skuFieldValue.setInputFieldValue(sku_priceField.getId(), String.valueOf(getSkuPrice(expressionParser.getSxData().getChannelId(), sxProduct, cmsSkuProp.getSkuCode())));
                        continue;
                    }
                    if (sku_quantityField != null && fieldId.equals(sku_quantityField.getId())) {
                        Integer skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
                        if (skuQuantity == null) {
                            skuQuantity = 0;
                        }
                        skuFieldValue.setInputFieldValue(sku_quantityField.getId(), String.valueOf(skuQuantity));
                        continue;
                    }
                    if (hscodeField != null && fieldId.equals(hscodeField.getId())) {
                        // hscode不做Mapping了，写死从个人税号里去取
                        String propValue = expressionParser.getSxData().getMainProduct().getCommon().getFields().getHsCodePrivate();
                        // added by morse.lu 2017/01/03 start
                        // 通过配置表(cms_mt_channel_config)来决定用hsCodeCross，还是hsCodePrivate，默认用hsCodePrivate
                        CmsChannelConfigBean hscodeConfig = CmsChannelConfigs.getConfigBean(expressionParser.getSxData().getChannelId(),
                                CmsConstants.ChannelConfig.HSCODE,
                                String.valueOf(expressionParser.getSxData().getCartId()) + CmsConstants.ChannelConfig.SX_HSCODE);
                        if (hscodeConfig != null) {
                            String hscodePropName = hscodeConfig.getConfigValue1(); // 目前配置的是code或者color或者codeDiff
                            if (!StringUtils.isEmpty(hscodePropName)) {
                                String val = expressionParser.getSxData().getMainProduct().getCommon().getFields().getStringAttribute(hscodePropName);
                                if (!StringUtils.isEmpty(val)) {
                                    propValue = val;
                                }
                            }
                        }
                        // added by morse.lu 2017/01/03 end
                        skuFieldValue.setInputFieldValue(hscodeField.getId(), propValue.split(",")[0]);
                        continue;
                    }
                    // added by morse.lu 2016/08/17 start
                    // deleted by morse.lu 2016/10/08 start
                    // 暂时不填，当作新的sku上传
//                    if (sku_skuIdField != null && fieldId.equals(sku_skuIdField.getId())) {
//                        ComplexValue complexValue = mapSkuComplexValue.get(cmsSkuProp.getSkuCode());
//                        if (complexValue != null) {
//                            Field oldField = complexValue.getValueField(fieldId);
//                            if (oldField != null) {
//                                skuFieldValue.setInputFieldValue(sku_skuIdField.getId(), ((InputField) oldField).getValue());
//                            }
//                        }
//                        continue;
//                    }
                    // deleted by morse.lu 2016/10/08 end
                    if (sku_productIdField != null && fieldId.equals(sku_productIdField.getId())) {
                        // modified by morse.lu 2016/10/18 start
//                        ComplexValue complexValue = mapSkuComplexValue.get(cmsSkuProp.getSkuCode());
//                        if (complexValue != null) {
//                            Field oldField = complexValue.getValueField(fieldId);
//                            if (oldField != null) {
//                                skuFieldValue.setInputFieldValue(sku_productIdField.getId(), ((InputField) oldField).getValue());
//                            }
//                        }
                        String skuCode = cmsSkuProp.getSkuCode();
                        String scProductId = expressionParser.getSxProductService().updateTmScProductId(
                                shopBean,
                                expressionParser.getSxData().getMainProduct(),
                                skuCode,
                                expressionParser.getSxProductService().getProductValueByMasterMapping("title", shopBean, expressionParser, user),
                                skuInventoryMap.get(skuCode) != null ? Integer.toString(skuInventoryMap.get(skuCode)) : "0"
                        );
                        skuFieldValue.setInputFieldValue(sku_productIdField.getId(), scProductId);
                        // modified by morse.lu 2016/10/18 end
                        // added by morse.lu 2017/01/05 start
                        expressionParser.getSxData().getSxSkuExInfo(skuCode, true).setScProductId(scProductId);
                        // added by morse.lu 2017/01/05 end
                        continue;
                    }
                    // added by morse.lu 2016/08/17 end

                    // 从各平台下面的fields.sku里取值
                    String val = getSkuValue(sxProduct, fieldId, cmsSkuProp.getSkuCode());
                    if (field.getType() == FieldTypeEnum.INPUT) {
                        skuFieldValue.setInputFieldValue(fieldId, val);
                    } else if (field.getType() == FieldTypeEnum.SINGLECHECK) {
                        skuFieldValue.setSingleCheckFieldValue(fieldId, new Value(val));
                    }
                }
                // modified by morse.lu 2016/07/04 end
            }
        }

        ((MultiComplexField)skuField).setComplexValues(complexValues);
        return skuField;
    }

    private Field buildColorExtendProp(ExpressionParser expressionParser, MappingBean colorExtendMapping, ShopBean shopBean, String user) throws Exception {
        SxData sxData = expressionParser.getSxData();
        boolean hasSomeSku = false;
        Map<CmsBtProductModel_Sku, CmsBtProductModel> skuProductMap = new HashMap<>();
        for (CmsBtProductModel sxProduct : sxData.getProductList()) {
            List<CmsBtProductModel_Sku> listSku = sxProduct.getCommon().getSkus();
            if (listSku.size() > 1) {
                hasSomeSku = true;
            }
            for (CmsBtProductModel_Sku sku : listSku) {
                skuProductMap.put(sku, sxProduct);
            }
        }

        Map<String, Field> fieldMap = ((MultiComplexField)colorExtendField).getFieldMap();

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsBtProductModel_Sku> entry : buildSkuResult.getColorCmsSkuPropMap().entrySet())
        {
            CmsBtProductModel_Sku cmsSkuProp = entry.getValue();
            // deleted by morse.lu 2016/07/04 start
            // 不Mapping了,不用了吧,虽然留着也不影响,但是set的对象不对,应该是common下的skus + 各平台下面的skus
//            expressionParser.setSkuPropContext(cmsSkuProp);
            // deleted by morse.lu 2016/07/04 end

            ComplexValue complexValue = new ComplexValue();

            if (colorExtend_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(colorExtend_colorField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(colorExtend_colorField.getId(), entry.getKey());
            }

            CmsBtProductModel sxProductBean = skuProductMap.get(cmsSkuProp);
            // modified by morse.lu 2016/07/04 start
            // 不用Mapping了
//            // tom 设置当前的产品内容 START TODO: 这段写的不是优雅, 之后看情况修改
//            expressionParser.pushMasterPropContext(sxProductBean.getCommon().getFields());
//            // tom 设置当前的产品内容 END TODO: 这段写的不是优雅, 之后看情况修改
            // modified by morse.lu 2016/07/04 end

            if (colorExtend_imageField != null) {
                // modified by morse.lu 2016/08/09 start
//                String propImage = sxProductBean.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
                String propImage = expressionParser.getSxProductService().getProductImages(sxProductBean, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE, sxData.getCartId()).get(0).getName();
                // modified by morse.lu 2016/08/09 end
                if (propImage != null && !"".equals(propImage)) {
                    if (StringUtils.isEmpty(getCodeImageTemplate())) {
                        $warn("图片模板url未设置");
                        complexValue.setInputFieldValue(colorExtend_imageField.getId(), null);
                    } else {
                        String codePropFullImageUrl = String.format(getCodeImageTemplate(), propImage);
//                        String codePropFullImageUrl = expressionParser.getSxProductService().getImageByTemplateId(sxData.getChannelId(), getCodeImageTemplate(), propImage);
//                    codePropFullImageUrl = expressionParser.getSxProductService().encodeImageUrl(codePropFullImageUrl);
//                        complexValue.setInputFieldValue(colorExtend_imageField.getId(), codePropFullImageUrl);

                        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
                            Set<String> url = new HashSet<>();
                            url.add(codePropFullImageUrl);
                            // 上传图片到天猫图片空间
//                            expressionParser.getSxProductService().uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
                            Map<String, String> retMap = expressionParser.getSxProductService().uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
                            complexValue.setInputFieldValue(colorExtend_imageField.getId(), retMap.get(codePropFullImageUrl));
                        } else {
                            complexValue.setInputFieldValue(colorExtend_imageField.getId(), codePropFullImageUrl);
                        }
                    }
                }
            }

            // modified by morse.lu 2016/07/04 start
            // 不用Mapping了
//            for (MappingBean mappingBean : ((ComplexMappingBean)colorExtendMapping).getSubMappings()) {
//                String propId = mappingBean.getPlatformPropId();
//                if (propId.equals(colorExtend_colorField.getId())
//                        || (colorExtend_imageField  != null && propId.equals(colorExtend_imageField.getId()))) {
//                    continue;
//                } else {
//                    // added by morse.lu 2016/05/27 start
//                    // TODO:一个产品多个sku时，暂定别名用skuCode，以外的场合还是用产品code(mapping表设定)
//                    if (hasSomeSku && propId.equals(colorExtend_aliasnameField.getId())) {
//                        // 别名
//                        complexValue.setInputFieldValue(propId, cmsSkuProp.getSkuCode());
//                        continue;
//                    }
//                    // added by morse.lu 2016/05/27 end
//                    RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                    String propValue = expressionParser.parse(ruleExpression, shopBean, user, null);
//                    Field subField = fieldMap.get(propId);
//                    if (subField.getType() == FieldTypeEnum.INPUT) {
//                        complexValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
//                    } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
//                        complexValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
//                    }
//                }
//            }
//
//            // tom 释放 START TODO: 这段写的不是优雅, 之后看情况修改
//            expressionParser.popMasterPropContext();
//            // tom 释放 END TODO: 这段写的不是优雅, 之后看情况修改

            if (colorExtend_aliasnameField != null) {
                // 别名
                // 一个产品多个sku时，暂定别名用skuCode，以外的场合还是用产品code
                if (hasSomeSku) {
                    // modified by morse.lu 2016/08/02 start
                    // color + size
//                    complexValue.setInputFieldValue(colorExtend_aliasnameField.getId(), cmsSkuProp.getSkuCode());
                    String alias = sxProductBean.getCommon().getFields().getColor() + "_" + cmsSkuProp.getSizeSx();
                    complexValue.setInputFieldValue(colorExtend_aliasnameField.getId(), alias);
                    // modified by morse.lu 2016/08/02 end
                } else {
                    // modified by morse.lu 2016/08/02 start
                    // 如果code长度大于60，那么用color
//                    complexValue.setInputFieldValue(colorExtend_aliasnameField.getId(), sxProductBean.getCommon().getFields().getCode());
                    // modified by morse.lu 2016/12/29 start
                    // 放到共通去了
//                    String alias = sxProductBean.getCommon().getFields().getCode();
//                    // added by morse.lu 2016/08/29 start
//                    // 通过配置表(cms_mt_channel_config)来决定用code，还是color，默认用code
//                    CmsChannelConfigBean aliasConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId()
//                            , CmsConstants.ChannelConfig.ALIAS
//                            , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.COLOR_ALIAS);
//                    if (aliasConfig != null) {
//                        String aliasPropName = aliasConfig.getConfigValue1(); // 目前配置的是code或者color
//                        if (!StringUtils.isEmpty(aliasPropName)) {
//                            String val = sxProductBean.getCommon().getFields().getStringAttribute(aliasPropName);
//                            if (!StringUtils.isEmpty(val)) {
//                                alias = val;
//                            }
//                        }
//                    }
//                    // added by morse.lu 2016/08/29 end
//                    if (alias.length() > 60) {
//                        alias = sxProductBean.getCommon().getFields().getColor();
//                    }
                    String alias = expressionParser.getSxProductService().getSxColorAlias(sxData.getChannelId(), sxData.getCartId(), sxProductBean, 60);
                    // modified by morse.lu 2016/12/29 end
                    complexValue.setInputFieldValue(colorExtend_aliasnameField.getId(), alias);
                    // modified by morse.lu 2016/08/02 end
                }
            }
            if (colorExtend_basecolorField != null) {
                // 原先也没做,这版也暂时不做
            }
            // modified by morse.lu 2016/07/04 end

            complexValues.add(complexValue);
        }
        ((MultiComplexField)colorExtendField).setComplexValues(complexValues);
        return colorExtendField;
    }

    @Override
    public List<Field> buildSkuInfoFieldChild(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        List<Field> skuInfoFields = new ArrayList<>();

        MappingBean skuMappingBean = null;
        MappingBean colorExtendMappingBean = null;

        // deleted by morse.lu 2016/07/04 start
        // 这一版不用Mapping啦,方法入参先留着,以防以后有特殊的需要Mapping的属性
//        List<MappingBean> mappingBeenList = cmsMtPlatformMappingModel.getProps();
//
//        for (MappingBean mappingBean : mappingBeenList) {
//            if (mappingBean.getPlatformPropId().equals(skuField.getId())) {
//                skuMappingBean = mappingBean;
//            }
//            if (colorExtendField != null && mappingBean.getPlatformPropId().equals(colorExtendField.getId())) {
//                colorExtendMappingBean = mappingBean;
//            }
//        }
        // deleted by morse.lu 2016/07/04 end

        skuField = buildSkuProp(skuField, expressionParser, skuMappingBean, skuInventoryMap, shopBean, user);
        skuInfoFields.add(skuField);

        if (colorExtendField != null) {
            Field colorExtendField = buildColorExtendProp(expressionParser, colorExtendMappingBean, shopBean, user);
            skuInfoFields.add(colorExtendField);
        }

        return skuInfoFields;
    }
}
