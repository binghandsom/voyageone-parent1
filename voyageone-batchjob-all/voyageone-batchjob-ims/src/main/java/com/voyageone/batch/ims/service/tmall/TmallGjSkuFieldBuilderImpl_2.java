package com.voyageone.batch.ims.service.tmall;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.value.ComplexValue;
import com.taobao.top.schema.value.Value;
import com.voyageone.batch.ims.bean.PlatformUploadRunState;
import com.voyageone.batch.ims.bean.TmallUploadRunState;
import com.voyageone.batch.ims.bean.tcb.UploadProductTcb;
import com.voyageone.batch.ims.enums.SkuPropNameEnum;
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
public class TmallGjSkuFieldBuilderImpl_2 extends AbstractSkuFieldBuilder {
    private String propId_sku;
    private String propId_sku_necklace_length;
    private String propId_sku_ring_size;
    private String propId_sku_price;
    private String propId_sku_quantity;
    private String propId_sku_outerId;
    private String propId_sku_barCode;

    private String propId_ring_size_extend;
    private String propId_ring_size_extend_aliasname;
    private String propId_ring_size_extend_size;

    private PlatformPropBean skuPlatformProp;
    private PlatformPropBean ringSizeExtendPlatformProp;

    private static Log logger = LogFactory.getLog(TmallGjSkuFieldBuilderImpl_2.class);

