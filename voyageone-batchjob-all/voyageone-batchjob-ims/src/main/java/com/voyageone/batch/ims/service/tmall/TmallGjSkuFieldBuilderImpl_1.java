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
public class TmallGjSkuFieldBuilderImpl_1 extends AbstractSkuFieldBuilder{
    private String propId_sku;
    private String propId_colorExtend;
    private String propId_skuExtend;

    private String propId_sku_color;
    private String propId_sku_size;
    private String propId_sku_price;
    private String propId_sku_quantity;
    private String propId_sku_outerId;
    private String propId_sku_barCode;

    private String propId_skuExtend_aliasname;
    private String propId_skuExtend_size;
    private String propId_colorExtend_aliasname;
    private String propId_colorExtend_color;
    private String propId_colorExtend_image;

    private String propId_input_custom_size1;
    private String propId_input_custom_size2;
    private String propId_input_custom_size3;
    private String propId_input_custom_size4;
    private String propId_input_custom_size5;

    private String propId_select_custom_size1;
    private String propId_select_custom_size2;
    private String propId_select_custom_size3;
    private String propId_select_custom_size4;
    private String propId_select_custom_size5;

    private PlatformPropBean skuPlatformProp;
    private PlatformPropBean colorExtendPlatformProp;
    private PlatformPropBean skuExtendPlatformProp;

    private static Log logger = LogFactory.getLog(TmallGjSkuFieldBuilderImpl_1.class);

