package com.voyageone.batch.cms.bean;

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
    ITEM_STATUS(10);

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
            default:
                return null;
        }
    }

    public int value()
    {
        return value;
    }
}
