package com.voyageone.service.impl.cms.sx.sku_field.tmall;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.ComplexMappingBean;
import com.voyageone.service.bean.cms.MappingBean;
import com.voyageone.service.bean.cms.SimpleMappingBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.sx.sku_field.AbstractSkuFieldBuilder;
import com.voyageone.service.model.cms.CmsMtPlatformPropSkuModel;
import com.voyageone.service.model.cms.constants.SkuTemplateConstants;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
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

    private Field colorExtend_aliasnameField;
    private Field colorExtend_colorField;
    private Field colorExtend_basecolorField;
    private Field colorExtend_imageField;

    private int availableColorIndex = 0;

    private BuildSkuResult buildSkuResult;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsBtProductModel_Sku> colorCmsSkuPropMap;

        //Build prop extend result
        Map<String, ComplexValue> codeValueComplexValueMap;

        public BuildSkuResult() {
            colorCmsSkuPropMap = new HashMap<>();
            codeValueComplexValueMap = new HashMap<>();
        }

        public Map<String, CmsBtProductModel_Sku> getColorCmsSkuPropMap() {
            return colorCmsSkuPropMap;
        }

        public Map<String, ComplexValue> getCodeValueComplexValueMap() {
            return codeValueComplexValueMap;
        }
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

    private void buildSkuColor(ComplexValue skuFieldValue, ExpressionParser expressionParser, MappingBean colorMapping, CmsBtProductModel_Sku cmsSkuProp, ShopBean shopBean, String user) throws Exception {
        if (sku_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> colorOptions = ((SingleCheckField)sku_colorField).getOptions();
            String colorValue = colorOptions.get(availableColorIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_colorField.getId(), new Value(colorValue));
            buildSkuResult.getColorCmsSkuPropMap().put(colorValue, cmsSkuProp);
        } else {
            RuleExpression skuColorExpression = ((SimpleMappingBean)colorMapping).getExpression();
            String skuColor = expressionParser.parse(skuColorExpression, shopBean, user, null);
            skuFieldValue.setInputFieldValue(sku_colorField.getId(), skuColor);
            buildSkuResult.getColorCmsSkuPropMap().put(skuColor, cmsSkuProp);
        }
    }

    private Field buildSkuProp(Field skuField, ExpressionParser expressionParser, MappingBean skuMapping, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        List<CmsBtProductModel> sxProducts = expressionParser.getSxData().getProductList();
        buildSkuResult = new BuildSkuResult();

        ComplexMappingBean skuMappingComplex = (ComplexMappingBean) skuMapping;
        List<MappingBean> subMappingBeans = skuMappingComplex.getSubMappings();
        Map<String, Field> fieldMap = ((MultiComplexField)skuField).getFieldMap();

        Map<String, MappingBean> skuSubMappingMap = new HashMap<>();
        for (MappingBean mappingBean : subMappingBeans) {
            String propId = mappingBean.getPlatformPropId();
            skuSubMappingMap.put(propId, mappingBean);
        }

        List<ComplexValue> complexValues = new ArrayList<>();
        for (CmsBtProductModel sxProduct : sxProducts) {
            List<CmsBtProductModel_Sku> cmsSkuPropBeans = sxProduct.getCommon().getSkus();
            for (CmsBtProductModel_Sku cmsSkuProp : cmsSkuPropBeans) {
                //CmsBtProductModel_Sku 是Map<String, Object>的子类
                expressionParser.setSkuPropContext(cmsSkuProp);
                ComplexValue skuFieldValue = new ComplexValue();
                complexValues.add(skuFieldValue);

                buildSkuColor(skuFieldValue, expressionParser, skuSubMappingMap.get(sku_colorField.getId()), cmsSkuProp, shopBean, user);

                for (MappingBean mappingBean : skuMappingComplex.getSubMappings()) {
                    String propId = mappingBean.getPlatformPropId();
                    // add by morse.lu 2016/05/15 start
                    // target店上新临时添加写死用
                    if ("hscode".equals(propId)) {
                        RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
                        String propValue = expressionParser.parse(ruleExpression, shopBean, user, null); // "0410004300, 戒指 ,对" 或者  "0410004300, 戒指 ,只"
                        skuFieldValue.setInputFieldValue(propId, propValue.split(",")[0]);
//                        skuFieldValue.setInputFieldValue(propId, "0410004300");
                        continue;
                    }
                    // add by morse.lu 2016/05/15 end
                    if (propId.equals(sku_colorField.getId())) {
                        continue;
                    } else if (propId.equals(sku_quantityField.getId())) {
                        int skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
                        skuFieldValue.setInputFieldValue(propId, String.valueOf(skuQuantity));
                    } else {
                        RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
                        String propValue = expressionParser.parse(ruleExpression, shopBean, user, null);
                        Field subField = fieldMap.get(propId);
                        if (subField.getType() == FieldTypeEnum.INPUT) {
                            skuFieldValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
                        } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
                            skuFieldValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
                        }
                    }
                }
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
            expressionParser.setSkuPropContext(cmsSkuProp);

            ComplexValue complexValue = new ComplexValue();

            if (colorExtend_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(colorExtend_colorField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(colorExtend_colorField.getId(), entry.getKey());
            }

            CmsBtProductModel sxProductBean = skuProductMap.get(cmsSkuProp);
            // tom 设置当前的产品内容 START TODO: 这段写的不是优雅, 之后看情况修改
            expressionParser.pushMasterPropContext(sxProductBean.getCommon().getFields());
            // tom 设置当前的产品内容 END TODO: 这段写的不是优雅, 之后看情况修改

            if (colorExtend_imageField != null) {
                String propImage = sxProductBean.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
                if (propImage != null && !"".equals(propImage)) {
                    if (StringUtils.isEmpty(getCodeImageTemplate())) {
                        $warn("图片模板url未设置");
                        complexValue.setInputFieldValue(colorExtend_imageField.getId(), null);
                    } else {
                        String codePropFullImageUrl = String.format(getCodeImageTemplate(), propImage);
//                        String codePropFullImageUrl = expressionParser.getSxProductService().getImageByTemplateId(sxData.getChannelId(), getCodeImageTemplate(), propImage);
//                    codePropFullImageUrl = expressionParser.getSxProductService().encodeImageUrl(codePropFullImageUrl);
                        complexValue.setInputFieldValue(colorExtend_imageField.getId(), codePropFullImageUrl);

                        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
                            Set<String> url = new HashSet<>();
                            url.add(codePropFullImageUrl);
                            // 上传图片到天猫图片空间
                            Map<String, String> retMap = expressionParser.getSxProductService().uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
//                            complexValue.setInputFieldValue(colorExtend_imageField.getId(), retMap.get(codePropFullImageUrl));
//                        } else {
//                            complexValue.setInputFieldValue(colorExtend_imageField.getId(), codePropFullImageUrl);
                        }
                    }
                }
            }

            for (MappingBean mappingBean : ((ComplexMappingBean)colorExtendMapping).getSubMappings()) {
                String propId = mappingBean.getPlatformPropId();
                if (propId.equals(colorExtend_colorField.getId())
                        || (colorExtend_imageField  != null && propId.equals(colorExtend_imageField.getId()))) {
                    continue;
                } else {
                    // added by morse.lu 2016/05/27 start
                    // TODO:一个产品多个sku时，暂定别名用skuCode，以外的场合还是用产品code(mapping表设定)
                    if (hasSomeSku && propId.equals(colorExtend_aliasnameField.getId())) {
                        // 别名
                        complexValue.setInputFieldValue(propId, cmsSkuProp.getSkuCode());
                        continue;
                    }
                    // added by morse.lu 2016/05/27 end
                    RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
                    String propValue = expressionParser.parse(ruleExpression, shopBean, user, null);
                    Field subField = fieldMap.get(propId);
                    if (subField.getType() == FieldTypeEnum.INPUT) {
                        complexValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
                    } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
                        complexValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
                    }
                }
            }

            // tom 释放 START TODO: 这段写的不是优雅, 之后看情况修改
            expressionParser.popMasterPropContext();
            // tom 释放 END TODO: 这段写的不是优雅, 之后看情况修改

            complexValues.add(complexValue);
        }
        ((MultiComplexField)colorExtendField).setComplexValues(complexValues);
        return colorExtendField;
    }

    @Override
    public List<Field> buildSkuInfoFieldChild(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        List<Field> skuInfoFields = new ArrayList<>();

        MappingBean skuMappingBean = null;
        MappingBean colorExtendMappingBean = null;

        List<MappingBean> mappingBeenList = cmsMtPlatformMappingModel.getProps();

        for (MappingBean mappingBean : mappingBeenList) {
            if (mappingBean.getPlatformPropId().equals(skuField.getId())) {
                skuMappingBean = mappingBean;
            }
            if (colorExtendField != null && mappingBean.getPlatformPropId().equals(colorExtendField.getId())) {
                colorExtendMappingBean = mappingBean;
            }
        }

        skuField = buildSkuProp(skuField, expressionParser, skuMappingBean, skuInventoryMap, shopBean, user);
        skuInfoFields.add(skuField);

        if (colorExtendField != null) {
            Field colorExtendField = buildColorExtendProp(expressionParser, colorExtendMappingBean, shopBean, user);
            skuInfoFields.add(colorExtendField);
        }

        return skuInfoFields;
    }
}