    private boolean init(List<PlatformPropBean> platformProps) {
        for (PlatformPropBean platformProp : platformProps) {
            List<PlatformSkuInfoBean> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(platformProp.getPlatformPropId(), platformProp.getPlatformCartId());

            PlatformSkuInfoBean tmallSkuInfo = null;
            for (PlatformSkuInfoBean tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + platformProp.getPlatformPropId() + ", cartId=" + platformProp.getPlatformCartId() + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_1.class.getName() + " is not applicable!");
                return false;
            }

            int fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if (fieldType < (1 << SkuTemplateSchema.SkuTemplate_1_Schema.FIELD_BIT_MIN)
                    || fieldType > (1 << (SkuTemplateSchema.SkuTemplate_1_Schema.FIELD_BIT_MAX + 1)))
                return false;

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU)) {
                propId_sku = platformProp.getPlatformPropId();
                skuPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE)) {
                propId_sku_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_BARCODE)) {
                propId_sku_barCode = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_COLOR)) {
                propId_sku_color = platformProp.getPlatformPropId();
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
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDCOLOR)) {
                propId_colorExtend = platformProp.getPlatformPropId();
                colorExtendPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDCOLOR_ALIASNAME)) {
                propId_colorExtend_aliasname = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDCOLOR_COLOR)) {
                propId_colorExtend_color = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDCOLOR_IMAGE)) {
                propId_colorExtend_image = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSIZE)) {
                skuExtendPlatformProp = platformProp;
                propId_skuExtend = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSIZE_SIZE)) {
                propId_skuExtend_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSIZE_ALIASNAME)) {
                propId_skuExtend_aliasname = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.INPUT_CUSTOM_SIZE_1)) {
                propId_input_custom_size1 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.INPUT_CUSTOM_SIZE_2)) {
                propId_input_custom_size2 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.INPUT_CUSTOM_SIZE_3)) {
                propId_input_custom_size3 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.INPUT_CUSTOM_SIZE_4)) {
                propId_input_custom_size4 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.INPUT_CUSTOM_SIZE_5)) {
                propId_input_custom_size5 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SELECT_CUSTOM_SIZE_1)) {
                propId_select_custom_size1 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SELECT_CUSTOM_SIZE_2)) {
                propId_select_custom_size2 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SELECT_CUSTOM_SIZE_3)) {
                propId_select_custom_size3 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SELECT_CUSTOM_SIZE_4)) {
                propId_select_custom_size4 = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_1_Schema.SELECT_CUSTOM_SIZE_5)) {
                propId_select_custom_size5 = platformProp.getPlatformPropId();
            }
        }

        return true;
    }

    private Field buildSkuProp(int platformId, String categoryCode, PlatformPropBean platformProp, List<String> excludeColorValues, CmsModelPropBean cmsModelProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();

        Map<String, CmsCodePropBean> usingColorMap = contextBuildCustomFields.getUsingColorMap();
        Map<String, CmsSkuPropBean> usingSkuMap = contextBuildCustomFields.getUsingSkuMap();

        MultiComplexField skuField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        if (propId_sku == null || !propId_sku.equals(platformProp.getPlatformPropId())) {
            return null;
        }
        skuField.setId(propId_sku);

        SingleCheckField colorCategoryField = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
        colorCategoryField.setId(propId_sku_color);

        PlatformPropBean platformProp_colorCategory = null;
        List<PlatformPropOptionBean> colorOptions = null;
        if (propId_sku_color != null && !"".equals(propId_sku_color.trim())) {
            platformProp_colorCategory = platformPropDao.selectPlatformPropByPropId(platformId, categoryCode, propId_sku_color, platformProp.getPlatformPropHash());
            colorOptions = platformPropDao.selectPlatformOptionsByPropHash(platformProp_colorCategory.getPlatformPropHash());
        }

        PlatformPropBean platformProp_size;
        List<PlatformPropOptionBean> sizeOptions = null;

        if (propId_sku_size != null && !"".equals(propId_sku_size)) {
            platformProp_size = platformPropDao.selectPlatformPropByPropId(platformId, categoryCode, propId_sku_size, platformProp.getPlatformPropHash());
             sizeOptions = platformPropDao.selectPlatformOptionsByPropHash(platformProp_size.getPlatformPropHash());
        }
        List<String> availableColorValues = new ArrayList<>();
        List<String> availableSizeValues = new ArrayList<>();

        //填充availableColorValue
        if (colorOptions != null) {
            for (PlatformPropOptionBean option : colorOptions) {
                String optionValue = option.getPlatformPropOptionValue();
                if (excludeColorValues == null)
                    availableColorValues.add(optionValue);
                else if (!excludeColorValues.contains(optionValue)) {
                    availableColorValues.add(optionValue);
                }
            }
        }

        //填充availableSizeValue
        if (sizeOptions != null) {
            for (PlatformPropOptionBean option : sizeOptions) {
                String optionValue = option.getPlatformPropOptionValue();
                availableSizeValues.add(optionValue);
            }
        }

        List<CmsCodePropBean> cmsCodeProps = cmsModelProp.getCmsCodePropBeanList();

        List<ComplexValue> complexValues = new ArrayList<>();
        int indexColor = 0, indexSize = 0;

        for (; indexColor < cmsCodeProps.size(); indexColor++)
        {
            CmsCodePropBean cmsCodeProp = cmsCodeProps.get(indexColor);


            //获取code级别的颜色属性
            String colorValue;
            if (availableColorValues.size() == 0) {
                colorValue = null;
            } else if (indexColor < availableColorValues.size())
            {
                colorValue = availableColorValues.get(indexColor);
                usingColorMap.put(colorValue, cmsCodeProp);
            } else {
                logger.error("No available color!");
                return null;
            }

            //如果颜色扩展存在，那么indexSize对每个颜色从0开始，否则，code挂在sku上，indexSize不置为0
            if (propId_colorExtend != null) {
                indexSize = 0;
            }

            int i=0;
            for (; i < cmsCodeProp.getCmsSkuPropBeanList().size(); i++)
            {
                CmsSkuPropBean cmsSkuProp = cmsCodeProp.getCmsSkuPropBeanList().get(i);
                //设置code级别的颜色属性
                ComplexValue skuFieldValue = new ComplexValue();
                if (propId_sku_color != null && !"".equals(propId_sku_color.trim())) {
                    skuFieldValue.setSingleCheckFieldValue(propId_sku_color, new Value(colorValue));
                }

                //设置sku级别的尺寸属性
                if (propId_sku_size != null) {
                    String size;
                    if (indexSize + i < availableSizeValues.size()) {
                        size = availableSizeValues.get(indexSize + i);
                    } else {
                        logger.error("no available size!");
                        return null;
                    }
                    usingSkuMap.put(size, cmsSkuProp);

                    skuFieldValue.setSingleCheckFieldValue(propId_sku_size, new Value(size));
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

                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_input_custom_size1);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_input_custom_size2);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_input_custom_size3);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_input_custom_size4);
                buildInputCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_input_custom_size5);

                buildSelectCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_select_custom_size1);
                buildSelectCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_select_custom_size2);
                buildSelectCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_select_custom_size3);
                buildSelectCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_select_custom_size4);
                buildSelectCustomProp(skuFieldValue, workLoadBean.getOrder_channel_id(), skuOuterId, propId_select_custom_size5);

                complexValues.add(skuFieldValue);
            }
            indexSize += i;
        }
        skuField.setComplexValues(complexValues);
        return skuField;
    }

    private void buildInputCustomProp(ComplexValue skuFieldValue, String channelId, String sku, String propId_input_custom_size) {
        if (propId_input_custom_size != null && !"".equals(propId_input_custom_size.trim())) {
            String skuPropValue = skuPropValueDao.selectSkuPropValue(channelId, sku, propId_input_custom_size);
            skuFieldValue.setInputFieldValue(propId_input_custom_size, skuPropValue);
        }
    }

    private void buildSelectCustomProp(ComplexValue skuFieldValue, String channelId, String sku, String propId_select_custom_size) {
        if (propId_select_custom_size != null && !"".equals(propId_select_custom_size.trim())) {
            String skuPropValue = skuPropValueDao.selectSkuPropValue(channelId, sku, propId_select_custom_size);
            skuFieldValue.setInputFieldValue(propId_select_custom_size, skuPropValue);
        }
    }

    private Field buildColorExtendProp(PlatformPropBean platformProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        Map<String, ComplexValue> srcUrlColorExtendValueMap = contextBuildCustomFields.getSrcUrlExtendCodePropMap();
        Map<String, CmsCodePropBean> usingColorMap = contextBuildCustomFields.getUsingColorMap();
        MultiComplexField colorExtendField = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        if (propId_colorExtend == null || !propId_colorExtend.equals(platformProp.getPlatformPropId())) {
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

    private Field buildSkuExtendProp(PlatformPropBean platformProp, TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields) {
        UploadProductTcb tcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoad = tcb.getWorkLoadBean();

        MultiComplexField sizeExtendProp = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        Map<String, CmsSkuPropBean> usingSkuMap = contextBuildCustomFields.getUsingSkuMap();

        if (!propId_skuExtend.equals(platformProp.getPlatformPropId())) {
            return null;
        }
        sizeExtendProp.setId(propId_skuExtend);

        List<ComplexValue> complexValueList = new ArrayList<>();


        for (Map.Entry<String, CmsSkuPropBean> cmsSkuPropBeanEntry : usingSkuMap.entrySet()) {
            String size = cmsSkuPropBeanEntry.getKey();
            CmsSkuPropBean cmsSkuProp = cmsSkuPropBeanEntry.getValue();

            ComplexValue extendSizeComplexValue = new ComplexValue();

            extendSizeComplexValue.setSingleCheckFieldValue(propId_skuExtend_size, new Value(size));

            String sku = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku);
            //TODO 这个size将来可能还会在进一步处理，从sizeMapping查出让国人容易读懂的尺码
            String customSize = skuPropValueDao.selectSkuPropValue(workLoad.getOrder_channel_id(), sku, propId_sku_size);
            if (customSize == null || "".equals(customSize)) {
                customSize = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku);
            }
            extendSizeComplexValue.setInputFieldValue(propId_skuExtend_aliasname, customSize);

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
    public List<Field> buildSkuInfoField(int cartId, String categoryCode, List<PlatformPropBean> platformProps, List<String> excludeColorValues, CmsModelPropBean cmsModelProp, PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields, Set<String> imageSet) {
        init(platformProps);
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;

        List<Field> skuInfoFields = new ArrayList<>();
        Field skuField = buildSkuProp(cartId, categoryCode, skuPlatformProp, excludeColorValues, cmsModelProp, tmallContextBuildCustomFields);

        if (propId_colorExtend != null) {
            Field colorExtendField = buildColorExtendProp(colorExtendPlatformProp, tmallContextBuildCustomFields, imageSet);
            skuInfoFields.add(colorExtendField);
        }

        if (skuExtendPlatformProp != null) {
            Field skuExtendField = buildSkuExtendProp(skuExtendPlatformProp, tmallContextBuildCustomFields);
            skuInfoFields.add(skuExtendField);
        }

        skuInfoFields.add(skuField);
        return skuInfoFields;
    }

    @Override
    public int updateInventoryField(String orderChannelId,
                                     PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                                     List fields) {
        TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = (TmallUploadRunState.TmallContextBuildCustomFields) contextBuildCustomFields;
        Map<String, CmsSkuPropBean> usingSkuMap = tmallContextBuildCustomFields.getUsingSkuMap();
        Map<String, CmsCodePropBean> usingColorMap = tmallContextBuildCustomFields.getUsingColorMap();

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
                String sku = null;
                if (propId_sku_size != null) {
                    String skuSize = complexValue.getSingleCheckFieldValue(propId_sku_size).getValue();
                    CmsSkuPropBean cmsSkuPropBean = usingSkuMap.get(skuSize);
                    sku = cmsSkuPropBean.getProp(CmsFieldEnum.CmsSkuEnum.sku);
                }

                String code = null;
                if (propId_sku_color != null) {
                    String skuColor = complexValue.getSingleCheckFieldValue(propId_sku_color).getValue();
                    CmsCodePropBean cmsCodePropBean = usingColorMap.get(skuColor);
                    code = cmsCodePropBean.getProp(CmsFieldEnum.CmsCodeEnum.code);
                }

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
