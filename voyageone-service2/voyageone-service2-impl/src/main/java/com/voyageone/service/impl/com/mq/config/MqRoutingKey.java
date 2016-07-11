package com.voyageone.service.impl.com.mq.config;

/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
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

    public static final String CMS_BATCH_JuMeiProductUpdateDealEndTimeJob = "voyageone_cms_batchjob_JuMeiProductUpdateDealEndTimeJobService_queue";

    /**
     * image create task
     */
    public static final String CMS_BATCH_CmsMtImageCreateTaskJob = "voyageone_cms_batchjob_CmsMtImageCreateTaskJob_queue";

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
    public static final String CMS_BATCH_PlatformProductUploadTmJob = "voyageone_cms_batchjob_CmsBuildPlatformProductUploadTmJob_queue";

}
