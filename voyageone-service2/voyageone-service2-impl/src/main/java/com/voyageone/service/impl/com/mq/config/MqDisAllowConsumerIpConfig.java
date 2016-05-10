package com.voyageone.service.impl.com.mq.config;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/5/9.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MqDisAllowConsumerIpConfig {

    private Map<String,List<String>> mqDisallowConsumerIpMap;

    public Map<String, List<String>> getMqDisallowConsumerIpMap() {
        return mqDisallowConsumerIpMap;
    }

    public void setMqDisallowConsumerIpMap(Map<String, List<String>> mqDisallowConsumerIpMap) {
        this.mqDisallowConsumerIpMap = mqDisallowConsumerIpMap;
    }
}
