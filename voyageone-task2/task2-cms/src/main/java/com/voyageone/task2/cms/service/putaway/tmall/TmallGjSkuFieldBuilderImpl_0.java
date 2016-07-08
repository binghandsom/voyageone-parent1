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
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.bean.PlatformUploadRunState;
import com.voyageone.task2.cms.bean.SkuTemplateSchema;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.TmallUploadRunState;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.model.PlatformSkuInfoModel;
import com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.ims.rule_expression.RuleExpression;

import java.util.*;

/**
 * Created by Leo on 15-7-14.
 * 线程安全: 否
 */
public class TmallGjSkuFieldBuilderImpl_0 extends AbstractSkuFieldBuilder {

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

    private boolean init(List<Field> fields, int cartId) {
        for (Field field : fields) {
            if ("hscode".equals(field.getId())) {
                continue;
            }
            List<PlatformSkuInfoModel> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(field.getId(), cartId);

            PlatformSkuInfoModel tmallSkuInfo = null;
            for (PlatformSkuInfoModel tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                    break;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + field.getId() + ", cartId=" + cartId + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_0.class.getName() + " is not applicable!");
                return false;
            }

            long fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if ((fieldType < (1L << SkuTemplateSchema.SkuTemplate_0_Schema.FIELD_BIT_MIN))
                    || (fieldType > (1L << (SkuTemplateSchema.SkuTemplate_0_Schema.FIELD_BIT_MAX + 1L)))) {
                return false;
            }

            //SKU
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.SKU)) {
                skuField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_BARCODE)) {
                sku_barCodeField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_COLOR)) {
                sku_colorField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_OUTERID)) {
                sku_outerIdField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_PRICE)) {
                sku_priceField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_QUANTITY)) {
                sku_quantityField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_MARKET_TIME)) {
                sku_market_timeField = field;
            }

            //EXTENDCOLOR
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR)) {
                colorExtendField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_ALIASNAME)) {
                colorExtend_aliasnameField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_COLOR)) {
                colorExtend_colorField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_IMAGE)) {
                colorExtend_imageField = field;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_BASECOLOR)) {
                colorExtend_basecolorField = field;
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
        return true;
    }

    private void buildSkuColor(ComplexValue skuFieldValue, MappingBean colorMapping, BuildSkuResult buildSkuResult, CmsBtProductModel_Sku cmsSkuProp) throws TaskSignal {
        if (sku_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> colorOptions = ((SingleCheckField)sku_colorField).getOptions();
            String colorValue = colorOptions.get(availableColorIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_colorField.getId(), new Value(colorValue));
            buildSkuResult.getColorCmsSkuPropMap().put(colorValue, cmsSkuProp);
        } else {
            RuleExpression skuColorExpression = ((SimpleMappingBean)colorMapping).getExpression();
            String skuColor = expressionParser.parse(skuColorExpression, null);
            skuFieldValue.setInputFieldValue(sku_colorField.getId(), skuColor);
            buildSkuResult.getColorCmsSkuPropMap().put(skuColor, cmsSkuProp);
        }
    }

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
//                buildSkuColor(skuFieldValue, skuSubMappingMap.get(sku_colorField.getId()), buildSkuResult, cmsSkuProp);
//
//                for (MappingBean mappingBean : skuMappingComplex.getSubMappings()) {
//                    String propId = mappingBean.getPlatformPropId();
//                    if (propId.equals(sku_colorField.getId())) {
//                        continue;
//                    } else if (propId.equals(sku_quantityField.getId())) {
//                        int skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
//                        skuFieldValue.setInputFieldValue(propId, String.valueOf(skuQuantity));
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
//        for (Map.Entry<String, CmsBtProductModel_Sku> entry : buildSkuResult.getColorCmsSkuPropMap().entrySet())
//        {
//            CmsBtProductModel_Sku cmsSkuProp = entry.getValue();
//            expressionParser.setSkuPropContext(cmsSkuProp);
//
//            ComplexValue complexValue = new ComplexValue();
//
//            if (colorExtend_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
//                complexValue.setSingleCheckFieldValue(colorExtend_colorField.getId(), new Value(entry.getKey()));
//            } else {
//                complexValue.setInputFieldValue(colorExtend_colorField.getId(), entry.getKey());
//            }
//
//            SxProductBean sxProductBean = skuProductMap.get(cmsSkuProp);
//
//            String propImage = sxProductBean.getCmsBtProductModel().getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
//            if (propImage != null && !"".equals(propImage)) {
//                String codePropFullImageUrl = UploadImageHandler.encodeImageUrl(String.format(codeImageTemplate, propImage));
//                complexValue.setInputFieldValue(colorExtend_imageField.getId(), codePropFullImageUrl);
//                imageSet.add(codePropFullImageUrl);
//
//                List<TmallUploadRunState.UrlStashEntity> stashEntities = srcUrlStashEntityMap.get(codePropFullImageUrl);
//                if (stashEntities == null) {
//                    stashEntities = new ArrayList<>();
//                    srcUrlStashEntityMap.put(codePropFullImageUrl, stashEntities);
//                }
//                stashEntities.add(new TmallUploadRunState.UrlStashEntity(colorExtend_imageField.getId(), complexValue));
//                buildSkuResult.getCodeValueComplexValueMap().put(entry.getKey(), complexValue);
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
//        ((MultiComplexField)colorExtendField).setComplexValues(complexValues);
//        return colorExtendField;
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

        List<MappingBean> mappingBeenList = cmsMtPlatformMappingModel.getProps();

        for (MappingBean mappingBean : mappingBeenList) {
            if (mappingBean.getPlatformPropId().equals(skuField.getId())) {
                skuMappingBean = mappingBean;
            }
            if (mappingBean.getPlatformPropId().equals(colorExtendField.getId())) {
                colorExtendMappingBean = mappingBean;
            }
        }
//        Field skuField = buildSkuProp(this.skuField, processSxProducts, skuMappingBean, skuInventoryMap, tmallContextBuildCustomFields);

        if (colorExtendField != null) {
//            Field colorExtendField = buildColorExtendProp(skuProductMap, tmallContextBuildCustomFields, colorExtendMappingBean, imageSet);
            skuInfoFields.add(colorExtendField);
        }

        skuInfoFields.add(skuField);
        return skuInfoFields;
    }
}
