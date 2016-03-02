package com.voyageone.common.mq.enums;

/**
 * 根据需要定义
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum MqRoutingKey {

    KEY1("CMSKEY1","voyageone_batchjob_queue_1")


    ;

    private String key;

    private String value;

    MqRoutingKey(String key, String value) {
        this.key=key;
        this.value=value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
