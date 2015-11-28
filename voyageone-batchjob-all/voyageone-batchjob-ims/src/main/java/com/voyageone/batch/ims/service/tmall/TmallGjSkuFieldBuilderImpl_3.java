package com.voyageone.batch.ims.service.tmall;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.value.ComplexValue;
import com.taobao.top.schema.value.Value;
import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.bean.PlatformUploadRunState;
import com.voyageone.batch.ims.bean.TmallUploadRunState;
import com.voyageone.batch.ims.bean.tcb.UploadProductTcb;
import com.voyageone.batch.ims.modelbean.*;
import com.voyageone.batch.ims.service.AbstractSkuFieldBuilder;
import com.voyageone.batch.ims.service.SkuTemplateSchema;
import com.voyageone.batch.ims.service.UploadImageHandler;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Created by Leo on 15-7-14.
 * 参考天猫分类: 50014993
 * 线程安全: 否
 */
public class TmallGjSkuFieldBuilderImpl_3 extends AbstractSkuFieldBuilder {
    private String propId_sku;
    private String propId_colorExtend;
    private String propId_skuExtend;

    private String propId_sku_color;
    private String propId_sku_price;
    private String propId_sku_quantity;
    private String propId_sku_outerId;
    private String propId_sku_barCode;
    private String propId_sku_size;

    private String propId_colorExtend_color;
    private String propId_colorExtend_image;

    private String propId_skuExtend_tip;
    private String propId_skuExtend_shengao;
    private String propId_skuExtend_tizhong;
    private String propId_skuExtend_jiankuan;
    private String propId_skuExtend_xiongwei;
    private String propId_skuExtend_yaowei;
    private String propId_skuExtend_xiuchang;
    private String propId_skuExtend_beikuan;
    private String propId_skuExtend_qianchang;
    private String propId_skuExtend_baiwei;
    private String propId_skuExtend_xiabaiwei;
    private String propId_skuExtend_xiukou;
    private String propId_skuExtend_xiufei;
    private String propId_skuExtend_zhongyao;
    private String propId_skuExtend_lingshen;
    private String propId_skuExtend_linggao;
    private String propId_skuExtend_lingkuan;
    private String propId_skuExtend_lingwei;
    private String propId_skuExtend_yuanbaihou;
    private String propId_skuExtend_yuanbai;
    private String propId_skuExtend_pingbaiyichang;
    private String propId_skuExtend_yichang;

    private String propId_sku_market_time;
    private String propId_skuExtend_size;

    private PlatformPropBean skuPlatformProp;

    private boolean colorIsSingleCheck;
    PlatformPropBean platformProp_colorCategory;
    private int availableColorIndex = 0;
    List<PlatformPropOptionBean> colorOptions;

    private boolean sizeIsSingleCheck;
    PlatformPropBean platformProp_sku_size;
    private int availableSizeIndex = 0;
    List<PlatformPropOptionBean> sizeOptions;

    private static Log logger = LogFactory.getLog(TmallGjSkuFieldBuilderImpl_0.class);

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsCodePropBean> colorCmsSkuPropMap;
        Map<String, CmsSkuPropBean> sizeCmsSkuPropMap;

        //Build prop extend result
        Map<String, ComplexValue> srcUrlComplexValueMap;

        public BuildSkuResult() {
            colorCmsSkuPropMap = new HashMap<>();
            sizeCmsSkuPropMap = new HashMap<>();
            srcUrlComplexValueMap = new HashMap<>();
        }

        public Map<String, CmsCodePropBean> getColorCmsSkuPropMap() {
            return colorCmsSkuPropMap;
        }

        public Map<String, CmsSkuPropBean> getSizeCmsSkuPropMap() {
            return sizeCmsSkuPropMap;
        }

