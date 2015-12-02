package com.voyageone.batch.ims.service;

/**
 * Created by Leo on 15-7-17.
 */
public class SkuTemplateSchema {

    //前3位表示模板，支持最多8个模板
    final static long TPL_MASK = 0xe000000000000000L;
    //SKUINFO的类型由剩余的61位组成，最多支持61个子类型
    //子类型每一个类型占一位，因为可能存在多个一个字段属性属于多个类型
    final static long FIELD_MASK = 0x1fffffffffffffffL;


    /**
     * description: 支持只有颜色和颜色扩展的sku, 颜色可以是SingleCheck也可以
     * 是Input，可以没有颜色扩展，如果有颜色扩展，颜色扩展中有必须alias_name
     * support category: 121364012
     */
    public class SkuTemplate_0_Schema {
        public final static int TPL_INDEX = 0;

        public final static int FIELD_BIT_MIN = 0;

        //SKU
        public final static int SKU = FIELD_BIT_MIN;
        public final static int SKU_COLOR = SKU + 1;
        public final static int SKU_PRICE = SKU_COLOR + 1;
        public final static int SKU_QUANTITY = SKU_PRICE + 1;
        public final static int SKU_OUTERID = SKU_QUANTITY + 1;
        public final static int SKU_BARCODE = SKU_OUTERID + 1;
        public final static int SKU_MARKET_TIME = SKU_BARCODE + 1;

        //EXTENDCOLOR
        public final static int EXTENDCOLOR = SKU_MARKET_TIME + 1;
        public final static int EXTENDCOLOR_ALIASNAME = EXTENDCOLOR + 1;
        public final static int EXTENDCOLOR_COLOR = EXTENDCOLOR_ALIASNAME + 1;
        public final static int EXTENDCOLOR_IMAGE = EXTENDCOLOR_COLOR + 1;
        public final static int EXTENDCOLOR_BASECOLOR = EXTENDCOLOR_IMAGE + 1;

        public final static int FIELD_BIT_MAX = EXTENDCOLOR_BASECOLOR;
    }

    /**
     * description: 支持只有尺寸，没有颜色，并且尺寸是一个singleCheck或一个input加若干个输入框,
     *              尺寸扩展如果存在，必须包括alias_name的sku。
     * support category: 121424025
     */
    public class SkuTemplate_1_Schema {
        public final static int TPL_INDEX = 1;

        public final static int FIELD_BIT_MIN = 0;

        //SKU
        public final static int SKU = FIELD_BIT_MIN;
        public final static int SKU_SIZE = SKU + 1;
        public final static int SKU_PRICE = SKU_SIZE + 1;
        public final static int SKU_QUANTITY = SKU_PRICE + 1;
        public final static int SKU_OUTERID = SKU_QUANTITY + 1;
        public final static int SKU_BARCODE = SKU_OUTERID + 1;
        public final static int SKU_CUSTOM_SIZE1 = SKU_BARCODE + 1;
        public final static int SKU_CUSTOM_SIZE2 = SKU_CUSTOM_SIZE1 + 1;
        public final static int SKU_CUSTOM_SIZE3 = SKU_CUSTOM_SIZE2 + 1;
        public final static int SKU_CUSTOM_SIZE4 = SKU_CUSTOM_SIZE3 + 1;
        public final static int SKU_CUSTOM_SIZE5 = SKU_CUSTOM_SIZE4 + 1;

        //EXTENDSIZE
        public final static int EXTENDSIZE = SKU_CUSTOM_SIZE5 + 1;
        public final static int EXTENDSKU_SIZE = EXTENDSIZE + 1;
        public final static int EXTENDSKU_ALIAS_NAME = EXTENDSKU_SIZE + 1;

        public final static int FIELD_BIT_MAX = EXTENDSKU_ALIAS_NAME;
    }


    /**
     * description: 支持既有颜色又有尺寸，并且颜色和尺寸
     */
    public class SkuTemplate_2_Schema {
        public final static int TPL_INDEX = 2;

        public final static int FIELD_BIT_MIN = 0;

        //SKU
        public final static int SKU = FIELD_BIT_MIN;
        public final static int SKU_SIZE = SKU + 1;
        public final static int SKU_COLOR = SKU_SIZE + 1;
        public final static int SKU_PRICE = SKU_COLOR + 1;
        public final static int SKU_QUANTITY = SKU_PRICE + 1;
        public final static int SKU_OUTERID = SKU_QUANTITY + 1;
        public final static int SKU_BARCODE = SKU_OUTERID + 1;
        public final static int SKU_MARKET_TIME = SKU_BARCODE + 1;

