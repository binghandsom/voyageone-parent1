package com.voyageone.service.impl.cms.sx.sku_field.tmall;

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
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.sx.sku_field.AbstractSkuFieldBuilder;
import com.voyageone.service.model.cms.CmsMtPlatformPropSkuModel;
import com.voyageone.service.model.cms.constants.SkuTemplateConstants;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by morse.lu 2016/05/06 (copy and modified from task2 / TmallGjSkuFieldBuilderImpl_1)
 * 参考天猫分类: 121484030  珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰
 * 线程安全: 是
 */
public class TmallGjSkuFieldBuilderImpl2 extends AbstractSkuFieldBuilder {

    private final static int TPL_INDEX = 2;

    private Field skuField;
    private Field skuExtendField;

    private Field sku_sizeField;
    private Field sku_priceField;
    private Field sku_quantityField;
    private Field sku_outerIdField;
    private Field sku_barCodeField;

    private Field skuExtend_aliasnameField;
    private Field skuExtend_sizeField;

    // added by morse.lu 2016/07/04 start
    private Field hscodeField;
    // added by morse.lu 2016/07/04 end

    private int availableSizeIndex = 0;

    private BuildSkuResult buildSkuResult;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsBtProductModel_Sku> sizeCmsSkuPropMap;
        // added by morse.lu 2016/07/07 start
        Map<CmsBtProductModel_Sku, CmsBtProductModel> skuProductMap;
        // added by morse.lu 2016/07/07 end

        public BuildSkuResult() {
            sizeCmsSkuPropMap = new HashMap<>();
            // added by morse.lu 2016/07/07 start
            skuProductMap = new HashMap<>();
            // added by morse.lu 2016/07/07 end
        }

        public Map<String, CmsBtProductModel_Sku> getSizeCmsSkuPropMap() {
            return sizeCmsSkuPropMap;
        }

