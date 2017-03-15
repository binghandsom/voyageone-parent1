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
import com.voyageone.service.model.cms.CmsMtChannelSkuConfigModel;
import com.voyageone.service.model.cms.CmsMtPlatformPropSkuModel;
import com.voyageone.service.model.cms.constants.SkuTemplateConstants;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 达尔文的sku模板（需要产品规格）
 * 支持 有颜色有尺码/只有颜色/只有尺码/没有颜色没有尺码
 * 支持和模板4(衣服之类)一样，有肩宽之类的尺码扩展定义(cms_mt_channel_sku_config)
 * 参考天猫分类: 50010808  彩妆/香水/美妆工具>唇膏/口红
 *
 * @author morse.lu 2016-08-05
 * @version 2.3.0
 * @since 2.3.0
 */
public class TmallGjSkuFieldBuilderImpl7 extends AbstractSkuFieldBuilder {

    private final static int TPL_INDEX = 7;

    private Field skuField;
    private Field colorExtendField;
    private Field skuExtendField;

    private Field sku_cspuIdField; // 产品规格
    private Field sku_colorField;
    private Field sku_sizeField;
    private Field sku_priceField;
    private Field sku_quantityField;
    private Field sku_outerIdField;
    private Field sku_imageField;
    private Field sku_packageTypeField;
    // added by morse.lu 2016/08/17 start
    private Field sku_skuIdField;
    private Field sku_productIdField;
    // added by morse.lu 2016/08/17 end

    private Field colorExtend_colorField;
    private Field colorExtend_imageField;
    private Field colorExtend_aliasnameField;
    private Field colorExtend_basecolorField;

    private Field skuExtend_sizeField;
    private Field skuExtend_aliasnameField;

    private Field sku_market_timeField;

    private Field hscodeField;

    private int availableColorIndex = 0;
    private int availableSizeIndex = 0;

    private BuildSkuResult buildSkuResult;

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsBtProductModel> colorCmsPropductMap;
        Map<String, CmsBtProductModel_Sku> sizeCmsSkuPropMap;
        Map<CmsBtProductModel, String> cmsPropductColorMap;
        // Map<原size, 对应的平台的size的option>
        Map<String, String> sizeMap;

        public BuildSkuResult() {
            colorCmsPropductMap = new HashMap<>();
            cmsPropductColorMap = new HashMap<>();
            sizeCmsSkuPropMap = new HashMap<>();
            sizeMap = new LinkedHashMap<>();
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

        public Map<String, String> getSizeMap() {
            return sizeMap;
        }
    }

