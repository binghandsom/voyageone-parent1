package com.voyageone.service.impl.com.mq.config;

/**
 * @author aooer 2016/4/18.
 * @version 2.6.0
 * @since 2.0.0
 */
public class MqRoutingKey {

    /**
     * jumei mq task
     */
    public static final String CMS_BATCH_JmBtPromotionImportTask = "voyageone_cms_batchjob_JmBtPromotionImportTask_queue";

    public static final String CMS_BATCH_JmBtPromotionExportTask = "voyageone_cms_batchjob_JmBtPromotionExportTask_queue";

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
    /**
     * jd mq task
     */
    public static final String CMS_BATCH_PlatformCategoryTreeJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategoryTreeJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJdJob_queue";

    public static final String CMS_BATCH_PlatformProductUploadJdJob = "voyageone_cms_batchjob_CmsBuildPlatformProductUploadJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJMJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJMJob_queue";

    public static final String CMS_BATCH_FeedExportJob = "voyageone_cms_batchjob_FeedExportJob_queue";

    public static final String CMS_BATCH_TMFieldsImportCms2Job = "voyageone_CMS_BATCH_TMFieldsImportCms2Job_queue";
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
     * 同步产品sku的价格至code的group价格范围
     */
    public static final String CMS_TASK_ProdcutPriceUpdateJob = "voyageone_cms_task_ProdcutPriceUpdateJob_queue";
    /**
     * 记录上下架操作历史
     */
    public static final String CMS_TASK_PlatformActiveLogJob = "voyageone_cms_task_PlatformActiveLogJob_queue";
    /**
     * 更新商品vo扣点相关
     */
    public static final String CMS_TASK_ProdcutVoRateUpdateJob = "voyageone_cms_task_ProdcutVoRateUpdateJob_queue";
    /**
     * 高级检索-异步生成文件
     */
    public static final String CMS_TASK_AdvSearch_FileDldJob = "voyageone_cms_task_AdvSearch_FileDldJob_queue";
    /**
     * 高级检索-批处理
     */
    public static final String CMS_TASK_AdvSearch_AsynProcessJob = "voyageone_cms_task_AdvSearch_AsynProcessJob_queue";
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
}
