package com.voyageone.task2.cms.service.putaway.tmall;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;
import com.taobao.top.schema.option.Option;
import com.taobao.top.schema.value.ComplexValue;
import com.taobao.top.schema.value.Value;
import com.voyageone.service.bean.cms.ComplexMappingBean;
import com.voyageone.service.bean.cms.MappingBean;
import com.voyageone.service.bean.cms.SimpleMappingBean;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.bean.PlatformUploadRunState;
import com.voyageone.task2.cms.bean.SkuTemplateSchema;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.TmallUploadRunState;
import com.voyageone.task2.cms.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.bean.tcb.TaskSignalType;
import com.voyageone.task2.cms.model.PlatformSkuInfoModel;
import com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.ims.rule_expression.RuleExpression;

import java.util.*;

/**
 * Created by Leo on 15-7-14.
 * 参考天猫分类: 50014993
 * 线程安全: 否
 */
public class TmallGjSkuFieldBuilderImpl_2 extends AbstractSkuFieldBuilder {
    private Field skuField;
    private Field colorExtendField;
    private Field skuExtendField;

    private Field sku_colorField;
    private Field sku_priceField;
    private Field sku_quantityField;
    private Field sku_outerIdField;
    private Field sku_barCodeField;
    private Field sku_sizeField;

    private Field colorExtend_aliasnameField;
    private Field colorExtend_colorField;
    private Field colorExtend_imageField;

    private Field skuExtend_aliasnameField;
    private Field skuExtend_sizeField;

    private int availableColorIndex = 0;

    private int availableSizeIndex = 0;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, SxProductBean> colorCmsPropductMap;
        Map<SxProductBean, String> cmsPropductColorMap;
        Map<String, CmsBtProductModel_Sku> sizeCmsSkuPropMap;

        public BuildSkuResult() {
            colorCmsPropductMap = new HashMap<>();
            cmsPropductColorMap = new HashMap<>();
            sizeCmsSkuPropMap = new HashMap<>();
        }

        public Map<SxProductBean, String> getCmsPropductColorMap() {
            return cmsPropductColorMap;
        }

        public void setCmsPropductColorMap(Map<SxProductBean, String> cmsPropductColorMap) {
            this.cmsPropductColorMap = cmsPropductColorMap;
        }

        public Map<String, SxProductBean> getColorCmsPropductMap() {
            return colorCmsPropductMap;
        }

        public Map<String, CmsBtProductModel_Sku> getSizeCmsSkuPropMap() {
            return sizeCmsSkuPropMap;
        }
    }

    private boolean init(List<Field> platformProps, int cartId) {
        for (Field field : platformProps) {
            if ("hscode".equals(field.getId())) {
                continue;
            }
            List<PlatformSkuInfoModel> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(field.getId(), cartId);

            PlatformSkuInfoModel tmallSkuInfo = null;
            for (PlatformSkuInfoModel tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                    break;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + field.getId() + ", cartId=" + cartId + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_2.class.getName() + " is not applicable!");
                return false;
            }

            long fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if ((fieldType < (1L << SkuTemplateSchema.SkuTemplate_2_Schema.FIELD_BIT_MIN))
                    || (fieldType > (1L << (SkuTemplateSchema.SkuTemplate_2_Schema.FIELD_BIT_MAX + 1L)))) {
                return false;
            }

            //SKU
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU)) {
                skuField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE)) {
                sku_sizeField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_BARCODE)) {
                sku_barCodeField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_COLOR)) {
                sku_colorField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_OUTERID)) {
                sku_outerIdField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_PRICE)) {
                sku_priceField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_QUANTITY)) {
                sku_quantityField = field;
            }

            //EXTENDCOLOR
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR)) {
                colorExtendField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_ALIASNAME)) {
                colorExtend_aliasnameField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_COLOR)) {
                colorExtend_colorField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_IMAGE)) {
                colorExtend_imageField = field;
            }

            //EXTENDSIZE
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE)) {
                skuExtendField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE)) {
                skuExtend_sizeField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_ALIASNAME)) {
                skuExtend_aliasnameField = field;
            }
        }
        if (skuField == null) {
            logger.warn(this.getClass().getName() + " requires sku!");
            return false;
        }

        if (sku_colorField == null) {
            logger.warn(this.getClass().getName() + " requires sku color!");
            return false;
        }
        if (sku_sizeField == null) {
            logger.warn(this.getClass().getName() + " requires sku size!");
            return false;
        }
        return true;
    }

    private void buildSkuColor(ComplexValue skuFieldValue, MappingBean colorMapping, BuildSkuResult buildSkuResult, SxProductBean sxProductBean) throws TaskSignal {
        String colorValue = buildSkuResult.getCmsPropductColorMap().get(sxProductBean);
            if (sku_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
                List<Option> colorOptions = ((SingleCheckField) sku_colorField).getOptions();
                if (colorValue == null) {
                    colorValue = colorOptions.get(availableColorIndex++).getValue();
                    buildSkuResult.getColorCmsPropductMap().put(colorValue, sxProductBean);
                    buildSkuResult.getCmsPropductColorMap().put(sxProductBean, colorValue);
                }
                skuFieldValue.setSingleCheckFieldValue(sku_colorField.getId(), new Value(colorValue));
            } else {
                if (colorValue == null) {
                    RuleExpression skuColorExpression = ((SimpleMappingBean) colorMapping).getExpression();
                    colorValue = expressionParser.parse(skuColorExpression, null);
                    buildSkuResult.getColorCmsPropductMap().put(colorValue, sxProductBean);
                    buildSkuResult.getCmsPropductColorMap().put(sxProductBean, colorValue);
                }
                skuFieldValue.setInputFieldValue(sku_colorField.getId(), colorValue);
            }
    }

