package com.voyageone.batch.ims.service;

/**
 * Created by Leo on 15-7-17.
 */
public class SkuTemplateSchema {

    //前8位表示模板，支持最多16个模板
    final static int TPL_MASK = 0xff000000;
    //SKUINFO的类型由剩余的3个字节组成，最多支持24个子类型
    //子类型每一个类型占一位，因为可能存在多个一个字段属性属于多个类型
    final static int FIELD_MASK = 0x00ffffff;

    public class SkuTemplate_1_Schema {
        public final static int TPL_INDEX = 0;

        public final static int FIELD_BIT_MIN = 0;
        public final static int SKU_COLOR = FIELD_BIT_MIN;
        public final static int SKU_SIZE = 1;
        public final static int SKU_PRICE = 2;
        public final static int SKU_QUANTITY = 3;
        public final static int SKU_OUTERID = 4;
        public final static int SKU_BARCODE = 5;
        public final static int SKU = 6;

        public final static int EXTENDCOLOR_ALIASNAME = 7;
        public final static int EXTENDCOLOR_COLOR = 8;
        public final static int EXTENDCOLOR_IMAGE = 9;
        public final static int EXTENDCOLOR = 10;

        public final static int EXTENDSIZE_SIZE = 11;
        public final static int EXTENDSIZE_ALIASNAME = 12;
        public final static int EXTENDSIZE = 13;

        public final static int INPUT_CUSTOM_SIZE_1 = 14;
        public final static int INPUT_CUSTOM_SIZE_2 = 15;
        public final static int INPUT_CUSTOM_SIZE_3 = 16;
        public final static int INPUT_CUSTOM_SIZE_4 = 17;
        public final static int INPUT_CUSTOM_SIZE_5 = 18;

        public final static int SELECT_CUSTOM_SIZE_1 = 19;
        public final static int SELECT_CUSTOM_SIZE_2 = 20;
        public final static int SELECT_CUSTOM_SIZE_3 = 21;
        public final static int SELECT_CUSTOM_SIZE_4 = 22;
        public final static int SELECT_CUSTOM_SIZE_5 = 23;

        public final static int FIELD_BIT_MAX = SELECT_CUSTOM_SIZE_5;
    }

    public static int encodeSkuType(int tpl, int ... types)
    {
        int skuType = tpl << 24;
        for (int type : types) {
            skuType |= (1 << type);
        }
        return skuType;
    }

    public static int decodeTpl(int skuType)
    {
        return (skuType &  TPL_MASK) >> 24;
    }

    public static int decodeFieldTypes(int skuType)
    {
        return (skuType & FIELD_MASK);
    }

    public static boolean containFieldType(int skuType, int fieldType)
    {
        return (decodeFieldTypes(skuType) & (1 << fieldType)) == (1 << fieldType);
    }
}
