package com.voyageone.service.impl.cms.sx.sku_field.tmall;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.MappingBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.bean.cms.product.SxData.SxDarwinSkuProps;
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
 * 达尔文的产品规格模板(不支持鞋类)
 * 需要判断，增加还是更新规格还是不更新
 * 更新的话还要判断，是对于原先错误的来更新，还是正确的来更新（只支持修改条形码、吊牌价以及产品规格主图）
 * 从模板3copy出来，修正了：
 * 1：上述的判断逻辑
 * 2：别名不用code,用color
 *
 * 参考天猫分类: 50010808  彩妆/香水/美妆工具>唇膏/口红
 *
 * @author morse.lu 2016-08-10
 * @version 2.3.0
 * @since 2.3.0
 */
public class TmallGjSkuFieldBuilderImpl8 extends AbstractSkuFieldBuilder {

    private final static int TPL_INDEX = 8;

    private Field cspuField;
    private Field colorExtendField;
    private Field skuExtendField;

    private Field sku_colorField;
    private Field sku_priceField;
    private Field sku_cspuImgField;
    private Field sku_barCodeField;
    private Field sku_sizeField;
    private List<Field> list_sku_attachImgField; // 资质图
    private List<Field> list_sku_noUpdateField; // 产品规格属性里，不会去设值的属性(例如:规格id,审核状态等)

    private Field colorExtend_aliasnameField;
    private Field colorExtend_colorField;
    private Field colorExtend_basecolorField;

    private Field skuExtend_aliasnameField;
    private Field skuExtend_sizeField;

    private String[] allowUpdateId = // 对于原先正确的来更新,允许更新的field_id
//            new String[]{"cspuImg"};
            new String[]{""}; // 目前只看到规格主图(实际不需要更新),吊牌价没看到,不知道id，以后再加，条形码我们作为逻辑主key，所以不能更新

    private int availableColorIndex = 0;
    private int availableSizeIndex = 0;