        // added by morse.lu 2016/07/07 start
        public Map<CmsBtProductModel_Sku, CmsBtProductModel> getSkuProductMap() {
            return skuProductMap;
        }
        // added by morse.lu 2016/07/07 end
    }

    @Override
    protected boolean init(List<Field> platformProps, int cartId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("cartId", cartId);
        searchMap.put("template", TPL_INDEX); // 模板2
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
                if (type.intValue() == SkuTemplateConstants.SKU_SIZE) {
                    sku_sizeField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_BARCODE) {
                    sku_barCodeField = platformProp;
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

                //EXTENDSIZE
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE) {
                    skuExtendField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_SIZE) {
                    skuExtend_sizeField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_ALIASNAME) {
                    skuExtend_aliasnameField = platformProp;
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
        if (sku_sizeField == null) {
            $warn(this.getClass().getName() + " requires sku size!");
            return false;
        }

        return true;
    }

    // modified by morse.lu 2016/07/04 start
    // 不用Mapping了
//    private void buildSkuSize(ComplexValue skuFieldValue, ExpressionParser expressionParser, CmsBtProductModel_Sku cmsSkuProp, MappingBean sizeMapping, ShopBean shopBean, String user) throws Exception {
    private void buildSkuSize(ComplexValue skuFieldValue, CmsBtProductModel productModel, CmsBtProductModel_Sku cmsSkuProp) throws Exception {
        if (sku_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> sizeOptions = ((SingleCheckField)sku_sizeField).getOptions();
            String sizeValue = sizeOptions.get(availableSizeIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_sizeField.getId(), new Value(sizeValue));
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
        } else {
            // modified by morse.lu 2016/07/04 start
            // 不用Mapping了
//            RuleExpression skuSizeExpression = ((SimpleMappingBean)sizeMapping).getExpression();
//            String skuSize = expressionParser.parse(skuSizeExpression, shopBean, user, null);
            String skuSize = getSkuValue(productModel, sku_sizeField.getId(), cmsSkuProp.getSkuCode());
            // modified by morse.lu 2016/07/04 end
            skuFieldValue.setInputFieldValue(sku_sizeField.getId(), skuSize);
            buildSkuResult.getSizeCmsSkuPropMap().put(skuSize, cmsSkuProp);
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

        List<ComplexValue> complexValues = new ArrayList<>();
        for (CmsBtProductModel sxProduct : sxProducts) {
            List<CmsBtProductModel_Sku> cmsSkuPropBeans = sxProduct.getCommon().getSkus();
            for (CmsBtProductModel_Sku cmsSkuProp : cmsSkuPropBeans) {
                // added by morse.lu 2016/07/07 start
                buildSkuResult.getSkuProductMap().put(cmsSkuProp, sxProduct);
                // added by morse.lu 2016/07/07 end
                //CmsBtProductModel_Sku 是Map<String, Object>的子类
                // deleted by morse.lu 2016/07/04 start
                // 不Mapping了,不用了吧,虽然留着也不影响,但是set的对象不对,应该是common下的skus + 各平台下面的skus
//                expressionParser.setSkuPropContext(cmsSkuProp);
                // deleted by morse.lu 2016/07/04 end
                ComplexValue skuFieldValue = new ComplexValue();
                complexValues.add(skuFieldValue);

                // modified by morse.lu 2016/07/04 start
                // 不用Mapping了
//                buildSkuSize(skuFieldValue, expressionParser, cmsSkuProp, skuSubMappingMap.get(sku_sizeField.getId()), shopBean, user);
                buildSkuSize(skuFieldValue, sxProduct, cmsSkuProp);
                // modified by morse.lu 2016/07/04 end

//                {
//                    List<Field> fList = ((MultiComplexField) skuField).getFields();
//                    for (Field fff : fList) {
//                        if (fff.getId().equals("in_prop_161712509")) {
//                            skuFieldValue.setInputFieldValue("in_prop_161712509", "0克拉");
//                            break;
//                        }
//                    }
//                }

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
//                    if (propId.equals(sku_sizeField.getId())) {
//                        continue;
//                    } else if (propId.equals(sku_quantityField.getId())) {
//                        int skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
//                        skuFieldValue.setInputFieldValue(propId, String.valueOf(skuQuantity));
//                    } else {
//                        RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                        String propValue = expressionParser.parse(ruleExpression, shopBean, user, null);
//                        if (propValue == null) {
//                            continue;
//                        }
//                        Field subField = fieldMap.get(propId);
//                        // 这里的程序有问题的, 没考虑到mapping里的platform属性减少了的情况. 临时加入一个判断避免一下
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
                    if (fieldId.equals(sku_sizeField.getId())) {
                        continue;
                    }
                    if (sku_barCodeField != null && fieldId.equals(sku_barCodeField.getId())) {
                        skuFieldValue.setInputFieldValue(sku_barCodeField.getId(), sxProduct.getCommon().getSku(cmsSkuProp.getSkuCode()).getBarcode());
                        continue;
                    }
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
                        skuFieldValue.setInputFieldValue(hscodeField.getId(), propValue.split(",")[0]);
                        continue;
                    }

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

    private Field buildSizeExtendProp(ExpressionParser expressionParser, MappingBean sizeExtendMapping, ShopBean shopBean, String user) throws Exception {
        Map<String, Field> fieldMap = ((MultiComplexField)skuExtendField).getFieldMap();

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsBtProductModel_Sku> entry : buildSkuResult.getSizeCmsSkuPropMap().entrySet())
        {
            CmsBtProductModel_Sku cmsSkuProp = entry.getValue();
            // deleted by morse.lu 2016/07/04 start
            // 不Mapping了,不用了吧,虽然留着也不影响,但是set的对象不对,应该是common下的skus + 各平台下面的skus
//            // tom 设置当前的父级内容 START TODO: 这段写的不是优雅, 之后看情况修改
//            Map<String, Object> masterWordEvaluationContext = entry.getValue();
//            expressionParser.pushMasterPropContext(masterWordEvaluationContext);
//            // tom 设置当前的父级内容 END TODO: 这段写的不是优雅, 之后看情况修改
//
//            expressionParser.setSkuPropContext(entry.getValue());
            // deleted by morse.lu 2016/07/04 end
            ComplexValue complexValue = new ComplexValue();

            if (skuExtend_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(skuExtend_sizeField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(skuExtend_sizeField.getId(), entry.getKey());
            }

            // modified by morse.lu 2016/07/04 start
            // 不用Mapping了
//            for (MappingBean mappingBean : ((ComplexMappingBean)sizeExtendMapping).getSubMappings()) {
//                String propId = mappingBean.getPlatformPropId();
//                if (propId.equals(skuExtend_sizeField.getId())) {
//                    continue;
//                } else {
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
            if (skuExtend_aliasnameField != null) {
                // 别名,用size
                // modified by morse.lu 2016/07/07 start
                // 先看看sizeNick有没有值，有的话直接用，没的话用size去尺码表里转换一下
//                complexValue.setInputFieldValue(skuExtend_aliasnameField.getId(), cmsSkuProp.getSize());
                String size = cmsSkuProp.getSizeNick();
                if (StringUtils.isEmpty(size)) {
                    size = getChangeSize(expressionParser, buildSkuResult.getSkuProductMap().get(cmsSkuProp), cmsSkuProp.getSize());
                }
                complexValue.setInputFieldValue(skuExtend_aliasnameField.getId(), size);
                // modified by morse.lu 2016/07/07 end
            }
            // modified by morse.lu 2016/07/04 end

            complexValues.add(complexValue);
        }

        ((MultiComplexField)skuExtendField).setComplexValues(complexValues);
        return skuExtendField;
    }

    @Override
    public List<Field> buildSkuInfoFieldChild(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        List<Field> skuInfoFields = new ArrayList<>();

        MappingBean skuMappingBean = null;
        MappingBean skuExtendMappingBean = null;

        // deleted by morse.lu 2016/07/04 start
        // 这一版不用Mapping啦,方法入参先留着,以防以后有特殊的需要Mapping的属性
//        List<MappingBean> mappingBeenList = cmsMtPlatformMappingModel.getProps();
//
//        for (MappingBean mappingBean : mappingBeenList) {
//            if (mappingBean.getPlatformPropId().equals(skuField.getId())) {
//                skuMappingBean = mappingBean;
//            }
//            if (skuExtendField != null && mappingBean.getPlatformPropId().equals(skuExtendField.getId())) {
//                skuExtendMappingBean = mappingBean;
//            }
//        }
        // deleted by morse.lu 2016/07/04 end

        skuField = buildSkuProp(skuField, expressionParser, skuMappingBean, skuInventoryMap, shopBean, user);
        skuInfoFields.add(skuField);

        if (skuExtendField != null) {
            Field skuExtendField = buildSizeExtendProp(expressionParser, skuExtendMappingBean, shopBean, user);
            skuInfoFields.add(skuExtendField);
        }

        return skuInfoFields;
    }
}
