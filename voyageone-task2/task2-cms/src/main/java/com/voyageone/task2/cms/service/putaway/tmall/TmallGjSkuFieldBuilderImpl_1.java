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
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.bean.PlatformUploadRunState;
import com.voyageone.task2.cms.bean.SkuTemplateSchema;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.TmallUploadRunState;
import com.voyageone.task2.cms.model.PlatformSkuInfoModel;
import com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder;
import com.voyageone.ims.rule_expression.RuleExpression;

import java.util.*;

/**
 * Created by Leo on 15-7-14.
 * 参考天猫分类: 121484030  珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰
 * 线程安全: 是
 */
public class TmallGjSkuFieldBuilderImpl_1 extends AbstractSkuFieldBuilder {
    private Field skuField;
    private Field skuExtendField;

    private Field sku_sizeField;
    private Field sku_priceField;
    private Field sku_quantityField;
    private Field sku_outerIdField;
    private Field sku_barCodeField;

    private Field skuExtend_aliasnameField;
    private Field skuExtend_sizeField;

    private int availableSizeIndex = 0;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsBtProductModel_Sku> sizeCmsSkuPropMap;


        public BuildSkuResult() {
            sizeCmsSkuPropMap = new HashMap<>();
        }

        public Map<String, CmsBtProductModel_Sku> getSizeCmsSkuPropMap() {
            return sizeCmsSkuPropMap;
        }

    }

    private boolean init(List<Field> platformProps, int cartId) {
        for (Field platformProp : platformProps) {
            if ("hscode".equals(platformProp.getId())) {
                continue;
            }
            List<PlatformSkuInfoModel> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(platformProp.getId(), cartId);

            PlatformSkuInfoModel tmallSkuInfo = null;
            for (PlatformSkuInfoModel tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                    break;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + platformProp.getId() + ", cartId=" + cartId + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_1.class.getName() + " is not applicable!");
                return false;
            }

            long fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if ((fieldType < (1L << (SkuTemplateSchema.SkuTemplate_1_Schema.FIELD_BIT_MIN)))
                    || (fieldType > (1L << (SkuTemplateSchema.SkuTemplate_1_Schema.FIELD_BIT_MAX + 1L)))) {
                return false;
            }

            //SKU
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU)) {
                skuField = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE)) {
                sku_sizeField = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_BARCODE)) {
                sku_barCodeField = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_OUTERID)) {
                sku_outerIdField = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_PRICE)) {
                sku_priceField = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_QUANTITY)) {
                sku_quantityField = platformProp;
            }

            //EXTENDSIZE
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSIZE)) {
                skuExtendField = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_SIZE)) {
                skuExtend_sizeField = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_ALIAS_NAME)) {
                skuExtend_aliasnameField = platformProp;
            }
        }
        if (skuField == null) {
            logger.warn(this.getClass().getName() + " requires sku!");
            return false;
        }
        if (sku_sizeField == null) {
            logger.warn(this.getClass().getName() + " requires sku size!");
            return false;
        }

        return true;
    }

    private void buildSkuSize(ComplexValue skuFieldValue, CmsBtProductModel_Sku cmsSkuProp, BuildSkuResult buildSkuResult,  MappingBean sizeMapping) {
        if (sku_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> sizeOptions = ((SingleCheckField)sku_sizeField).getOptions();

            String sizeValue = sizeOptions.get(availableSizeIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_sizeField.getId(), new Value(sizeValue));
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
        } else {
            RuleExpression skuSizeExpression = ((SimpleMappingBean)sizeMapping).getExpression();
            String skuSize = expressionParser.parse(skuSizeExpression, null);
            skuFieldValue.setInputFieldValue(sku_sizeField.getId(), skuSize);
            buildSkuResult.getSizeCmsSkuPropMap().put(skuSize, cmsSkuProp);
        }
    }

    private Field buildSkuProp(Field skuField, List<SxProductBean> sxProducts, MappingBean skuMapping, Map<String, Integer> skuInventoryMap, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        BuildSkuResult buildSkuResult = new BuildSkuResult();
        contextBuildCustomFields.setBuildSkuResult(buildSkuResult);

        ComplexMappingBean skuMappingComplex = (ComplexMappingBean) skuMapping;
        List<MappingBean> subMappingBeans = skuMappingComplex.getSubMappings();
        Map<String, Field> fieldMap = ((MultiComplexField)skuField).getFieldMap();

        Map<String, MappingBean> skuSubMappingMap = new HashMap<>();
        for (MappingBean mappingBean : subMappingBeans) {
            String propId = mappingBean.getPlatformPropId();
            skuSubMappingMap.put(propId, mappingBean);
        }

        List<ComplexValue> complexValues = new ArrayList<>();
        for (SxProductBean sxProduct : sxProducts) {
            List<CmsBtProductModel_Sku> cmsSkuPropBeans = sxProduct.getCmsBtProductModel().getSkus();
            for (CmsBtProductModel_Sku cmsSkuProp : cmsSkuPropBeans) {
                //CmsBtProductModel_Sku 是Map<String, Object>的子类
                expressionParser.setSkuPropContext(cmsSkuProp);
                ComplexValue skuFieldValue = new ComplexValue();
                complexValues.add(skuFieldValue);

                buildSkuSize(skuFieldValue, cmsSkuProp, buildSkuResult, skuSubMappingMap.get(sku_sizeField.getId()));

                // (由于天猫突然增加属性并要求六个小时内刷完入关方式, 临时写死) 现在取消写死的内容, 由运营自己填写 tom 20160424 START
//                {
//                    List<Field> fList = ((MultiComplexField) skuField).getFieldList();
//                    for (Field fff : fList) {
//                        if (fff.getId().equals("in_prop_161712509")) {
//                            skuFieldValue.setInputFieldValue("in_prop_161712509", "0克拉");
//                            break;
//                        }
//                    }
//                }
                // (由于天猫突然增加属性并要求六个小时内刷完入关方式, 临时写死) 现在取消写死的内容, 由运营自己填写 tom 20160424 END

                for (MappingBean mappingBean : skuMappingComplex.getSubMappings()) {
                    String propId = mappingBean.getPlatformPropId();
                    if (propId.equals(sku_sizeField.getId())) {
                        continue;
                    } else if (propId.equals(sku_quantityField.getId())) {
                        int skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
                        skuFieldValue.setInputFieldValue(propId, String.valueOf(skuQuantity));
                    } else {
                        RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
                        String propValue = expressionParser.parse(ruleExpression, null);
                        if (propValue == null) {
                            continue;
                        }
                        Field subField = fieldMap.get(propId);
                        // 这里的程序有问题的, 没考虑到mapping里的platform属性减少了的情况. 临时加入一个判断避免一下
                        if (subField != null) {
                            if (subField.getType() == FieldTypeEnum.INPUT) {
                                skuFieldValue.setInputFieldValue(mappingBean.getPlatformPropId(), propValue);
                            } else if (subField.getType() == FieldTypeEnum.SINGLECHECK) {
                                skuFieldValue.setSingleCheckFieldValue(mappingBean.getPlatformPropId(), new Value(propValue));
                            }
                        }
                    }
                }
            }
        }

        ((MultiComplexField)skuField).setComplexValues(complexValues);
        contextBuildCustomFields.getCustomFields().add(skuField);
        return skuField;
    }

    private Field buildSizeExtendProp(Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields, MappingBean sizeExtendMapping) {
        BuildSkuResult buildSkuResult = (BuildSkuResult) contextBuildCustomFields.getBuildSkuResult();
        Map<String, Field> fieldMap = ((MultiComplexField)skuExtendField).getFieldMap();

        // tom 设置当前的父级内容 START TODO: 这段写的不是优雅, 之后看情况修改
//        List<Object> masterWordEvaluationContextList = (List<Object>)(expressionParser.getMasterWordCmsBtProduct().getFields().get("prop_extend_9066257"));
//        Map<String, Object> masterWordEvaluationContext = (Map<String, Object>)(masterWordEvaluationContextList.get(0));
        // tom 设置当前的父级内容 END TODO: 这段写的不是优雅, 之后看情况修改

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsBtProductModel_Sku> entry : buildSkuResult.getSizeCmsSkuPropMap().entrySet())
        {
            // tom 设置当前的父级内容 START TODO: 这段写的不是优雅, 之后看情况修改
            Map<String, Object> masterWordEvaluationContext = entry.getValue();
            expressionParser.pushMasterPropContext(masterWordEvaluationContext);
            // tom 设置当前的父级内容 END TODO: 这段写的不是优雅, 之后看情况修改

            expressionParser.setSkuPropContext(entry.getValue());
            ComplexValue complexValue = new ComplexValue();

            if (skuExtend_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(skuExtend_sizeField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(skuExtend_sizeField.getId(), entry.getKey());
            }

            for (MappingBean mappingBean : ((ComplexMappingBean)sizeExtendMapping).getSubMappings()) {
                String propId = mappingBean.getPlatformPropId();
                if (propId.equals(skuExtend_sizeField.getId())) {
                    continue;
                } else {
                    RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
                    String propValue = expressionParser.parse(ruleExpression, null);
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

        // tom 释放 START TODO: 这段写的不是优雅, 之后看情况修改
//        expressionParser.popMasterPropContext();
        // tom 释放 END TODO: 这段写的不是优雅, 之后看情况修改

        ((MultiComplexField)skuExtendField).setComplexValues(complexValues);
        contextBuildCustomFields.getCustomFields().add(skuExtendField);
        return skuExtendField;
    }

    @Override
    public boolean isYourFood(List platformProps, int cartId) {
        return init(platformProps, cartId);
    }

    @Override
    public List<Field> buildSkuInfoField(int cartId, String categoryCode, List platformProps, List<SxProductBean> processSxProducts, Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        init(platformProps, cartId);
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;

        List<Field> skuInfoFields = new ArrayList<>();

        MappingBean skuMappingBean = null;
        MappingBean skuExtendMappingBean = null;

        List<MappingBean> mappingBeenList = cmsMtPlatformMappingModel.getProps();

        for (MappingBean mappingBean : mappingBeenList) {
            if (mappingBean.getPlatformPropId().equals(skuField.getId())) {
                skuMappingBean = mappingBean;
            }
            if (mappingBean.getPlatformPropId().equals(skuExtendField.getId())) {
                skuExtendMappingBean = mappingBean;
            }
        }

        skuField = buildSkuProp(skuField, processSxProducts, skuMappingBean, skuInventoryMap, tmallContextBuildCustomFields);
        skuInfoFields.add(skuField);

        if (skuExtendField != null) {
            Field skuExtendField = buildSizeExtendProp(skuProductMap, tmallContextBuildCustomFields, skuExtendMappingBean);
            skuInfoFields.add(skuExtendField);
        }
        return skuInfoFields;
    }
}
