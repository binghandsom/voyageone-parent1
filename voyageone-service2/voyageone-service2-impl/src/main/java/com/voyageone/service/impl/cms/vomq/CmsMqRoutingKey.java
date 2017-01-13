package com.voyageone.service.impl.cms.vomq;

/**
 * @author aooer 2016/4/18.
 * @version 2.10.0
 * @since 2.0.0
 */
public class CmsMqRoutingKey {


    // 聚美活动文件生成导出
    public static final String CMS_JM_PROMOTION_EXPORT = "VOCmsJmPromotionExportQueue";

    /*修改最终售价*/
    public static final String CMS_UPDATE_PRODUCT_SALE_PRICE = "VOCmsUpdateProductSalePriceQueue";

    // 聚美活动文件 导入
    public static final String CMS_JM_PROMOTION_IMPORT = "VOCmsJmPromotionImportQueue";

    // 聚美平台上传更新
    public static final String CMS_JM_PRODUCT_UPDATE = "VOCmsJmProductUpdateQueue";

    // 聚美刷新参考价
    public static final String CMS_JM_REFRESH_PRICE = "VOCmsJMRefreshPriceQueue";

    // 聚美活动中的产品的库存同步
    public static final String CMS_JM_PROMOTION_PRODUCT_STOCK_SYNC= "VOCmsJmPromotionProductStockSyncQueue";

    //高级搜索-设置自由标签
    public  static  final  String CMS_PPRODUCT_FREE_TAGS_UPDATE="VOCmsProductFreeTagsUpdateQueue";
     //高级检索-取得产品的bi信息
    public static final String CMS_PPRODUCT_BI_DATA = "VOCMSProductBIDataQueue";

    //品牌黑名单，屏蔽任务
    public static final String CMS_BRAND_BLOCK = "VOCmsBrandBlockQueue";

    // 批量更新商品
    public static final String CMS_BATCH_UPDATE_PRODUCT = "VOCmsBatchUpdateProductQueue";

    // 保存店铺分类
    public static final String CMS_SAVE_CHANNEL_CATEGORY = "VOCmsSaveChannelCategoryQueue";

    // Feed文件导出
    public static final String CMS_FEED_EXPORT = "VOCmsFeedExportQueue";

    // 同步产品sku的价格至code的group价格范围
    public static final String CMS_PRODUCT_PRICE_UPDATE = "VOCmsProductPriceUpdateQueue";

    // 更新商品vo扣点相关
    public static final String CMS_PRODUCT_VO_RATE_UPDATE = "VOCmsProductVoRateUpdateQueue";

    // 高级检索-异步生成文件
    public static final String CMS_ADV_SEARCH_EXPORT = "VOCmsAdvSearchExportQueue";

    // 高级检索-重新计算中国指导价
    public static final String CMS_ADV_SEARCH_REFRESH_RETAIL_PRICE = "VOCmsAdvSearchRefreshRetailPriceQueue";

    // 批量设置平台属性
    public static final String CMS_BATCH_PLATFORM_FIELDS = "VOCmsPlatformFieldsSetQueue";

    // 获取货架里的商品信息
    public static final String CMS_BATCH_SHELVES_MONITOR = "VOCmsShelvesMonitorGetQueue";

     // 货架监控
    public static final String CMS_BATCH_SHELVES_IMAGE_UPLOAD = "VOCmsShelvesImageUploadQueue";

     // cart追加 把product表中的platform追加一个cart
    public static final String CMS_BATCH_CART_ADD_JOB = "VOCmsPlatformCartAddQueue";

    // 默认属性功能，用于强制对某商品或某类目进行属性的重新计算赋值和上新
    public static final String CMS_TASK_REFRESH_PRODUCTS = "VOCmsRefreshProductsQueue";

    public static final String CMS_PLATFORM_CATEGORY_UPDATE = "VOCmsPlatformCategoryUpdateQueue";

