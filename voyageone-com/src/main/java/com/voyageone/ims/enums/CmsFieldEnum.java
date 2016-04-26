package com.voyageone.ims.enums;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Leo on 15-6-15.
 */
public class CmsFieldEnum {

    /*@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS
            , include = JsonTypeInfo.As.EXTERNAL_PROPERTY
            , property = "type")
            */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME
            , include = JsonTypeInfo.As.PROPERTY
            , property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CmsModelEnum.class, name = "CmsModelEnum"),
            @JsonSubTypes.Type(value = CmsCodeEnum.class, name = "CmsCodeEnum"),
            @JsonSubTypes.Type(value = CmsSkuEnum.class, name = "CmsSkuEnum"),
    })
    public interface CmsFieldEnumIntf {}

    public enum CmsModelEnum implements CmsFieldEnumIntf{
        brand,
        model,
        category_id,
        longTitle,   // mongo数据库里的名字变了:title_cn->longTitle
        productNameEn, // mongo数据库里的名字变了:title_en->productNameEn
        long_description_cn,
        long_description_en,
        short_description_cn,
        short_description_en,


        /*
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "." + this.name();
        }
        */
    }

    public enum CmsCodeEnum implements CmsFieldEnumIntf {
        code_id,
        code,
        price,
        msrp,
        product_image,
        box_image,
        oblique_image,
        art_image,
        handmade_image,
        attribute_1_key,
        attribute_2_key,
        attribute_3_key,
        attribute_4_key,
        attribute_5_key,
        attribute_6_key,
        attribute_7_key,
        attribute_8_key,
        attribute_9_key,
        attribute_10_key,
        attribute_1_value,
        attribute_2_value,
        attribute_3_value,
        attribute_4_value,
        attribute_5_value,
        attribute_6_value,
        attribute_7_value,
        attribute_8_value,
        attribute_9_value,
        attribute_10_value

        /*
        public String toString() {
            return this.getClass().getSimpleName() + "." + this.name();
        }
        */
    }

    public enum CmsSkuEnum implements CmsFieldEnumIntf{
        sku,
        sku_price,
        sku_quantity,
        size,
        bar_code
    }

    public static CmsFieldEnumIntf valueOf (String fieldStr) {
        CmsFieldEnumIntf cmsFieldEnumIntf;

        try {
            cmsFieldEnumIntf = CmsModelEnum.valueOf(fieldStr);
            return cmsFieldEnumIntf;
        } catch (IllegalArgumentException e) { }

        try {
            cmsFieldEnumIntf = CmsCodeEnum.valueOf(fieldStr);
            return cmsFieldEnumIntf;
        } catch (IllegalArgumentException e) {}

        try {
            cmsFieldEnumIntf = CmsSkuEnum.valueOf(fieldStr);
            return cmsFieldEnumIntf;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
