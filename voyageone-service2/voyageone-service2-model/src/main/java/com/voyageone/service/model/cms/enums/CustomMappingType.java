package com.voyageone.service.model.cms.enums;

/**
 * Created by Leo on 15-7-10.
 */
public enum CustomMappingType {
    BRAND_INFO(0),
    SKU_INFO(1),
    PRICE_SECTION(2),
    TMALL_SERVICE_VERSION(3),
    TMALL_STYLE_CODE(4),
    TMALL_ITEM_QUANTITY(5),
    TMALL_ITEM_PRICE(6),
    TMALL_XINGHAO(7),
    TMALL_OUT_ID(8),
    TMALL_SHOP_CATEGORY(9),
    ITEM_STATUS(10),
    // added by morse.lu 2016/06/29 start
    ITEM_DESCRIPTION(11),
    ITEM_WIRELESS_DESCRIPTION(12),
    IMAGE(13),
    FREIGHT(14), // 运费模板
    CSPU(15), // 产品规格
    PRODUCT_ID(16), // 货品Id
    DARWIN_SKU(17), // darwin_sku
    IS_XINPIN(18),  // 商品是否为新品
    ;
    // added by morse.lu 2016/06/29 end

    // added by morse.lu 2016/07/19 start
    public enum ImageProp {
        PRODUCT_IMAGES("product_images", "产品图片-"), // 产品图片
        ITEM_IMAGES("item_images", "商品图片-"), // 商品图片
        VERTICAL_IMAGE("vertical_image", "竖图-"), // 商品竖图
        ITEM_ATTACH_IMAGES("item_attach_images", "商品资质图片-"), // 商品资质图片(1:吊牌图,2:耐久性标签",3:质检报告,4:合格证)
        DIAOPAI_PIC("diaopai_pic", "新品吊牌图"), // 吊牌图
        WHITE_BG_IMAGE("white_bg_image", "透明图"), // 透明素材图
        ITEM_WIRELESS_IMAGES("item_wireless_images", "商品无线图片-"), // 商品无线图片
        ;

        private final String propId;
        private final String baseDictName;

        private ImageProp(String propId, String baseDictName) {
            this.propId = propId;
            this.baseDictName = baseDictName;
        }

        public String getPropId() {
            return propId;
        }

        public String getBaseDictName() {
            return baseDictName;
        }
    }
    // added by morse.lu 2016/07/19 start

    private int value;

    CustomMappingType(int i) {
        this.value = i;
    }

    public static CustomMappingType valueOf(int value)
    {
        switch (value)
        {
            case 0:
                return BRAND_INFO;
            case 1:
                return SKU_INFO;
            case 2:
                return PRICE_SECTION;
            case 3:
                return TMALL_SERVICE_VERSION;
            case 4:
                return TMALL_STYLE_CODE;
            case 5:
                return TMALL_ITEM_QUANTITY;
            case 6:
                return TMALL_ITEM_PRICE;
            case 7:
                return TMALL_XINGHAO;
            case 8:
                return TMALL_OUT_ID;
            case 9:
                return TMALL_SHOP_CATEGORY;
            case 10:
                return ITEM_STATUS;
            // added by morse.lu 2016/06/29 start
            case 11:
                return ITEM_DESCRIPTION;
            case 12:
                return ITEM_WIRELESS_DESCRIPTION;
            case 13:
                return IMAGE;
            case 14:
                return FREIGHT;
            case 15:
                return CSPU;
            case 16:
                return PRODUCT_ID;
            case 17:
                return DARWIN_SKU;
            case 18:
                return IS_XINPIN;
            // added by morse.lu 2016/06/29 end
            default:
                return null;
        }
    }

    public int value()
    {
        return value;
    }
}
