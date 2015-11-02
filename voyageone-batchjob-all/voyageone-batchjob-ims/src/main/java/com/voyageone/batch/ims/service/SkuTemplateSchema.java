package com.voyageone.batch.ims.service;

/**
 * Created by Leo on 15-7-17.
 */
public class SkuTemplateSchema {

    //前3位表示模板，支持最多8个模板
    final static long TPL_MASK = 0xe000000000000000l;
    //SKUINFO的类型由剩余的61位组成，最多支持61个子类型
    //子类型每一个类型占一位，因为可能存在多个一个字段属性属于多个类型
    final static long FIELD_MASK = 0x1fffffffffffffffl;

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

    public class SkuTemplate_2_Schema {
        public final static int TPL_INDEX = 1;

        public final static int FIELD_BIT_MIN = 0;
        public final static int SKU_COLOR = FIELD_BIT_MIN;
        public final static int SKU_SIZE = 1;
        public final static int SKU_PRICE = 2;
        public final static int SKU_QUANTITY = 3;
        public final static int SKU_OUTERID = 4;
        public final static int SKU_BARCODE = 5;
        public final static int SKU_MARKET_TIME = 6;
        public final static int SKU = 7;

        public final static int EXTENDCOLOR_ALIASNAME = 8;
        public final static int EXTENDCOLOR_COLOR = 9;
        public final static int EXTENDCOLOR_IMAGE = 10;
        public final static int EXTENDCOLOR = 11;
        public final static int EXTENDCOLOR_BASECOLOR = 12;

        public final static int EXTENDSIZE = 13;
        public final static int EXTENDSIZE_SIZE = 14;
        public final static int EXTENDSIZE_TIP = 15;
        public final static int EXTENDSIZE_SHENGAO = 16;
        public final static int EXTENDSIZE_SHENGAO_RANGE = 17;
        public final static int EXTENDSIZE_TIZHONG = 18;
        public final static int EXTENDSIZE_TIZHONG_RANGE = 19;
        public final static int EXTENDSIZE_JIANKUAN = 20;
        public final static int EXTENDSIZE_JIANKUAN_RANGE = 21;
        public final static int EXTENDSIZE_XIONGWEI = 22;
        public final static int EXTENDSIZE_XIONGWEI_RANGE = 23;
        public final static int EXTENDSIZE_YAOWEI = 24;
        public final static int EXTENDSIZE_YAOWEI_RANGE = 25;
        public final static int EXTENDSIZE_XIUCHANG = 26;
        public final static int EXTENDSIZE_XIUCHANG_RANGE = 27;
        public final static int EXTENDSIZE_YICHANG = 28;
        public final static int EXTENDSIZE_YICHANG_RANGE = 29;
        public final static int EXTENDSIZE_BEIKUAN = 30;
        public final static int EXTENDSIZE_BEIKUAN_RANGE = 31;
        public final static int EXTENDSIZE_QIANCHANG = 32;
        public final static int EXTENDSIZE_QIANCHANG_RANGE = 33;
        public final static int EXTENDSIZE_BAIWEI = 34;
        public final static int EXTENDSIZE_BAIWEI_RANGE = 35;
        public final static int EXTENDSIZE_XIABAIWEI = 36;
        public final static int EXTENDSIZE_XIABAIWEI_RANGE = 37;
        public final static int EXTENDSIZE_XIUKOU = 38;
        public final static int EXTENDSIZE_XIUKOU_RANGE = 39;
        public final static int EXTENDSIZE_XIUFEI = 40;
        public final static int EXTENDSIZE_XIUFEI_RANGE = 41;
        public final static int EXTENDSIZE_ZHONGYAO = 42;
        public final static int EXTENDSIZE_ZHONGYAO_RANGE = 43;
        public final static int EXTENDSIZE_LINGSHEN = 44;
        public final static int EXTENDSIZE_LINGSHEN_RANGE = 45;
        public final static int EXTENDSIZE_LINGGAO = 46;
        public final static int EXTENDSIZE_LINGGAO_RANGE = 47;
        public final static int EXTENDSIZE_LINGKUAN = 48;
        public final static int EXTENDSIZE_LINGKUAN_RANGE = 49;
        public final static int EXTENDSIZE_LINGWEI = 50;
        public final static int EXTENDSIZE_LINGWEI_RANGE = 51;
        public final static int EXTENDSIZE_YUANBAIHOU = 52;
        public final static int EXTENDSIZE_YUANBAIHOU_RANGE = 53;
        public final static int EXTENDSIZE_YUANBAI = 54;
        public final static int EXTENDSIZE_YUANBAI_RANGE = 55;
        public final static int EXTENDSIZE_PINGBAI = 56;
        public final static int EXTENDSIZE_PINGBAI_RANGE = 57;
        public final static int EXTENDSIZE_CUSTOM_SIZE_1 = 58;
        public final static int EXTENDSIZE_CUSTOM_SIZE_2 = 59;
        public final static int EXTENDSIZE_CUSTOM_SIZE_3 = 60;
        public final static int EXTENDSIZE_CUSTOM_SIZE_4 = 61;

        public final static int FIELD_BIT_MAX = EXTENDSIZE_CUSTOM_SIZE_4;
    }

    public static long encodeSkuType(long tpl, long ... types)
    {
        long skuType = tpl << 61;
        for (long type : types) {
            skuType |= (1l << type);
        }
        return skuType;
    }

    public static long decodeTpl(long skuType)
    {
        return (skuType &  TPL_MASK) >> 61;
    }

    public static long decodeFieldTypes(long skuType)
    {
        return (skuType & FIELD_MASK);
    }

    public static boolean containFieldType(long skuType, long fieldType)
    {
        return (decodeFieldTypes(skuType) & (1l << fieldType)) == (1l << fieldType);
    }
}
