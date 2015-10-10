package com.voyageone.batch.ims.service.tmall;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Leo on 15-7-14.
 * 参考天猫分类: 50014993
 * 线程安全: 是
 */
public class TmallGjSkuFieldBuilderImpl_3 extends AbstractSkuFieldBuilder
{
    private String propId_sku;
    private String propId_sku_necklace_length;
    private String propId_sku_pearl_diameter;
    private String propId_sku_color;
    private String propId_sku_price;
    private String propId_sku_quantity;
    private String propId_sku_outerId;
    private String propId_sku_barCode;

    private String propId_colorExtend;
    private String propId_colorExtend_aliasname;
    private String propId_colorExtend_color;
    private String propId_colorExtend_image;

    private PlatformPropBean skuPlatformProp;
    private PlatformPropBean colorExtendPlatformProp;

    private static Log logger = LogFactory.getLog(TmallGjSkuFieldBuilderImpl_3.class);

    private boolean init(List<PlatformPropBean> platformProps) {
        for (PlatformPropBean platformProp : platformProps) {
            List<PlatformSkuInfoBean> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(platformProp.getPlatformPropId(), platformProp.getPlatformCartId());

            PlatformSkuInfoBean tmallSkuInfo = null;
            for (PlatformSkuInfoBean tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX)
                {
                    tmallSkuInfo = tmallSkuInfoEach;
                }
            }
            if (tmallSkuInfo == null)
            {
                logger.warn("No sku info find for platformProp: [prop_id=" + platformProp.getPlatformPropId() + ", cartId=" + platformProp.getPlatformCartId() + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_3.class.getName() + " is not applicable!");
                return false;
            }

            int fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if (fieldType < (1 << SkuTemplateSchema.SkuTemplate_3_Schema.FIELD_BIT_MIN)
                    || fieldType > (1<< (SkuTemplateSchema.SkuTemplate_3_Schema.FIELD_BIT_MAX + 1)))
                return false;

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU))
            {
                propId_sku = platformProp.getPlatformPropId();
                skuPlatformProp = platformProp;
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_NECKLANCE_LENGTH)) {
                propId_sku_necklace_length = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_PEARL_DIAMTER)) {
                propId_sku_pearl_diameter = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_COLOR)) {
                propId_sku_color = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_BARCODE)) {
                propId_sku_barCode = platformProp.getPlatformPropId();
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

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR)) {
                propId_colorExtend = platformProp.getPlatformPropId();
                colorExtendPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_ALIASNAME)) {
                propId_colorExtend_aliasname = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_COLOR)) {
                propId_colorExtend_color = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_IMAGE)) {
                propId_colorExtend_image = platformProp.getPlatformPropId();
            }
        }

        return true;
    }

    private Field buildSkuProp(int cartId, String categoryCode, PlatformPropBean platformProp, List<String> excludeColorValues, CmsModelPropBean cmsModelProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();
        Map<String, CmsCodePropBean> usingColorMap = contextBuildCustomFields.getUsingColorMap();

        MultiComplexField skuField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        if (propId_sku == null || !propId_sku.equals(platformProp.getPlatformPropId())) {
            return null;
        }
        skuField.setId(propId_sku);

        SingleCheckField colorCategoryField = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
        colorCategoryField.setId(propId_sku_color);

        PlatformPropBean platformProp_colorCategory = platformPropDao.selectPlatformPropByPropId(cartId, categoryCode, propId_sku_color, platformProp.getPlatformPropHash());
        List<PlatformPropOptionBean> colorOptions = platformPropDao.selectPlatformOptionsByPropHash(platformProp_colorCategory.getPlatformPropHash());

        List<String> availableColorValues = new ArrayList<>();

        //填充availableColorValue
        for (PlatformPropOptionBean option : colorOptions)
        {
            String optionValue = option.getPlatformPropOptionValue();
            if (excludeColorValues == null)
                availableColorValues.add(optionValue);
            else if (!excludeColorValues.contains(optionValue))
            {
                availableColorValues.add(optionValue);
            }
        }

        List<CmsCodePropBean> cmsCodeProps = cmsModelProp.getCmsCodePropBeanList();

        List<ComplexValue> complexValues = new ArrayList<>();
        for (int indexColor = 0; indexColor < cmsCodeProps.size(); indexColor++)
        {
            CmsCodePropBean cmsCodeProp = cmsCodeProps.get(indexColor);


            //获取code级别的颜色属性
            String colorValue;
            if (indexColor < availableColorValues.size())
            {
                colorValue = availableColorValues.get(indexColor);
            } else {
                logger.error("No available color!");
                return null;
            }

            usingColorMap.put(colorValue, cmsCodeProp);

            for (int indexSize = 0; indexSize < cmsCodeProp.getCmsSkuPropBeanList().size(); indexSize++)
            {
                CmsSkuPropBean cmsSkuProp = cmsCodeProp.getCmsSkuPropBeanList().get(indexSize);
                //设置code级别的颜色属性
                ComplexValue skuFieldValue = new ComplexValue();
                skuFieldValue.setSingleCheckFieldValue(propId_sku_color, new Value(colorValue));

                //珍珠直径
                String pearlDiameterValue = skuPropValueDao.selectSkuPropValue(workLoadBean.getOrder_channel_id(),
                            cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku), SkuPropNameEnum.pearl_diameter);
                skuFieldValue.setInputFieldValue(propId_sku_pearl_diameter, pearlDiameterValue);

                //项链长度
                String necklaceLengthValue = skuPropValueDao.selectSkuPropValue(workLoadBean.getOrder_channel_id(),
                        cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku), SkuPropNameEnum.necklace_length);
                if (propId_sku_necklace_length != null) {
                    skuFieldValue.setInputFieldValue(propId_sku_necklace_length, necklaceLengthValue);
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
    @Override
    public boolean isYourFood(List<PlatformPropBean> platformProps) {
        return init(platformProps);
    }

    @Override
    public List<Field> buildSkuInfoField(int cartId, String categoryCode, List<PlatformPropBean> platformProps, List<String> excludeColorValues, CmsModelPropBean cmsModelProp, PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        init(platformProps);
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;

        Field skuField = buildSkuProp(cartId, categoryCode, skuPlatformProp, excludeColorValues, cmsModelProp, tmallContextBuildCustomFields);
        Field colorExtendField = buildColorExtendProp(colorExtendPlatformProp, tmallContextBuildCustomFields, imageSet);

        List<Field> skuInfoFields = new ArrayList<>();
        skuInfoFields.add(skuField);
        skuInfoFields.add(colorExtendField);

        return skuInfoFields;
    }

    private Field buildColorExtendProp(PlatformPropBean platformProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        Map<String, ComplexValue> srcUrlColorExtendValueMap = contextBuildCustomFields.getSrcUrlExtendCodePropMap();
        Map<String, CmsCodePropBean> usingColorMap = contextBuildCustomFields.getUsingColorMap();
        MultiComplexField colorExtendField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        if (!propId_colorExtend.equals(platformProp.getPlatformPropId())) {
            return null;
        }
        colorExtendField.setId(propId_colorExtend);

        List<ComplexValue> complexValues = new ArrayList<>();
        for (Map.Entry<String, CmsCodePropBean> entry : usingColorMap.entrySet())
        {
            CmsCodePropBean cmsCodeProp = entry.getValue();

            ComplexValue complexValue = new ComplexValue();
            complexValue.setSingleCheckFieldValue(propId_colorExtend_color, new Value(entry.getKey()));
            //TODO 暂时使用code作为别名
            complexValue.setInputFieldValue(propId_colorExtend_aliasname, cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.code));

            //颜色图使用竖图
            String propImageStr = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.product_image);
            if (propImageStr != null && !"".equals(propImageStr)) {
                String propImages[] = propImageStr.split(",");
                String propImage = propImages[0];
                String codePropFullImageUrl = String.format(codeImageTemplate, propImage);

                complexValue.setInputFieldValue(propId_colorExtend_image, codePropFullImageUrl);
                imageSet.add(codePropFullImageUrl);
                if (srcUrlColorExtendValueMap != null)
                {
                    srcUrlColorExtendValueMap.put(codePropFullImageUrl, complexValue);
                }
            }
            complexValues.add(complexValue);
        }
        colorExtendField.setComplexValues(complexValues);
        return colorExtendField;
    }
    @Override
    public int updateInventoryField(String orderChannelId,
                                    PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                                    List fields) {
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;
        Map<String, CmsCodePropBean> usingColorMap = tmallContextBuildCustomFields.getUsingColorMap();

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
                String sku = complexValue.getInputFieldValue(propId_sku_outerId);

                String skuColor = complexValue.getSingleCheckFieldValue(propId_sku_color).getValue();
                CmsCodePropBean cmsCodePropBean = usingColorMap.get(skuColor);

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
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;
        Map<String, ComplexValue>  srcUrlColorExtendValueMap = tmallContextBuildCustomFields.getSrcUrlExtendCodePropMap();

        for (Map.Entry<String,String> urlEntry : urlMap.entrySet())
        {
            String srcUrl = urlEntry.getKey();
            String destUrl = urlEntry.getValue();

            ComplexValue complexValue = srcUrlColorExtendValueMap.get(srcUrl);
            //注意，此处complexValue可能为空，因为srcUrl可以是其他字段（非SKU）的图片
            if (complexValue != null) {
                complexValue.setInputFieldValue(propId_colorExtend_image, destUrl);
            }
        }
    }
}