    private boolean init(List<PlatformPropBean> platformProps) {
        for (PlatformPropBean platformProp : platformProps) {
            List<PlatformSkuInfoBean> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(platformProp.getPlatformPropId(), platformProp.getPlatformCartId());

            PlatformSkuInfoBean tmallSkuInfo = null;
            for (PlatformSkuInfoBean tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX)
                {
                    tmallSkuInfo = tmallSkuInfoEach;
                }
            }
            if (tmallSkuInfo == null)
            {
                logger.warn("No sku info find for platformProp: [prop_id=" +  platformProp.getPlatformPropId() + ", cartId=" + platformProp.getPlatformCartId() + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_2.class.getName() + " is not applicable!");
                return false;
            }

            int fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if (fieldType < (1 << SkuTemplateSchema.SkuTemplate_2_Schema.FIELD_BIT_MIN)
                    || fieldType > (1<< (SkuTemplateSchema.SkuTemplate_2_Schema.FIELD_BIT_MAX + 1)))
                return false;

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU))
            {
                propId_sku = platformProp.getPlatformPropId();
                skuPlatformProp = platformProp;
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_NECKLANCE_LENGTH)) {
                propId_sku_necklace_length = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_RING_SIZE)) {
                propId_sku_ring_size = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_BARCODE)) {
                propId_sku_barCode = platformProp.getPlatformPropId();
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

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTEND_RING_SIZE)) {
                propId_ring_size_extend = platformProp.getPlatformPropId();
                ringSizeExtendPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTEND_RING_SIZE_SIZE)) {
                propId_ring_size_extend_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTEND_RING_SIZE_ALIASNAME)) {
                propId_ring_size_extend_aliasname = platformProp.getPlatformPropId();
            }
        }

        return true;
    }

    private Field buildSkuProp(int platformId, String categoryCode, PlatformPropBean platformProp, List<String> excludeRingSizeValues, CmsModelPropBean cmsModelProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();
        Map<String, Map.Entry<CmsCodePropBean, CmsSkuPropBean>> usingRingSizeMap = contextBuildCustomFields.getUsingRingSizeMap();

        MultiComplexField skuField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        if (propId_sku == null || !propId_sku.equals(platformProp.getPlatformPropId())) {
            return null;
        }
        skuField.setId(propId_sku);

        InputField necklaceLengthField = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
        necklaceLengthField.setId(propId_sku_necklace_length);

        PlatformPropBean platformProp_ringSize = platformPropDao.selectPlatformPropByPropId(platformId, categoryCode, propId_sku_ring_size, platformProp.getPlatformPropHash());
        List<PlatformPropOptionBean> ringSizeOptions = platformPropDao.selectPlatformOptionsByPropHash(platformProp_ringSize.getPlatformPropHash());

        List<String> availableRingSizeValues = new ArrayList<>();

        //填充availableRingSizeValue
        for (PlatformPropOptionBean option : ringSizeOptions)
        {
            String optionValue = option.getPlatformPropOptionValue();
            if (excludeRingSizeValues == null)
                availableRingSizeValues.add(optionValue);
            else if (!excludeRingSizeValues.contains(optionValue))
            {
                availableRingSizeValues.add(optionValue);
            }
        }

        List<CmsCodePropBean> cmsCodeProps = cmsModelProp.getCmsCodePropBeanList();

        List<ComplexValue> complexValues = new ArrayList<>();

        String orderChannelId = workLoadBean.getOrder_channel_id();
        int indexRingSize = 0;
        //不同的code上传到不同的尺码上
        for (int indexCode = 0; indexCode < cmsCodeProps.size(); indexCode++)
        {
            CmsCodePropBean cmsCodeProp = cmsCodeProps.get(indexCode);

            for (int indexSize = 0; indexSize < cmsCodeProp.getCmsSkuPropBeanList().size(); indexSize++, indexRingSize++)
            {
                CmsSkuPropBean cmsSkuProp = cmsCodeProp.getCmsSkuPropBeanList().get(indexSize);
                ComplexValue skuFieldValue = new ComplexValue();
                String ringSize;
                //获取code级别的ringSize属性
                if (indexRingSize < availableRingSizeValues.size())
                {
                    ringSize = availableRingSizeValues.get(indexRingSize);
                } else {
                    logger.error("No available ring size!");
                    return null;
                }

                Map.Entry<CmsCodePropBean, CmsSkuPropBean> entry = new AbstractMap.SimpleEntry<>( cmsCodeProp, cmsSkuProp);
                usingRingSizeMap.put(ringSize, entry);

                skuFieldValue.setSingleCheckFieldValue(propId_sku_ring_size, new Value(ringSize));
                if (propId_sku_necklace_length != null) {
                    String necklace_length = skuPropValueDao.selectSkuPropValue(orderChannelId, cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku), SkuPropNameEnum.necklace_length);
                    skuFieldValue.setInputFieldValue(propId_sku_necklace_length, necklace_length);
                }

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
                complexValues.add(skuFieldValue);
            }
        }
        skuField.setComplexValues(complexValues);
        return skuField;
    }

    private Field buildRingSizeExtendProp(PlatformPropBean platformProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        UploadProductTcb tcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoad = tcb.getWorkLoadBean();
        Map<String, Map.Entry<CmsCodePropBean, CmsSkuPropBean>> usingRingSizeMap = contextBuildCustomFields.getUsingRingSizeMap();
        MultiComplexField ringSizeExtendField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        if (!propId_ring_size_extend.equals(platformProp.getPlatformPropId())) {
            return null;
        }
        ringSizeExtendField.setId(propId_ring_size_extend);

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, Map.Entry<CmsCodePropBean, CmsSkuPropBean>> entry : usingRingSizeMap.entrySet())
        {
            CmsCodePropBean cmsCodeProp = entry.getValue().getKey();
            CmsSkuPropBean cmsSkuProp = entry.getValue().getValue();

            ComplexValue complexValue = new ComplexValue();
            complexValue.setSingleCheckFieldValue(propId_ring_size_extend_size, new Value(entry.getKey()));

            //TODO 暂时使用code+'_'+size作为别名
            String aliasName = skuPropValueDao.selectSkuPropValue(workLoad.getOrder_channel_id(), cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku), SkuPropNameEnum.ring_size);
            complexValue.setInputFieldValue(propId_ring_size_extend_aliasname, aliasName);
            complexValues.add(complexValue);
        }
        ringSizeExtendField.setComplexValues(complexValues);
        return ringSizeExtendField;
    }

    @Override
    public boolean isYourFood(List<PlatformPropBean> platformProps) {
        return init(platformProps);
    }

    @Override
    public List<Field> buildSkuInfoField(int cartId, String categoryCode, List<PlatformPropBean> platformProps, List<String> excludeColorValues, CmsModelPropBean cmsModelProp, PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        init(platformProps);
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;

        Field skuField = buildSkuProp(cartId, categoryCode, skuPlatformProp, excludeColorValues, cmsModelProp, tmallContextBuildCustomFields);
        Field ringSizeExtendField = buildRingSizeExtendProp(ringSizeExtendPlatformProp, tmallContextBuildCustomFields, imageSet);

        Field skuExtendField = null;

        List<Field> skuInfoFields = new ArrayList<>();
        skuInfoFields.add(skuField);
        skuInfoFields.add(ringSizeExtendField);
        if (skuExtendField != null)
            skuInfoFields.add(skuExtendField);
        return skuInfoFields;
    }

    @Override
    public int updateInventoryField(String orderChannelId,
                                     PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                                     List fields) {
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;
        Map<String, Map.Entry<CmsCodePropBean, CmsSkuPropBean>> usingRingSizeMap = tmallContextBuildCustomFields.getUsingRingSizeMap();

        List<Field> tmallFields = fields;
        MultiComplexField skuProp = null;
        int totalInventory = 0;

        for (Field field : tmallFields)
        {
            if (propId_sku.equals(field.getId())) {
                skuProp = (MultiComplexField) field;
                break;
            }
        }

        if (skuProp != null)
        {
            for (ComplexValue complexValue : skuProp.getComplexValues()) {
                String skuRingSize = complexValue.getSingleCheckFieldValue(propId_sku_ring_size).getValue();
                CmsCodePropBean cmsCodePropBean = usingRingSizeMap.get(skuRingSize).getKey();
                CmsSkuPropBean cmsSkuPropBean = usingRingSizeMap.get(skuRingSize).getValue();
                String sku = cmsSkuPropBean.getProp(CmsFieldEnum.CmsSkuEnum.sku);

                String code = cmsCodePropBean.getProp(CmsFieldEnum.CmsCodeEnum.code);

                String skuQuantityStr = skuInfoDao.getSkuInventory(orderChannelId, code, sku);
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
        logger.warn("模板" + this.getClass().getSimpleName() + "不支持更新sku属性图片的操作!");
    }
}
