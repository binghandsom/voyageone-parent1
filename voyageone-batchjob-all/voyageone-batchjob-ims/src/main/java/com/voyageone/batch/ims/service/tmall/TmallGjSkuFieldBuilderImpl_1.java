package com.voyageone.batch.ims.service.tmall;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;
import com.taobao.top.schema.value.ComplexValue;
import com.taobao.top.schema.value.Value;
import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.bean.PlatformUploadRunState;
import com.voyageone.batch.ims.bean.TmallUploadRunState;
import com.voyageone.batch.ims.bean.tcb.UploadProductTcb;
import com.voyageone.batch.ims.modelbean.*;
import com.voyageone.batch.ims.service.AbstractSkuFieldBuilder;
import com.voyageone.batch.ims.service.SkuTemplateSchema;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Created by Leo on 15-7-14.
 * 参考天猫分类: 50014993
 * 线程安全: 是
 */
public class TmallGjSkuFieldBuilderImpl_1 extends AbstractSkuFieldBuilder{
    private String propId_sku;
    private String propId_skuExtend;

    private String propId_sku_size;
    private String propId_sku_price;
    private String propId_sku_quantity;
    private String propId_sku_outerId;
    private String propId_sku_barCode;

    private String propId_skuExtend_aliasname;
    private String propId_skuExtend_size;

    private String propId_custom_size1;
    private String propId_custom_size2;
    private String propId_custom_size3;
    private String propId_custom_size4;
    private String propId_custom_size5;

    private PlatformPropBean skuPlatformProp;
    private PlatformPropBean skuExtendPlatformProp;

    private boolean sizeIsSingleCheck;
    PlatformPropBean platformProp_sku_size;
    private int availableSizeIndex = 0;
    List<PlatformPropOptionBean> sizeOptions;

    private static Log logger = LogFactory.getLog(TmallGjSkuFieldBuilderImpl_1.class);

    private class BuildSkuResult {
        //Build sku prop result
        Map<String, CmsSkuPropBean> sizeCmsSkuPropMap;


        public BuildSkuResult() {
            sizeCmsSkuPropMap = new HashMap<>();
        }

        public Map<String, CmsSkuPropBean> getSizeCmsSkuPropMap() {
            return sizeCmsSkuPropMap;
        }

    }

    private boolean init(List<PlatformPropBean> platformProps) {
        for (PlatformPropBean platformProp : platformProps) {
            List<PlatformSkuInfoBean> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(platformProp.getPlatformPropId(), platformProp.getPlatformCartId());

            PlatformSkuInfoBean tmallSkuInfo = null;
            for (PlatformSkuInfoBean tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                    break;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + platformProp.getPlatformPropId() + ", cartId=" + platformProp.getPlatformCartId() + "]");
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
                propId_sku = platformProp.getPlatformPropId();
                skuPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE)) {
                propId_sku_size = platformProp.getPlatformPropId();
                sizeIsSingleCheck = platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_BARCODE)) {
                propId_sku_barCode = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_OUTERID)) {
                propId_sku_outerId = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_PRICE)) {
                propId_sku_price = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_QUANTITY)) {
                propId_sku_quantity = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_CUSTOM_SIZE1)) {
                propId_custom_size1 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_CUSTOM_SIZE2)) {
                propId_custom_size2 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_CUSTOM_SIZE3)) {
                propId_custom_size3 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_CUSTOM_SIZE4)) {
                propId_custom_size4 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_CUSTOM_SIZE5)) {
                propId_custom_size5 = platformProp.getPlatformPropId();
            }

            //EXTENDSIZE
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSIZE)) {
                skuExtendPlatformProp = platformProp;
                propId_skuExtend = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_SIZE)) {
                propId_skuExtend_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_ALIAS_NAME)) {
                propId_skuExtend_aliasname = platformProp.getPlatformPropId();
            }
        }
        if (propId_sku == null) {
            logger.warn(this.getClass().getName() + " requires sku!");
            return false;
        }
        if (propId_sku_size == null) {
            logger.warn(this.getClass().getName() + " requires sku size!");
            return false;
        }

        return true;
    }

    private void buildSkuSize(ComplexValue skuFieldValue, BuildSkuResult buildSkuResult, int cartId, CmsSkuPropBean cmsSkuProp, String categoryCode, String skuPlatforomPropHash) {
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
            String sizeValue = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku);
            skuFieldValue.setInputFieldValue(propId_sku_size, sizeValue);
            buildSkuResult.getSizeCmsSkuPropMap().put(sizeValue, cmsSkuProp);
        }
    }

    private Field buildSkuProp(int cartId, String categoryCode, PlatformPropBean platformProp, CmsModelPropBean cmsModelProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        BuildSkuResult buildSkuResult = new BuildSkuResult();
        contextBuildCustomFields.setBuildSkuResult(buildSkuResult);

        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();

        MultiComplexField skuField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        skuField.setId(propId_sku);

        SingleCheckField skuSizeField = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
        skuSizeField.setId(propId_sku_size);

        List<ComplexValue> complexValues = new ArrayList<>();
        for (CmsCodePropBean cmsCodeProp : cmsModelProp.getCmsCodePropBeanList()) {
            List<CmsSkuPropBean> cmsSkuPropBeans = cmsCodeProp.getCmsSkuPropBeanList();
            for (CmsSkuPropBean cmsSkuProp : cmsSkuPropBeans) {
                ComplexValue skuFieldValue = new ComplexValue();

                buildSkuSize(skuFieldValue, buildSkuResult, cartId, cmsSkuProp, categoryCode, platformProp.getPlatformPropHash());

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

                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size1);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size2);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size3);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size4);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_custom_size5);
                complexValues.add(skuFieldValue);
            }
        }

        skuField.setComplexValues(complexValues);
        return skuField;
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

            String sku = entry.getValue().getProp(CmsFieldEnum.CmsSkuEnum.sku);
            buildInputCustomProp(complexValue, workLoadBean.getOrder_channel_id(), sku, propId_skuExtend_aliasname);
            complexValues.add(complexValue);
        }
        sizeExtendField.setComplexValues(complexValues);
        return sizeExtendField;
    }

    private void buildInputCustomProp(ComplexValue skuFieldValue, String channelId, String sku, String propId_input_custom_size) {
        if (propId_input_custom_size != null && !"".equals(propId_input_custom_size.trim())) {
            String skuPropValue = skuPropValueDao.selectSkuPropValue(channelId, sku, propId_input_custom_size);
            if (skuPropValue == null) {
                skuPropValue = sku;
            }
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

        for (Field field : (List<Field>)fields) {
            if (propId_sku.equals(field.getId())) {
                skuProp = (MultiComplexField) field;
                break;
            }
        }

        if (skuProp != null) {
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
}
