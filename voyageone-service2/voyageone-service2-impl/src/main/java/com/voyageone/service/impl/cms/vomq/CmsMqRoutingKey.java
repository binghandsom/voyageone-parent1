package com.voyageone.service.impl.cms.vomq;

/**
 * @author aooer 2016/4/18.
 * @version 2.10.0
 * @since 2.0.0
 */
public class CmsMqRoutingKey {

    // 聚美活动 - 生成导出文件
    public static final String CMS_JM_PROMOTION_EXPORT = "VOCmsJmPromotionExportQueue";

    // 聚美活动 - 活动商品文件导入
    public static final String CMS_JM_PROMOTION_IMPORT = "VOCmsJmPromotionImportQueue";

    // 聚美活动 - 活动产品的库存同步
    public static final String CMS_JM_PROMOTION_PRODUCT_STOCK_SYNC= "VOCmsJmPromotionProductStockSyncQueue";

    // 聚美活动 - 平台上传更新
    public static final String CMS_JM_PROMOTION_PRODUCT_UPDATE = "VOCmsJmPromotionProductUpdateQueue";

    // 聚美活动 - 获取聚美日常销售的参考价
    public static final String CMS_JM_PROMOTION_REFRESH_PRICE = "VOCmsJMPromotionRefreshPriceQueue";

    // 高级检索 - 批量修改中国最终售价
    public static final String CMS_UPDATE_PRODUCT_PLATFORM_SALE_PRICE = "VOCmsUpdateProductPlatformSalePriceQueue";

    // 高级搜索 - 批量设置自由标签
    public static final String CMS_UPDATE_PRODUCT_FREE_TAGS ="VOCmsUpdateProductFreeTagsQueue";

    // 高级检索 - 批量重新计算中国指导价
    public static final String CMS_REFRESH_PLATFORM_RETAIL_PRICE = "VOCmsRefreshPlatformRetailPriceQueue";

    // 高级检索 - 批量部分上新
    public static final String CMS_PART_APPROVAL = "VOCmsPartApprovalQueue";

    // 高级检索 - 批量更新商品vo扣点
    public static final String CMS_UPDATE_PRODUCT_VO_RATE = "VOCmsUpdateProductVoRateQueue";

    // 高级检索 - 批量确认中国指导价变更
    public static final String CMS_CONFIRM_PLATFORM_RETAIL_PRICE = "VOCmsConfirmPlatformRetailPriceQueue";

    // 高级检索 - 批量确认美元MSRP价格变更
    public static final String CMS_CONFIRM_CLIENT_MSRP_PRICE = "VOCmsConfirmClientMsrpPriceQueue";

    // 高级检索 - 批量设置店铺分类
    public static final String CMS_UPDATE_PRODUCT_PLATFORM_CHANNEL_CATEGORY = "VOCmsUpdateProductPlatformChannelCategoryQueue";

    // 高级检索 - 批量设置平台属性
    public static final String CMS_UPDATE_PRODUCT_PLATFORM_FIELDS = "VOCmsUpdateProductPlatformFieldsQueue";

    // 高级检索 - 批量设置平台类目
    public static final String CMS_UPDATE_PRODUCT_PLATFORM_CATEGORY = "VOCmsUpdateProductPlatformCategoryQueue";

    // 高级检索 - 批量审批商品平台状态为Approve
    public static final String CMS_UPDATE_PRODUCT_PLATFORM_STATUS_TO_APPROVE = "VOCmsUpdateProductPlatformStatusToApproveQueue";

    // 高级检索 - 批量设置商品上下架
    public static final String CMS_UPDATE_PRODUCT_PLATFORM_PSTATUS_OFF_OR_ON = "VOCmsUpdateProductPlatformPStatusOffOrOnQueue";

    // 高级检索 - 批量更新共通属性
    public static final String CMS_UPDATE_PRODUCT_FIELDS = "VOCmsUpdateProductFieldsQueue";

    // 高级检索 - 批量智能上新
    public static final String CMS_UPDATE_PRODUCT_PLATFORM_STATUS_TO_APPROVE_BY_SMART = "VOCmsUpdateProductPlatformStatusToApproveBySmartQueue";

