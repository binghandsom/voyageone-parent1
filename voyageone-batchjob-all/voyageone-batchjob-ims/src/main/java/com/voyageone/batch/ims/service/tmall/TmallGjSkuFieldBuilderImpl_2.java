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

import java.util.*;

/**
 * Created by Leo on 15-7-14.
 * 参考天猫分类: 50014993
 * 线程安全: 是
 */
public class TmallGjSkuFieldBuilderImpl_2 extends AbstractSkuFieldBuilder{
    private String propId_sku;
    private String propId_colorExtend;
    private String propId_skuExtend;

    private String propId_sku_color;
    private String propId_sku_size;
    private String propId_sku_price;
    private String propId_sku_quantity;
    private String propId_sku_outerId;
    private String propId_sku_barCode;

    private String propId_colorExtend_aliasname;
    private String propId_colorExtend_color;
    private String propId_colorExtend_image;

    private String propId_skuExtend_size;
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

    private PlatformPropBean skuPlatformProp;
    private PlatformPropBean colorExtendPlatformProp;
    private PlatformPropBean skuExtendPlatformProp;

    private static Log logger = LogFactory.getLog(TmallGjSkuFieldBuilderImpl_2.class);

    private boolean init(List<PlatformPropBean> platformProps) {
        for (PlatformPropBean platformProp : platformProps) {
            List<PlatformSkuInfoBean> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfo(platformProp.getPlatformPropId(), platformProp.getPlatformCartId());

            PlatformSkuInfoBean tmallSkuInfo = null;
            for (PlatformSkuInfoBean tmallSkuInfoEach : tmallSkuInfos) {
                if (SkuTemplateSchema.decodeTpl(tmallSkuInfoEach.getSku_type()) == SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX) {
                    tmallSkuInfo = tmallSkuInfoEach;
                }
            }

            if (tmallSkuInfo == null) {
                logger.warn("No sku info find for platformProp: [prop_id=" + platformProp.getPlatformPropId() + ", cartId=" + platformProp.getPlatformCartId() + "]");
                logger.warn(TmallGjSkuFieldBuilderImpl_2.class.getName() + " is not applicable!");
                return false;
            }

            long fieldType = SkuTemplateSchema.decodeFieldTypes(tmallSkuInfo.getSku_type());

            if (fieldType < (1l << SkuTemplateSchema.SkuTemplate_2_Schema.FIELD_BIT_MIN)
                    || fieldType > (1l << (SkuTemplateSchema.SkuTemplate_2_Schema.FIELD_BIT_MAX + 1)))
                return false;

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU)) {
                propId_sku = platformProp.getPlatformPropId();
                skuPlatformProp = platformProp;
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE)) {
                propId_sku_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_BARCODE)) {
                propId_sku_barCode = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_COLOR)) {
                propId_sku_color = platformProp.getPlatformPropId();
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
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR)) {
                propId_colorExtend = platformProp.getPlatformPropId();
                colorExtendPlatformProp = platformProp;
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
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE)) {
                skuExtendPlatformProp = platformProp;
                propId_skuExtend = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE)) {
                propId_skuExtend_size = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_BAIWEI)) {
                propId_skuExtend_baiwei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_BEIKUAN)) {
                propId_skuExtend_beikuan = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_JIANKUAN)) {
                propId_skuExtend_jiankuan = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_LINGGAO)) {
                propId_skuExtend_linggao = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_LINGKUAN)) {
                propId_skuExtend_lingkuan = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_LINGSHEN)) {
                propId_skuExtend_lingshen = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_LINGWEI)) {
                propId_skuExtend_lingwei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_PINGBAI)) {
                propId_skuExtend_pingbaiyichang = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_QIANCHANG)) {
                propId_skuExtend_qianchang = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SHENGAO)) {
                propId_skuExtend_shengao = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_TIP)) {
                propId_skuExtend_tip = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_TIZHONG)) {
                propId_skuExtend_tizhong = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_XIABAIWEI)) {
                propId_skuExtend_xiabaiwei = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_XIONGWEI)) {
                propId_skuExtend_xiongwei = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_XIUCHANG)) {
                propId_skuExtend_xiuchang = platformProp.getPlatformPropId();
            }
            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_XIUFEI)) {
                propId_skuExtend_xiufei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_XIUKOU)) {
                propId_skuExtend_xiukou = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_YICHANG)) {
                propId_skuExtend_yichang = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_YAOWEI)) {
                propId_skuExtend_yaowei = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_ZHONGYAO)) {
                propId_skuExtend_zhongyao = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_YUANBAIHOU)) {
                propId_skuExtend_yuanbaihou = platformProp.getPlatformPropId();
            }

            if (SkuTemplateSchema.containFieldType(fieldType, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_YUANBAI)) {
                propId_skuExtend_yuanbai = platformProp.getPlatformPropId();
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

        PlatformPropBean platformProp_colorCategory;
        List<PlatformPropOptionBean> colorOptions = null;
        if (propId_sku_color != null && !"".equals(propId_sku_color.trim())) {
            platformProp_colorCategory = platformPropDao.selectPlatformPropByPropId(platformId, categoryCode, propId_sku_color, platformProp.getPlatformPropHash());
            colorOptions = platformPropDao.selectPlatformOptionsByPropHash(platformProp_colorCategory.getPlatformPropHash());
        }

        PlatformPropBean platformProp_size;

        if (propId_sku_size != null && !"".equals(propId_sku_size)) {
            platformProp_size = platformPropDao.selectPlatformPropByPropId(platformId, categoryCode, propId_sku_size, platformProp.getPlatformPropHash());
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

        List<CmsCodePropBean> cmsCodeProps = cmsModelProp.getCmsCodePropBeanList();

        List<ComplexValue> complexValues = new ArrayList<>();
        int indexColor = 0;

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

            for (int i=0; i < cmsCodeProp.getCmsSkuPropBeanList().size(); i++)
            {
                CmsSkuPropBean cmsSkuProp = cmsCodeProp.getCmsSkuPropBeanList().get(i);
                //设置code级别的颜色属性
                ComplexValue skuFieldValue = new ComplexValue();
                if (propId_sku_color != null && !"".equals(propId_sku_color.trim())) {
                    skuFieldValue.setSingleCheckFieldValue(propId_sku_color, new Value(colorValue));
                }
                String size = cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.size);

                //设置sku级别的尺寸属性
                if (propId_sku_size != null) {
                    usingSkuMap.put(size, cmsSkuProp);
                    skuFieldValue.setInputFieldValue(propId_sku_size, size);
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
        UploadProductTcb uploadProductTcb = contextBuildCustomFields.getPlatformContextBuildFields().getPlatformUploadRunState().getUploadProductTcb();
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();

        MultiComplexField sizeExtendProp = (MultiComplexField) FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
        Map<String, CmsSkuPropBean> usingSkuMap = contextBuildCustomFields.getUsingSkuMap();

        if (!propId_skuExtend.equals(platformProp.getPlatformPropId())) {
            return null;
        }
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

        for (Map.Entry<String, CmsSkuPropBean> cmsSkuPropBeanEntry : usingSkuMap.entrySet()) {
            String size = cmsSkuPropBeanEntry.getKey();
            ComplexValue extendSizeComplexValue = new ComplexValue();

            extendSizeComplexValue.setInputFieldValue(propId_skuExtend_size, size);

            size="L";
            String sizeId = customSizeNameIdMap.get(size);
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
                    String skuSize = complexValue.getInputFieldValue(propId_sku_size);
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