//    private void buildSkuSize(ComplexValue skuFieldValue, CmsBtProductModel_Sku cmsSkuProp, BuildSkuResult buildSkuResult,  MappingBean sizeMapping) throws TaskSignal {
//        if (sku_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
//            List<Option> sizeOptions = ((SingleCheckField)sku_sizeField).getOptions();
//
//            String sizeValue = sizeOptions.get(availableSizeIndex++).getValue();
//            skuFieldValue.setSingleCheckFieldValue(sku_sizeField.getId(), new Value(sizeValue));
//            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
//        } else {
//            if (sizeMapping == null) {
//                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("You have to set sku size's mapping when it is a input"));
//            }
//            RuleExpression skuSizeExpression = ((SimpleMappingBean)sizeMapping).getExpression();
//            String skuSize = expressionParser.parse(skuSizeExpression, null);
//            skuFieldValue.setInputFieldValue(sku_sizeField.getId(), skuSize);
//            buildSkuResult.getSizeCmsSkuPropMap().put(skuSize, cmsSkuProp);
//        }
//    }
//
//    private Field buildSkuProp(Field skuField, List<SxProductBean> sxProducts, MappingBean skuMapping, Map<String, Integer> skuInventoryMap, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) throws TaskSignal {
//        BuildSkuResult buildSkuResult = new BuildSkuResult();
//        contextBuildCustomFields.setBuildSkuResult(buildSkuResult);
//
//        ComplexMappingBean skuMappingComplex = (ComplexMappingBean) skuMapping;
//        List<MappingBean> subMappingBeans = skuMappingComplex.getSubMappings();
//        Map<String, Field> fieldMap = ((MultiComplexField)skuField).getFieldMap();
//
//        Map<String, MappingBean> skuSubMappingMap = new HashMap<>();
//        for (MappingBean mappingBean : subMappingBeans) {
//            String propId = mappingBean.getPlatformPropId();
//            skuSubMappingMap.put(propId, mappingBean);
//        }
//
//        List<ComplexValue> complexValues = new ArrayList<>();
//        for (SxProductBean sxProduct : sxProducts) {
//            List<CmsBtProductModel_Sku> cmsSkuPropBeans = sxProduct.getCmsBtProductModel().getSkus();
//            for (CmsBtProductModel_Sku cmsSkuProp : cmsSkuPropBeans) {
//                //CmsBtProductModel_Sku 是Map<String, Object>的子类
//                expressionParser.setSkuPropContext(cmsSkuProp);
//                ComplexValue skuFieldValue = new ComplexValue();
//                complexValues.add(skuFieldValue);
//
//                buildSkuColor(skuFieldValue, skuSubMappingMap.get(sku_colorField.getId()), buildSkuResult, sxProduct);
//                buildSkuSize(skuFieldValue, cmsSkuProp, buildSkuResult, skuSubMappingMap.get(sku_sizeField.getId()));
//
//                for (MappingBean mappingBean : skuMappingComplex.getSubMappings()) {
//                    String propId = mappingBean.getPlatformPropId();
//                    if (propId.equals(sku_sizeField.getId())
//                            || propId.equals(sku_colorField.getId())) {
//                        continue;
//                    } else if (propId.equals(sku_quantityField.getId())) {
//                        Integer skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
//                        String skuQuantityStr = "0";
//                        if (skuQuantity != null) {
//                            skuQuantityStr = skuQuantity.toString();
//                        }
//                        skuFieldValue.setInputFieldValue(propId, skuQuantityStr);
//                    } else {
//                        RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                        String propValue = expressionParser.parse(ruleExpression, null);
//                        Field subField = fieldMap.get(propId);
//                        if (subField.getType() == FieldTypeEnum.INPUT) {
//                            skuFieldValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
//                        } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
//                            skuFieldValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
//                        }
//                    }
//                }
//            }
//        }
//
//        ((MultiComplexField)skuField).setComplexValues(complexValues);
//        return skuField;
//    }
//
//    private Field buildColorExtendProp(Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields, MappingBean colorExtendMapping, Set<String> imageSet) throws TaskSignal {
//        BuildSkuResult buildSkuResult = (BuildSkuResult) contextBuildCustomFields.getBuildSkuResult();
//        Map<String, Field> fieldMap = ((MultiComplexField)colorExtendField).getFieldMap();
//        Map<String, List<TmallUploadRunState.UrlStashEntity>> srcUrlStashEntityMap = ((TmallUploadRunState.TmallContextBuildFields)contextBuildCustomFields.getPlatformContextBuildFields()).getSrcUrlStashEntityMap();
//
//        List<ComplexValue> complexValues = new ArrayList<>();
//        CmsBtProductModel oldCmsBtProduct = expressionParser.getMasterWordCmsBtProduct();
//        for (Map.Entry<String, SxProductBean> entry : buildSkuResult.getColorCmsPropductMap().entrySet())
//        {
//            SxProductBean sxProductBean = entry.getValue();
//
//            expressionParser.setMasterWordCmsBtProduct(sxProductBean.getCmsBtProductModel());
//
//            ComplexValue complexValue = new ComplexValue();
//
//            if (colorExtend_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
//                complexValue.setSingleCheckFieldValue(colorExtend_colorField.getId(), new Value(entry.getKey()));
//            } else {
//                complexValue.setInputFieldValue(colorExtend_colorField.getId(), entry.getKey());
//            }
//
//            String propImage = sxProductBean.getCmsBtProductModel().getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
//            if (propImage != null && !"".equals(propImage)) {
//                String codePropFullImageUrl = UploadImageHandler.encodeImageUrl(String.format(codeImageTemplate, propImage));
//                complexValue.setInputFieldValue(colorExtend_imageField.getId(), codePropFullImageUrl);
//                imageSet.add(codePropFullImageUrl);
//                List<TmallUploadRunState.UrlStashEntity> stashEntities = srcUrlStashEntityMap.get(codePropFullImageUrl);
//                if (stashEntities == null) {
//                    stashEntities = new ArrayList<>();
//                    srcUrlStashEntityMap.put(codePropFullImageUrl, stashEntities);
//                }
//                stashEntities.add(new TmallUploadRunState.UrlStashEntity(colorExtend_imageField.getId(), complexValue));
//            }
//
//            for (MappingBean mappingBean : ((ComplexMappingBean)colorExtendMapping).getSubMappings()) {
//                String propId = mappingBean.getPlatformPropId();
//                if (propId.equals(colorExtend_colorField.getId())
//                        || (colorExtend_imageField  != null && propId.equals(colorExtend_imageField.getId()))) {
//                    continue;
//                } else {
//                    RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                    String propValue = expressionParser.parse(ruleExpression, null);
//                    Field subField = fieldMap.get(propId);
//                    if (subField.getType() == FieldTypeEnum.INPUT) {
//                        complexValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
//                    } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
//                        complexValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
//                    }
//                }
//            }
//
//            complexValues.add(complexValue);
//        }
//        expressionParser.setMasterWordCmsBtProduct(oldCmsBtProduct);
//        ((MultiComplexField)colorExtendField).setComplexValues(complexValues);
//        return colorExtendField;
//    }
//
//    private Field buildSizeExtendProp(Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields, MappingBean sizeExtendMapping) throws TaskSignal {
//        BuildSkuResult buildSkuResult = (BuildSkuResult) contextBuildCustomFields.getBuildSkuResult();
//        Map<String, Field> fieldMap = ((MultiComplexField)skuExtendField).getFieldMap();
//
//        List<ComplexValue> complexValues = new ArrayList<>();
//        for (Map.Entry<String, CmsBtProductModel_Sku> entry : buildSkuResult.getSizeCmsSkuPropMap().entrySet())
//        {
//            ComplexValue complexValue = new ComplexValue();
//            expressionParser.setSkuPropContext(entry.getValue());
//
//            if (skuExtend_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
//                complexValue.setSingleCheckFieldValue(skuExtend_sizeField.getId(), new Value(entry.getKey()));
//            } else {
//                complexValue.setInputFieldValue(skuExtend_sizeField.getId(), entry.getKey());
//            }
//
//            for (MappingBean mappingBean : ((ComplexMappingBean)sizeExtendMapping).getSubMappings()) {
//                String propId = mappingBean.getPlatformPropId();
//                if (propId.equals(skuExtend_sizeField.getId())) {
//                    continue;
//                } else {
//                    RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                    String propValue = expressionParser.parse(ruleExpression, null);
//                    Field subField = fieldMap.get(propId);
//                    if (subField.getType() == FieldTypeEnum.INPUT) {
//                        complexValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
//                    } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
//                        complexValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
//                    }
//                }
//            }
//
//            complexValues.add(complexValue);
//        }
//
//        ((MultiComplexField)skuExtendField).setComplexValues(complexValues);
//        contextBuildCustomFields.getCustomFields().add(skuExtendField);
//        return skuExtendField;
//    }

    @Override
    public boolean isYourFood(List platformProps, int cartId) {
        return init(platformProps, cartId);
    }

    @Override
    public List<Field> buildSkuInfoField(int cartId, String categoryCode, List platformProps, List<SxProductBean> processSxProducts, Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) throws TaskSignal {
        init(platformProps, cartId);
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;

        List<Field> skuInfoFields = new ArrayList<>();

        MappingBean skuMappingBean = null;
        MappingBean colorExtendMappingBean = null;
        MappingBean skuExtendMappingBean = null;

        List<MappingBean> mappingBeenList = cmsMtPlatformMappingModel.getProps();

        for (MappingBean mappingBean : mappingBeenList) {
            if (mappingBean.getPlatformPropId().equals(skuField.getId())) {
                skuMappingBean = mappingBean;
            }
            if (mappingBean.getPlatformPropId().equals(colorExtendField.getId())) {
                colorExtendMappingBean = mappingBean;
            }
            if (skuExtendField != null && mappingBean.getPlatformPropId().equals(skuExtendField.getId())) {
                skuExtendMappingBean = mappingBean;
            }
        }

//        skuField = buildSkuProp(skuField, processSxProducts, skuMappingBean, skuInventoryMap, tmallContextBuildCustomFields);
        skuInfoFields.add(skuField);

        if (colorExtendField != null) {
//            Field colorExtendField = buildColorExtendProp(skuProductMap, tmallContextBuildCustomFields, colorExtendMappingBean, imageSet);
            skuInfoFields.add(colorExtendField);
        }
        if (skuExtendField != null) {
//            Field skuExtendField = buildSizeExtendProp(skuProductMap, tmallContextBuildCustomFields, skuExtendMappingBean);
            skuInfoFields.add(skuExtendField);
        }
        return skuInfoFields;
    }
}