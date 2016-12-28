package com.voyageone.common.configs;

/**
 * Created by dell on 2016/12/22.
 */
public class VOTestContext {
    public boolean isInitMQRabbitListenerConfig() {
        return isInitMQRabbitListenerConfig;
    }

    public void setInitMQRabbitListenerConfig(boolean initMQRabbitListenerConfig) {
        isInitMQRabbitListenerConfig = initMQRabbitListenerConfig;
    }
    //是否初始化MQ监听类配置
    boolean isInitMQRabbitListenerConfig=true;
}
