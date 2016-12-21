package com.voyageone.common.mq.config;

/**
 * Created by dell on 2016/12/21.
 */
public interface IVOMQOnStartup<TMQMessageBody extends IMQMessageBody> {
    void onStartup(TMQMessageBody messageBody) throws Exception;
}
