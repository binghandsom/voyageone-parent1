package com.voyageone.common.mq.enums;

/**
 * 根据需要定义
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum MqRoutingKey {
    CMS_BATCH_PROMOTION("voyageone_cms_batchjob_promotion_queue"),
    CMS_BATCH_JmBtPromotionImportTask("voyageone_cms_batchjob_JmBtPromotionImportTask_queue"),
    CMS_BATCH_JmBtPromotionExportTask("voyageone_cms_batchjob_JmBtPromotionExportTask_queue"),
    CMS_BATCH_JuMeiProductUpdate("voyageone_cms_batchjob_JuMeiProductUpdate_queue");

    private String value;

    MqRoutingKey(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