        public Map<String, ComplexValue> getSrcUrlComplexValueMap() {
            return srcUrlComplexValueMap;
        }
    }

    private boolean init(List<PlatformPropBean> platformProps) {
        for (PlatformPropBean platformProp : platformProps) {
            List<PlatformSkuInfoBean> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(platformProp.getPlatformPropId(), platformProp.getPlatformCartId());

            PlatformSkuInfoBean tmallSkuInfo = null;
            for (PlatformSkuInfoBean tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                    break;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + platformProp.getPlatformPropId() + ", cartId=" + platformProp.getPlatformCartId() + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_3.class.getName() + " is not applicable!");
                return false;
            }

            long fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if ((fieldType < (1L << SkuTemplateSchema.SkuTemplate_3_Schema.FIELD_BIT_MIN))
                    || (fieldType > (1L << (SkuTemplateSchema.SkuTemplate_3_Schema.FIELD_BIT_MAX + 1L)))) {
                return false;
            }

            //SKU
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU)) {
                propId_sku = platformProp.getPlatformPropId();
                skuPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_SIZE)) {
                propId_sku_size = platformProp.getPlatformPropId();
                sizeIsSingleCheck = platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_BARCODE)) {
                propId_sku_barCode = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_COLOR)) {
                propId_sku_color = platformProp.getPlatformPropId();
                colorIsSingleCheck = platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_OUTERID)) {
                propId_sku_outerId = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_PRICE)) {
                propId_sku_price = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_QUANTITY)) {
                propId_sku_quantity = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_MARKET_TIME)) {
                propId_sku_market_time = platformProp.getPlatformPropId();
            }

            //EXTENDCOLOR
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR)) {
                propId_colorExtend = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_COLOR)) {
                propId_colorExtend_color = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_IMAGE)) {
                propId_colorExtend_image = platformProp.getPlatformPropId();
            }

            //EXTENDSIZE
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE)) {
                propId_skuExtend = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SIZE)) {
                propId_skuExtend_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BAIWEI)) {
                propId_skuExtend_baiwei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BEIKUAN)) {
                propId_skuExtend_beikuan = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_JIANKUAN)) {
                propId_skuExtend_jiankuan = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGGAO)) {
                propId_skuExtend_linggao = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGKUAN)) {
                propId_skuExtend_lingkuan = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGSHEN)) {
                propId_skuExtend_lingshen = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGWEI)) {
                propId_skuExtend_lingwei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_PINGBAI)) {
                propId_skuExtend_pingbaiyichang = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_QIANCHANG)) {
                propId_skuExtend_qianchang = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SHENGAO)) {
                propId_skuExtend_shengao = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIP)) {
                propId_skuExtend_tip = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIZHONG)) {
                propId_skuExtend_tizhong = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIABAIWEI)) {
                propId_skuExtend_xiabaiwei = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIONGWEI)) {
                propId_skuExtend_xiongwei = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUCHANG)) {
                propId_skuExtend_xiuchang = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUFEI)) {
                propId_skuExtend_xiufei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUKOU)) {
                propId_skuExtend_xiukou = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YICHANG)) {
                propId_skuExtend_yichang = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YAOWEI)) {
                propId_skuExtend_yaowei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_ZHONGYAO)) {
                propId_skuExtend_zhongyao = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAIHOU)) {
                propId_skuExtend_yuanbaihou = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAI)) {
                propId_skuExtend_yuanbai = platformProp.getPlatformPropId();
            }
        }

        return true;
    }

    private void buildSkuColor(ComplexValue skuFieldValue, BuildSkuResult buildSkuResult, int cartId, CmsCodePropBean cmsCodeProp, String categoryCode, String skuPlatforomPropHash) {

        if (colorIsSingleCheck) {
            if (platformProp_colorCategory == null) {
                platformProp_colorCategory = platformPropDao.selectPlatformPropByPropId(cartId, categoryCode, propId_sku_color, skuPlatforomPropHash);
            }

            if (colorOptions == null) {
                colorOptions = platformPropDao.selectPlatformOptionsByPropHash(platformProp_colorCategory.getPlatformPropHash());
            }
            String colorValue = colorOptions.get(availableColorIndex).getPlatformPropOptionValue();
            skuFieldValue.setSingleCheckFieldValue(propId_sku_color, new Value(colorValue));
            buildSkuResult.getColorCmsSkuPropMap().put(colorValue, cmsCodeProp);
        } else {
            String colorValue = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.code);
            skuFieldValue.setInputFieldValue(propId_sku_color, colorValue);
            buildSkuResult.getColorCmsSkuPropMap().put(colorValue, cmsCodeProp);
        }
    }

    private void buildSkuSize(ComplexValue skuFieldValue, BuildSkuResult buildSkuResult, int cartId, WorkLoadBean workLoadBean, CmsSkuPropBean cmsSkuProp, String categoryCode, String skuPlatforomPropHash) {
        if (sizeIsSingleCheck) {
            if (platformProp_sku_size == null) {
                platformProp_sku_size = platformPropDao.selectPlatformPropByPropId(cartId, categoryCode, propId_sku_size, skuPlatforomPropHash);
            }

            if (sizeOptions == null) {
                sizeOptions = platformPropDao.selectPlatformOptionsByPropHash(platformProp_sku_size.getPlatformPropHash());
            }
            String sizeValue = sizeOptions.get(availableSizeIndex).getPlatformPropOptionValue();
            skuFieldValue.setSingleCheckFieldValue(propId_sku_size, new Value(sizeValue));
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
        } else {
            String sku = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku);
            String sizeValue = skuPropValueDao.selectSkuPropValue(workLoadBean.getOrder_channel_id(), sku, propId_sku_size);
            if (sizeValue == null) {
                sizeValue = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.size);
            }
            skuFieldValue.setInputFieldValue(propId_sku_size, sizeValue);
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
        }
    }

    private Field buildSkuProp(int cartId, String categoryCode, PlatformPropBean platformProp, CmsModelPropBean cmsModelProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();

        BuildSkuResult buildSkuResult = new BuildSkuResult();
        contextBuildCustomFields.setBuildSkuResult(buildSkuResult);

        MultiComplexField skuField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        skuField.setId(propId_sku);

        //SingleCheckField colorCategoryField = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
        //colorCategoryField.setId(propId_sku_color);

        List<ComplexValue> complexValues = new ArrayList<>();
        for (CmsCodePropBean cmsCodeProp : cmsModelProp.getCmsCodePropBeanList()) {
            List<CmsSkuPropBean> cmsSkuPropBeans = cmsCodeProp.getCmsSkuPropBeanList();
            for (CmsSkuPropBean cmsSkuProp : cmsSkuPropBeans) {
                ComplexValue skuFieldValue = new ComplexValue();

                buildSkuColor(skuFieldValue, buildSkuResult, cartId, cmsCodeProp, categoryCode, platformProp.getPlatformPropHash());

                String skuPrice = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku_price);
                skuFieldValue.setInputFieldValue(propId_sku_price, skuPrice);

                String skuQuantity = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku_quantity);
                skuFieldValue.setInputFieldValue(propId_sku_quantity, skuQuantity);

                String skuOuterId = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku); //商家编码使用sku的值
                skuFieldValue.setInputFieldValue(propId_sku_outerId, skuOuterId);

                String skuBarCode = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.bar_code);
                //条形码可为空
                if (skuBarCode != null) {
                    skuFieldValue.setInputFieldValue(propId_sku_barCode, skuBarCode);
                }

                buildSkuSize(skuFieldValue, buildSkuResult, cartId, workLoadBean, cmsSkuProp, categoryCode, platformProp.getPlatformPropHash());

                if (propId_sku_market_time != null && !"".equals(propId_sku_market_time)) {
                    Date now = DateTimeUtil.getDate();
                    String marketTime = String.format("%d-%d-%d", 1900 + now.getYear(), now.getMonth(), now.getDay());
                    skuFieldValue.setInputFieldValue(propId_sku_market_time, marketTime);
                }

                complexValues.add(skuFieldValue);
                availableSizeIndex++;
            }
            availableColorIndex++;
        }

        skuField.setComplexValues(complexValues);
        return skuField;
    }

    private Field buildColorExtendProp(TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        BuildSkuResult buildSkuResult = (BuildSkuResult) contextBuildCustomFields.getBuildSkuResult();
        MultiComplexField colorExtendField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        colorExtendField.setId(propId_colorExtend);

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsCodePropBean> entry : buildSkuResult.getColorCmsSkuPropMap().entrySet())
        {
            CmsCodePropBean cmsCodeProp = entry.getValue();

            ComplexValue complexValue = new ComplexValue();
            if (colorIsSingleCheck) {
                complexValue.setSingleCheckFieldValue(propId_colorExtend_color, new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(propId_colorExtend_color, entry.getKey());
            }

            //颜色图使用竖图
            String propImageStr = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.product_image);
            if (propImageStr != null && !"".equals(propImageStr)) {
                String propImages[] = propImageStr.split(",");
                String propImage = propImages[0];
                String codePropFullImageUrl = UploadImageHandler.encodeImageUrl(String.format(codeImageTemplate, propImage));
                complexValue.setInputFieldValue(propId_colorExtend_image, codePropFullImageUrl);
                imageSet.add(codePropFullImageUrl);
                buildSkuResult.getSrcUrlComplexValueMap().put(codePropFullImageUrl, complexValue);
            }
            complexValues.add(complexValue);
        }
        colorExtendField.setComplexValues(complexValues);
        return colorExtendField;
    }

    private Field buildSizeExtendProp(TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();

        BuildSkuResult buildSkuResult = (BuildSkuResult) contextBuildCustomFields.getBuildSkuResult();
        Map<String, CmsSkuPropBean> sizeCmsSkuPropMap = buildSkuResult.getSizeCmsSkuPropMap();

        MultiComplexField sizeExtendProp = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        sizeExtendProp.setId(propId_skuExtend);

        List<ComplexValue> complexValueList = new ArrayList<>();

        Map<String, Map<String, String>> allCustomSizePropMap = new HashMap<>();
        List<Map<String, Object>> allCustomSizeProps = customSizePropDao.selectCustomSizeProp(workLoadBean.getOrder_channel_id());
        Map<String, String> customSizeNameIdMap = new HashMap<>();

        //构造两个map，一个是customSizeNameIdMap sizeName->sizeId, such as: L->1
        //另一个是allCustomSizePropMap, sizeId ->CustomSize属性map, such as: 1->[{shengao:170}, {tizhong:70}]
        for (Map<String, Object> customSizeProp : allCustomSizeProps) {
            String sizeId = customSizeProp.get("custom_size_id").toString();
            String propId = customSizeProp.get("custom_prop_id").toString();
            String propValue = customSizeProp.get("custom_prop_value").toString();
            Map<String, String> eachCustomSizePropMap = allCustomSizePropMap.get(sizeId);

            if (eachCustomSizePropMap == null) {
                eachCustomSizePropMap = new HashMap<>();
                allCustomSizePropMap.put(sizeId, eachCustomSizePropMap);
            }
            eachCustomSizePropMap.put(propId, propValue);

            if (propId != null && propId.equals(propId_skuExtend_size)) {
                customSizeNameIdMap.put(propValue, sizeId);
            }
            allCustomSizePropMap.put(sizeId, eachCustomSizePropMap);
        }

        for (Map.Entry<String, CmsSkuPropBean> cmsSkuPropBeanEntry : sizeCmsSkuPropMap.entrySet()) {
            String size = cmsSkuPropBeanEntry.getKey();
            ComplexValue extendSizeComplexValue = new ComplexValue();

            extendSizeComplexValue.setInputFieldValue(propId_skuExtend_size, size);

            String sizeId = customSizeNameIdMap.get(size);
            if (sizeId == null) {
                logger.error("No customSize found for size:" + size);
                return null;
            }
            Map<String, String> customSizePropMap = allCustomSizePropMap.get(sizeId);

            for (Map.Entry<String, String> entry : customSizePropMap.entrySet())
            {
                extendSizeComplexValue.setInputFieldValue(entry.getKey(), entry.getValue());
            }

            complexValueList.add(extendSizeComplexValue);
        }

        sizeExtendProp.setComplexValues(complexValueList);
        return sizeExtendProp;
    }

    @Override
    public boolean isYourFood(List<PlatformPropBean> platformProps) {
        return init(platformProps);
    }

    @Override
    public List<Field> buildSkuInfoField(int cartId, String categoryCode, List<PlatformPropBean> platformProps, CmsModelPropBean cmsModelProp, PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        init(platformProps);
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;

        List<Field> skuInfoFields = new ArrayList<>();
        Field skuField = buildSkuProp(cartId, categoryCode, skuPlatformProp, cmsModelProp, tmallContextBuildCustomFields);
        skuInfoFields.add(skuField);

        if (propId_colorExtend != null) {
            Field colorExtendField = buildColorExtendProp(tmallContextBuildCustomFields, imageSet);
            skuInfoFields.add(colorExtendField);
        }
        if (propId_skuExtend != null) {
            Field skuExtendField = buildSizeExtendProp(tmallContextBuildCustomFields);
            skuInfoFields.add(skuExtendField);
        }
        return skuInfoFields;
    }

    @Override
    public int updateInventoryField(String orderChannelId,
                                    PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                                    List fields) {
        MultiComplexField skuProp = null;
        int totalInventory = 0;

        for (Field field : (List<Field>)fields)
        {
            if (propId_sku.equals(field.getId())) {
                skuProp = (MultiComplexField) field;
                break;
            }
        }

        if (skuProp != null)
        {
            for (ComplexValue complexValue : skuProp.getComplexValues()) {
                String skuOuterId = complexValue.getInputFieldValue(propId_sku_outerId);

                String skuQuantityStr = skuInfoDao.getSkuInventory(orderChannelId, null, skuOuterId);
                int skuQuantity = 0;
                if (skuQuantityStr != null) {
                    skuQuantity = Integer.valueOf(skuQuantityStr);
                }
                totalInventory += skuQuantity;
                complexValue.setInputFieldValue(propId_sku_quantity, String.valueOf(skuQuantity));
            }
        }
        return totalInventory;
    }

    @Override
    public void updateSkuPropImage(Map<String, String> urlMap, PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields) {
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;
        BuildSkuResult buildSkuResult = (BuildSkuResult) tmallContextBuildCustomFields.getBuildSkuResult();

        for (Map.Entry<String, ComplexValue> entry : buildSkuResult.getSrcUrlComplexValueMap().entrySet())
        {
            String srcUrl = entry.getKey();
            String destUrl = urlMap.get(srcUrl);

            ComplexValue complexValue = entry.getValue();
            complexValue.setInputFieldValue(propId_colorExtend_image, destUrl);
        }
    }
}