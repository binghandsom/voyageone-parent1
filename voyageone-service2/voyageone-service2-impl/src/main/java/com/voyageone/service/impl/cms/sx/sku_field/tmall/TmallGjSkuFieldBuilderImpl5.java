package com.voyageone.service.impl.cms.sx.sku_field.tmall;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.MappingBean;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.sx.sku_field.AbstractSkuFieldBuilder;
import com.voyageone.service.model.cms.CmsMtPlatformPropSkuModel;
import com.voyageone.service.model.cms.constants.SkuTemplateConstants;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 既没有颜色也没有尺码
 * 参考天猫分类: 50009859  咖啡/麦片/冲饮>藕粉/麦片/冲饮品>冲饮麦片  口味
 * 线程安全: 是
 *
 * @author morse.lu 2016/06/08
 * @since 2.0.0
 */
public class TmallGjSkuFieldBuilderImpl5 extends AbstractSkuFieldBuilder {

    private final static int TPL_INDEX = 5;

    private Field skuField;
    private Field skuExtendField;

    private Field sku_masterField;
    private Field sku_priceField;
    private Field sku_quantityField;
    private Field sku_outerIdField;
    private Field sku_barCodeField;
    // added by morse.lu 2016/08/17 start
    private Field sku_skuIdField;
    private Field sku_productIdField;
    // added by morse.lu 2016/08/17 end

    private Field skuExtend_aliasnameField;
    private Field skuExtend_masterField;

    // added by morse.lu 2016/07/04 start
    private Field hscodeField;
    // added by morse.lu 2016/07/04 end

    private BuildSkuResult buildSkuResult;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsBtProductModel_Sku> cmsSkuPropMap;

        public BuildSkuResult() {
            cmsSkuPropMap = new HashMap<>();
        }

        public Map<String, CmsBtProductModel_Sku> getCmsSkuPropMap() {
            return cmsSkuPropMap;
        }
    }

    @Override
    protected boolean init(List<Field> platformProps, int cartId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("cartId", cartId);
        searchMap.put("template", TPL_INDEX); // 模板5
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
                if (type.intValue() == SkuTemplateConstants.SKU_MASTER) {
                    sku_masterField = platformProp;
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
                // added by morse.lu 2016/08/17 start
                if (type.intValue() == SkuTemplateConstants.SKU_ID) {
                    sku_skuIdField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_PRODUCTID) {
                    sku_productIdField = platformProp;
                }
                // added by morse.lu 2016/08/17 end

                //EXTENDMASTER
                if (type.intValue() == SkuTemplateConstants.EXTENDMASTER) {
                    skuExtendField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDMASTER_MASTER) {
                    skuExtend_masterField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDMASTER_ALIASNAME) {
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
        if (sku_masterField == null) {
            $warn(this.getClass().getName() + " requires sku master!");
            return false;
        }

        return true;
    }

    // modified by morse.lu 2016/07/04 start
    // 不用Mapping了
//    private void buildSkuMaster(ComplexValue skuFieldValue, ExpressionParser expressionParser, CmsBtProductModel_Sku cmsSkuProp, MappingBean mapping, ShopBean shopBean, String user) throws Exception {
//        RuleExpression skuExpression = ((SimpleMappingBean) mapping).getExpression();
//        String value = expressionParser.parse(skuExpression, shopBean, user, null);
    private void buildSkuMaster(ComplexValue skuFieldValue, CmsBtProductModel productModel, CmsBtProductModel_Sku cmsSkuProp) throws Exception {
        String value = getSkuValue(productModel, sku_masterField.getId(), cmsSkuProp.getSkuCode());
        // modified by morse.lu 2016/07/04 end
        if (sku_masterField.getType() == FieldTypeEnum.SINGLECHECK) {
            skuFieldValue.setSingleCheckFieldValue(sku_masterField.getId(), new Value(value));
        } else {
            skuFieldValue.setInputFieldValue(sku_masterField.getId(), value);
        }
        buildSkuResult.getCmsSkuPropMap().put(value, cmsSkuProp);
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
//                buildSkuMaster(skuFieldValue, expressionParser, cmsSkuProp, skuSubMappingMap.get(sku_masterField.getId()), shopBean, user);
                buildSkuMaster(skuFieldValue, sxProduct, cmsSkuProp);
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
//                    if (propId.equals(sku_masterField.getId())) {
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
                    if (fieldId.equals(sku_masterField.getId())) {
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
                        // modified by morse.lu 2017/07/31 start
                        // 对应 第一，第二计量单位和销售单位
                        // hscode||||清关要素||||计量单位|||销售单位
//                        skuFieldValue.setInputFieldValue(hscodeField.getId(), propValue.split(",")[0]);
                        if (!StringUtils.isEmpty(propValue)) {
                            skuFieldValue.setInputFieldValue(hscodeField.getId(),
                                    expressionParser.getSxProductService().constructTmHscode(sxProduct, propValue, shopBean));
                        }
                        // modified by morse.lu 2017/07/31 end
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
                                expressionParser.getSxData(),
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

    private Field buildMasterExtendProp(ExpressionParser expressionParser, MappingBean masterExtendMapping, ShopBean shopBean, String user) throws Exception {
        Map<String, Field> fieldMap = ((MultiComplexField)skuExtendField).getFieldMap();

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsBtProductModel_Sku> entry : buildSkuResult.getCmsSkuPropMap().entrySet())
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

            if (skuExtend_masterField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(skuExtend_masterField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(skuExtend_masterField.getId(), entry.getKey());
            }

            // modified by morse.lu 2016/07/04 start
            // 不用Mapping了
//            for (MappingBean mappingBean : ((ComplexMappingBean) masterExtendMapping).getSubMappings()) {
//                String propId = mappingBean.getPlatformPropId();
//                if (propId.equals(skuExtend_masterField.getId())) {
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
                // 别名,暂时没有碰到，理论上是没有的,先用code吧
                complexValue.setInputFieldValue(skuExtend_aliasnameField.getId(), cmsSkuProp.getSkuCode());
            }

            complexValues.add(complexValue);
        }

        ((MultiComplexField)skuExtendField).setComplexValues(complexValues);
        return skuExtendField;
    }

    @Override
    public List<Field> buildSkuInfoFieldChild(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
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
            Field skuExtendField = buildMasterExtendProp(expressionParser, skuExtendMappingBean, shopBean, user);
            skuInfoFields.add(skuExtendField);
        }

        return skuInfoFields;
    }
}