        public final static int SKU_CUSTOM_SIZE1 = SKU_MARKET_TIME + 1;
        public final static int SKU_CUSTOM_SIZE2 = SKU_CUSTOM_SIZE1 + 1;
        public final static int SKU_CUSTOM_SIZE3 = SKU_CUSTOM_SIZE2 + 1;
        public final static int SKU_CUSTOM_SIZE4 = SKU_CUSTOM_SIZE3 + 1;
        public final static int SKU_CUSTOM_SIZE5 = SKU_CUSTOM_SIZE4 + 1;

        //EXTENDCOLOR
        public final static int EXTENDCOLOR= SKU_CUSTOM_SIZE5 + 1;
        public final static int EXTENDCOLOR_COLOR = EXTENDCOLOR + 1;
        public final static int EXTENDCOLOR_ALIASNAME = EXTENDCOLOR_COLOR + 1;
        public final static int EXTENDCOLOR_IMAGE = EXTENDCOLOR_ALIASNAME + 1;
        public final static int EXTENDCOLOR_BASECOLOR = EXTENDCOLOR_IMAGE + 1;

        //EXTENDSIZE
        public final static int EXTENDSIZE = EXTENDCOLOR_BASECOLOR + 1;
        public final static int EXTENDSIZE_SIZE = EXTENDSIZE + 1;
        public final static int EXTENDSIZE_ALIASNAME = EXTENDSIZE_SIZE + 1;

        public final static int FIELD_BIT_MAX = EXTENDSIZE_ALIASNAME;
    }

    public class SkuTemplate_3_Schema {
        public final static int TPL_INDEX = 3;

        public final static int FIELD_BIT_MIN = 0;

        //SKU
        public final static int SKU = FIELD_BIT_MIN;
        public final static int SKU_COLOR = SKU + 1;
        public final static int SKU_SIZE = SKU_COLOR + 1;
        public final static int SKU_PRICE = SKU_SIZE + 1;
        public final static int SKU_QUANTITY = SKU_PRICE + 1;
        public final static int SKU_OUTERID = SKU_QUANTITY + 1;
        public final static int SKU_BARCODE = SKU_OUTERID + 1;
        public final static int SKU_MARKET_TIME = SKU_BARCODE + 1;

        //EXTENDCOLOR
        public final static int EXTENDCOLOR = SKU_MARKET_TIME + 1;
        public final static int EXTENDCOLOR_COLOR = EXTENDCOLOR + 1;
        public final static int EXTENDCOLOR_IMAGE = EXTENDCOLOR_COLOR + 1;
        public final static int EXTENDCOLOR_BASECOLOR = EXTENDCOLOR_IMAGE + 1;
        public final static int EXTENDCOLOR_ALIASNAME = EXTENDCOLOR_BASECOLOR + 1;

