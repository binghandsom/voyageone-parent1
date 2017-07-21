package com.voyageone.service.impl;

/**
 * Created by dell on 2016/4/18.
 */
public interface CmsProperty {
    interface Props {
        //  Code 文件模板文件
        String SEARCH_ADVANCE_EXPORT_PATH = "cms.search.advance.export.path";
        String SEARCH_ADVANCE_EXPORT_TEMPLATE_PRODUCT = "cms.search.advance.export.file.product";
        String SEARCH_ADVANCE_EXPORT_TEMPLATE_GROUP = "cms.search.advance.export.file.group";
        String SEARCH_ADVANCE_EXPORT_TEMPLATE_SKU = "cms.search.advance.export.file.sku";

        String PROMOTION_EXPORT_TEMPLATE = "cms.promotion.export.file";
        String CMS_JM_EXPORT_PATH="cms.jm.export.path";
        String CMS_JM_IMPORT_PATH ="cms.jm.import.path";
        String STOCK_EXPORT_TEMPLATE = "cms.stock.export.file";
        String CMS_Image_Ceate_Import_Path ="cms.image.create.import.path";
        String CMS_PROMOTION_EXPORT_JUHUASUAN = "cms.promotion.export.juhuasuan";
        String CMS_PROMOTION_EXPORT_TMALL = "cms.promotion.export.tmall";
        String CMS_PROMOTION_EXPORT_NEW = "cms.promotion.export.new";

        String PROMOTION_EXPORT_PATH = "cms.promotion.export.path";
        String PROMOTION_JUHUASUAN_EXPORT_PATH = "cms.promotion.juhuasuan.export.path";
        String PROMOTION_TMALL_EXPORT_PATH = "cms.promotion.tmall.export.path";
        String PROMOTION_JIAGEPILU_EXPORT_PATH = "cms.promotion.new.export.path";

        /**
         * 价格披露商品导入模板下载路径
         */
        String CMS_JIAGEPILU_IMPORT_TEMPLATE_PATH = "cms.jiagepilu.import.template.path";
        String CMS_JIAGEPILU_IMPORT_ERROR_PATH = "cms.jiagepilu.import.error.path";
    }
}