    private static final String CSPU_ID = "cspu_id"; // 产品规格ID的field_id

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
            sizeCmsSkuPropMap = new HashMap<>();
            cmsPropductColorMap = new HashMap<>();
            sizeMap = new LinkedHashMap<>();
        }

        public Map<CmsBtProductModel, String> getCmsPropductColorMap() {
            return cmsPropductColorMap;
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
    protected boolean init(List<Field> platformProps, int cartId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("cartId", cartId);
        searchMap.put("template", TPL_INDEX); // 模板8
        list_sku_attachImgField = new ArrayList<>();
        list_sku_noUpdateField = new ArrayList<>();

        List<CmsMtPlatformPropSkuModel> listTmallSkuInfos = cmsMtPlatformPropSkuDao.selectList(searchMap);

        // Map<prop_id, List<CmsMtPlatformPropSkuModel>>
        Map<String, List<CmsMtPlatformPropSkuModel>> mapTmallSkuInfos = listTmallSkuInfos.stream().collect(Collectors.groupingBy(model -> model.getPropId()));

        for (Field platformProp : platformProps) {
            if (mapTmallSkuInfos.get(platformProp.getId()) == null) {
                throw new BusinessException("The darwinSku's cspu info does not find for platformProp: [prop_id=" + platformProp.getId() + ", cartId=" + cartId + "]");
            }

            // 取得这个propId所有的type
            List<Integer> listType = mapTmallSkuInfos.get(platformProp.getId()).stream().map(CmsMtPlatformPropSkuModel::getSkuType).collect(Collectors.toList());

            for (Integer type : listType) {
                // cspuList
                if (type.intValue() == SkuTemplateConstants.CSPU) {
                    cspuField = platformProp;
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
                if (type.intValue() == SkuTemplateConstants.SKU_PRICE) {
                    sku_priceField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.CSPU_IMG) {
                    sku_cspuImgField = platformProp;
                }
                if (type.intValue() == SkuTemplateConstants.CSPU_ATTACH_IMG) {
                    list_sku_attachImgField.add(platformProp);
                }
                if (type.intValue() == SkuTemplateConstants.CSPU_NO_UPDATE) {
                    list_sku_noUpdateField.add(platformProp);
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
                if (type.intValue() == SkuTemplateConstants.EXTENDSIZE_ALIASNAME) {
                    skuExtend_aliasnameField = platformProp;
                }

                // 暂时不知道匹配什么
                if (type.intValue() == SkuTemplateConstants.UNKOWN) {
                    addUnkownField(platformProp);
                }
            }
        }

        if (cspuField == null) {
            throw new BusinessException(this.getClass().getName() + " requires cspu!");
        }
        if (sku_barCodeField == null) {
            throw new BusinessException(this.getClass().getName() + " requires barcode!");
        }

        return true;
    }

    private void buildSkuColor(ComplexValue skuFieldValue, CmsBtProductModel sxProduct, CmsBtProductModel_Sku cmsSkuProp) throws Exception {
        String colorValue = buildSkuResult.getCmsPropductColorMap().get(sxProduct);
        if (sku_colorField.getType() == FieldTypeEnum.SINGLECHECK) {
            if (colorValue == null) {
                List<Option> colorOptions = ((SingleCheckField) sku_colorField).getOptions();
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
                colorValue = sxProduct.getCommon().getFields().getColor();
                buildSkuResult.getColorCmsPropductMap().put(colorValue, sxProduct);
                buildSkuResult.getCmsPropductColorMap().put(sxProduct, colorValue);
            }
            skuFieldValue.setInputFieldValue(sku_colorField.getId(), colorValue);
        }
    }

    private void buildSkuSize(ComplexValue skuFieldValue, CmsBtProductModel productModel, CmsBtProductModel_Sku cmsSkuProp, String barcode) throws Exception {
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
                throw new BusinessException(String.format("最多只能%d个不同尺码!请拆分group!", sizeOptions.size()));
            }
            String sizeValue = sizeOptions.get(availableSizeIndex++).getValue();
            skuFieldValue.setSingleCheckFieldValue(sku_sizeField.getId(), new Value(sizeValue));
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
            buildSkuResult.getSizeMap().put(sizeSx, sizeValue);
        } else {
            String skuSize = getCspuValue(productModel, sku_sizeField.getId(), barcode);
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
        SxData sxData = expressionParser.getSxData();
        List<CmsBtProductModel> sxProducts = expressionParser.getSxData().getProductList();
        buildSkuResult = new BuildSkuResult();
        // 对于原先正确的来更新,允许更新的field_id
        List<String> listAllowUpdateId = Arrays.asList(allowUpdateId);
        // 需要特殊处理的,不会直接用画面填的去更新，或者根本不要去更新的field_id
        List<String> listCusId = getCusIds();
        // 更新时用的，原先规格的值
        List<ComplexValue> multiComplexDefaultValues = ((MultiComplexField) skuField).getDefaultComplexValues();
        // Map<cspu_id, ComplexValue>
        Map<String, ComplexValue> mapBarcodeComplexValue = new HashMap<>();
        if (ListUtils.notNull(multiComplexDefaultValues)) {
            for (ComplexValue complexValue : multiComplexDefaultValues) {
                String cspuId = "";
                for (String fieldId : complexValue.getFieldKeySet()) {
                    if (CSPU_ID.equals(fieldId)) {
                        cspuId = ((InputField) complexValue.getValueField(fieldId)).getValue();
                        break;
                    }
                }
                mapBarcodeComplexValue.put(cspuId, complexValue);
            }
        }

        List<ComplexValue> complexValues = new ArrayList<>();
        for (CmsBtProductModel sxProduct : sxProducts) {
            List<CmsBtProductModel_Sku> cmsSkuPropBeans = sxProduct.getCommon().getSkus();
            for (CmsBtProductModel_Sku cmsSkuProp : cmsSkuPropBeans) {
                String skuCode= cmsSkuProp.getSkuCode();
                SxDarwinSkuProps sxDarwinSkuProps = sxData.getDarwinSkuProps(skuCode, false);
                if (sxDarwinSkuProps == null) {
                    // 应该不会为空，以防万一吧
                    continue;
                }

                String barcode = sxDarwinSkuProps.getBarcode();

                if (!StringUtils.isEmpty(sxDarwinSkuProps.getCspuId())) {
                    // 原先已有此规格
                    if (!sxDarwinSkuProps.isAllowUpdate()) {
                        // 不允许更新
                        continue;
                    } else {
                        // 允许更新
                        if (sxDarwinSkuProps.isErr()) {
                            // 原先有错
                            // 因为拿不到原先的值,和add一样新做一个ComplexValue,设值需要的值,再追加一个field规格ID
                            ComplexValue skuFieldValue = new ComplexValue();
                            complexValues.add(skuFieldValue);
                            skuFieldValue.setInputFieldValue(CSPU_ID, sxDarwinSkuProps.getCspuId());
                            // 图片设值
                            setImageFieldValue(expressionParser, sxProduct, barcode, skuFieldValue, shopBean, user);
                            for (Field field : ((MultiComplexField)skuField).getFields()) {
                                if (!listCusId.contains(field.getId())) {
                                    setFieldValue(sxProduct, barcode, skuFieldValue, field);
                                }
                            }
                        } else {
                            // 原先没错
                            ComplexValue skuFieldValue = mapBarcodeComplexValue.get(sxDarwinSkuProps.getCspuId());
                            if (skuFieldValue== null) {
                                throw new BusinessException(String.format("规格[barcode=%s]审核有错,错误信息取得失败!", barcode));
                            }
                            complexValues.add(skuFieldValue);
                            for (Field field : ((MultiComplexField)skuField).getFields()) {
                                if (listAllowUpdateId.contains(field.getId())) {
                                    setFieldValue(sxProduct, barcode, skuFieldValue, field);
                                }
                            }
                        }
                    }
                } else {
                    // 新增规格

                    //CmsBtProductModel_Sku 是Map<String, Object>的子类
                    ComplexValue skuFieldValue = new ComplexValue();
                    complexValues.add(skuFieldValue);

                    if (sku_colorField != null) {
                        buildSkuColor(skuFieldValue, sxProduct, cmsSkuProp);
                    }
                    if (sku_sizeField != null) {
                        buildSkuSize(skuFieldValue, sxProduct, cmsSkuProp, barcode);
                    }

                    // 图片设值
                    setImageFieldValue(expressionParser, sxProduct, barcode, skuFieldValue, shopBean, user);

                    for (Field field : ((MultiComplexField)skuField).getFields()) {
                        String fieldId = field.getId();
                        if (sku_barCodeField != null && fieldId.equals(sku_barCodeField.getId())) {
                            skuFieldValue.setInputFieldValue(sku_barCodeField.getId(), sxDarwinSkuProps.getBarcode());
                            continue;
                        }
                        if (sku_priceField != null && fieldId.equals(sku_priceField.getId())) {
                            skuFieldValue.setInputFieldValue(sku_priceField.getId(), String.valueOf(getSkuPrice(expressionParser.getSxData().getChannelId(), sxProduct, cmsSkuProp.getSkuCode())));
                            continue;
                        }

                        if (!listCusId.contains(field.getId())) {
                            // 从各平台下面的fields.sku里取值
                            setFieldValue(sxProduct, barcode, skuFieldValue, field);
                        }
                    }
                }
            }
        }

        ((MultiComplexField)skuField).setComplexValues(complexValues);
        return skuField;
    }

    /**
     * 需要特殊处理的,不会直接用画面填的去更新，或者根本不要去更新的field_id
     */
    private List<String> getCusIds() {
        List<String> listCusId = list_sku_noUpdateField.stream().map(Field::getId).collect(Collectors.toList()); // 对于原先错误的来更新,不能更新的field_id

        // 一些图片
        if (sku_cspuImgField != null) {
            listCusId.add(sku_cspuImgField.getId()); // 规格主图
        }
        list_sku_attachImgField.forEach(imageField-> listCusId.add(imageField.getId())); // 资质图

        // 颜色扩展，尺码扩展
        if (sku_colorField != null) {
            listCusId.add(sku_colorField.getId()); // 颜色扩展
        }
        if (sku_sizeField != null) {
            listCusId.add(sku_sizeField.getId()); // 尺码扩展
        }

        return listCusId;
    }

    /**
     * 图片设值
     */
    private void setImageFieldValue(ExpressionParser expressionParser, CmsBtProductModel sxProduct, String barcode, ComplexValue skuFieldValue, ShopBean shopBean, String user) throws Exception {
        // 暂时画面写死已经上传到平台的url完整路径，所以先用下 {}2， 以后改回成画面有上传按钮，只填写图片名，再用回{}1，
//        { // {}1
//            SxData sxData = expressionParser.getSxData();
//            String imageTemplate = getCodeImageTemplate();
//
//            // Map<Field, oriUrl>
//            Map<Field, String> mapFieldUrl = new HashMap<>();
//            Set<String> url = new HashSet<>();
//
//            if (sku_cspuImgField != null) {
//                // 规格主图
//                String propImage = expressionParser.getSxProductService().getProductImages(sxProduct, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
//                String codePropFullImageUrl = String.format(imageTemplate, propImage);
//                url.add(codePropFullImageUrl);
//                mapFieldUrl.put(sku_cspuImgField, codePropFullImageUrl);
//            }
//
//            for (Field attachImg : list_sku_attachImgField) {
//                // 资质图
//                String propImage = getCspuValue(sxProduct, attachImg.getId(), barcode);
//                if (StringUtils.isEmpty(propImage)) {
//                    // 资质图没有填
//                    throw new BusinessException(String.format("产品规格的%s没有上传!", attachImg.getName()));
//                }
//                String codePropFullImageUrl = String.format(imageTemplate, propImage);
//                url.add(codePropFullImageUrl);
//                mapFieldUrl.put(attachImg, codePropFullImageUrl);
//            }
//
//            // 上传图片到天猫图片空间
//            Map<String, String> retMap = expressionParser.getSxProductService().uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
//
//            for (Map.Entry<Field, String> entry : mapFieldUrl.entrySet()) {
//                Field imageField = entry.getKey();
//                String oriUrl = entry.getValue();
//                String platformUrl = retMap.get(oriUrl);
//                if (StringUtils.isEmpty(platformUrl)) {
//                    throw new BusinessException(String.format("产品规格的%s的图片上传失败!", imageField.getName()));
//                }
//                skuFieldValue.setInputFieldValue(imageField.getId(), platformUrl);
//            }
//        }
        { // {}2
            if (sku_cspuImgField != null) {
                // 规格主图
                String propImage = getCspuValue(sxProduct, sku_cspuImgField.getId(), barcode);
                if (StringUtils.isEmpty(propImage)) {
                    // 资质图没有填
                    throw new BusinessException(String.format("产品规格的%s没有填!", sku_cspuImgField.getName()));
                }
                skuFieldValue.setInputFieldValue(sku_cspuImgField.getId(), propImage);
            }

            for (Field attachImg : list_sku_attachImgField) {
                // 资质图
                String propImage = getCspuValue(sxProduct, attachImg.getId(), barcode);
                skuFieldValue.setInputFieldValue(attachImg.getId(), propImage);
            }
        }
    }

    /**
     * 直接用画面填的去更新
     */
    private void setFieldValue(CmsBtProductModel sxProduct, String barcode, ComplexValue skuFieldValue, Field field) {
        // ComplexValue里有的话更新，没的话create
        String fieldId = field.getId();
        Field oldField = skuFieldValue.getValueField(fieldId);
        // 从各平台下面的fields.cspuList里取值
        String val = getCspuValue(sxProduct, fieldId, barcode);
        if (field.getType() == FieldTypeEnum.INPUT) {
            if (oldField != null) {
                ((InputField) oldField).setValue(val);
            } else {
                skuFieldValue.setInputFieldValue(fieldId, val);
            }
        } else if (field.getType() == FieldTypeEnum.SINGLECHECK) {
            if (oldField != null) {
                ((SingleCheckField) oldField).setValue(new Value(val));
            } else {
                skuFieldValue.setSingleCheckFieldValue(fieldId, new Value(val));
            }
        }
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

            if (colorExtend_aliasnameField != null) {
                // 别名,用产品color
                // modified by morse.lu 2016/12/29 start
//                complexValue.setInputFieldValue(colorExtend_aliasnameField.getId(), sxProduct.getCommon().getFields().getColor());
                String alias = sxProduct.getCommon().getFields().getColor();
                if (StringUtils.isEmpty(alias) || alias.length() > 60) {
                    throw new BusinessException("颜色别名(颜色/口味/香型)必须填写,且长度不能超过60");
                }
                complexValue.setInputFieldValue(colorExtend_aliasnameField.getId(), alias);
                // modified by morse.lu 2016/12/29 end
            }
            if (colorExtend_basecolorField != null) {
                // 这一版不让选，直接用第一个颜色
                if (colorExtend_basecolorField.getType() == FieldTypeEnum.SINGLECHECK) {
                    String baseColor = ((SingleCheckField) colorExtend_basecolorField).getOptions().get(0).getValue();
                    complexValue.setSingleCheckFieldValue(colorExtend_basecolorField.getId(), new Value(baseColor));
                } else if (colorExtend_basecolorField.getType() == FieldTypeEnum.MULTICHECK) {
                    List<Value> values = new ArrayList<>();
                    String baseColor = ((MultiCheckField) colorExtend_basecolorField).getOptions().get(0).getValue();
                    values.add(new Value(baseColor));
                    complexValue.setMultiCheckFieldValues(colorExtend_basecolorField.getId(), values);
                }
            }

            complexValues.add(complexValue);
        }
        ((MultiComplexField)colorExtendField).setComplexValues(complexValues);
        return colorExtendField;
    }

    private Field buildSizeExtendProp(ExpressionParser expressionParser, MappingBean sizeExtendMapping, ShopBean shopBean, String user) throws Exception {
        Map<String, Field> fieldMap = ((MultiComplexField)skuExtendField).getFieldMap();

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
                // 别名,用size
                {
                    // 0708 第二版，尺码扩展不再根据sku数量来填坑了,如果size相同，只会填一个坑
                    complexValue.setInputFieldValue(skuExtend_aliasnameField.getId(), entry.getKey());
                }
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
        MappingBean colorExtendMappingBean = null;
        MappingBean skuExtendMappingBean = null;

        cspuField = buildSkuProp(cspuField, expressionParser, skuMappingBean, skuInventoryMap, shopBean, user);
        skuInfoFields.add(cspuField);

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
}