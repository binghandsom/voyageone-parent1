package com.voyageone.service.impl.cms.vomq;

import com.voyageone.service.impl.cms.vomq.vomessage.body.SaveChannelCategoryMQMessageBody;

/**
 * @author aooer 2016/4/18.
 * @version 2.10.0
 * @since 2.0.0
 */
public class CmsMqRoutingKey {


    // 聚美活动文件生成导出
    public static final String CMS_JM_PROMOTION_EXPORT = "VOCmsJmPromotionExportQueue";

    /*批量更新商品*/
    public static final String CMS_BATCH_UPDATE_PRODUCT = "VOCmsBatchUpdateProductQueue";

    /*保存店铺分类*/
    public static final String CMS_SAVE_CHANNEL_CATEGORY = "VOCmsSaveChannelCategoryQueue";


    /**
     * jumei mq task
     */
    public static final String CMS_BATCH_JmBtPromotionImportTask = "voyageone_cms_batchjob_JmBtPromotionImportTask_queue";


    public static final String CMS_BATCH_JmPromotionRecovery = "voyageone_cms_batchjob_JmPromotionRecovery_queue";

    public static final String CMS_BATCH_JuMeiProductUpdate = "voyageone_cms_batchjob_JuMeiProductUpdate_queue";

    public static final String CMS_BATCH_JmSynPromotionDealPrice = "voyageone_cms_batchjob_JmSynPromotionDealPrice_queue";

    public static final String CMS_BATCH_JuMeiProductUpdateDealEndTimeJob = "voyageone_cms_batchjob_JuMeiProductUpdateDealEndTimeJobService_queue";
    /**
     * 聚美活动中的产品的库存同步
     */
    public static final String CMS_BATCH_JmPromotionProductStockSyncServiceJob = "voyageone_cms_batchjob_JmPromotionProductStockSyncService_queue";
    /**
     * image create task
     */
    public static final String CMS_BATCH_CmsMtImageCreateTaskJob = "voyageone_cms_batchjob_CmsMtImageCreateTaskJob_queue";

    public static final String CMS_BATCH_PlatformFieldsTaskJob = "voyageone_cms_batchjob_PlatformFieldsTaskJob_queue";

    public static final String CMS_BATCH_CmsBatchSetMainCategoryJob = "voyageone_cms_batchjob_SetMainCategoryJob_queue";
    /**
     * jd mq task
     */
    public static final String CMS_BATCH_PlatformCategoryTreeJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategoryTreeJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJdJob_queue";

    public static final String CMS_BATCH_PlatformProductUploadJdJob = "voyageone_cms_batchjob_CmsBuildPlatformProductUploadJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJMJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJMJob_queue";

    // Feed文件导出
    public static final String CMS_FEED_EXPORT = "VOCmsFeedExportQueue";
    //public static final String CMS_BATCH_FeedExportJob = "voyageone_cms_batchjob_FeedExportJob_queue";

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

    public static final String CMS_BATCH_PlatformProductUploadTmJob = "voyageone_cms_batchjob_CmsBuildPlatformProductUploadTmJob_queue";
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
     * 货架监控
     */
    public static final String CMS_BATCH_ShelvesMonitorJob = "voyageone_cms_batchjob_ShelvesMonitor_queue";

    /**
     * 货架监控
     */
    public static final String CMS_BATCH_ShelvesImageUploadJob = "voyageone_cms_batchjob_ShelvesImageUpload_queue";

    /**
     * cart追加 把product表中的platform追加一个cart
     */
    public static final String CMS_BATCH_CartAddJob = "voyageone_cms_batchjob_CartAdd_queue";

    /**
     * 同步产品sku的价格至code的group价格范围
     */
    //public static final String CMS_TASK_ProdcutPriceUpdateJob = "voyageone_cms_task_ProdcutPriceUpdateJob_queue";
    public static final String CMS_PRODUCT_PRICE_UPDATE = "VOCmsProductPriceUpdateQueue";

    /**
     * 记录上下架操作历史
     */
    public static final String CMS_TASK_PlatformActiveLogJob = "voyageone_cms_task_PlatformActiveLogJob_queue";
    /**
     * 更新商品vo扣点相关
     */
    //public static final String CMS_TASK_ProdcutVoRateUpdateJob = "voyageone_cms_task_ProdcutVoRateUpdateJob_queue";
    public static final String CMS_PRODUCT_VORATE_UPDATE = "VOCmsProductVoRateUpdateQueue";

    /**
     * 高级检索-异步生成文件
     */
    //public static final String CMS_TASK_AdvSearch_FileDldJob = "voyageone_cms_task_AdvSearch_FileDldJob_queue";
    public static final String CMS_ADV_SEARCH_EXPORT = "VOCmsAdvSearchExportQueue";

    /*高级检索-指导价变更确认*/
    //public static final String CMS_TASK_AdvSearch_AsynProcessJob = "voyageone_cms_task_AdvSearch_AsynProcessJob_queue";
    public static final String CMS_ADV_SEARCH_CONFIRM_RETAIL_PRICE = "VOCmsAdvSearchConfirmRetailPriceQueue";

    /*高级检索-重新计算指导价*/
    //public static final String CMS_TASK_AdvSearch_RefreshRetailPriceServiceJob = "voyageone_cms_task_AdvSearch_RefreshRetailPriceService_queue";
    public static final String CMS_ADV_SEARCH_REFRESH_RETAIL_PRICE = "VOCmsAdvSearchRefreshRetailPriceQueue";
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
     * 默认属性功能，用于强制对某商品或某类目进行属性的重新计算赋值和上新
     * create by jonas on 2016-11-02 19:26:28
     *
     * @since 2.9.0
     */
    public static final String CMS_TASK_REFRESH_PRODUCTS = "voyageone_cms_task_CmsRefreshProductsJobService_queue";
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