        //EXTENDSIZE
        public final static int EXTENDSIZE = EXTENDCOLOR_ALIASNAME + 1;
        public final static int EXTENDSIZE_SIZE = EXTENDSIZE + 1;
        public final static int EXTENDSIZE_TIP = EXTENDSIZE_SIZE + 1;
        public final static int EXTENDSIZE_SHENGAO = EXTENDSIZE_TIP + 1;
        public final static int EXTENDSIZE_SHENGAO_RANGE = EXTENDSIZE_SHENGAO + 1;
        public final static int EXTENDSIZE_TIZHONG = EXTENDSIZE_SHENGAO_RANGE + 1;
        public final static int EXTENDSIZE_TIZHONG_RANGE = EXTENDSIZE_TIZHONG + 1;
        public final static int EXTENDSIZE_JIANKUAN = EXTENDSIZE_TIZHONG_RANGE + 1;
        public final static int EXTENDSIZE_JIANKUAN_RANGE = EXTENDSIZE_JIANKUAN + 1;
        public final static int EXTENDSIZE_XIONGWEI = EXTENDSIZE_JIANKUAN_RANGE + 1;
        public final static int EXTENDSIZE_XIONGWEI_RANGE = EXTENDSIZE_XIONGWEI + 1;
        public final static int EXTENDSIZE_YAOWEI = EXTENDSIZE_XIONGWEI_RANGE + 1;
        public final static int EXTENDSIZE_YAOWEI_RANGE = EXTENDSIZE_YAOWEI + 1;
        public final static int EXTENDSIZE_XIUCHANG = EXTENDSIZE_YAOWEI_RANGE + 1;
        public final static int EXTENDSIZE_XIUCHANG_RANGE = EXTENDSIZE_XIUCHANG + 1;
        public final static int EXTENDSIZE_YICHANG = EXTENDSIZE_XIUCHANG_RANGE + 1;
        public final static int EXTENDSIZE_YICHANG_RANGE = EXTENDSIZE_YICHANG + 1;
        public final static int EXTENDSIZE_BEIKUAN = EXTENDSIZE_YICHANG_RANGE + 1;
        public final static int EXTENDSIZE_BEIKUAN_RANGE = EXTENDSIZE_BEIKUAN + 1;
        public final static int EXTENDSIZE_QIANCHANG = EXTENDSIZE_BEIKUAN_RANGE + 1;
        public final static int EXTENDSIZE_QIANCHANG_RANGE = EXTENDSIZE_QIANCHANG + 1;
        public final static int EXTENDSIZE_BAIWEI = EXTENDSIZE_QIANCHANG_RANGE + 1;
        public final static int EXTENDSIZE_BAIWEI_RANGE = EXTENDSIZE_BAIWEI + 1;
        public final static int EXTENDSIZE_XIABAIWEI = EXTENDSIZE_BAIWEI_RANGE + 1;
        public final static int EXTENDSIZE_XIABAIWEI_RANGE = EXTENDSIZE_XIABAIWEI + 1;
        public final static int EXTENDSIZE_XIUKOU = EXTENDSIZE_XIABAIWEI_RANGE + 1;
        public final static int EXTENDSIZE_XIUKOU_RANGE = EXTENDSIZE_XIUKOU + 1;
        public final static int EXTENDSIZE_XIUFEI = EXTENDSIZE_XIUKOU_RANGE + 1;
        public final static int EXTENDSIZE_XIUFEI_RANGE = EXTENDSIZE_XIUFEI + 1;
        public final static int EXTENDSIZE_ZHONGYAO = EXTENDSIZE_XIUFEI_RANGE + 1;
        public final static int EXTENDSIZE_ZHONGYAO_RANGE = EXTENDSIZE_ZHONGYAO + 1;
        public final static int EXTENDSIZE_LINGSHEN = EXTENDSIZE_ZHONGYAO_RANGE + 1;
        public final static int EXTENDSIZE_LINGSHEN_RANGE = EXTENDSIZE_LINGSHEN + 1;
        public final static int EXTENDSIZE_LINGGAO = EXTENDSIZE_LINGSHEN_RANGE + 1;
        public final static int EXTENDSIZE_LINGGAO_RANGE = EXTENDSIZE_LINGGAO + 1;
        public final static int EXTENDSIZE_LINGKUAN = EXTENDSIZE_LINGGAO_RANGE + 1;
        public final static int EXTENDSIZE_LINGKUAN_RANGE = EXTENDSIZE_LINGKUAN + 1;
        public final static int EXTENDSIZE_LINGWEI = EXTENDSIZE_LINGKUAN_RANGE + 1;
        public final static int EXTENDSIZE_LINGWEI_RANGE = EXTENDSIZE_LINGWEI + 1;
        public final static int EXTENDSIZE_YUANBAIHOU = EXTENDSIZE_LINGWEI_RANGE + 1;
        public final static int EXTENDSIZE_YUANBAIHOU_RANGE = EXTENDSIZE_YUANBAIHOU + 1;
        public final static int EXTENDSIZE_YUANBAI = EXTENDSIZE_YUANBAIHOU_RANGE + 1;
        public final static int EXTENDSIZE_YUANBAI_RANGE = EXTENDSIZE_YUANBAI + 1;
        public final static int EXTENDSIZE_PINGBAI = EXTENDSIZE_YUANBAI_RANGE + 1;
        public final static int EXTENDSIZE_PINGBAI_RANGE = EXTENDSIZE_PINGBAI + 1;
        public final static int EXTENDSIZE_CUSTOM_SIZE_1 = EXTENDSIZE_PINGBAI_RANGE + 1;
        public final static int EXTENDSIZE_CUSTOM_SIZE_2 = EXTENDSIZE_CUSTOM_SIZE_1 + 1;
        public final static int EXTENDSIZE_CUSTOM_SIZE_3 = EXTENDSIZE_CUSTOM_SIZE_2 + 1;
        public final static int EXTENDSIZE_CUSTOM_SIZE_4 = EXTENDSIZE_CUSTOM_SIZE_3 + 1;

        public final static int FIELD_BIT_MAX = EXTENDSIZE_CUSTOM_SIZE_4;
    }

    public static long encodeSkuType(long tpl, long ... types)
    {
        long skuType = tpl << 61;
        for (long type : types) {
            skuType |= (1L << type);
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
        return (decodeFieldTypes(skuType) & (1L << fieldType)) == (1L << fieldType);
    }
}