    // 高级检索 - 异步生成文件
    public static final String CMS_EXPORT_PRODUCT_INFO = "VOCmsExportProductInfoQueue";

    // 高级检索 - 批量设置主类目
    public static final String CMS_BATCH_SET_MAIN_CATEGORY = "VOCmsBatchSetMainCategoryQueue";

    // Feed检索 - 异步生成Feed导出文件
    public static final String CMS_EXPORT_FEED_INFO = "VOCmsExportFeedInfoQueue";

    // 黑名单 - 变更黑名单操作
    public static final String CMS_BRAND_BLOCK = "VOCmsBrandBlockQueue";

    // 货架管理 - 获取货架里的商品信息
    public static final String CMS_GET_SHELVES_MONITOR = "VOCmsGetShelvesMonitorQueue";

    // 货架管理 - 货架监控
    public static final String CMS_UPLOAD_SHELVES_IMAGE = "VOCmsUploadShelvesImageQueue";

    // 天猫活动管理 - 天猫特价宝刷新
    public static final String CMS_TM_TE_JIA_BAO_DEL = "VOCmsTmTeJiaBaoDelQueue";

    // 天猫活动管理 - 天猫活动刷价格
    public static final String CMS_PROMOTION= "VOCmsPromotionQueue";

    // 定时任务 - 取得产品的bi信息
    public static final String CMS_BATCH_GET_PRODUCT_BI_DATA = "VOCmsBatchGetProductBIDataQueue";

    // 定时任务 - 同步产品sku的价格至code的group价格范围
    public static final String CMS_BATCH_COUNT_PRODUCT_PRICE = "VOCmsBatchCountProductPriceQueue";

    // 手动触发 - cart追加 把product表中的platform追加一个cart
    public static final String CMS_ADD_PLATFORM_CART = "VOCmsAddPlatformCartQueue";

    // 平台属性默认设置 - 用于强制对某商品或某类目进行属性的重新计算赋值和上新
    public static final String CMS_REFRESH_PRODUCT_PLATFORM_FIELDS = "VOCmsRefreshProductPlatformFieldsQueue";

    // 聚美活动价格同步到聚美商城价格
    public static final String CMS_JM_MALL_PROMOTION_PRICE_SYNC = "VOCmsJmMallPromotionPriceSyncQueue";

    // 检测产品数据是否正确
    public static final String CMS_CHECK_PRODUCT_IS_RIGHT = "VOCmsCheckProductIsRightQueue";

    // 添加新group
    public static final String CMS_ADD_NEW_GROUP = "VOCmsAddNewGroupQueue";

    // feed数据导入接口
    public static final String CMS_FEED_IMPORT_MQ_JOB = "CmsFeedImportMQJobQueue";

    // feed数据批量设置主类目
    public static final String CMS_FEED_SET_CATEGORY_MQ_JOB = "CmsFeedSetCategoryQueue";


    // VMS价格和库存变更 通知CMS更新feedInfo
    public static final String CMS_FEED_SKU_PQ_MQ_JOB = "CmsFeedSkuPqMQJobQueue";

    //sneakerHead根据活动里面的产品找出同一group的商品加入活动
    public static final String CMS_SNEAKERHEAD_ADD_PROMOTION = "CmsSneakerheadAddPromotionMQJobQueue";

    // 活动(非聚美)商品导出
    public static final String CMS_PROMOTION_EXPORT = "CmsPromotionExportMQJobQueue";

    //批量lock平台    added by piao
    public static final String  CMS_ADV_SEARCH_LOCK_PRODUCTS = "VOCmsAdvSearchLockProductsMQJobQueue";

    /**库存相关MQ*/
    public static final String CMS_STOCK_CART_CHANGED_STOCK = "VOCmsStockCartChangedStockMqJobQueue";

    /**组合商品推送MQ*/
    public static final String EWMS_MQ_GROUP_SKU = "ewms_mq_group_sku";

    /**
     * 往wms推送产品信息(新建/更新)
     */
    public static final String EWMS_MQ_CREATE_OR_UPDATE_PRODUCT = "ewms_mq_create_or_update_product";
    // 高级检索批量修改商品标题
    public static final String CMS_BATCH_UPDATE_PRODUCT_TITLE = "VOCmsBatchUpdateProductTitleMQJobQueue";

