package com.voyageone.common.mq.config;

/**
 * Created by dell on 2016/12/21.
 */
public interface IVOMQMessageBodyClass<TMQMessageBody extends IMQMessageBody> {
          Class<TMQMessageBody> getTMQMessageBodyClass();
}
