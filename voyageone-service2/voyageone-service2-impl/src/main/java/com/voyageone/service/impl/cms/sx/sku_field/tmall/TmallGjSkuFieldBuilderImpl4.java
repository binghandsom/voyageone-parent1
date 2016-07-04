package com.voyageone.service.impl.cms.sx.sku_field.tmall;

import com.voyageone.base.exception.BusinessException;
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
import com.voyageone.service.model.cms.CmsMtChannelSkuConfigModel;
import com.voyageone.service.model.cms.CmsMtPlatformPropSkuModel;
import com.voyageone.service.model.cms.constants.SkuTemplateConstants;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by morse.lu 2016/05/06 (copy and modified from task2 / TmallGjSkuFieldBuilderImpl_3)
 * 参考天猫分类: 121412004  女装/女士精品>背心吊带
 * 线程安全: 否
 */
public class TmallGjSkuFieldBuilderImpl4 extends AbstractSkuFieldBuilder {

    private final static int TPL_INDEX = 4;

    private Field skuField;
    private Field colorExtendField;
    private Field skuExtendField;

    private Field sku_colorField;
    private Field sku_priceField;
    private Field sku_quantityField;
    private Field sku_outerIdField;
    private Field sku_barCodeField;

    private Field colorExtend_colorField;
    private Field colorExtend_imageField;

    private Field skuExtend_tipField;
    private Field skuExtend_shengaoField;
    private Field skuExtend_tizhongField;
    private Field skuExtend_jiankuanField;
    private Field skuExtend_xiongweiField;
    private Field skuExtend_yaoweiField;
    private Field skuExtend_xiuchangField;
    private Field skuExtend_beikuanField;
    private Field skuExtend_qianchangField;
    private Field skuExtend_baiweiField;
    private Field skuExtend_xiabaiweiField;
    private Field skuExtend_xiukouField;
    private Field skuExtend_xiufeiField;
    private Field skuExtend_zhongyaoField;
    private Field skuExtend_lingshenField;
    private Field skuExtend_linggaoField;
    private Field skuExtend_lingkuanField;
    private Field skuExtend_lingweiField;
    private Field skuExtend_yuanbaihouField;
    private Field skuExtend_yuanbaiField;
    private Field skuExtend_pingbaiyichangField;
    private Field skuExtend_yichangField;

    private Field sku_market_timeField;
    private Field skuExtend_sizeField;


    private int availableColorIndex = 0;

    Field sku_sizeField;
    private int availableSizeIndex = 0;

    private BuildSkuResult buildSkuResult;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsBtProductModel> colorCmsPropductMap;
        Map<String, CmsBtProductModel_Sku> sizeCmsSkuPropMap;
        Map<CmsBtProductModel, String> cmsPropductColorMap;

        public BuildSkuResult() {
            colorCmsPropductMap = new HashMap<>();
            cmsPropductColorMap = new HashMap<>();
            sizeCmsSkuPropMap = new HashMap<>();
        }

        public Map<CmsBtProductModel, String> getCmsPropductColorMap() {
            return cmsPropductColorMap;
        }

        public void setCmsPropductColorMap(Map<CmsBtProductModel, String> cmsPropductColorMap) {
            this.cmsPropductColorMap = cmsPropductColorMap;
        }

        public Map<String, CmsBtProductModel> getColorCmsPropductMap() {
            return colorCmsPropductMap;
        }