    // 同步LastReceiveTime
    public static final String CMS_SYN_LAST_RECEIVE_TIME = "VoCmsSynLastReceiveTimeMQJobQueue";

    // wms new item
    public static final String CMS_USA_PRODUCT_ADD_UPDATE = "VOCmsUsaProductAddUpdateMQJobQueue";

    // 自定义时间销量统计
    public static final String CMS_SALE_DATA_STATISTICS = "VOCmsSaleDataStatisticsMQJobQueue";

    //CMS修改价格
    public static final String CMS_USA_PRODUCT_UPDATE_PRICE = "VoCmsUsaProductUpdatePriceMQJobQueue";
    //商品上下架
    public static final String CMS_USA_PRODUCT_LIST_DELIST = "VoCmsUsaProductListDelistMQJobQueue";
    //修改类目
    public static final String CMS_USA_PRODUCT_UPDATE_CATEGORY = "VoCmsUsaProductUpdateCategoryMQJobQueue";
    //修改标签
    public static final String CMS_USA_PRODUCT_UPDATE_TAGS = "VoCmsUsaProductUpdateTagsMQJobQueue";
    // 推送产品至us
    public static final String CMS_USA_PRODUCT_TRANSFER = "VOUsSynchronizeProductQueue";
    // 推送类目至us
    public static final String CMS_USA_CATEGORY_TRANSFER = "VOUsSynchronizeCategoryQueue";
    // 接收类目信息
    public static final String CMS_USA_CATEGORY_RECEIVE = "VOUsReceiveCategoryQueue";

    public static final String CMS_USA_PRODUCT_SALES_UPDATE = "VOCmsUsaProductSalesQueue";

    // 未整理的业务内容

    /**
     * image create task
     */
    public static final String CMS_BATCH_CmsMtImageCreateTaskJob = "voyageone_cms_batchjob_CmsMtImageCreateTaskJob_queue";

    public static final String CMS_BATCH_CmsBatchRefreshMainCategoryJob = "voyageone_cms_batchjob_RefreshMainCategoryJob_queue";
    /**
     * jd mq task
     */
    public static final String CMS_BATCH_PlatformCategoryTreeJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategoryTreeJdJob_queue";
    public static final String CMS_BATCH_PlatformCategoryTreeKlJob = "voyageone_cms_batchjob_CmsBuildPlatformCategoryTreeKlJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJdJob_queue";

    public static final String CMS_BATCH_PlatformProductUploadJdJob = "voyageone_cms_batchjob_CmsBuildPlatformProductUploadJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJMJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJMJob_queue";
    public static final String CMS_BATCH_PlatformCategorySchemaKLJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaKLJob_queue";

    public static final String CMS_BATCH_TMScItemImportCms2Job = "voyageone_cms_batchjob_TMScItemImportCms2Job_queue";
    public static final String CMS_BATCH_TMFieldsImportCms2Job = "voyageone_cms_batchjob_TMFieldsImportCms2Job_queue";
    public static final String CMS_BATCH_TMGroupImportCms2Job = "voyageone_cms_batchjob_TMGroupImportCms2Job_queue";

    public static final String CMS_BATCH_JDFieldsImportCms2Job = "voyageone_cms_batchjob_JDFieldsImportCms2Job_queue";
    public static final String CMS_BATCH_JDGroupImportCms2Job = "voyageone_cms_batchjob_JDGroupImportCms2Job_queue";

    public static final String CMS_BATCH_KLFieldsImportCms2Job = "voyageone_cms_batchjob_KLFieldsImportCms2Job_queue";
    public static final String CMS_BATCH_KLGroupImportCms2Job = "voyageone_cms_batchjob_KLGroupImportCms2Job_queue";

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

    public static final String EWMS_MQ_STOCK_SYNC_PLATFORM_STOCK ="ewms_mq_stock_sync_platform_stock";


    /**
     * 重新设置商品标题
     */
    public static final String CMS_RESET_PRODUCT_TITLE = "VOCmsResetProductTitleMQJobQueue";

}