    @Override
    protected boolean init(List<Field> fields, int cartId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("cartId", cartId);
        searchMap.put("template", TPL_INDEX); // 模板7
        List<CmsMtPlatformPropSkuModel> listTmallSkuInfos = cmsMtPlatformPropSkuDao.selectList(searchMap);

        // Map<prop_id, List<CmsMtPlatformPropSkuModel>>
        Map<String, List<CmsMtPlatformPropSkuModel>> mapTmallSkuInfos = listTmallSkuInfos.stream().collect(Collectors.groupingBy(CmsMtPlatformPropSkuModel::getPropId));

        for (Field platformProp : fields) {
            if ("hscode".equals(platformProp.getId())) {
                hscodeField = platformProp;
                continue;
            }
            if (platformProp.getType() == FieldTypeEnum.LABEL) {
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
                if (type.intValue() == SkuTemplateConstants.SKU_CSPUID) {
                    sku_cspuIdField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.SKU_SIZE) {
                    sku_sizeField = platformProp;
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
                if (type.intValue() == SkuTemplateConstants.SKU_IMAGE) {
                    sku_imageField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.DARWINSKU_PACKAGE_TYPE) {
                    sku_packageTypeField = platformProp;
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
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_COLOR) {
                    colorExtend_colorField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_IMAGE) {
                    colorExtend_imageField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_ALIASNAME) {
                    colorExtend_aliasnameField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDCOLOR_BASECOLOR) {
                    colorExtend_basecolorField = platformProp;
                }

                //EXTENDSIZE
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE) {
                    skuExtendField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_SIZE) {
                    skuExtend_sizeField = platformProp;
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

        // deleted by morse.lu 2016/10/13 start
        // 可能没有规格，有部分类目，因为一个产品只有一个规格，所以商品不需要填规格，schema也没有规格属性
//        if (sku_cspuIdField == null) {
//            $warn(this.getClass().getName() + " requires sku cspuId!");
//            return false;
//        }
        // deleted by morse.lu 2016/10/13 end

        return true;
    }

    private void buildSkuColor(ComplexValue skuFieldValue, CmsBtProductModel sxProduct, CmsBtProductModel_Sku cmsSkuProp) throws Exception {
        String colorValue = buildSkuResult.getCmsPropductColorMap().get(sxProduct);
        if (sku_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
            List<Option> colorOptions = ((SingleCheckField) sku_colorField).getOptions();
            if (colorValue == null) {
                if (availableColorIndex >= colorOptions.size()) {
                    throw new BusinessException(String.format("最多只能%d个code!请拆分group!", colorOptions.size()));
                }
                colorValue = colorOptions.get(availableColorIndex++).getValue();
                buildSkuResult.getColorCmsPropductMap().put(colorValue, sxProduct);
                buildSkuResult.getCmsPropductColorMap().put(sxProduct, colorValue);
            }
            skuFieldValue.setSingleCheckFieldValue(sku_colorField.getId(), new Value(colorValue));
        } else {
            if (colorValue == null) {
                colorValue = getSkuValue(sxProduct, sku_colorField.getId(), cmsSkuProp.getSkuCode());
                if (StringUtils.isEmpty(colorValue)) {
                    // 没填的话用code
                    colorValue = sxProduct.getCommon().getFields().getCode();
                }
                buildSkuResult.getColorCmsPropductMap().put(colorValue, sxProduct);
                buildSkuResult.getCmsPropductColorMap().put(sxProduct, colorValue);
            }
            skuFieldValue.setInputFieldValue(sku_colorField.getId(), colorValue);
        }
    }

    private void buildSkuSize(ComplexValue skuFieldValue, CmsBtProductModel productModel, CmsBtProductModel_Sku cmsSkuProp) throws Exception {
        if (sku_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
            String sizeSx = cmsSkuProp.getSizeSx();
            String size = buildSkuResult.getSizeMap().get(sizeSx);
            if (!StringUtils.isEmpty(size)) {
                // 有相同的size,用平台的size的同一个option
                skuFieldValue.setSingleCheckFieldValue(sku_sizeField.getId(), new Value(size));
                return;
            }
            List<Option> sizeOptions = ((SingleCheckField)sku_sizeField).getOptions();
            if (availableSizeIndex >= sizeOptions.size()) {
                throw new BusinessException(String.format("最多只能%d个sku!请拆分group!", sizeOptions.size()));
            }
            String sizeValue = sizeOptions.get(availableSizeIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_sizeField.getId(), new Value(sizeValue));
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
            buildSkuResult.getSizeMap().put(sizeSx, sizeValue);
        } else {
            String skuSize = getSkuValue(productModel, sku_sizeField.getId(), cmsSkuProp.getSkuCode());
            if (StringUtils.isEmpty(skuSize)) {
                // 没填的话用sizeSx
                skuSize = cmsSkuProp.getSizeSx();
            }
            skuFieldValue.setInputFieldValue(sku_sizeField.getId(), skuSize);
            buildSkuResult.getSizeCmsSkuPropMap().put(skuSize, cmsSkuProp);
            buildSkuResult.getSizeMap().put(skuSize, skuSize);
        }
    }

    private Field buildSkuProp(Field skuField, ExpressionParser expressionParser, MappingBean skuMapping, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        List<CmsBtProductModel> sxProducts = expressionParser.getSxData().getProductList();
        buildSkuResult = new BuildSkuResult();
        int skuCnt;
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
            skuCnt = 0;
            List<CmsBtProductModel_Sku> cmsSkuPropBeans = sxProduct.getCommon().getSkus();
            for (CmsBtProductModel_Sku cmsSkuProp : cmsSkuPropBeans) {
                skuCnt++;
                //CmsBtProductModel_Sku 是Map<String, Object>的子类
                ComplexValue skuFieldValue = new ComplexValue();
                complexValues.add(skuFieldValue);

                if (sku_colorField != null) {
                    buildSkuColor(skuFieldValue, sxProduct, cmsSkuProp);
                }
                if (sku_sizeField != null) {
                    buildSkuSize(skuFieldValue, sxProduct, cmsSkuProp);
                }

                for (Field field : ((MultiComplexField)skuField).getFields()) {
                    String fieldId = field.getId();
                    if (sku_colorField != null && fieldId.equals(sku_colorField.getId())) {
                        continue;
                    }
                    if (sku_sizeField != null && fieldId.equals(sku_sizeField.getId())) {
                        continue;
                    }
                    if (sku_cspuIdField != null && fieldId.equals(sku_cspuIdField.getId())) {
                        skuFieldValue.setSingleCheckFieldValue(fieldId, new Value(expressionParser.getSxData().getDarwinSkuProps(cmsSkuProp.getSkuCode(), false).getCspuId()));
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

                    if (sku_imageField != null && fieldId.equals(sku_imageField.getId())) {
                        skuFieldValue.setInputFieldValue(sku_imageField.getId(), getImageUrl(expressionParser, shopBean, user, expressionParser.getSxData(), sxProduct));
                        continue;
                    }
                    if (sku_packageTypeField != null && fieldId.equals(sku_packageTypeField.getId())) {
                        // 套餐类型(天猫默认是"官方标配[6536025]",且必须要有"官方标配"
                        // 由于schema里没有备注可以填,而套餐类型又没有别名
                        // 所以无法备注各个套餐区别,暂时这一版只能不支持多种套餐,即不支持一个code下多个sku
                        // 干脆代码也写死"官方标配",以后看情况再改
                        // 参照类目"影音电器>耳机/耳麦"
                        if (skuCnt > 1) {
                            // 不支持一个code下多个sku
                            String errMsg = "本类目不支持一个code下多个sku!";
                            expressionParser.getSxData().setErrorMessage(errMsg);
                            throw new BusinessException(errMsg);
                        }
                        skuFieldValue.setSingleCheckFieldValue(sku_packageTypeField.getId(), new Value("6536025"));
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
            }
        }

        ((MultiComplexField)skuField).setComplexValues(complexValues);
        return skuField;
    }

    private Field buildColorExtendProp(ExpressionParser expressionParser, MappingBean colorExtendMapping, ShopBean shopBean, String user) throws Exception {
        SxData sxData = expressionParser.getSxData();
        Map<String, Field> fieldMap = ((MultiComplexField)colorExtendField).getFieldMap();

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsBtProductModel> entry : buildSkuResult.getColorCmsPropductMap().entrySet())
        {
            CmsBtProductModel sxProduct = entry.getValue();
            ComplexValue complexValue = new ComplexValue();

            if (colorExtend_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(colorExtend_colorField.getId(), new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(colorExtend_colorField.getId(), entry.getKey());
            }

            if (colorExtend_imageField != null) {
                complexValue.setInputFieldValue(colorExtend_imageField.getId(), getImageUrl(expressionParser, shopBean, user, sxData, sxProduct));
            }

            if (colorExtend_aliasnameField != null) {
                // 别名,用产品code
                // modified by morse.lu 2016/12/29 start
                // 放到共通去了
//                String alias = sxProduct.getCommon().getFields().getCode();
//                // added by morse.lu 2016/08/29 start
//                // 通过配置表(cms_mt_channel_config)来决定用code，还是color，默认用code
//                CmsChannelConfigBean aliasConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId()
//                        , CmsConstants.ChannelConfig.ALIAS
//                        , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.COLOR_ALIAS);
//                if (aliasConfig != null) {
//                    String aliasPropName = aliasConfig.getConfigValue1(); // 目前配置的是code或者color
//                    if (!StringUtils.isEmpty(aliasPropName)) {
//                        String val = sxProduct.getCommon().getFields().getStringAttribute(aliasPropName);
//                        if (!StringUtils.isEmpty(val)) {
//                            alias = val;
//                        }
//                    }
//                }
//                // added by morse.lu 2016/08/29 end
//                if (alias.length() > 60) {
//                    alias = sxProduct.getCommon().getFields().getColor();
//                }
                String alias = expressionParser.getSxProductService().getSxColorAlias(sxData.getChannelId(), sxData.getCartId(), sxProduct, 60);
                // modified by morse.lu 2016/12/29 end
                complexValue.setInputFieldValue(colorExtend_aliasnameField.getId(), alias);
            }
            if (colorExtend_basecolorField != null) {
                // 原先也没做,这版也暂时不做
            }

            complexValues.add(complexValue);
        }
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

            if (propId != null && propId.equals(skuExtend_sizeField.getId())) {
                customSizeNameIdMap.put(propValue, sizeId);
            } else {
                eachCustomSizePropMap.put(propId, propValue);
            }
        }

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, String> entry : buildSkuResult.getSizeMap().entrySet())
        {

            ComplexValue complexValue = new ComplexValue();

            if (skuExtend_sizeField.getType() == FieldTypeEnum.SINGLECHECK) {
                complexValue.setSingleCheckFieldValue(skuExtend_sizeField.getId(), new Value(entry.getValue()));
            } else {
                complexValue.setInputFieldValue(skuExtend_sizeField.getId(), entry.getValue());
            }

            if (skuExtend_aliasnameField != null) {
                complexValue.setInputFieldValue(skuExtend_aliasnameField.getId(), entry.getKey());
            }

            String skuSize = entry.getKey();
            String sizeId = customSizeNameIdMap.get(skuSize);
            if (sizeId != null) {
                // 可能有脚长之类的扩展，可能没有
                Map<String, String> customSizePropMap = allCustomSizePropMap.get(sizeId);
                for (Map.Entry<String, String> customSizeEntry : customSizePropMap.entrySet()) {
                    complexValue.setInputFieldValue(customSizeEntry.getKey(), customSizeEntry.getValue());
                }
            }

            complexValues.add(complexValue);
        }

        ((MultiComplexField)skuExtendField).setComplexValues(complexValues);
        return skuExtendField;
    }

    private String getImageUrl(ExpressionParser expressionParser, ShopBean shopBean, String user, SxData sxData, CmsBtProductModel sxProduct) throws Exception {
        String imageUrl = null;
        String propImage = expressionParser.getSxProductService().getProductImages(sxProduct, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE, sxData.getCartId()).get(0).getName();
        if (propImage != null && !"".equals(propImage)) {
            if (StringUtils.isEmpty(getCodeImageTemplate())) {
                $warn("图片模板url未设置");
            } else {
                String codePropFullImageUrl = String.format(getCodeImageTemplate(), propImage);

                if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
                    Set<String> url = new HashSet<>();
                    url.add(codePropFullImageUrl);
                    // 上传图片到天猫图片空间
                    Map<String, String> retMap = expressionParser.getSxProductService().uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
                    imageUrl = retMap.get(codePropFullImageUrl);
                } else {
                    imageUrl = codePropFullImageUrl;
                }
            }
        }

        return imageUrl;
    }

    @Override
    public List<Field> buildSkuInfoFieldChild(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        List<Field> skuInfoFields = new ArrayList<>();

        MappingBean skuMappingBean = null;
        MappingBean colorExtendMappingBean = null;
        MappingBean skuExtendMappingBean = null;

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