        public Map<String, CmsBtProductModel_Sku> getSizeCmsSkuPropMap() {
            return sizeCmsSkuPropMap;
        }
    }

    @Override
    protected boolean init(List<Field> fields, int cartId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("cartId", cartId);
        searchMap.put("template", TPL_INDEX); // 模板4
        List<CmsMtPlatformPropSkuModel> listTmallSkuInfos = cmsMtPlatformPropSkuDao.selectList(searchMap);

        // Map<prop_id, List<CmsMtPlatformPropSkuModel>>
        Map<String, List<CmsMtPlatformPropSkuModel>> mapTmallSkuInfos = listTmallSkuInfos.stream().collect(Collectors.groupingBy(model -> model.getPropId()));

        for (Field platformProp : fields) {
            if ("hscode".equals(platformProp.getId())) {
                continue;
            }
            if (isIgnore(platformProp.getId())) {
                $info("Ignore sku prop: " + platformProp.getId());
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
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_COLOR) {
                    colorExtend_colorField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_IMAGE) {
                    colorExtend_imageField = platformProp;
                }

                //EXTENDSIZE
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE) {
                    skuExtendField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_SIZE) {
                    skuExtend_sizeField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_BAIWEI) {
                    skuExtend_baiweiField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_BEIKUAN) {
                    skuExtend_beikuanField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_JIANKUAN) {
                    skuExtend_jiankuanField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_LINGGAO) {
                    skuExtend_linggaoField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_LINGKUAN) {
                    skuExtend_lingkuanField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_LINGSHEN) {
                    skuExtend_lingshenField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_LINGWEI) {
                    skuExtend_lingweiField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_PINGBAI) {
                    skuExtend_pingbaiyichangField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_QIANCHANG) {
                    skuExtend_qianchangField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_SHENGAO) {
                    skuExtend_shengaoField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_TIP) {
                    skuExtend_tipField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_TIZHONG) {
                    skuExtend_tizhongField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_XIABAIWEI) {
                    skuExtend_xiabaiweiField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_XIONGWEI) {
                    skuExtend_xiongweiField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_XIUCHANG) {
                    skuExtend_xiuchangField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_XIUFEI) {
                    skuExtend_xiufeiField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_XIUKOU) {
                    skuExtend_xiukouField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_YICHANG) {
                    skuExtend_yichangField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_YAOWEI) {
                    skuExtend_yaoweiField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_ZHONGYAO) {
                    skuExtend_zhongyaoField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_YUANBAIHOU) {
                    skuExtend_yuanbaihouField = platformProp;
                }

                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_YUANBAI) {
                    skuExtend_yuanbaiField = platformProp;
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
        if (sku_sizeField == null) {
            $warn(this.getClass().getName() + " requires sku size!");
            return false;
        }
        return true;
    }

    private void buildSkuColor(ComplexValue skuFieldValue, ExpressionParser expressionParser, MappingBean colorMapping, CmsBtProductModel sxProduct, ShopBean shopBean, String user) throws Exception {
        String colorValue = buildSkuResult.getCmsPropductColorMap().get(sxProduct);
        if (sku_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> colorOptions = ((SingleCheckField) sku_colorField).getOptions();
            if (colorValue == null) {
                colorValue = colorOptions.get(availableColorIndex++).getValue();
                buildSkuResult.getColorCmsPropductMap().put(colorValue, sxProduct);
                buildSkuResult.getCmsPropductColorMap().put(sxProduct, colorValue);
            }
            skuFieldValue.setSingleCheckFieldValue(sku_colorField.getId(), new Value(colorValue));
        } else {
            if (colorValue == null) {
                RuleExpression skuColorExpression = ((SimpleMappingBean) colorMapping).getExpression();
                colorValue = expressionParser.parse(skuColorExpression, shopBean, user, null);
                buildSkuResult.getColorCmsPropductMap().put(colorValue, sxProduct);
                buildSkuResult.getCmsPropductColorMap().put(sxProduct, colorValue);
            }
            skuFieldValue.setInputFieldValue(sku_colorField.getId(), colorValue);
        }
    }

    private void buildSkuSize(ComplexValue skuFieldValue, ExpressionParser expressionParser, CmsBtProductModel_Sku cmsSkuProp, MappingBean sizeMapping, ShopBean shopBean, String user) throws Exception {
        if (sku_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> sizeOptions = ((SingleCheckField)sku_sizeField).getOptions();
            String sizeValue = sizeOptions.get(availableSizeIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_sizeField.getId(), new Value(sizeValue));
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
        } else {
            if (sizeMapping == null) {
                throw new BusinessException("You have to set sku size's mapping when it is a input");
            }
            RuleExpression skuSizeExpression = ((SimpleMappingBean)sizeMapping).getExpression();
            String skuSize = expressionParser.parse(skuSizeExpression, shopBean, user, null);
            if (skuSize == null) {
                throw new BusinessException("sku size(" + sku_sizeField.getId() + ") can't be null");
            }
            skuFieldValue.setInputFieldValue(sku_sizeField.getId(), skuSize);
            buildSkuResult.getSizeCmsSkuPropMap().put(skuSize, cmsSkuProp);
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

                buildSkuColor(skuFieldValue, expressionParser, skuSubMappingMap.get(sku_colorField.getId()), sxProduct, shopBean, user);
                buildSkuSize(skuFieldValue, expressionParser, cmsSkuProp, skuSubMappingMap.get(sku_sizeField.getId()), shopBean, user);

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
                    if (propId.equals(sku_sizeField.getId())
                            || propId.equals(sku_colorField.getId())) {
                        continue;
                    } else if (propId.equals(sku_quantityField.getId())) {
                        Integer skuQuantity = skuInventoryMap.get(cmsSkuProp.getSkuCode());
                        String skuQuantityStr = "0";
                        if (skuQuantity != null) {
                            skuQuantityStr = skuQuantity.toString();
                        }
                        skuFieldValue.setInputFieldValue(propId, skuQuantityStr);
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
        Map<String, Field> fieldMap = ((MultiComplexField)colorExtendField).getFieldMap();

        List<ComplexValue> complexValues = new ArrayList<>();
        CmsBtProductModel oldCmsBtProduct = expressionParser.getMasterWordCmsBtProduct();
        for (Map.Entry<String, CmsBtProductModel> entry : buildSkuResult.getColorCmsPropductMap().entrySet())
        {
            CmsBtProductModel sxProduct = entry.getValue();

            expressionParser.setMasterWordCmsBtProduct(sxProduct);
            ComplexValue complexValue = new ComplexValue();

            if (colorExtend_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(colorExtend_colorField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(colorExtend_colorField.getId(), entry.getKey());
            }

            if (colorExtend_imageField != null) {
                String propImage = sxProduct.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
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

            complexValues.add(complexValue);
        }
        expressionParser.setMasterWordCmsBtProduct(oldCmsBtProduct);
        ((MultiComplexField)colorExtendField).setComplexValues(complexValues);
        return colorExtendField;
    }

    private Field buildSizeExtendProp(ExpressionParser expressionParser, MappingBean sizeExtendMapping, ShopBean shopBean, String user) throws Exception {
        SxData sxData = expressionParser.getSxData();
        Map<String, Field> fieldMap = ((MultiComplexField)skuExtendField).getFieldMap();

        // Map<custom_size_id, Map<custom_prop_id, custom_prop_value>>  (cms_mt_channel_sku_config)
        Map<String, Map<String, String>> allCustomSizePropMap = new HashMap<>();
        List<CmsMtChannelSkuConfigModel> allCustomSizeProps = cmsMtChannelSkuConfigDao.selectList(new HashMap<String, Object>(){{put("channelId", sxData.getChannelId());}});
        Map<String, String> customSizeNameIdMap = new HashMap<>();

        //构造两个map，一个是customSizeNameIdMap sizeName->sizeId, such as: L->1
        //另一个是allCustomSizePropMap, sizeId ->CustomSize属性map, such as: 1->[{shengao:170}, {tizhong:70}]
        for (CmsMtChannelSkuConfigModel customSizeProp : allCustomSizeProps) {
            String sizeId = customSizeProp.getCustomSizeId().toString();
            String propId = customSizeProp.getCustomPropId();
            String propValue = customSizeProp.getCustomPropValue();
            Map<String, String> eachCustomSizePropMap = allCustomSizePropMap.get(sizeId);

            if (eachCustomSizePropMap == null) {
                eachCustomSizePropMap = new HashMap<>();
                allCustomSizePropMap.put(sizeId, eachCustomSizePropMap);
            }
            eachCustomSizePropMap.put(propId, propValue);

            if (propId != null && propId.equals(skuExtend_sizeField.getId())) {
                customSizeNameIdMap.put(propValue, sizeId);
            }
            allCustomSizePropMap.put(sizeId, eachCustomSizePropMap);
        }

        List<ComplexValue> complexValues = new ArrayList<>();
        List<String> skipProps = new ArrayList<>();
        for (Map.Entry<String, CmsBtProductModel_Sku> entry : buildSkuResult.getSizeCmsSkuPropMap().entrySet())
        {
            expressionParser.setSkuPropContext(entry.getValue());
            ComplexValue complexValue = new ComplexValue();

            if (skuExtend_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(skuExtend_sizeField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(skuExtend_sizeField.getId(), entry.getKey());
            }
            skipProps.add(skuExtend_sizeField.getId());
            //TODO 从CUSTOM_SIZE表中读尺寸
            String skuSize = entry.getKey();
            String sizeId = customSizeNameIdMap.get(skuSize);
            if (sizeId == null) {
                $error("No customSize found for size:" + skuSize);
                return null;
            }
            Map<String, String> customSizePropMap = allCustomSizePropMap.get(sizeId);

            for (Map.Entry<String, String> customSizeEntry : customSizePropMap.entrySet())
            {
                skipProps.add(customSizeEntry.getKey());
                complexValue.setInputFieldValue(customSizeEntry.getKey(), customSizeEntry.getValue());
            }

            for (MappingBean mappingBean : ((ComplexMappingBean)sizeExtendMapping).getSubMappings()) {
                String propId = mappingBean.getPlatformPropId();
                if (skipProps.contains(propId) || isIgnore(propId)) {
                    continue;
                } else {
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
            skipProps.clear();

            complexValues.add(complexValue);
        }

        ((MultiComplexField)skuExtendField).setComplexValues(complexValues);
        return skuExtendField;
    }

    @Override
    public List<Field> buildSkuInfoFieldChild(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
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

        skuField = buildSkuProp(skuField, expressionParser, skuMappingBean, skuInventoryMap, shopBean, user);
        skuInfoFields.add(skuField);

        if (colorExtendField != null) {
            Field colorExtendField = buildColorExtendProp(expressionParser, colorExtendMappingBean, shopBean, user);
            skuInfoFields.add(colorExtendField);
        }
        if (skuExtendField != null) {
            Field skuExtendField = buildSizeExtendProp(expressionParser, skuExtendMappingBean, shopBean, user);
            skuInfoFields.add(skuExtendField);
        }

        return skuInfoFields;
    }

    private boolean isIgnore(String propId) {
        if (propId.endsWith("_range") || propId.endsWith("_from")
                || propId.endsWith("_to") || propId.startsWith("size_mapping_-")) {
            return true;
        }
        return false;
    }
}