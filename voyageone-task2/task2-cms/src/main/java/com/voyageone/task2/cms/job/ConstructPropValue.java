package com.voyageone.task2.cms.job;

import com.voyageone.task2.cms.bean.CustomMappingType;
import com.voyageone.task2.cms.bean.SkuTemplateSchema;
import com.voyageone.task2.cms.dao.PlatformPropCustomMappingDao;
import com.voyageone.task2.cms.dao.PlatformSkuInfoDao;
import com.voyageone.task2.cms.model.CustomPlatformPropMappingModel;
import com.voyageone.task2.cms.model.PlatformSkuInfoModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import com.voyageone.common.configs.Enums.*;

/**
 * Created by Leo on 15-7-3.
 */
public class ConstructPropValue {

    public static void constructPlatformPropMappingCustom(PlatformPropCustomMappingDao platformPropCustomMappingDao) { CustomPlatformPropMappingModel customPlatformPropMappingModel = new CustomPlatformPropMappingModel();
        customPlatformPropMappingModel.setCartId(23);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.BRAND_INFO);
        customPlatformPropMappingModel.setPlatformPropId("prop_20000");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.TMALL_STYLE_CODE);
        customPlatformPropMappingModel.setPlatformPropId("prop_13021751");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.SKU_INFO);
        customPlatformPropMappingModel.setPlatformPropId("sku");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("prop_1627207");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("prop_10537981");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("sku_price");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("sku_quantity");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("sku_outerId");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("sku_barcode");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("prop_extend_1627207");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("alias_name");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("prop_image");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("prop_extend_10537981");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("in_prop_151018199");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("in_prop_150988152");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("prop_9066257");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("prop_extend_9066257");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("in_prop_150778146");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("sku_MarketTime");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("in_prop_148242406");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("prop_20509");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("prop_extend_20509");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("prop_14067173");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("std_size_prop_20509_-1");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("std_size_prop_20518_-1");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("custom_prop_1");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("basecolor");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("in_prop_1627207");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("std_size_extends_20509");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("std_size_extends_20518");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_tip");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_shengao");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_shengao_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_tizhong");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_tizhong_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_jiankuan");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_jiankuan_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiongwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiongwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yaowei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yaowei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiuchang");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiuchang_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yichang");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yichang_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_baiwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_baiwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiabaiwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiabaiwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiukou");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiukou_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiufei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_xiufei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_zhongyao");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_zhongyao_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_lingshen");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_lingshen_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_linggao");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_linggao_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_lingkuan");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_lingkuan_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_lingwei");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_lingwei_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yuanbaihou");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yuanbaihou_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_pingbai");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_pingbai_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yuanbai");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setPlatformPropId("size_mapping_yuanbai_range");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        //=================================SKU_INFO END==============================

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.TMALL_ITEM_QUANTITY);
        customPlatformPropMappingModel.setPlatformPropId("quantity");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.TMALL_ITEM_PRICE);
        customPlatformPropMappingModel.setPlatformPropId("price");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.TMALL_XINGHAO);
        customPlatformPropMappingModel.setPlatformPropId("prop_1626510");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
        customPlatformPropMappingModel.setPlatformPropId("in_prop_1626510");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.TMALL_SERVICE_VERSION);
        customPlatformPropMappingModel.setPlatformPropId("service_version");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.TMALL_OUT_ID);
        customPlatformPropMappingModel.setPlatformPropId("outer_id");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.TMALL_SHOP_CATEGORY);
        customPlatformPropMappingModel.setPlatformPropId("seller_cids");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.PRICE_SECTION);
        customPlatformPropMappingModel.setPlatformPropId("prop_13618191");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.PRICE_SECTION);
        customPlatformPropMappingModel.setPlatformPropId("prop_21548");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);

        customPlatformPropMappingModel.setCustomMappingType(CustomMappingType.ITEM_STATUS);
        customPlatformPropMappingModel.setPlatformPropId("item_status");
        platformPropCustomMappingDao.insertCustomMapping(customPlatformPropMappingModel);
    }

    public static void constructTmallSkuInfo(PlatformSkuInfoDao platformSkuInfoDao) {
        PlatformSkuInfoModel tmallSkuInfo = new PlatformSkuInfoModel();
        tmallSkuInfo.setCart_id(Integer.valueOf(CartEnums.Cart.TG.getId()));

        //============================================== 模板0 BEGIN =================================================================================
        tmallSkuInfo.setProp_id("sku");
        long skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色
        tmallSkuInfo.setProp_id("prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_MarketTime");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_MARKET_TIME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色扩展
        tmallSkuInfo.setProp_id("prop_extend_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_ALIASNAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_image");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_IMAGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("basecolor");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_BASECOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("in_prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_0_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_0_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_0_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);
        //============================================== 模板0 END ===================================================================================

        //============================================== 模板1 BEGIN =================================================================================
        //SKU
        tmallSkuInfo.setProp_id("sku");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_9066257");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE
                , SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //戒指手寸扩展 规格
        tmallSkuInfo.setProp_id("prop_extend_9066257");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_ALIAS_NAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //项链长度
        tmallSkuInfo.setProp_id("in_prop_150988152");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_CUSTOM_SIZE1);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //手链长度
        tmallSkuInfo.setProp_id("in_prop_151018199");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_1_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_1_Schema.SKU_SIZE
                , SkuTemplateSchema.SkuTemplate_1_Schema.EXTENDSKU_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);
        //============================================== 模板1 END ===================================================================================


        //============================================== 模板2 BEGIN ===================================================================================
        tmallSkuInfo.setProp_id("sku");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //sku
        tmallSkuInfo.setProp_id("prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色扩展
        tmallSkuInfo.setProp_id("prop_extend_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_ALIASNAME, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_ALIASNAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_image");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_IMAGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_10537981");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("basecolor");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDCOLOR_BASECOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);


        //尺码扩展
        tmallSkuInfo.setProp_id("prop_extend_10537981");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //尺寸
        tmallSkuInfo.setProp_id("prop_20509");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //尺寸扩展
        tmallSkuInfo.setProp_id("prop_extend_20509");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //手链长度
        tmallSkuInfo.setProp_id("in_prop_151018199");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE
                , SkuTemplateSchema.SkuTemplate_2_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //项链长度
        tmallSkuInfo.setProp_id("in_prop_148242406");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //珍珠直径
        tmallSkuInfo.setProp_id("in_prop_150778146");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_2_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_2_Schema.SKU_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //============================================== 模板2 END ======================================================================================

        //============================================== 模板3 BEGIN ===================================================================================
        tmallSkuInfo.setProp_id("sku");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色
        tmallSkuInfo.setProp_id("prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色
        tmallSkuInfo.setProp_id("in_prop_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_COLOR,
                SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_COLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("std_size_prop_20509_-1");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("std_size_prop_20518_-1");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_SIZE, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_price");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_PRICE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_quantity");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_QUANTITY);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_outerId");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_OUTERID);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_barcode");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_BARCODE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("sku_MarketTime");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.SKU_MARKET_TIME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //颜色扩展
        tmallSkuInfo.setProp_id("prop_extend_1627207");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("prop_image");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_IMAGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("basecolor");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_BASECOLOR);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("alias_name");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDCOLOR_ALIASNAME);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        //尺码扩展
        tmallSkuInfo.setProp_id("std_size_extends_20509");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("std_size_extends_20518");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_tip");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIP);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_shengao");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SHENGAO);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_shengao_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_SHENGAO_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_tizhong");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIZHONG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_tizhong_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_TIZHONG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_jiankuan");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_JIANKUAN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_jiankuan_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_JIANKUAN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiongwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIONGWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiongwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIONGWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yaowei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YAOWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yaowei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YAOWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiuchang");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUCHANG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiuchang_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUCHANG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yichang");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YICHANG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yichang_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YICHANG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_beikuan");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BEIKUAN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_beikuan_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BEIKUAN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_qianchang");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_QIANCHANG);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_qianchang_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_QIANCHANG_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_baiwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BAIWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_baiwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_BAIWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiabaiwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIABAIWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiabaiwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIABAIWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiukou");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUKOU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiukou_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUKOU_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiufei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUFEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_xiufei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_XIUFEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_zhongyao");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_ZHONGYAO);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_zhongyao_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_ZHONGYAO_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingshen");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGSHEN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingshen_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGSHEN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_linggao");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGGAO);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_linggao_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGGAO_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingkuan");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGKUAN);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingkuan_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGKUAN_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingwei");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGWEI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_lingwei_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_LINGWEI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbaihou");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAIHOU);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbaihou_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAIHOU_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_pingbai");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_PINGBAI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_pingbai_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_PINGBAI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbai");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAI);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("size_mapping_yuanbai_range");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_YUANBAI_RANGE);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);

        tmallSkuInfo.setProp_id("custom_prop_1");
        skuType = SkuTemplateSchema.encodeSkuType(SkuTemplateSchema.SkuTemplate_3_Schema.TPL_INDEX, SkuTemplateSchema.SkuTemplate_3_Schema.EXTENDSIZE_CUSTOM_SIZE_1);
        tmallSkuInfo.setSku_type(skuType);
        platformSkuInfoDao.insertPlatformSkuInfo(tmallSkuInfo);
        //============================================== 模板3 END ======================================================================================
    }

    public static String convertUrlToPattern(String url) {
        url = url.replace("%s", "$@_*");
        url = url.replace("%", "%%");
        url = url.replace("$@_*", "%s");
        return url;
    }

    public static void main(String args[]) {
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        PlatformSkuInfoDao platformSkuInfoDao = ctx.getBean(PlatformSkuInfoDao.class);
        PlatformPropCustomMappingDao platformPropCustomMappingDao = ctx.getBean(PlatformPropCustomMappingDao.class);

        constructPlatformPropMappingCustom(platformPropCustomMappingDao);
        constructTmallSkuInfo(platformSkuInfoDao);
        System.out.println("Complete!!");
    }
}
