package com.voyageone.service.impl.com.mq.config;

/**
 * @author aooer 2016/4/22.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoRabbitMqLocalConfig {
    private boolean local=false;

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