    /**
     * image create task
     */
    public static final String CMS_BATCH_CmsMtImageCreateTaskJob = "voyageone_cms_batchjob_CmsMtImageCreateTaskJob_queue";

    public static final String CMS_BATCH_CmsBatchSetMainCategoryJob = "voyageone_cms_batchjob_SetMainCategoryJob_queue";

    public static final String CMS_BATCH_CmsBatchRefreshMainCategoryJob = "voyageone_cms_batchjob_RefreshMainCategoryJob_queue";
    /**
     * jd mq task
     */
    public static final String CMS_BATCH_PlatformCategoryTreeJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategoryTreeJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJdJob_queue";

    public static final String CMS_BATCH_PlatformProductUploadJdJob = "voyageone_cms_batchjob_CmsBuildPlatformProductUploadJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJMJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJMJob_queue";

    public static final String CMS_BATCH_TMFieldsImportCms2Job = "voyageone_cms_batchjob_TMFieldsImportCms2Job_queue";
    public static final String CMS_BATCH_TMGroupImportCms2Job = "voyageone_cms_batchjob_TMGroupImportCms2Job_queue";

    public static final String CMS_BATCH_JDFieldsImportCms2Job = "voyageone_cms_batchjob_JDFieldsImportCms2Job_queue";
    public static final String CMS_BATCH_JDGroupImportCms2Job = "voyageone_cms_batchjob_JDGroupImportCms2Job_queue";

    public static final String CMS_BATCH_PlatformSellercatJob = "voyageone_cms_batchjob_CmsPlatformSellercatJob_queue";

    public static final String CMS_BATCH_CmsProductGroupMergeJob = "voyageone_cms_batchjob_CmsProductGroupMergeJob_queue";

    /**
     * tmall mq task
     */
    public static final String CMS_BATCH_PlatformCategorySchemaTmJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaTmJob_queue";

    /**
     * ca api task
     */
    public static final String CMS_BATCH_CA_Feed_Analysis = "voyageone_cms_batchjob_Ca_FeedAnalysis_queue";

    public static final String CMS_BATCH_CA_Update_Quantity = "voyageone_vms_wsdl_mq_update_products_quantity_queue";

    /**
     * 平台标题翻译
     */
    public static final String CMS_BATCH_PlatformTitleTranslateJob = "voyageone_cms_batchjob_CmsPlatformTitleTranslateJob_queue";

    /**
     * 记录上下架操作历史
     */
    //public static final String CMS_TASK_PlatformActiveLogJob = "voyageone_cms_task_PlatformActiveLogJob_queue";
    public static final String CMS_PLATFORM_ACTIVE_LOG = "VOCmsPlatformActiveSetQueue";


    /*高级检索-指导价变更确认*/
    //public static final String CMS_TASK_AdvSearch_AsynProcessJob = "voyageone_cms_task_AdvSearch_AsynProcessJob_queue";
    public static final String CMS_ADV_SEARCH_CONFIRM_RETAIL_PRICE = "VOCmsAdvSearchConfirmRetailPriceQueue";
    /**
     * 高级检索-取得产品的bi信息
     */
    public static final String CMS_TASK_AdvSearch_GetBIDataJob = "voyageone_cms_task_AdvSearch_GetBIDataJob_queue";
    /**
     * 品牌黑名单，屏蔽任务
     * create by jonas
     *
     * @since 2.6.0
     */
    public static final String CMS_TASK_BRANDBLOCKJOB = "voyageone_cms_task_CmsBrandBlockJobService_queue";

    /**
     * 天猫同购共通标题描述翻译
     *
     * @since 2.10.0
     */
    public static final String CMS_TASK_TranslateByTonggouJob = "voyageone_cms_task_TranslateByTonggouJobService_queue";
    /**
     * 京东类目销售属性取得
     *
     * @since 2.10.0
     */
    public static final String CMS_TASK_CatelogySaleAttrJdJob = "voyageone_cms_task_CatelogySaleAttrJdJobService_queue";

}

