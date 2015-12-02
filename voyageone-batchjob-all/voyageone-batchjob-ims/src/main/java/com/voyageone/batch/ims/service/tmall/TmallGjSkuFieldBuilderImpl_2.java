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
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Created by Leo on 15-7-14.
 * 参考天猫分类: 50014993
 * 线程安全: 否
 */
public class TmallGjSkuFieldBuilderImpl_2 extends AbstractSkuFieldBuilder {
    private String propId_sku;
    private String propId_colorExtend;
    private String propId_skuExtend;

    private String propId_sku_color;
    private String propId_sku_price;
    private String propId_sku_quantity;
    private String propId_sku_outerId;
    private String propId_sku_barCode;
    private String propId_sku_size;

    private String propId_custom_size1;
    private String propId_custom_size2;
    private String propId_custom_size3;
    private String propId_custom_size4;
    private String propId_custom_size5;

    private String propId_colorExtend_aliasname;
    private String propId_colorExtend_color;
    private String propId_colorExtend_image;

    private String propId_skuExtend_aliasname;
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
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                    break;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + platformProp.getPlatformPropId() + ", cartId=" + platformProp.getPlatformCartId() + "]");
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
                propId_sku = platformProp.getPlatformPropId();
                skuPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE)) {
                propId_sku_size = platformProp.getPlatformPropId();
                sizeIsSingleCheck = platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_BARCODE)) {
                propId_sku_barCode = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_COLOR)) {
                propId_sku_color = platformProp.getPlatformPropId();
                colorIsSingleCheck = platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_OUTERID)) {
                propId_sku_outerId = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_PRICE)) {
                propId_sku_price = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_QUANTITY)) {
                propId_sku_quantity = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE1)) {
                propId_custom_size1 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE2)) {
                propId_custom_size2 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE3)) {
                propId_custom_size3 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE4)) {
                propId_custom_size4 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_CUSTOM_SIZE5)) {
                propId_custom_size5 = platformProp.getPlatformPropId();
            }
            //EXTENDCOLOR
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR)) {
                propId_colorExtend = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_ALIASNAME)) {
                propId_colorExtend_aliasname = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_COLOR)) {
                propId_colorExtend_color = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_IMAGE)) {
                propId_colorExtend_image = platformProp.getPlatformPropId();
            }

            //EXTENDSIZE
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE)) {
                propId_skuExtend = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE)) {
                propId_skuExtend_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_ALIASNAME)) {
                propId_skuExtend_aliasname = platformProp.getPlatformPropId();
            }
        }
        if (propId_sku == null) {
            logger.warn(this.getClass().getName() + " requires sku!");
            return false;
        }
        if (propId_sku_size == null) {
            if (propId_custom_size1 == null
                    && propId_custom_size2 == null
                    && propId_custom_size3 == null
                    && propId_custom_size4 == null
                    && propId_custom_size5 == null ) {
                logger.warn(this.getClass().getName() + " requires sku size!");
                return false;
            } else {
                if (propId_custom_size5 != null) {
                    propId_sku_size = propId_custom_size5;
                    propId_custom_size5 = null;
                } else if (propId_custom_size4 != null) {
                    propId_sku_size = propId_custom_size4;
                    propId_custom_size4 = null;
                }  else if (propId_custom_size3 != null) {
                    propId_sku_size = propId_custom_size3;
                    propId_custom_size3 = null;
                }  else if (propId_custom_size2 != null) {
                    propId_sku_size = propId_custom_size2;
                    propId_custom_size2 = null;
                }  else if (propId_custom_size1 != null) {
                    propId_sku_size = propId_custom_size1;
                    propId_custom_size1 = null;
                }
            }
        }
        if (propId_sku_color == null) {
            logger.warn(this.getClass().getName() + " requires sku color!");
            return false;
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
            String sizeValue = sizeOptions.get(availableSizeIndex++).getPlatformPropOptionValue();
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

               buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size1);
               buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size2);
               buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size3);
               buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size4);
               buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size5);

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
            //TODO
            complexValue.setInputFieldValue(propId_colorExtend_aliasname, cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.code));

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
        BuildSkuResult buildSkuResult = (BuildSkuResult) contextBuildCustomFields.getBuildSkuResult();
        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();

        MultiComplexField sizeExtendField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        sizeExtendField.setId(propId_skuExtend);

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsSkuPropBean> entry : buildSkuResult.getSizeCmsSkuPropMap().entrySet())
        {
            ComplexValue complexValue = new ComplexValue();
            if (sizeIsSingleCheck) {
                complexValue.setSingleCheckFieldValue(propId_skuExtend_size, new Value(entry.getKey()));
            } else {
                complexValue.setInputFieldValue(propId_skuExtend_size, entry.getKey());
            }

            if (propId_skuExtend_aliasname != null) {
                String sku = entry.getValue().getProp(CmsFieldEnum.CmsSkuEnum.sku);
                String skuSize = entry.getValue().getProp(CmsFieldEnum.CmsSkuEnum.size);

                //别名的设置，如果size在ims_bt_prop_value_sku中存在，那么使用该值，否则，使用sku作为别名
                String skuPropValue = skuPropValueDao.selectSkuPropValue(workLoadBean.getOrder_channel_id(), sku, propId_sku_size);
                if (skuPropValue == null) {
                    skuPropValue = skuSize;
                }
                complexValue.setInputFieldValue(propId_skuExtend_aliasname, skuPropValue);
            }
            complexValues.add(complexValue);
        }
        sizeExtendField.setComplexValues(complexValues);
        return sizeExtendField;
    }

    private void buildInputCustomProp(ComplexValue skuFieldValue, String channelId, String sku, String propId_input_custom_size) {
        if (propId_input_custom_size != null && !"".equals(propId_input_custom_size.trim())) {
            String skuPropValue = skuPropValueDao.selectSkuPropValue(channelId, sku, propId_input_custom_size);
            skuFieldValue.setInputFieldValue(propId_input_custom_size, skuPropValue);
        }
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